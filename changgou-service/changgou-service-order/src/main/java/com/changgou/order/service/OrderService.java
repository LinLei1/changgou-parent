package com.changgou.order.service;

import com.changgou.order.pojo.Order;
import com.github.pagehelper.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {

    /**
     * 删除订单[修改状态],回滚库存
     * @param outtradeno
     */
    void deleteOrder(String outtradeno);

    /**
     * 修改订单状态
     * 1.修改支付时间,以微信传过来的时间为准
     * 2.修改支付状态
     * @param outtradeno : 订单号
     * @param paytime :支付时间
     * @param transactionid : 微信交易流水号
     */
    void updateStatus(String outtradeno, String paytime,String transactionid) throws Exception;

    /***
     * 查询所有
     * @return
     */
    List<Order> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Order findById(String id);

    /***
     * 新增
     * @param order
     */
    void add(Order order);

    /***
     * 修改
     * @param order
     */
    void update(Order order);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Order> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Order> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Order> findPage(Map<String, Object> searchMap, int page, int size);




}
