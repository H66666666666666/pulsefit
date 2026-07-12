package com.itbin.service;

import com.itbin.pojo.Emp;
import com.itbin.pojo.EmpQueryParam;
import com.itbin.pojo.LoginInfo;
import com.itbin.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {



    PageResult<Emp> page(EmpQueryParam empQueryParam);

    void add(Emp emp);

    void deleteEmpById(Integer[] ids);

    Emp findByid(Integer Id);

    void update(Emp emp);

    List<Emp> findAll();

    LoginInfo login(Emp emp);
}
