package com.changgou.controller;

import com.changgou.exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义全局异常处理器,可在处理方法中声明一个map集合,存储错误信息
 */
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = UserNotExistException.class)  //指定处理异常的类型
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  //http状态码,服务器内部错误
    public Map<String, Object> error(UserNotExistException e){
        Map<String,Object> result = new HashMap<>();
        result.put("id",e.getId());
        result.put("message",e.getMessage());
        return result;
    }
}
