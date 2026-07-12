package com.itbin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLog {
    private Integer id;
    private Integer operatorId;
    private LocalDateTime operationTime;
    private String className;
    private String methodName;
    private String methodParams;
    private String returnValue;
    private Long costTime;
    private String operatorName;
}
