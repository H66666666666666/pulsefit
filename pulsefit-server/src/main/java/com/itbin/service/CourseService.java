package com.itbin.service;

import com.itbin.pojo.Course;
import com.itbin.pojo.PageResult;

public interface CourseService {
    PageResult<Course> page(Integer page, Integer pageSize, String coachName, String status);
    Course findById(Integer id);
    void add(Course course);
    void update(Course course);
    void deleteById(Integer id);
}
