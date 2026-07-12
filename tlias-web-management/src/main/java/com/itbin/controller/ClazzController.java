package com.itbin.controller;


import com.itbin.anno.Log;
import com.itbin.exception.ClazzDeleteException;
import com.itbin.pojo.Clazz;
import com.itbin.pojo.ClazzQueryParam;
import com.itbin.pojo.PageResult;
import com.itbin.pojo.Result;
import com.itbin.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    //分页查询班级
    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam){
        log.info("分页查询，参数：{}",clazzQueryParam);
        PageResult<Clazz> pageResult=clazzService.page(clazzQueryParam);
        return Result.success(pageResult);
    }

    //新增班级
    //1.显示所有员工-查询全部员工
    //在emp上
    //2。新增员工
    @Log
    @PostMapping
    public Result add(@RequestBody Clazz clazz){
        log.info("新增班级，班级信息：{}",clazz);
        clazzService.add(clazz);
        return Result.success();
    }

    //修改班级信息
    //1.回显班级信息-根据id查询班级信息
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer Id){
        log.info("查询班级信息：{}",Id);
        Clazz clazz =  clazzService.findByid(Id);
        return Result.success(clazz);
    }
    //2.修改班级信息
    @Log
    @PutMapping
    public Result update(@RequestBody Clazz clazz){
        log.info("修改班级信息：{}",clazz);
        clazzService.update(clazz);
        return Result.success();
    }


    //根据id删除班级
    @Log
    @DeleteMapping("/{id}")
    public Result delete( @PathVariable Integer id) throws ClazzDeleteException {
        //System.out.println("根据id删除部门数据："+id);
        log.info("根据id删除班级数据：{}", id);
        clazzService.deleteByid(id);
        return Result.success();
    }

    //查询全部班级
    @GetMapping("/list")
    public Result list(){
        log.info("查询全部班级数据");
        return Result.success(clazzService.findAll());
    }



}
