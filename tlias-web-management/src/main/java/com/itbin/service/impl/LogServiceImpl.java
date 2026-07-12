package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.LogMapper;
import com.itbin.pojo.OperateLog;
import com.itbin.pojo.PageResult;
import com.itbin.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;
    @Override
    public PageResult<OperateLog> page(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);

        List<OperateLog> rows=logMapper.list();

        Page<OperateLog> p=(Page<OperateLog>) rows;
        return new PageResult<>(p.getTotal(),p.getResult());
    }
}
