package com.changgou.intercepter;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 自定义拦截器,进行请求拦截统计时间
 *
 */
//@Component
public class TimeIntercepter implements HandlerInterceptor {

    /**
     * 在控制器方法执行前执行
     * @param request
     * @param response
     * @param handler: 控制器方法对象,拿不到方法中具体入参的值
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle");

        System.out.println(((HandlerMethod)handler).getBean().getClass().getName());
        System.out.println(((HandlerMethod)handler).getMethod().getName());

        request.setAttribute("startTime",new Date().getTime());
        return true;
    }

    /**
     * 在控制器方法执行后执行,如果控制器方法抛异常,此方法不会被执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
        Long start = (Long) request.getAttribute("startTime");
        System.out.println("time intercepter 耗时: " +(new Date().getTime()-start));
    }

    /**
     * 这个方法无论有无异常都会被调用,前提是异常没有被处理掉,处理掉了此方法不会执行
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.println("afterCompletion");
        Long start = (Long) request.getAttribute("startTime");
        System.out.println("time intercepter 耗时: " +(new Date().getTime()-start));
        System.out.println("ex is :" +ex);
    }
}
