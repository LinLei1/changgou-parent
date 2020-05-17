package com.changgou.oauth.service.impl;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    //此对象用于实现请求发送
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @param grant_type
     * @return
     */
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type) throws Exception {
        //获取指定服务的注册数据
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        //调用的请求地址 http://localhost:9001/oauth/token
        String url = serviceInstance.getUri()+"/oauth/token";

        //请求提交的数据封装
        MultiValueMap<String,String> parameterMap = new LinkedMultiValueMap<>();
        parameterMap.add("username",username);
        parameterMap.add("password",password);
        parameterMap.add("grant_type",grant_type);

        //请求头封装
        String Authorization = "Basic "+ new String(Base64.getEncoder().encode((clientId+":"+clientSecret).getBytes()), "UTF-8");
        MultiValueMap<String,String> headerMap = new LinkedMultiValueMap<>();
        headerMap.add("Authorization",Authorization);

        //HttpEntity->创建该对象,封装了请求头和请求体
        HttpEntity httpEntity = new HttpEntity(parameterMap,headerMap);



        /**
         * 1: 请求地址
         * 2:提交方式
         * 3:requestEntity: 请求提交的数据封装 请求体|请求头
         */
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        //用户登录后的令牌信息
        Map<String,String> map = response.getBody();

        //将令牌信息转换为AuthToken对象
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(map.get("access_token"));
        authToken.setRefreshToken(map.get("refresh_token"));
        authToken.setJti(map.get("jti"));

        return authToken;
    }
}
