package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.EmpExprMapper;
import com.itbin.mapper.EmpMapper;
import com.itbin.pojo.*;
import com.itbin.service.EmpLogService;
import com.itbin.service.EmpService;
import com.itbin.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;
//-----------------原始分页查询-----------------
    //分页查询
    //page 页码
    //pageSize 每页记录数
//    @Override
//    public PageResult<Emp> page(Integer page, Integer pageSize) {
//        //调用Mapper接口，查询总记录数
//        Long total=empMapper.count();
//
//        //调用Mapper接口，查询分页数据
//        Integer start=(page-1)*pageSize;
//        List<Emp> rows=empMapper.list();
//
//
//        //封装结果
//        return new PageResult<Emp>(total ,rows);


    //PageHelper分页查询

    @Transactional(rollbackFor = Exception.class)//默认出现运行异常才会回滚
    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        //设置分页查询参数
        PageHelper.startPage(empQueryParam.getPage(),empQueryParam.getPageSize());

        //执行查询
        List<Emp> rows=empMapper.list(empQueryParam);
        //解析查询结果，并封装成PageResult<Emp>
        Page<Emp> p=(Page<Emp>) rows;
        return new PageResult<Emp>(p.getTotal(),p.getResult());
    }

//    @Override
//    public void add(Emp emp) {
//        emp.setCreateTime(LocalDateTime.now());
//        emp.setUpdateTime(LocalDateTime.now());
//        empMapper.add(emp);
//
//        //保存员工的工作经历
//        List<EmpExpr> empExprs = emp.getEmpExprs();
//        if (!CollectionUtils.isEmpty(empExprs)){
//          //遍历集合
//          empExprs.forEach(empExpr -> {
//              empExpr.setEmpId(emp.getId());
//         });
//         empExprMapper.addEmpExpr(empExprs);
//        }
//   }
//        if (CollectionUtils.isEmpty(empExprs)) {
//            log.warn("员工 {} 没有工作经历", emp.getName());
//            return;
//        }
//
//        // 设置员工ID并批量插入
//        empExprs.forEach(expr -> expr.setEmpId(emp.getId()));
//        empExprMapper.addEmpExpr(empExprs);
//    }
// 修正后的完整逻辑

    // 在 EmpServiceImpl.java 中修正
    @Override
    public void add(Emp emp) {
        try {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.add(emp);

            // 保存员工的工作经历
            List<EmpExpr> exprList = emp.getExprList();
            if(!CollectionUtils.isEmpty(exprList)){
                //遍历集合, 为empId赋值
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.addEmpExpr(exprList);
            }
        } finally {
            EmpLog emplog = new EmpLog(null, LocalDateTime.now(), "添加员工"+emp);
            empLogService.insertLog(emplog);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteEmpById(Integer[] ids) {
        empMapper.deleteEmpById(ids);
        empMapper.deleteEmpExprById(ids);
    }

    @Override
    public Emp findByid(Integer Id) {
        Emp emp=empMapper.findByid(Id);
        return emp;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        //根据id修改员工的基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateBtId(emp);
        //根据id修改员工的工作经历
        //先删除员工所有的工作经历
        List<Integer> list = Arrays.asList(emp.getId());
        Integer[] array = list.toArray(new Integer[0]);
        empMapper.deleteEmpExprById(array);
        //再保存员工新的工作经历
        List<EmpExpr> emprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(emprList)){
            emprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.addEmpExpr(emprList);
        }

    }

    @Override
    public List<Emp> findAll() {
        return empMapper.findAll();
    }

    @Override
    public LoginInfo login(Emp emp) {
        //调用mapper接口，根据员工的用户名和密码查询员工信息
        Emp empLogin = empMapper.getUsernameAndPassword(emp);

        //判断是否存在这个员工，如果存在，组装登录成功信息
        if (empLogin != null){

            //生成Jwt令牌
            Map<String, Object> claims=new HashMap<>();
            claims.put("id",empLogin.getId());
            claims.put("username",empLogin.getUsername());
            String Jwt= JwtUtils.generateToken(claims);


            return new LoginInfo(empLogin.getId(), empLogin.getUsername(), empLogin.getName(), Jwt);


        }

        //失败，返回null
        return null;
    }


}
