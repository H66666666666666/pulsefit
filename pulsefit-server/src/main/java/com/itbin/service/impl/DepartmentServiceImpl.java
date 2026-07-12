package com.itbin.service.impl;

import com.itbin.mapper.DepartmentMapper;
import com.itbin.pojo.Department;
import com.itbin.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> findAll() {
        return departmentMapper.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        departmentMapper.deleteById(id);
    }

    @Override
    public void addDepartment(Department department) {
        department.setCreateTime(LocalDateTime.now());
        department.setUpdateTime(LocalDateTime.now());
        departmentMapper.insert(department);
    }

    @Override
    public Department findById(Integer id) {
        return departmentMapper.findById(id);
    }

    @Override
    public void update(Department department) {
        department.setUpdateTime(LocalDateTime.now());
        departmentMapper.update(department);
    }
}
