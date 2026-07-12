package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.PageResult;
import com.itbin.pojo.Result;
import com.itbin.pojo.Student;
import com.itbin.pojo.StudentQueryParam;
import com.itbin.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    //分页查询学生
    @GetMapping
    public Result page(StudentQueryParam studentQueryParam){
        log.info("分页查询，参数：{}",studentQueryParam);
        PageResult<Student> pageResult=studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }


    //新增学员
    @Log
    @PostMapping
    public Result add(@RequestBody Student student){
        log.info("新增员工，员工信息：{}",student);
        studentService.add(student);
        return Result.success();
    }

    //修改学员信息
    //1.查询回显-根据id查询员工信息
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer Id){
        log.info("查询员工信息：{}",Id);
        Student student=studentService.findByid(Id);
        return Result.success(student);
    }
    //2.修改员工信息
    @Log
    @PutMapping
    public Result update(@RequestBody Student student){
        log.info("修改员工信息：{}",student);
        studentService.update(student);
        return Result.success();
    }

    //删除学员
    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer[] ids){
        log.info("删除员工：{}",ids);
        studentService.deleteEmpById(ids);
        return Result.success();
    }

    //违纪处理-违纪处理一次，需要将违纪次数+1，违纪扣分+前端输入的分数。
//    @PutMapping("/discipline/{id}/{score}")
//    public Result discipline(@PathVariable("id") Integer id,@PathVariable("score") Short score){
//        log.info("处理违纪，员工id：{}，扣分：{}",id,score);
//        studentService.discipline(id,score);
//        return Result.success();
//    }

    @PutMapping("/violation/{id}/{score}")
    public Result discipline(@PathVariable("id") Integer id, @PathVariable("score") Short score) {
        log.info("处理违纪，员工id：{}，扣分：{}", id, score);
        studentService.discipline(id, score);
        return Result.success();
    }


}
