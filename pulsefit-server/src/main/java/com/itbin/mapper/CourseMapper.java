package com.itbin.mapper;

import com.itbin.pojo.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> list(@Param("coachName") String coachName, @Param("status") String status);
    Course findById(Integer id);
    void add(Course course);
    void update(Course course);
    void deleteById(Integer id);
    void incrementBooked(@Param("id") Integer id);
    void decrementBooked(@Param("id") Integer id);
}
