package com.itbin.service;

import com.itbin.pojo.*;

import java.util.List;

public interface StaffService {
    PageResult<Staff> page(StaffQueryParam staffQueryParam);

    void add(Staff staff);

    void deleteStaffById(Integer[] ids);

    Staff findById(Integer id);

    void update(Staff staff);

    List<Staff> findAll();

    AuthSession login(Staff staff);
}
