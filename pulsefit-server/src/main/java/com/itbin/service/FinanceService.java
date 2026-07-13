package com.itbin.service;

import com.itbin.pojo.FinanceRecord;
import com.itbin.pojo.PageResult;

import java.util.List;
import java.util.Map;

public interface FinanceService {
    PageResult<FinanceRecord> page(Integer page, Integer pageSize, String type, String startDate, String endDate);
    void add(FinanceRecord record);
    List<Map<String, Object>> summary(String startDate, String endDate);
}
