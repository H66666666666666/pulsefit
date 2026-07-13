package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.FinanceRecord;
import com.itbin.pojo.Result;
import com.itbin.service.FinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/finances")
public class FinanceController {
    @Autowired
    private FinanceService financeService;

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String type, String startDate, String endDate) {
        return Result.success(financeService.page(page, pageSize, type, startDate, endDate));
    }

    @Log
    @PostMapping
    public Result add(@RequestBody FinanceRecord record) {
        financeService.add(record);
        return Result.success();
    }

    @GetMapping("/summary")
    public Result summary(String startDate, String endDate) {
        return Result.success(financeService.summary(startDate, endDate));
    }
}
