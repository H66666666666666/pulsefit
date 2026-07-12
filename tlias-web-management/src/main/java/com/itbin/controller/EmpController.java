package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.Emp;
import com.itbin.pojo.EmpQueryParam;
import com.itbin.pojo.PageResult;
import com.itbin.pojo.Result;
import com.itbin.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;


/**
 * REST控制器注解
 *
 * 该注解用于标记一个类作为RESTful Web服务的控制器，
 * 它是@Controller和@ResponseBody注解的组合注解。
 *
 * 使用此注解的类中的所有处理方法都会自动继承@ResponseBody注解，
 * 将返回值直接写入HTTP响应体中，而不是解析为视图名称。
 *
 * 此注解通常用于构建基于REST架构风格的Web API接口。
 */
@RestController
@Slf4j
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    @GetMapping
    public Result page(EmpQueryParam empQueryParam){

        log.info("分页查询，参数：{}",empQueryParam);
        PageResult<Emp> pageResult=empService.page(empQueryParam);
        return Result.success(pageResult);

    }

    @Log
    @PostMapping
    public Result add(@RequestBody Emp emp){
        log.info("新增员工，员工信息：{}",emp);
        empService.add(emp);
        return Result.success();
    }


    //根据id删除员工
    @Log
    @DeleteMapping
    public Result delete(Integer[] ids) {
        //System.out.println("根据id删除部门数据："+id);
        log.info("根据id删除员工数据：{}", Arrays.toString(ids));
        empService.deleteEmpById(ids);
        return Result.success();
    }

    //修改员工信息
    //1.查询回显
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer Id){
        log.info("查询员工信息：{}",Id);
        Emp emp =  empService.findByid(Id);
        return Result.success(emp);
    }
    //2.修改员工信息
    @Log
    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("修改员工信息：{}",emp);
        empService.update(emp);
        return Result.success();
    }


    //查询全部员工
    @GetMapping("/list")
    public Result list(){
        log.info("查询全部员工数据");
        return Result.success(empService.findAll());
    }

}
