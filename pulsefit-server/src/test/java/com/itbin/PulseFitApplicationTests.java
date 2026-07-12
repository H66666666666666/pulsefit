package com.itbin;

import com.itbin.mapper.DepartmentMapper;
import com.itbin.mapper.StaffMapper;
import com.itbin.pojo.Department;
import com.itbin.pojo.Staff;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PulseFitApplicationTests {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testDepartmentMapper() {
        List<Department> departments = departmentMapper.findAll();
        assertNotNull(departments, "Department list should not be null");
        System.out.println("Departments loaded: " + departments.size());
    }

    @Test
    void testStaffMapper() {
        List<Staff> staff = staffMapper.findAll();
        assertNotNull(staff, "Staff list should not be null");
        System.out.println("Staff loaded: " + staff.size());
    }
}
