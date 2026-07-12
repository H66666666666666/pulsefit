package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.Department;
import com.itbin.pojo.Result;
import com.itbin.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public Result list() {
        log.info("查询全部运营部门数据");
        List<Department> departmentList = departmentService.findAll();
        return Result.success(departmentList);
    }

    @Log
    @DeleteMapping
    public Result delete(Integer id) {
        log.info("根据id删除运营部门数据：" + id);
        departmentService.deleteById(id);
        return Result.success();
    }

    @Log
    @PostMapping
    public Result add(@RequestBody Department department) {
        log.info("新增运营部门：" + department);
        departmentService.addDepartment(department);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer id) {
        log.info("查询运营部门信息：" + id);
        Department department = departmentService.findById(id);
        return Result.success(department);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Department department) {
        log.info("修改运营部门信息：" + department);
        departmentService.update(department);
        return Result.success();
    }
}
