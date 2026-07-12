package com.itbin.controller;

import com.itbin.pojo.OperationLog;
import com.itbin.pojo.PageResult;
import com.itbin.pojo.Result;
import com.itbin.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/operation-logs")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping
    public Result page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("分页查询操作日志，参数：page={}, pageSize={}", page, pageSize);
        PageResult<OperationLog> pageResult = operationLogService.page(page, pageSize);
        return Result.success(pageResult);
    }
}
