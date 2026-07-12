package com.itbin.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.StudentMapper;
import com.itbin.pojo.PageResult;
import com.itbin.pojo.Student;
import com.itbin.pojo.StudentQueryParam;
import com.itbin.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Student> page(StudentQueryParam studentQueryParam) {
        //设置分页查询参数
        PageHelper.startPage(studentQueryParam.getPage(),studentQueryParam.getPageSize());
        //执行查询
        List<Student> rows=studentMapper.list(studentQueryParam);
        //解析查询结果，并封装成PageResult<Student>
        Page<Student> p=(Page<Student>) rows;
        return new PageResult<Student>(p.getTotal(),p.getResult());
    }

    @Override
    public void add(Student student) {
        //设置时间
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.add(student);
    }

    @Override
    public Student findByid(Integer id) {
        Student student=studentMapper.findByid(id);
        return student;
    }

    @Override
    public void update(Student student) {
        //更新时间
        student.setUpdateTime(LocalDateTime.now());
        //修改员工信息
        studentMapper.update(student);
    }

    @Override
    public void deleteEmpById(Integer[] ids) {
        studentMapper.deleteStudentById(ids);
    }

//    @Override
//    public void discipline(Integer id, Short score) {
//        // 获取学生信息
//        Student student = studentMapper.findByid(id);
//
//        // 违纪次数+1
//        if (student.getViolationCount() == null) {
//            student.setViolationCount((short) 1);
//        } else {
//            student.setViolationCount((short) (student.getViolationCount() + 1));
//        }
//
//        // 违纪扣分累加（在原基础上加上本次扣分）
//        if (student.getViolationScore() == null) {
//            student.setViolationScore(score);
//        } else {
//            student.setViolationScore((short) (student.getViolationScore() + score));
//        }
//
//        // 设置更新时间
//        student.setUpdateTime(LocalDateTime.now());
//
//        // 保存更新到数据库
//        studentMapper.update(student);
//    }

//    @Override
//    public void discipline(Integer id, Short score) {
//        // 1. 查询学生是否存在
//        Student student = studentMapper.findByid(id);
//
//
//        // 2. 计算新的违纪次数和分数
//        short newCount = (short) (student.getViolationCount() + 1);
//        short newScore = (short) (student.getViolationScore() + score);
//
//        // 3. 检查分数是否超出tinyint unsigned范围（0-255）
//        if (newScore > 255) {
//            throw new IllegalArgumentException("违纪分数累计超过上限（255）");
//        }
//
//        // 4. 更新学生信息
//        student.setViolationCount(newCount);
//        student.setViolationScore(newScore);
//        student.setUpdateTime(LocalDateTime.now());
//        studentMapper.update(student); // 复用现有update方法
//    }


    @Override
    public void discipline(Integer id, Short score) {
        // 1. 查询学生是否存在
        Student student = studentMapper.findByid(id);
        if (student == null) {
            throw new IllegalArgumentException("学生不存在");
        }

        // 2. 校验输入分数是否合法（新增）
        if (score <= 0) {
            throw new IllegalArgumentException("扣分必须为正数");
        }

        // 3. 计算新的违纪次数和分数
        short newCount = (short) (student.getViolationCount() + 1);
        short newScore = (short) (student.getViolationScore() + score);

        // 4. 检查分数是否超出tinyint unsigned范围（0-255）
        if (newScore > 255) {
            throw new IllegalArgumentException("违纪分数累计超过上限（255）");
        }

        // 5. 更新学生信息
        student.setViolationCount(newCount);
        student.setViolationScore(newScore);
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }
}
