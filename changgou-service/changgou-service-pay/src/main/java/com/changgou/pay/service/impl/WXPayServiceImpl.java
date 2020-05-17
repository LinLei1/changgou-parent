package com.changgou.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WXPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class WXPayServiceImpl implements WXPayService {

    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.partner}")
    private String partner;
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    @Value("${weixin.notifyurl}")
    private String notifyurl;

    /**
     * 支付状态查询
     * @param outtradeno
     * @return
     */
    @Override
    public Map queryStatus(String outtradeno) {
        try {
            Map<String,String> paramMap = new HashMap<>();
            //微信支付分配的公众账号ID（企业号corpid即为此appId）
            paramMap.put("appid",appid);
            //微信支付分配的商户号
            paramMap.put("mch_id",partner);
            //随机字符串，长度要求在32位以内
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            //订单号
            paramMap.put("out_trade_no",outtradeno);
            //Map转为xml字符串,可以携带签名
            String xmlparameters = WXPayUtil.generateSignedXml(paramMap, partnerkey);

            //需要请求的url地址
            String url = "https://api.mch.weixin.qq.com/pay/orderquery";
            HttpClient httpClient = new HttpClient(url);
            //提交方式
            httpClient.setHttps(true);
            //提交参数
            httpClient.setXmlParam(xmlparameters);
            //执行请求
            httpClient.post();
            //获取返回的数据
            String result = httpClient.getContent();
            //返回数据转为Map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建二维码
     * @param parameterMap
     * @return
     */
    @Override
    public Map createnative(Map<String, String> parameterMap) {
        try {
            Map<String,String> paramMap = new HashMap<>();
            //微信支付分配的公众账号ID（企业号corpid即为此appId）
            paramMap.put("appid",appid);
            //微信支付分配的商户号
            paramMap.put("mch_id",partner);
            //随机字符串，长度要求在32位以内
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            //商品简单描述
            paramMap.put("body","畅购商城");
            //订单号
            paramMap.put("out_trade_no",parameterMap.get("outtradeno"));
            //交易金额: 单位 分
            paramMap.put("total_fee",parameterMap.get("totalfee"));
            //用户的客户端IP
            paramMap.put("spbill_create_ip","127.0.0.1");
            paramMap.put("notify_url",notifyurl);
            paramMap.put("trade_type","NATIVE");

            //获取自定义数据
            String exchange = parameterMap.get("exchange");
            String routingkey = parameterMap.get("routingkey");
            Map<String,String> attachMap = new HashMap<>();
            attachMap.put("exchange",exchange);
            attachMap.put("routingkey",routingkey);
            String attach = JSON.toJSONString(attachMap);

            //如果是秒杀订单,需要传username
            String username = parameterMap.get("username");
            if (!StringUtils.isEmpty(username)){
                attachMap.put("username",username);
            }

            paramMap.put("attach",attach);

            //Map转为xml字符串,可以携带签名
            String xmlparameters = WXPayUtil.generateSignedXml(paramMap, partnerkey);

            //需要请求的url地址
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            HttpClient httpClient = new HttpClient(url);
            //提交方式
            httpClient.setHttps(true);
            //提交参数
            httpClient.setXmlParam(xmlparameters);
            //执行请求
            httpClient.post();
            //获取返回的数据
            String result = httpClient.getContent();
            //返回数据转为Map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
