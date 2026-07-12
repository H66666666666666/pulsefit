package com.itbin.pojo;

import lombok.Data;

import java.time.LocalDate;

/**
 * Prior work experience of a staff member
 */
@Data
public class StaffExperience {
    private Integer id;
    private Integer staffId;
    private LocalDate begin;
    private LocalDate end;
    private String company;
    private String position;
}
