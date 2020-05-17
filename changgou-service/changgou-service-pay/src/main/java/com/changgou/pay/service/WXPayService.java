package com.changgou.pay.service;


import java.util.Map;

public interface WXPayService {

    /**
     * 支付状态查询
     * @param outtradeno
     * @return
     */
    Map queryStatus(String outtradeno);

    /**
     * 创建二维码
     * @param parameterMap
     * @return
     */
    Map createnative(Map<String,String> parameterMap);
}
