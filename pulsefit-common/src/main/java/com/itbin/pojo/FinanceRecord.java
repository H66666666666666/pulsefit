package com.itbin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinanceRecord {
    private Integer id;
    private String recordType;
    private String category;
    private BigDecimal amount;
    private String description;
    private Integer relatedId;
    private LocalDate recordDate;
    private LocalDateTime createTime;
}
