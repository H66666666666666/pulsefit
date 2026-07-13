package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.StaffExperienceMapper;
import com.itbin.mapper.StaffMapper;
import com.itbin.pojo.*;
import com.itbin.service.StaffService;
import com.itbin.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private StaffExperienceMapper staffExperienceMapper;

    @Override
    public PageResult<Staff> page(StaffQueryParam staffQueryParam) {
        PageHelper.startPage(staffQueryParam.getPage(), staffQueryParam.getPageSize());
        List<Staff> rows = staffMapper.list(staffQueryParam);
        Page<Staff> p = (Page<Staff>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(Staff staff) {
        staff.setCreateTime(LocalDateTime.now());
        staff.setUpdateTime(LocalDateTime.now());
        staffMapper.add(staff);

        List<StaffExperience> experienceList = staff.getExperienceList();
        if (!CollectionUtils.isEmpty(experienceList)) {
            experienceList.forEach(exp -> exp.setStaffId(staff.getId()));
            staffExperienceMapper.addStaffExperience(experienceList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteStaffById(Integer[] ids) {
        staffMapper.deleteStaffById(ids);
        staffMapper.deleteStaffExperienceById(ids);
    }

    @Override
    public Staff findById(Integer id) {
        return staffMapper.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Staff staff) {
        staff.setUpdateTime(LocalDateTime.now());
        staffMapper.updateById(staff);

        List<Integer> list = Arrays.asList(staff.getId());
        Integer[] array = list.toArray(new Integer[0]);
        staffMapper.deleteStaffExperienceById(array);

        List<StaffExperience> experienceList = staff.getExperienceList();
        if (!CollectionUtils.isEmpty(experienceList)) {
            experienceList.forEach(exp -> exp.setStaffId(staff.getId()));
            staffExperienceMapper.addStaffExperience(experienceList);
        }
    }

    @Override
    public List<Staff> findAll() {
        return staffMapper.findAll();
    }

    @Override
    public AuthSession login(Staff staff) {
        Staff staffLogin = staffMapper.getUsernameAndPassword(staff);

        if (staffLogin != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", staffLogin.getId());
            claims.put("username", staffLogin.getUsername());
            claims.put("sysRole", staffLogin.getSysRole() != null ? staffLogin.getSysRole() : "COACH");
            String jwt = JwtUtils.generateToken(claims);

            return new AuthSession(staffLogin.getId(), staffLogin.getUsername(), staffLogin.getName(),
                    staffLogin.getSysRole(), jwt);
        }
        return null;
    }
}
