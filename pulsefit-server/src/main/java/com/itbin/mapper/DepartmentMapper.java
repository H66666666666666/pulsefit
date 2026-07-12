package com.itbin.mapper;

import com.itbin.pojo.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    @Select("select id, name, create_time, update_time from pf_department order by update_time desc")
    List<Department> findAll();

    @Delete("delete from pf_department where id=#{id}")
    void deleteById(Integer id);

    @Insert("insert into pf_department(name, create_time, update_time) values(#{name},#{createTime},#{updateTime})")
    void insert(Department department);

    @Select("select id, name, create_time, update_time from pf_department where id=#{id}")
    Department findById(Integer id);

    @Update("update pf_department set name=#{name},update_time=#{updateTime} where id=#{id}")
    void update(Department department);
}
