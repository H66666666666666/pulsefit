package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.CourseMapper;
import com.itbin.pojo.Course;
import com.itbin.pojo.PageResult;
import com.itbin.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public PageResult<Course> page(Integer page, Integer pageSize, String coachName, String status) {
        PageHelper.startPage(page, pageSize);
        List<Course> rows = courseMapper.list(coachName, status);
        Page<Course> p = (Page<Course>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public Course findById(Integer id) {
        return courseMapper.findById(id);
    }

    @Override
    public void add(Course course) {
        courseMapper.add(course);
    }

    @Override
    public void update(Course course) {
        courseMapper.update(course);
    }

    @Override
    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }
}
