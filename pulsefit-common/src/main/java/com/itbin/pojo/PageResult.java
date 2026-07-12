package com.itbin.pojo;

//分页结果封装类


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor//全参构造
@NoArgsConstructor//无参构造
@Data
public class PageResult<T> {
    private Long total; //总记录数
    private List<T> rows; //当前页结果
}
