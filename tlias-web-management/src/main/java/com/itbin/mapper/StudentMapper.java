package com.itbin.mapper;

import com.itbin.pojo.Student;
import com.itbin.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    //Short getStudentScore(Integer id);


    //检查班级是否有关联学生的方法
    Integer countStudentsByClazzId(@Param("clazzId") Integer id);

    List<Student> list(StudentQueryParam studentQueryParam);

    void add(Student student);

    Student findByid(Integer id);

    void update(Student student);

    void deleteStudentById(Integer[] ids);

    @MapKey("clazzName")
    List<Map<String, Object>> countStudentCountData();


    @MapKey("name")
    List<Map<String, Object>> countStudentDegreeData();
}
