package com.itbin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Integer id;
    private String name;
    private String memberNo;
    private Integer gender; // 1: 男, 2: 女
    private String phone;
    private String idCard;
    private String referralChannel; // 线上渠道 / 门店渠道
    private String address;
    private String membershipLevel; // 基础会员, 银卡会员, 黄金会员, 铂金会员, 钻石会员, 黑金会员
    private LocalDate membershipDate;
    private Integer trainingCampId;
    private Short serviceAlertCount;
    private Short riskPoints;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String trainingCampName;
}
