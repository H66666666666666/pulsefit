package com.itbin.mapper;

import com.itbin.pojo.Staff;
import com.itbin.pojo.StaffQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StaffMapper {

    List<Staff> list(StaffQueryParam staffQueryParam);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO pf_staff(username, name, gender, phone, role, salary, image, entry_date, department_id, create_time, update_time) " +
            "VALUES(#{username}, #{name}, #{gender}, #{phone}, #{role}, #{salary}, #{image}, #{entryDate}, #{departmentId}, #{createTime}, #{updateTime})")
    void add(Staff staff);

    void deleteStaffById(Integer[] ids);

    void deleteStaffExperienceById(Integer[] ids);

    Staff findById(Integer id);

    void updateById(Staff staff);

    @MapKey("pos")
    List<Map<String, Object>> countStaffRoleData();

    @MapKey("name")
    List<Map<String, Object>> countStaffGenderData();

    List<Staff> findAll();

    Staff getUsernameAndPassword(Staff staff);
}
