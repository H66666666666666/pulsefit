package com.itbin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRoleOption {
    private List<Object> roleList;
    private List<Object> dataList;
}
