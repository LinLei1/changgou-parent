package com.changgou.seckill.service.impl;

import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SecKillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecKillGoodsServiceImpl implements SecKillGoodsService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据时间和秒杀商品ID,查询秒杀商品数据
     * @param time
     * @param id
     * @return
     */
    @Override
    public SeckillGoods one(String time, Long id) {
        return (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_"+time).get(id);
    }

    /**
     * 根据时间区间,查询秒杀商品频道列表数据
     * @param time
     * @return
     */
    @Override
    public List<SeckillGoods> list(String time) {
        List<SeckillGoods> list = redisTemplate.boundHashOps("SeckillGoods_"+time).values();
        return list;
    }
}
