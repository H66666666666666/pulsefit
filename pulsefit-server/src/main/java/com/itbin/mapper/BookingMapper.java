package com.itbin.mapper;

import com.itbin.pojo.Booking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BookingMapper {
    List<Booking> list(@Param("memberId") Integer memberId);
    Booking findByMemberAndCourse(@Param("memberId") Integer memberId, @Param("courseId") Integer courseId);
    void add(Booking booking);
    void cancel(Integer id);
    void signIn(@Param("id") Integer id);
}
