package com.changgou.filter;

import com.changgou.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器
 * 实现用户权限鉴别(校验)
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    //指定令牌的名字
    private static final String AUTHORIZE_TOKEN = "Authorization";

    /**
     * 全局拦截
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //用户如果是登录或者是一些不需要校验权限的请求，直接放行
        String uri = request.getURI().toString();
        if (!URLFilter.hasAuthorize(uri)){
            return chain.filter(exchange);
        }
        //获取用户令牌信息
        //优先从头里面获取token信息
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        //boolean true: 令牌在头文件中 false: 令牌不在头文件中->将令牌封装到头文件中,再传递给其他微服务
        boolean hasToken = true;

        //从请求参数中获取令牌
        if (StringUtils.isEmpty(token)){
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            hasToken = false;
        }

        //从cookie中获取
        if (StringUtils.isEmpty(token)){
            HttpCookie httpCookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if (httpCookie!=null){
                token = httpCookie.getValue();
            }
        }


        //令牌为空,则不允许访问,直接拦截 bearer前缀一定要
        if (StringUtils.isEmpty(token)){
            //设置没有权限的状态码 401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //响应空数据
            return response.setComplete();
        }else if (hasToken){
                //判断当前令牌是否有bearer前缀,如果没有,则添加前缀bearer
                if (!token.startsWith("bearer ")&&!token.startsWith("Bearer")){
                    token = "bearer "+token;
                }
            }else {
                //判断当前令牌是否有bearer前缀,如果没有,则添加前缀bearer
                if (!token.startsWith("bearer ")&&!token.startsWith("Bearer")){
                    token = "bearer "+token;
                    //在放行之前,将令牌封装到头文件中,不然Oauth2.0框架没法校验令牌
                    request.mutate().header(AUTHORIZE_TOKEN,token);
                }
            }
        //有效放行
        return chain.filter(exchange);
    }

    /**
     * 排序  越小越先执行
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
