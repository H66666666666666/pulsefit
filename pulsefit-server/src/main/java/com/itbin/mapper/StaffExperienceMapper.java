package com.itbin.mapper;

import com.itbin.pojo.StaffExperience;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StaffExperienceMapper {
    void addStaffExperience(List<StaffExperience> experienceList);
}
