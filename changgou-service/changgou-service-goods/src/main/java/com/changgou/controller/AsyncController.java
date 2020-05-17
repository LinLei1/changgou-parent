package com.changgou.controller;

import com.changgou.mockQueue.DeferredResultHolder;
import com.changgou.mockQueue.MockQueue;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

@RestController
public class AsyncController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MockQueue mockQueue;

    @Autowired
    DeferredResultHolder deferredResultHolder;

    /**
     * 典型的同步处理方式
     * @return
     * @throws Exception
     */
    @RequestMapping("/order")
    public String order()throws Exception{
        logger.info("主线程开始");
        Thread.sleep(1000);
        logger.info("主线程返回");
        return "success";
    }

    /**
     * 异步处理方式,不妨碍tomcat主线程执行,使用这种异步处理方式,服务器吞吐量会有一个比较大的提升,因为在副线程执行业务逻辑的时候.
     * 主线程还可以去处理http请求,但这种方式,副线程由主线程发起
     * @return
     * @throws Exception
     */
    @RequestMapping("/order1")
    public Callable<String> order1()throws Exception{
        logger.info("主线程开始");

        Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                logger.info("副线程开始");
                Thread.sleep(1000);
                logger.info("副线程返回");
                return "success";
            }
        };
        logger.info("主线程返回");
        return result;
    }

    @RequestMapping("/order2")
    public DeferredResult<String> order2()throws Exception {
        logger.info("主线程开始");

        String orderNumber = RandomStringUtils.randomNumeric(8);
        mockQueue.setPlaceOrder(orderNumber);

        DeferredResult<String> result = new DeferredResult<>();
        deferredResultHolder.getMap().put(orderNumber, result);  //返回结果给浏览器
        logger.info("主线程返回");
        return result;
    }
}
