package com.changgou.seckill.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    /***
     * 创建Queue1 延时队列负责数据暂时存储 会过期,过期后将数据发给Queue2
     */
    @Bean
    public Queue delaySeckillQueue(){
        return QueueBuilder
                .durable("delaySeckillQueue")
                .withArgument("x-dead-letter-exchange","seckillExchange")  //此队列中消息过期之后,进入死信队列交换机,将过期消息转发到死信队列
                .withArgument("x-dead-letter-routing-key","seckillQueue")  //指定路由键
                .build();
    }

    /**
     * 真正被监听的消息队列   Queue2
     *
     */
    @Bean
    public Queue seckillQueue(){
        return new Queue("seckillQueue");
    }

    /***
     * 秒杀 死信队列交换机
     *
     */
    @Bean
    public Exchange seckillExchange(){
        return new DirectExchange("seckillExchange");
    }

    /**
     * 死信队列绑定死信交换机
     *
     */
    @Bean
    public Binding seckillQueueBindingExchange(Queue seckillQueue,Exchange seckillExchange){
        return BindingBuilder.bind(seckillQueue).to(seckillExchange).with("seckillQueue").noargs();
    }
}
