package com.changgou.config;

import com.changgou.filter.TimeFilter;
import com.changgou.intercepter.TimeIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

//@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private TimeIntercepter timeIntercepter;

    /**
     * 用于处理多线程的拦截器
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        //configurer.registerCallableInterceptors()
    }

    /**
     * 覆盖默认拦截器配置
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeIntercepter);
    }

    /**
     * 过滤器配置
     * @return
     */
    @Bean
    public FilterRegistrationBean timeFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TimeFilter timeFilter = new TimeFilter();
        registrationBean.setFilter(timeFilter);
        List<String> urls = new ArrayList<>();
        urls.add("/*");  //指定过滤的url
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }
}
