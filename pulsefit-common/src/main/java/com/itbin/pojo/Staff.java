package com.itbin.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Staff entity.
 * password is write-only: accepted during deserialization but never serialized.
 * Gson (the active HTTP serializer) skips transient fields.
 * Jackson (used in tests) honors @JsonIgnoreProperties.
 */
@Data
@JsonIgnoreProperties(value = {"password"}, allowSetters = true)
public class Staff {
    private Integer id;
    private String username;
    @ToString.Exclude
    private transient String password;
    private String name;
    private Integer gender; // 1:男, 2:女
    private String phone;
    private String role; // 主教练, 助理教练, 运营主管, 课程顾问, 门店经理
    private Integer salary;
    private String image;
    private LocalDate entryDate;
    private Integer departmentId; // FK -> pf_department.id
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String departmentName;
    private List<StaffExperience> experienceList;
}
