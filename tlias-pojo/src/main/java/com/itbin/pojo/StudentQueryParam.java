package com.itbin.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class StudentQueryParam {
    private Integer page=1;
    private Integer pageSize=10;
    private String name;
    private Integer clazzId;
    private Integer degree;
    @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate begin;
    @DateTimeFormat (pattern = "yyyy-MM-dd") private LocalDate end;

}
