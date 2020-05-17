package com.changgou.seckill.controller;

import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SecKillGoodsService;
import entity.DateUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/seckill/goods")
public class SecKillGoodsController {

    @Autowired
    private SecKillGoodsService secKillGoodsService;

    /**
     * 根据时间和秒杀商品ID查询秒杀商品详情
     * @param time
     * @param id
     * @return
     */
    @GetMapping(value = "/one")
    public Result<SeckillGoods> one(String time,Long id){
        SeckillGoods seckillGoods = secKillGoodsService.one(time, id);
        return new Result<>(true,StatusCode.OK,"查询秒杀商品详情成功!",seckillGoods);
    }

    /**
     * 查询秒杀商品时间菜单
     * @return
     */
    @GetMapping(value = "/menus")
    public Result<List<Date>> menus(){
        List<Date> dateMenus = DateUtil.getDateMenus();
        return new Result<>(true,StatusCode.OK,"查询秒杀时间菜单成功!",dateMenus);
    }

    /**
     * 根据时间区间,查询秒杀商品频道列表数据
     * @param time
     * @return
     */
    @RequestMapping("/list")
    public Result<List<SeckillGoods>> list(@RequestParam("time") String time){
        List<SeckillGoods> seckillGoodsList = secKillGoodsService.list(time);
        return new Result<>(true, StatusCode.OK,"查询成功",seckillGoodsList);
    }
}
