package com.changgou.mockQueue;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于在http请求处理线程与http请求响应线程之间来传递result的对象
 */
@Component
public class DeferredResultHolder {

    /**
     * String: 可以理解为订单号
     * DeferredResult: 理解为订单处理结果
     */
    private Map<String, DeferredResult<String>> map = new HashMap<>();

    public Map<String, DeferredResult<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, DeferredResult<String>> map) {
        this.map = map;
    }
}
