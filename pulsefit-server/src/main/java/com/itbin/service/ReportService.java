package com.itbin.service;

import com.itbin.pojo.CampMemberOption;
import com.itbin.pojo.StaffRoleOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    StaffRoleOption getStaffRoleData();

    List<Map<String, Object>> getStaffGenderData();

    CampMemberOption getCampMemberData();

    List<Map<String, Object>> getMembershipLevelData();
}
