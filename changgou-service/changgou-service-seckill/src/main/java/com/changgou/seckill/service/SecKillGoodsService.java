package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillGoods;

import java.util.List;

public interface SecKillGoodsService {

    /**
     * 根据时间和秒杀商品ID,查询秒杀商品数据
     * @param time
     * @param id
     * @return
     */
    SeckillGoods one(String time,Long id);

    /**
     * 根据时间区间,查询秒杀商品频道列表数据
     * @param time
     * @return
     */
    List<SeckillGoods> list(String time);


}
