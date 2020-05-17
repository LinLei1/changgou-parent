package com.changgou.seckill.service.impl;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillOrderService;
import com.changgou.seckill.task.MultiThreadingCreateOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.SeckillStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    SeckillOrderMapper seckillOrderMapper;

    @Autowired
    MultiThreadingCreateOrder multiThreadingCreateOrder;



    /***
     * 删除订单
     * @param username
     */
    @Override
    public void deleteOrder(String username) {
        //删除订单
        redisTemplate.boundHashOps("SeckillOrder").delete(username);
        //查询用户排队信息,SeckillStatus
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
        //删除排队信息
        clearUserQueue(username);
        //回滚库存->注意: redis递增->redis不一定有商品
        String namespace = "SeckillGoods_"+ seckillStatus.getTime();
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(namespace).get(seckillStatus.getGoodsId());

        //如果商品为空
        if (seckillGoods==null){
            //数据库中查询,此时数据库中数据一定为0
            seckillGoods = seckillGoodsMapper.selectByPrimaryKey(seckillStatus.getGoodsId());
            //更新数据库的库存
            seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
            seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
        }else {
            seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
        }
        redisTemplate.boundHashOps(namespace).put(seckillGoods.getId(),seckillGoods);

        //队列
        redisTemplate.boundListOps("SeckillGoodsCountList_"+seckillGoods.getId()).leftPushAll(seckillGoods.getId());
    }

    /**
     * 修改秒杀订单支付状态
     * @param username: 用户名
     * @param transactionid: 交易流水号
     * @param endtime: 支付完成时间
     */
    @Override
    public void updatePayStatus(String username, String transactionid, String endtime) {
        //查询订单
        SeckillOrder seckillOrder =(SeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
        if (seckillOrder!=null){
            try {
                //修改订单状态信息
                seckillOrder.setStatus("1");
                seckillOrder.setTransactionId(transactionid);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date payTimeInfo = simpleDateFormat.parse(endtime);
                seckillOrder.setPayTime(payTimeInfo);
                seckillOrderMapper.insertSelective(seckillOrder);

                //删除redis中的订单
                redisTemplate.boundHashOps("SeckillOrder").delete(username);

                //删除用户排队信息
                clearUserQueue(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清理用户排队抢单信息
     * @param username
     */
    public void clearUserQueue(String username){
        //排队唯一标识
        redisTemplate.boundHashOps("UserQueueCount").delete(username);
        //排队信息清理
        redisTemplate.boundHashOps("UserQueueStatus").delete(username);
    }

    /**
     * 秒杀订单状态查询
     * @param username
     * @return
     */
    @Override
    public SeckillStatus queryStatus(String username){
        return (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
    }

    /**
     * 秒杀下单排队
     * @param time: 时区
     * @param id: 秒杀商品ID
     * @param username: 用户名
     */
    @Override
    public Boolean add(String time, Long id, String username) {
        //由于对redis库的操作是单线程的,所以在高并发的情况下,userQueueCount的值也不会重复
        Long userQueueCount = redisTemplate.boundHashOps("UserQueueCount").increment(username, 1);  //1为增量
        if (userQueueCount>1){
            throw new RuntimeException("请勿重复排队!");
        }
        //创建排队对象
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(), 1, id, time);
        //用户抢单状态->用于查询
        redisTemplate.boundHashOps("UserQueueStatus").put(username,seckillStatus);
        //List是队列类型,用户抢单排队
        redisTemplate.boundListOps("SeckillOrderQueue").leftPush(seckillStatus);
        //异步执行
        multiThreadingCreateOrder.createOrder();
        return true;
    }

    /**
     * SeckillOrder条件+分页查询
     * @param seckillOrder: 查询条件
     * @param page: 页码
     * @param size: 页大小
     * @return
     *//*
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder,int page,int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(seckillOrder);
        //执行搜索
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    *//**
     * seckillOrder分页查询
     * @param page
     * @param size
     * @return
     *//*
    public PageInfo<SeckillOrder> findPage(int page,int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }*/
}
