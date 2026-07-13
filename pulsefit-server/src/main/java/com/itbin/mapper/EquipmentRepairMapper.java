package com.itbin.mapper;

import com.itbin.pojo.EquipmentRepair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EquipmentRepairMapper {
    List<EquipmentRepair> list(@Param("equipmentId") Integer equipmentId);
    void add(EquipmentRepair repair);
    void audit(EquipmentRepair repair);
    void startRepair(EquipmentRepair repair);
    void finishRepair(EquipmentRepair repair);
}
