package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

public interface UserLoginService {
    /**
     * 用户登录(密码模式)
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @param grant_type
     * @return
     */
    AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type) throws Exception;
}
