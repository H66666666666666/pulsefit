package com.itbin.service.impl;

import com.itbin.mapper.MemberMapper;
import com.itbin.mapper.StaffMapper;
import com.itbin.pojo.CampMemberOption;
import com.itbin.pojo.StaffRoleOption;
import com.itbin.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public StaffRoleOption getStaffRoleData() {
        List<Map<String, Object>> list = staffMapper.countStaffRoleData();
        List<Object> roleList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
        return new StaffRoleOption(roleList, dataList);
    }

    @Override
    public List<Map<String, Object>> getStaffGenderData() {
        return staffMapper.countStaffGenderData();
    }

    @Override
    public CampMemberOption getCampMemberData() {
        List<Map<String, Object>> list = memberMapper.countCampMemberData();
        List<Object> campList = list.stream().map(dataMap -> dataMap.get("campName")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
        return new CampMemberOption(campList, dataList);
    }

    @Override
    public List<Map<String, Object>> getMembershipLevelData() {
        return memberMapper.countMembershipLevelData();
    }
}
