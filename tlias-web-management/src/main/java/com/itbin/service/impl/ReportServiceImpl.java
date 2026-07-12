package com.itbin.service.impl;

import com.itbin.mapper.EmpMapper;
import com.itbin.mapper.StudentMapper;
import com.itbin.pojo.Result;
import com.itbin.pojo.clazzOption;
import com.itbin.pojo.jobOption;
import com.itbin.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Override
    public jobOption getEmpJobData() {
        //调用Mapper接口，获取统计数据
        List<Map<String,Object>> List=empMapper.countEmpJobData();
        //组装结果，并返回
        List<Object> jobList = List.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = List.stream().map(dataMap -> dataMap.get("num")).toList();
        return new jobOption(jobList,dataList);
    }

    @Override
    public List<Map<String, Object>> getEmpGenderData() {
       return empMapper.countEmpGenderData();
    }

    @Override
    public clazzOption getStudentCountData() {
        //调用Mapper接口，获取统计数据
        List<Map<String,Object>> List=studentMapper.countStudentCountData();
        //组装结果，并返回
       List< Object> clazzList=List.stream().map(dataMap -> dataMap.get("clazzName")).toList();
       List< Object> dataList=List.stream().map(dataMap -> dataMap.get("num")).toList();
       return new clazzOption(clazzList,dataList);
    }

    @Override
    public List<Map<String, Object>> getStudentDegreeData() {
        return studentMapper.countStudentDegreeData();
    }
}
