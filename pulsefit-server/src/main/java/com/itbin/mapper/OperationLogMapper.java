package com.itbin.mapper;

import com.itbin.pojo.OperationLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OperationLogMapper {

    @Insert("INSERT INTO pf_operation_log(operator_id, operation_time, class_name, method_name, method_params, return_value, cost_time) " +
            "VALUES(#{operatorId}, #{operationTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime})")
    void insert(OperationLog operationLog);

    @Select("SELECT pol.*, ps.name AS operatorName FROM pf_operation_log pol LEFT JOIN pf_staff ps ON pol.operator_id = ps.id ORDER BY pol.operation_time DESC")
    List<OperationLog> page();
}
