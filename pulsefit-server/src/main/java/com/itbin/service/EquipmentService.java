package com.itbin.service;

import com.itbin.pojo.Equipment;
import com.itbin.pojo.EquipmentRepair;
import com.itbin.pojo.PageResult;

import java.util.List;

public interface EquipmentService {
    PageResult<Equipment> page(Integer page, Integer pageSize, String keyword);
    void add(Equipment equipment);
    void update(Equipment equipment);

    List<EquipmentRepair> listRepairs(Integer equipmentId);
    void reportRepair(EquipmentRepair repair);
    void auditRepair(Integer id, String auditStatus, String auditorName);
    void startRepair(Integer id);
    void finishRepair(Integer id, String repairDesc, java.math.BigDecimal repairCost);
}
