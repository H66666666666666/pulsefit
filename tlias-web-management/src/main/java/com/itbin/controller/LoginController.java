package com.itbin.controller;


import com.itbin.pojo.Emp;
import com.itbin.pojo.LoginInfo;
import com.itbin.pojo.Result;
import com.itbin.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController

public class LoginController {

    @Autowired
    private EmpService empService;
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp){
        log.info("员工登录，员工信息：{}",emp);
        LoginInfo loginInfo = empService.login(emp);
        if (loginInfo != null){
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误");
    }

}
