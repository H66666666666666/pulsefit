package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.OperationLogMapper;
import com.itbin.pojo.OperationLog;
import com.itbin.pojo.PageResult;
import com.itbin.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public PageResult<OperationLog> page(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<OperationLog> rows = operationLogMapper.page();
        Page<OperationLog> p = (Page<OperationLog>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }
}
