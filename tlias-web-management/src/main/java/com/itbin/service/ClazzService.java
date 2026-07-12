package com.itbin.service;

import com.itbin.exception.ClazzDeleteException;
import com.itbin.pojo.Clazz;
import com.itbin.pojo.ClazzQueryParam;
import com.itbin.pojo.PageResult;

import java.util.List;

public interface ClazzService {
    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);

    void add(Clazz clazz);

    Clazz findByid(Integer id);

    void update(Clazz clazz);

    void deleteByid(Integer id)throws ClazzDeleteException;


    List<Clazz> findAll();
}
