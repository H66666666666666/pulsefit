package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.exception.ClazzDeleteException;
import com.itbin.mapper.ClazzMapper;
import com.itbin.mapper.StudentMapper;
import com.itbin.pojo.Clazz;
import com.itbin.pojo.ClazzQueryParam;
import com.itbin.pojo.PageResult;
import com.itbin.pojo.Student;
import com.itbin.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private StudentMapper studentMapper;



    @Override
    public PageResult<Clazz> page(ClazzQueryParam clazzQueryParam) {
        //给status赋值，
        // - 当前时间 > 结课时间：状态未 已结课。
        //  - 当前时间 < 开课时间：状态未 未开班。
        //  - 否则，就是 在读中。


        //设置分页查询参数
        PageHelper.startPage(clazzQueryParam.getPage(),clazzQueryParam.getPageSize());


        //执行查询
        List<Clazz> rows=clazzMapper.list(clazzQueryParam);

        //解析查询结果，并封装成PageResult<Emp>
        Page<Clazz> p=(Page<Clazz>) rows;
        return new PageResult<>(p.getTotal(),p.getResult());
    }

    @Override
    public void add(Clazz clazz) {
        //设置时间
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.add(clazz);
    }

    @Override
    public Clazz findByid(Integer id) {
        Clazz clazz=clazzMapper.findByid(id);
        return clazz;
    }

    @Override
    public void update(Clazz clazz) {
        //更新时间
        clazz.setUpdateTime(LocalDateTime.now());
        //修改班级信息
        clazzMapper.update(clazz);

    }

    @Override
    public void deleteByid(Integer id) {
         //检查班级下是否有学生
        Integer studentCount = studentMapper.countStudentsByClazzId(id);
        if (studentCount > 0) {
            throw new ClazzDeleteException("对不起, 该班级下有学生, 不能直接删除");
        }
        clazzMapper.deleteByid(id);
    }

    @Override
    public List<Clazz> findAll() {
        List<Clazz> clazzList = clazzMapper.findAll();
        return clazzList;
    }


//    @Override
//    public void deleteByid(Integer id) {
//        // 检查班级下是否有学生
//        Integer studentCount = studentMapper.countStudentsByClazzId(id);
//        if (studentCount > 0) {
//            throw new ClazzDeleteException("对不起, 该班级下有学生, 不能直接删除");
//        }
//
//        try {
//            clazzMapper.deleteByid(id);
//        } catch (Exception e) {
//            log.error("删除班级时发生异常，ID: {}, 错误信息: {}", id, e.getMessage());
//            throw new RuntimeException("删除班级失败：" + e.getMessage());
//        }
//    }
}
