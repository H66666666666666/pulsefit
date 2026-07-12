package com.itbin.service;

import com.itbin.pojo.PageResult;
import com.itbin.pojo.Student;
import com.itbin.pojo.StudentQueryParam;

public interface StudentService {
    PageResult<Student> page(StudentQueryParam studentQueryParam);

    void add(Student student);

    Student findByid(Integer id);

    void update(Student student);

    void deleteEmpById(Integer[] ids);

    void discipline(Integer id,Short score);
}
