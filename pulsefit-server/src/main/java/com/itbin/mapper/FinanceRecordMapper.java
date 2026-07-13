package com.itbin.mapper;

import com.itbin.pojo.FinanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import java.util.Map;

@Mapper
public interface FinanceRecordMapper {
    List<FinanceRecord> list(@Param("type") String type,
                             @Param("startDate") String startDate,
                             @Param("endDate") String endDate);
    void add(FinanceRecord record);
    List<Map<String, Object>> summary(@Param("startDate") String startDate,
                                      @Param("endDate") String endDate);
}
