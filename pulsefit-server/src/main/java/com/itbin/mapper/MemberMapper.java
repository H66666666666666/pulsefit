package com.itbin.mapper;

import com.itbin.pojo.Member;
import com.itbin.pojo.MemberQueryParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {

    Integer countMembersByCampId(@Param("campId") Integer id);

    List<Member> list(MemberQueryParam queryParam);

    void add(Member member);

    Member findById(Integer id);

    void update(Member member);

    void deleteMemberById(Integer[] ids);

    @MapKey("campName")
    List<Map<String, Object>> countCampMemberData();

    @MapKey("name")
    List<Map<String, Object>> countMembershipLevelData();
}
