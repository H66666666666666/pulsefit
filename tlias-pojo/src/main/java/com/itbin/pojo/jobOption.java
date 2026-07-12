package com.itbin.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class jobOption {
    private List jobList;//班级列表
    private List dataList;//数据列表
}
