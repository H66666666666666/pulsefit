package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.FinanceRecordMapper;
import com.itbin.pojo.FinanceRecord;
import com.itbin.pojo.PageResult;
import com.itbin.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private FinanceRecordMapper financeRecordMapper;

    @Override
    public PageResult<FinanceRecord> page(Integer page, Integer pageSize, String type, String startDate, String endDate) {
        PageHelper.startPage(page, pageSize);
        List<FinanceRecord> rows = financeRecordMapper.list(type, startDate, endDate);
        Page<FinanceRecord> p = (Page<FinanceRecord>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public void add(FinanceRecord record) {
        financeRecordMapper.add(record);
    }

    @Override
    public List<Map<String, Object>> summary(String startDate, String endDate) {
        return financeRecordMapper.summary(startDate, endDate);
    }
}
