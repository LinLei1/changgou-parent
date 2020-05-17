package com.changgou.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;
//@Component  //在这加这个注解是全局过滤,局部过滤参考config
public class TimeFilter implements Filter {
    /**
     * 初始化一些数据
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("my filter init");
    }

    /**
     * 过滤逻辑处理,统计所有请求耗时
     * @param request
     * @param response
     * @param chain: 过滤器链
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("time filter start");
        long start = new Date().getTime();
        chain.doFilter(request,response);
        System.out.println("time filter 耗时:"+(new Date().getTime() - start));
        System.out.println("time filter finish");
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        System.out.println("my filter destory");
    }
}
