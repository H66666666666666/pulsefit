package com.itbin.mapper;

import com.itbin.pojo.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EquipmentMapper {
    List<Equipment> list(@Param("keyword") String keyword);
    Equipment findById(Integer id);
    void add(Equipment equipment);
    void update(Equipment equipment);
    void updateStatus(@Param("id") Integer id, @Param("status") String status);
}
