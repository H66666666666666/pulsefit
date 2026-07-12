package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.PageResult;
import com.itbin.pojo.Result;
import com.itbin.pojo.Staff;
import com.itbin.pojo.StaffQueryParam;
import com.itbin.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@Slf4j
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public Result page(StaffQueryParam staffQueryParam) {
        log.info("分页查询教练与员工，参数：{}", staffQueryParam);
        PageResult<Staff> pageResult = staffService.page(staffQueryParam);
        return Result.success(pageResult);
    }

    @Log
    @PostMapping
    public Result add(@RequestBody Staff staff) {
        log.info("新增教练/员工，信息：{}", staff);
        staffService.add(staff);
        return Result.success();
    }

    @Log
    @DeleteMapping
    public Result delete(Integer[] ids) {
        log.info("根据id删除教练/员工数据：{}", Arrays.toString(ids));
        staffService.deleteStaffById(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer id) {
        log.info("查询教练/员工信息：{}", id);
        Staff staff = staffService.findById(id);
        return Result.success(staff);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Staff staff) {
        log.info("修改教练/员工信息：{}", staff);
        staffService.update(staff);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        log.info("查询全部教练/员工数据");
        return Result.success(staffService.findAll());
    }
}
