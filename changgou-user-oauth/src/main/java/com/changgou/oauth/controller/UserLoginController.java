package com.changgou.oauth.controller;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.util.AuthToken;
import com.changgou.oauth.util.Result;
import com.changgou.oauth.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserLoginController {

    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //客户端密钥
    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Autowired
    private UserLoginService userLoginService;

    /**
     * 登录方法
     */
    @RequestMapping(value = "/login")
    public Result login(String username,String password)throws Exception{
        //调用userLoginService实现登录
        String grant_type = "password";
        AuthToken authToken = userLoginService.login(username, password, clientId, clientSecret, grant_type);
        if (authToken!=null){
            return new Result(true, StatusCode.OK,"登录成功!",authToken);
        }
        return new Result(true,StatusCode.LOGINERROR,"登录失败!");
    }
}
