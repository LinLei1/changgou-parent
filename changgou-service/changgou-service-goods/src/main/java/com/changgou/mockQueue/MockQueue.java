package com.changgou.mockQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 模拟使用消息队列完成下单操作
 */
@Component
public class MockQueue {

    private String placeOrder;  //代表下单的消息,当它有值,就认为消息队列接到一个下单的消息

    private String completeOrder;  //代表订单完成的消息,当它有值,就认为消息队列接到一个订单完成的消息

    private Logger logger = LoggerFactory.getLogger(getClass());

    public String getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(String placeOrder) {
        new Thread(()->{
            logger.info("接到下单请求" + placeOrder);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.completeOrder = placeOrder;  //这个属性设上值就认为订单完成
            logger.info("下单完成!" + placeOrder);
        }).start();
    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}
