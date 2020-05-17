package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name ="user")
@RequestMapping("/user")
public interface UserFeign {

    /***
     * 添加用户积分
     * @param points
     * @return
     */
    @GetMapping(value = "/points/add")
    Result addPoints(@RequestParam Integer points);
    /**
     * 调用搜索实现
     */
    @GetMapping
    Map search(@RequestParam(required = false) Map<String, String> searchMap) throws Exception;

    /**
     * 根据ID查询用户信息
     *
     */
    @GetMapping({"/load/{id}"})
    Result<User> findById(@PathVariable String id);
}
