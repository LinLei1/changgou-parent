package com.changgou.order.controller;

import com.changgou.order.config.TokenDecode;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 添加购物车
     * @param skuId：商品ID
     * @param num：购买数量
     * @return
     */
    @GetMapping("/addCart")
    public Result addCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num){
        Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("username");
        cartService.addCart(skuId,num,username);
        return new Result(true, StatusCode.OK,"添加购物车成功！");
    }

    /**
     * 获取购物车中商品信息
     * @return
     */
    @GetMapping("/list")
    public Result<Map<String, String>> list(){
        Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("username");
        Map resultMap = cartService.list(username);
        return new Result<Map<String,String>>(true,StatusCode.OK,"查询购物车成功！",resultMap);
    }
}
