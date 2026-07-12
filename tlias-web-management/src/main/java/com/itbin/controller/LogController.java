package com.itbin.controller;


import com.itbin.pojo.OperateLog;
import com.itbin.pojo.PageResult;
import com.itbin.pojo.Result;
import com.itbin.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
//@RequestMapping("/log")
public class LogController {


    @Autowired
    private LogService logService;
    //分页展示员工操作日志
    @GetMapping("/log/page")
    public Result page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        log.info("分页查询员工操作日志，参数：page={},pageSize={}",page,pageSize);
        PageResult<OperateLog> pageResult=logService.page(page,pageSize);
        return Result.success(pageResult);

    }

}
