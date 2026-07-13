package com.itbin.mapper;

import com.itbin.pojo.MemberCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberCardMapper {
    MemberCard findByMemberId(Integer memberId);
    void add(MemberCard card);
    void update(MemberCard card);
    void deductTimes(@Param("id") Integer id);
}
