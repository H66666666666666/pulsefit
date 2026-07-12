package com.itbin.controller;

import com.itbin.pojo.Result;
import com.itbin.pojo.clazzOption;
import com.itbin.pojo.jobOption;
import com.itbin.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;



    //职位人数统计
    @RequestMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("职位人数统计");
        jobOption jobOption=reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    //统计性别人数
    @RequestMapping("/empGenderData")
    public Result getEmpGenderData(){
        log.info("统计性别人数");
        List<Map<String,Object>>  genderlist=reportService.getEmpGenderData();
        return Result.success(genderlist);
    }

    //班级人数统计
    @RequestMapping("/studentCountData")
    public Result getStudentCountData(){
        log.info("班级人数统计");
        clazzOption studentCountlist=reportService.getStudentCountData();
        return Result.success(studentCountlist);
    }

    //统计学员学历
    @RequestMapping("/studentDegreeData")
    public Result getStudentDegreeData(){
        log.info("统计学员学历");
        List<Map<String,Object>>  degreeList=reportService.getStudentDegreeData();
        return Result.success(degreeList);
    }


}
