package com.itbin.mapper;


import com.itbin.pojo.EmpExpr;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExprMapper {
    void addEmpExpr(List<EmpExpr> exprList);
}
