package com.itbin.mapper;


import com.itbin.pojo.Clazz;
import com.itbin.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClazzMapper {
    //分页查询班级
    List<Clazz> list(ClazzQueryParam clazzQueryParam);

    void add(Clazz clazz);

    Clazz findByid(Integer id);

    void update(Clazz clazz);

    void deleteByid(Integer id);

    List<Clazz> findAll();

    //检查班级是否有关联学生的方法


}
