package com.itbin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckinLog {
    private Integer id;
    private Integer memberId;
    private String memberName;
    private Integer cardId;
    private LocalDateTime checkinTime;
    private Integer courseId;
    private String courseName;
    private Integer timesConsumed;
    private Integer remainingAfter;
    private LocalDateTime createTime;
}
