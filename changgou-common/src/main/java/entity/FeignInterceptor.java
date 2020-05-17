package entity;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

/**
 * 定义拦截器，处理服务间令牌传递问题，在feign调用前将令牌封装到头中
 * 哪个微服务要用，直接new 一个@Bean 注入就行
 */
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        //记录了当前用户请求的所有数据，包括请求头和请求参数等
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        /**
         * 获取请求头中数据
         * 获取所有头的名字
         */
        Enumeration<String> headerNames = requestAttributes.getRequest().getHeaderNames();

        while (headerNames.hasMoreElements()){
            //请求头的key
            String headerKey = headerNames.nextElement();
            //获取请求头中的值
            String headerValue = requestAttributes.getRequest().getHeader(headerKey);

            //将请求头中信息封装到头中，使用Feign调用的时候，会传递给下一个微服务
            requestTemplate.header(headerKey,headerValue);
        }
    }
}
