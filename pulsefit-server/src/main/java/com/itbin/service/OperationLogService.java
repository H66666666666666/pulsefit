package com.itbin.service;

import com.itbin.pojo.OperationLog;
import com.itbin.pojo.PageResult;

public interface OperationLogService {
    PageResult<OperationLog> page(Integer page, Integer pageSize);
}
