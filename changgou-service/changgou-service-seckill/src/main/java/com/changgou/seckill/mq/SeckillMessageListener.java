package com.changgou.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.service.SeckillOrderService;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "${mq.pay.queue.seckillorder}")
public class SeckillMessageListener {

    @Autowired
    SeckillOrderService seckillOrderService;

    /****
     * 消息监听
     * @param message
     */
    @RabbitHandler
    public void getMessage(String message){
        try {
            //将支付消息转为Map
            Map<String,String> resultMap = JSON.parseObject(message,Map.class);
            //return_code->通信标识-SUCCESS
            String return_code = resultMap.get("return_code");
            //out_trade_no->订单号
            String out_trade_no = resultMap.get("out_trade_no");
            //获取自定义数据
            String attach = resultMap.get("attach");
            if (return_code.equals("SUCCESS")){
                //result_code->业务结果-SUCCESS->修改订单状态
                String result_code = resultMap.get("result_code");
                Map<String,String> attatchMap = JSON.parseObject(attach,Map.class);
                if (result_code.equals("SUCCESS")){
                    //修改订单状态
                    seckillOrderService.updatePayStatus(attatchMap.get("username"),resultMap.get("transaction_id"),resultMap.get("time_end"));
                    //清理用户排队信息

                }else {
                    //删除订单[真实工作中逻辑删除,即存入到Mysql] ->回滚库存
                    seckillOrderService.deleteOrder(attatchMap.get("username"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
