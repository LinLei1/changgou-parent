package com.changgou.seckill.service;

import entity.SeckillStatus;

public interface SeckillOrderService {

    /**
     * 删除订单
     * @param username
     */
    void deleteOrder(String username);

    /***
     * 修改订单状态
     * @param username
     * @param transactionid
     * @param endtime
     */
    void updatePayStatus(String username,String transactionid,String endtime);

    /**
     * 秒杀订单状态查询
     * @param username
     * @return
     */
    SeckillStatus queryStatus(String username);

    /**
     * 添加秒杀订单
     * @param time
     * @param id
     * @param username
     */
    Boolean add(String time, Long id, String username);
}
