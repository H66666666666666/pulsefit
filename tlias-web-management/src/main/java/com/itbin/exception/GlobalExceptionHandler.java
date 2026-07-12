package com.itbin.exception;

import com.itbin.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
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

     //添加新的异常处理方法
    @ExceptionHandler(ClazzDeleteException.class)
    public Result handleClazzDeleteException(ClazzDeleteException e) {
        log.error("班级删除异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("参数异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }
}
