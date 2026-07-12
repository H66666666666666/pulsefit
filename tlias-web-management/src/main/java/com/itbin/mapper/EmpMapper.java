package com.itbin.mapper;

import com.itbin.pojo.Emp;
import com.itbin.pojo.EmpExpr;
import com.itbin.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {


    //--------------------------------原始分页查询实现----------------------------------
    //查询总记录数
    //@Select("select count(*) from emp  left join dept on emp.dept_id=dept.id")
    //public Long count();

    //分页查询
    //@Select("select emp.*,dept.name from emp left join dept on dept_id=dept.id limit #{start},#{pageSize}")
    //public List<Emp> list(Integer start, Integer pageSize);


    //

    //分页查询
    //@Select("select emp.*,dept.name deptName from emp left join dept on dept_id=dept.id where emp.name like '%#{name}%' and gender=#{gender} and entry_date between #{begin} and #{end} order by emp.update_time desc ")
    public List<Emp> list(EmpQueryParam empQueryParam);

    //新增员工
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "VALUES(#{username}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    public void add(Emp emp);


    void deleteEmpById(Integer[] ids);

    void deleteEmpExprById(Integer[] ids);

    Emp findByid(Integer Id);

    void updateBtId(Emp emp);

    //职位人数统计
    @MapKey("pos")
    public List<Map<String,Object>> countEmpJobData();

    @MapKey("name")
    List<Map<String, Object>> countEmpGenderData();

    List<Emp> findAll();


    //根据用户名和密码查询员工
    Emp getUsernameAndPassword(Emp emp);
}
