package com.changgou.oauth.interceptor;

import com.changgou.oauth.util.AdminToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * 定义拦截器，在feign调用user前拦截，生成管理员令牌获取用户数据,不需要再用户微服务配置该路径放行了，因为不安全
 */
@Configuration
public class TokenRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //生成admin令牌
        String token = AdminToken.adminToken();
        requestTemplate.header("Authorization","bearer "+token);
    }
}
