package com.itbin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Integer id;
    private Integer courseId;
    private Integer memberId;
    private String memberName;
    private LocalDateTime bookingTime;
    private String status;
    private LocalDateTime signInTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String courseName;
    private String coachName;
    private String courseDateStr;
}
