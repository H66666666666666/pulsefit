package com.itbin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentRepair {
    private Integer id;
    private Integer equipmentId;
    private String equipmentNo;
    private String equipmentName;
    private Integer reporterId;
    private String reporterName;
    private String faultDesc;
    private LocalDateTime reportTime;
    private String auditStatus;
    private LocalDateTime auditTime;
    private String auditorName;
    private LocalDateTime repairStartTime;
    private LocalDateTime repairEndTime;
    private String repairDesc;
    private BigDecimal repairCost;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
