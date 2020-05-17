package com.changgou.pay.controller;
import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WXPayService;
import com.github.wxpay.sdk.WXPayUtil;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@RequestMapping(value = "/weixin/pay")
@RestController
public class WXPayController {
    @Autowired
    WXPayService wxPayService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 微信支付结果通知回调方法
     * @param request
     * @return
     * @throws Exception
     */
    public String notifyurl(HttpServletRequest request)throws Exception{
        //获取网络输入流
        ServletInputStream is = request.getInputStream();
        //创建一个OutputStream->输入文件中
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len=is.read(buffer))!=-1){
            baos.write(buffer,0,len);
        }
        //微信支付结果的字节数组
        byte[] bytes = baos.toByteArray();
        String xmlresult = new String(bytes, "UTF-8");
        //XML字符串->Map
        Map<String, String> resultMap = WXPayUtil.xmlToMap(xmlresult);

        //获取自定义参数
        String attach = resultMap.get("attach");
        Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
        //发送支付结果给消息队列
        rabbitTemplate.convertAndSend(attachMap.get("exchange"),attachMap.get("routingkey"), JSON.toJSONString(resultMap));
        //返回给微信的数据
        String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        return result;
    }

    /**
     * 微信支付状态查询
     * @param outtradeno
     * @return
     */
    @GetMapping(value = "/status/query")
    public Result queryStatus(String outtradeno){
        //查询支付状态
        Map map = wxPayService.queryStatus(outtradeno);
        return new Result(true,StatusCode.OK,"查询支付状态成功!",map);
    }

    /**
     * 创建二维码
     * 普通订单:
     *      exchange: exchange.order
     *      routingkey: queue.order
     *
     * 秒杀订单:
     *      exchange: exchange.seckillorder
     *      routingkey: queue.seckillorder
     *
     * exchange + routingkey 的信息封装成JSON->attach到微信服务器,实现自定义数据传递
     *
     * @param parameterMap
     * @return
     */
    @RequestMapping(value = "/create/native")
    public Result createNative(@RequestParam Map<String,String> parameterMap){
        Map<String,String> resultMap = wxPayService.createnative(parameterMap);
        return new Result(true, StatusCode.OK,"创建二维码预付订单成功!",resultMap);
    }
}
