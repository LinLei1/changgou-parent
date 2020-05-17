package com.changgou.order.mq.queue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    /***
     * 创建Queue1 延时队列 会过期,过期后将数据发给Queue2
     */
    @Bean
    public Queue orderDelayQueue(){
        return QueueBuilder
                .durable("orderDelayQueue")
                .withArgument("x-dead-letter-exchange","orderListenerExchange")  //此队列中消息过期之后,通过死信交换机,转发到死信队列
                .withArgument("x-dead-letter-routing-key","orderListenerQueue")  //指定路由键
                .build();
    }

    /**
     * 创建死信队列Queue2
     * @return
     */
    @Bean
    public Queue orderListenerQueue(){
        return new Queue("orderListenerQueue",true);
    }

    /**
     * 创建死信交换机
     */
    @Bean
    public Exchange orderListenerExchange(){
        return new DirectExchange("orderListenerExchange");
    }

    /***
     * 死信队列绑定死信交换机
     */
    @Bean
    public Binding orderListenerBinding(Queue orderListenerQueue,Exchange orderListenerExchange){
        return BindingBuilder.bind(orderListenerQueue).to(orderListenerExchange).with("orderListenerQueue").noargs();
    }
}
