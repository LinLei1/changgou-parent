package com.changgou.seckill.controller;

import com.changgou.seckill.service.SeckillOrderService;
import entity.Result;
import entity.SeckillStatus;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/seckill/order")
public class SeckillOrderController {

    @Autowired
    SeckillOrderService seckillOrderService;


    /**
     * 抢购状态查询
     * @return
     */
    @GetMapping(value = "/query")
    public Result queryStatus(){
        String username = "szitheima";
        SeckillStatus seckillStatus = seckillOrderService.queryStatus(username);
        if (seckillStatus!=null){
            return new Result(false,StatusCode.OK,"订单查询成功!",seckillStatus);
        }
        return new Result(false,StatusCode.NOTFOUNDERROR,"没有抢购信息!");
    }


    /**
     * 添加秒杀订单
     * @param time
     * @param id
     * @return
     */
    @RequestMapping(value = "/add")
    public Result add(String time,Long id,String username){
        //String username = "szitheima";
        seckillOrderService.add(time,id,username);
        return new Result(true, StatusCode.OK,"正在排队中...");
    }
}
