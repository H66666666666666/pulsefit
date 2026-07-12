package com.itbin.exception;

import com.itbin.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("请求路径不存在：{} {}", e.getHttpMethod(), e.getRequestURL());
        return Result.error("请求的资源不存在");
    }

    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("服务器发生异常：{}", e.getMessage());
        return Result.error("服务器发生异常");
    }

    //重复异常处理
    @ExceptionHandler
    public Result handleException(DuplicateKeyException e) {
        log.error("重复异常：{}", e.getMessage());
        String message = e.getMessage();
        int i=message.indexOf("Duplicate entry");
        String errMsg = message.substring(i);
        String[] arr=errMsg.split(" ");
        return Result.error(arr[2]+"已存在");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("参数异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }
}
