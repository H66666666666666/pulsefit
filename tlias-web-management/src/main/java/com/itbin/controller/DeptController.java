package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.Dept;
import com.itbin.pojo.Result;
import com.itbin.service.DeptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.ResultMap;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j//日志记录
@RequestMapping("/depts")
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
public class DeptController {

    //private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeptController.class);日志记录


    @Autowired
    private DeptService deptService;

    @GetMapping
   public Result list() {
        //ystem.out.println("查询全部部门数据");
        log.info("查询全部部门数据");
        List<Dept> deptList =deptService.findAll();
        return Result.success(deptList);
   }

   //方式一：HttpServletRequest 获取请求数据
    /*
   @DeleteMapping("/depts")
    public Result delete(HttpServletRequest request) {
        System.out.println("根据id删除部门数据");
        String idStr =request.getParameter("id");
        int id=Integer.parseInt(idStr);
        deptService.deleteById(id);
        return Result.success();
   }
   */

    //方式二：@RequestParam 获取请求数据
    //一旦声明了@RequestParam,该参数在请求时必须传递，如果不传递将会报错（默认required为ture）
    /*
    @DeleteMapping("/depts")
    public Result delete(@RequestParam(value = "id", required = false) Integer id) {
        System.out.println("根据id删除部门数据");
        deptService.deleteById(id);
        return Result.success();
    }
     */

    //方式三：如果请求参数名和形参变量名相同，直接定义方法形参即可接收
    @Log
    @DeleteMapping
    public Result delete(Integer id) {
        //System.out.println("根据id删除部门数据："+id);
        log.info("根据id删除部门数据："+id);
        deptService.deleteById(id);
        return Result.success();
    }


    //新增部门
    @Log
    @PostMapping
    //@RequestBody 使得 /depts 接口能够直接接收并解析前端发送的部门信息 JSON 数据，赋值给 dept 参数。
    public Result add(@RequestBody Dept dept) {
        //System.out.println("新增部门："+dept);
        log.info("新增部门："+dept);
        deptService.addDept(dept);
        return Result.success();

    }

    //修改部门
    //1.查询回显
    //根据id查询部门
    //@PathVariable声明获取路径参数
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer deptId){
        //System.out.println("查询部门信息："+deptId);
        log.info("查询部门信息："+deptId);
        Dept dept =  deptService.findByid(deptId);
        return Result.success(dept);
    }
    //2.根据id修改
    @Log
    @PutMapping
    public Result update(@RequestBody Dept dept){
        //System.out.println("修改部门信息");
        log.info("修改部门信息："+dept);
        deptService.update(dept);
        return Result.success();
    }



}

