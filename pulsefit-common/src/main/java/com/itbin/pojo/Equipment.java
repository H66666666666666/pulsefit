package com.itbin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    private Integer id;
    private String equipmentNo;
    private String equipmentName;
    private String category;
    private LocalDate purchaseDate;
    private String status;
    private String location;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
