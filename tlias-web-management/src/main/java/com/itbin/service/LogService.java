package com.itbin.service;

import com.itbin.pojo.OperateLog;
import com.itbin.pojo.PageResult;

public interface LogService {
    PageResult<OperateLog> page(Integer page, Integer pageSize);
}
