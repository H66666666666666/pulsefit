package com.itbin.mapper;

import com.itbin.pojo.MemberCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MemberCardMapper {
    MemberCard findByMemberId(Integer memberId);
    List<MemberCard> findAll();
    void add(MemberCard card);
    void update(MemberCard card);
    void deductTimes(@Param("id") Integer id);
}
