package com.itbin.service;

import com.itbin.pojo.Result;
import com.itbin.pojo.clazzOption;
import com.itbin.pojo.jobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    jobOption getEmpJobData();

    List<Map<String, Object>> getEmpGenderData();

    clazzOption getStudentCountData();

    List<Map<String, Object>> getStudentDegreeData();
}
