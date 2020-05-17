package com.changgou.seckill.timer;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import entity.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 定时将秒杀商品存入Redis缓存
 */

@Component
public class SeckillGoodsPushTask {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void  loadSecKillGoodsToRedis(){
        /**
         * 1.查询所有符合条件的秒杀商品
         * 	1) 获取时间段集合并循环遍历出每一个时间段
         * 	2) 获取每一个时间段名称,用于后续redis中key的设置
         * 	3) 状态必须为审核通过 status=1
         * 	4) 商品库存个数>0
         * 	5) 秒杀商品开始时间>=当前时间段
         * 	6) 秒杀商品结束<当前时间段+2小时
         * 	7) 排除之前已经加载到Redis缓存中的商品数据
         * 	8) 执行查询获取对应的结果集
         * 2.将秒杀商品存入缓存
         */

        List<Date> dateMenus = DateUtil.getDateMenus(); // 5个

        for (Date dateMenu : dateMenus) {
            String timespace ="SeckillGoods_"+ DateUtil.date2Str(dateMenu,"yyyyMMddHH");

            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();

            criteria.andEqualTo("status","1");
            criteria.andGreaterThan("stockCount",0);
            criteria.andGreaterThanOrEqualTo("startTime",dateMenu);
            criteria.andLessThan("endTime",DateUtil.addDateHour(dateMenu,2));

            //排除已经存入到redis中的SeckillGoods,即求出当前命名空间下所有的商品ID(key) ,每次查询排除掉之前存在商品key的数据
            Set keys = redisTemplate.boundHashOps(timespace).keys();
            if (keys != null && keys.size()>0){
                criteria.andNotIn("id",keys);
            }
            //查询数据
            List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);

            //添加到缓存中
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                redisTemplate.opsForHash().put(timespace,seckillGoods.getId(),seckillGoods);
                //给每个商品做个队列,放什么无所谓,主要是能不能取到,能取到则代表有库存,解决并发超卖
                redisTemplate.boundListOps("SeckillGoodsCountList_"+seckillGoods.getId()).leftPushAll(putAllIds(seckillGoods.getStockCount(),seckillGoods.getId()));
            }
        }
    }

    /**
     * 获取每个商品的ID集合
     * @param num
     * @param id
     * @return
     */
    public Long[] putAllIds(Integer num,Long id){
        Long[] ids = new Long[num];
        for (int i = 0; i <ids.length ; i++) {
            ids[i] = id;
        }
        return ids;
    }
}
