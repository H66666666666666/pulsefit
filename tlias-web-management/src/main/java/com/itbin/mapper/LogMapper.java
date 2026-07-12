package com.itbin.mapper;


import com.itbin.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {


    @Select("select ol.*, emp.name as operateEmpName from operate_log ol left join emp on ol.operate_emp_id = emp.id")
    List<OperateLog> list();
}
