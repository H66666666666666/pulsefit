package com.itbin.service;

import com.itbin.pojo.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> findAll();

    void deleteById(Integer id);

    void addDepartment(Department department);

    Department findById(Integer id);

    void update(Department department);
}
