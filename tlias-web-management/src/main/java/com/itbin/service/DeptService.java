package com.itbin.service;

import com.itbin.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> findAll();
    void deleteById(Integer id);

    void addDept(Dept dept);

    Dept findByid(Integer deptId);

    void update(Dept dept);
}
