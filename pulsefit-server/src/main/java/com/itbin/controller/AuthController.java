package com.itbin.controller;

import com.itbin.pojo.AuthSession;
import com.itbin.pojo.Result;
import com.itbin.pojo.Staff;
import com.itbin.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/auth/login")
    public Result login(@RequestBody Staff staff) {
        log.info("员工登录，用户名：{}", staff.getUsername());
        AuthSession authSession = staffService.login(staff);
        if (authSession != null) {
            return Result.success(authSession);
        }
        return Result.error("用户名或密码错误");
    }
}
