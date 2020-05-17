package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.OrderService;
import com.changgou.order.pojo.Order;
import com.changgou.user.feign.UserFeign;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.IdWorker;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    SkuFeign skuFeign;


    /**
     * 删除订单[修改状态],回滚库存
     * @param outtradeno
     */
    @Override
    public void deleteOrder(String outtradeno) {
        Order order = orderMapper.selectByPrimaryKey(outtradeno);
        //修改状态
        order.setUpdateTime(new Date());
        order.setPayStatus("2");  //支付失败
        //修改到数据库中
        orderMapper.updateByPrimaryKeySelective(order);
        //回滚库存,要调goods商品微服务
    }

    /**
     * 修改订单状态
     * 1.修改支付时间,以微信传过来的时间为准
     * 2.修改支付状态
     * @param outtradeno : 订单号
     * @param paytime :支付时间
     * @param transactionid : 微信交易流水号
     */
    @Override
    public void updateStatus(String outtradeno, String paytime, String transactionid)throws Exception {
        //时间转换
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date payTimeInfo = simpleDateFormat.parse(paytime);
        //查询订单
        Order order = orderMapper.selectByPrimaryKey(outtradeno);
        //修改订单信息
        order.setPayTime(payTimeInfo);
        order.setPayStatus("1");  //支付状态
        order.setTransactionId(transactionid);  //交易流水号
        //修改到数据库
        orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * 增加Order
     *
     * @param order
     */
    @Override
    public void add(Order order){
        /**
         * 1.价格校验
         * 2.当前购物车和订单明细捆绑了,没有拆开
         */
        order.setId(idWorker.nextId()); //主键ID
        //获取订单明细,购物车集合
        List<OrderItem> orderItems = new ArrayList<>();  //redisTemplate.boundHashOps("Cart_" + order.getUsername()).values();
        //获取勾选的商品ID,需要下单的商品,将要下单的商品ID信息从购物车中移除
        for (Long skuId: order.getSkuIds()){
            orderItems.add((OrderItem) redisTemplate.boundHashOps("Cart_"+ order.getUsername()).get(skuId));
            redisTemplate.boundHashOps("Cart_" + order.getUsername()).delete(skuId);
        }
        int totalNum = 0;  //总数量
        int totalMoney = 0;  //总金额

        //封装Map<Long,Integer> 封装递减数据
        Map<String,String> decrmap = new HashMap<>();
        for (OrderItem orderItem: orderItems) {
            totalNum+=orderItem.getNum();
            totalMoney+=orderItem.getMoney();

            //订单明细的ID
            orderItem.setId(idWorker.nextId());
            //订单明细所属的订单
            orderItem.setOrderId(order.getId());
            //是否退货
            orderItem.setIsReturn("0");
            //封装递减数据
            decrmap.put(orderItem.getSkuId().toString(),orderItem.getNum().toString());
        }

        //订单添加一次
        order.setCreateTime(new Date());  //创建时间
        order.setUpdateTime(order.getCreateTime());  //修改时间
        order.setSourceType("1");  //订单来源
        order.setOrderStatus("0");  //未支付
        order.setPayStatus("0");  //未支付
        order.setIsDelete("0");  //未删除

        /**
         * 订单购买商品总数量 = 每个商品的总数量之和
         *                      获取订单明细->购物车集合
         *                      循环订单明细,每个商品的购买数量
         */
        order.setTotalNum(totalNum);
        /***
         * 订单总金额 = 每个商品总金额之和
         */
        order.setTotalMoney(totalMoney);  //订单总金额
        order.setPayMoney(totalMoney);  //实付金额-优惠价格的时候,就不一样

        //添加订单信息
        orderMapper.insertSelective(order);
        //循环添加订单明细信息
        for (OrderItem orderItem: orderItems){
            orderItemMapper.insertSelective(orderItem);
        }
        //库存递减
        skuFeign.decrCount(decrmap);

        //添加用户积分活跃度 +1
        userFeign.addPoints(1);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置订单创建时间
        simpleDateFormat.format(new Date());
        //添加订单
        rabbitTemplate.convertAndSend("orderDelayQueue", (Object) order.getId(), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置延时时间,即队列中数据十秒后过期
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
    }


    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Order findById(String id){
        return  orderMapper.selectByPrimaryKey(id);
    }


    /**
     * 修改
     * @param order
     */
    @Override
    public void update(Order order){
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        orderMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Order> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return orderMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Order> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Order>)orderMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Order> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Order>)orderMapper.selectByExample(example);
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 订单id
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andEqualTo("id",searchMap.get("id"));
           	}
            // 支付类型，1、在线支付、0 货到付款
            if(searchMap.get("payType")!=null && !"".equals(searchMap.get("payType"))){
                criteria.andEqualTo("payType",searchMap.get("payType"));
           	}
            // 物流名称
            if(searchMap.get("shippingName")!=null && !"".equals(searchMap.get("shippingName"))){
                criteria.andLike("shippingName","%"+searchMap.get("shippingName")+"%");
           	}
            // 物流单号
            if(searchMap.get("shippingCode")!=null && !"".equals(searchMap.get("shippingCode"))){
                criteria.andLike("shippingCode","%"+searchMap.get("shippingCode")+"%");
           	}
            // 用户名称
            if(searchMap.get("username")!=null && !"".equals(searchMap.get("username"))){
                criteria.andLike("username","%"+searchMap.get("username")+"%");
           	}
            // 买家留言
            if(searchMap.get("buyerMessage")!=null && !"".equals(searchMap.get("buyerMessage"))){
                criteria.andLike("buyerMessage","%"+searchMap.get("buyerMessage")+"%");
           	}
            // 是否评价
            if(searchMap.get("buyerRate")!=null && !"".equals(searchMap.get("buyerRate"))){
                criteria.andLike("buyerRate","%"+searchMap.get("buyerRate")+"%");
           	}
            // 收货人
            if(searchMap.get("receiverContact")!=null && !"".equals(searchMap.get("receiverContact"))){
                criteria.andLike("receiverContact","%"+searchMap.get("receiverContact")+"%");
           	}
            // 收货人手机
            if(searchMap.get("receiverMobile")!=null && !"".equals(searchMap.get("receiverMobile"))){
                criteria.andLike("receiverMobile","%"+searchMap.get("receiverMobile")+"%");
           	}
            // 收货人地址
            if(searchMap.get("receiverAddress")!=null && !"".equals(searchMap.get("receiverAddress"))){
                criteria.andLike("receiverAddress","%"+searchMap.get("receiverAddress")+"%");
           	}
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if(searchMap.get("sourceType")!=null && !"".equals(searchMap.get("sourceType"))){
                criteria.andEqualTo("sourceType",searchMap.get("sourceType"));
           	}
            // 交易流水号
            if(searchMap.get("transactionId")!=null && !"".equals(searchMap.get("transactionId"))){
                criteria.andLike("transactionId","%"+searchMap.get("transactionId")+"%");
           	}
            // 订单状态
            if(searchMap.get("orderStatus")!=null && !"".equals(searchMap.get("orderStatus"))){
                criteria.andEqualTo("orderStatus",searchMap.get("orderStatus"));
           	}
            // 支付状态
            if(searchMap.get("payStatus")!=null && !"".equals(searchMap.get("payStatus"))){
                criteria.andEqualTo("payStatus",searchMap.get("payStatus"));
           	}
            // 发货状态
            if(searchMap.get("consignStatus")!=null && !"".equals(searchMap.get("consignStatus"))){
                criteria.andEqualTo("consignStatus",searchMap.get("consignStatus"));
           	}
            // 是否删除
            if(searchMap.get("isDelete")!=null && !"".equals(searchMap.get("isDelete"))){
                criteria.andEqualTo("isDelete",searchMap.get("isDelete"));
           	}

            // 数量合计
            if(searchMap.get("totalNum")!=null ){
                criteria.andEqualTo("totalNum",searchMap.get("totalNum"));
            }
            // 金额合计
            if(searchMap.get("totalMoney")!=null ){
                criteria.andEqualTo("totalMoney",searchMap.get("totalMoney"));
            }
            // 优惠金额
            if(searchMap.get("preMoney")!=null ){
                criteria.andEqualTo("preMoney",searchMap.get("preMoney"));
            }
            // 邮费
            if(searchMap.get("postFee")!=null ){
                criteria.andEqualTo("postFee",searchMap.get("postFee"));
            }
            // 实付金额
            if(searchMap.get("payMoney")!=null ){
                criteria.andEqualTo("payMoney",searchMap.get("payMoney"));
            }

        }
        return example;
    }

}
