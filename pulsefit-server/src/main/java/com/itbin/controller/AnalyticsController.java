package com.itbin.controller;

import com.itbin.pojo.CampMemberOption;
import com.itbin.pojo.Result;
import com.itbin.pojo.StaffRoleOption;
import com.itbin.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/staff-roles")
    public Result getStaffRoleData() {
        log.info("岗位人数统计");
        StaffRoleOption staffRoleOption = reportService.getStaffRoleData();
        return Result.success(staffRoleOption);
    }

    @GetMapping("/staff-gender")
    public Result getStaffGenderData() {
        log.info("员工性别人数统计");
        List<Map<String, Object>> genderList = reportService.getStaffGenderData();
        return Result.success(genderList);
    }

    @GetMapping("/camp-members")
    public Result getCampMemberData() {
        log.info("训练营人数统计");
        CampMemberOption campMemberOption = reportService.getCampMemberData();
        return Result.success(campMemberOption);
    }

    @GetMapping("/membership-levels")
    public Result getMembershipLevelData() {
        log.info("会员级别统计");
        List<Map<String, Object>> levelList = reportService.getMembershipLevelData();
        return Result.success(levelList);
    }
}
