package com.itbin.mapper;

import com.itbin.pojo.CheckinLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CheckinLogMapper {
    void add(CheckinLog log);
    List<CheckinLog> listByMember(@Param("memberId") Integer memberId);
}
