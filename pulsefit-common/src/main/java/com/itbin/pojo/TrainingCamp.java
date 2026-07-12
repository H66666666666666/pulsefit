package com.itbin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCamp {
    private Integer id;
    private String name;
    private String venue;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer headCoachId;
    private String programType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String headCoachName;
    private String status; // 招募中, 进行中, 已结束
}
