package com.itbin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCard {
    private Integer id;
    private Integer memberId;
    private String memberName;
    private String cardType;
    private Integer totalTimes;
    private Integer remainingTimes;
    private LocalDate expireDate;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
