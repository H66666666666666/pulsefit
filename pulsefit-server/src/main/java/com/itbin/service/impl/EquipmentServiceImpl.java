package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.EquipmentMapper;
import com.itbin.mapper.EquipmentRepairMapper;
import com.itbin.pojo.Equipment;
import com.itbin.pojo.EquipmentRepair;
import com.itbin.pojo.PageResult;
import com.itbin.service.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentRepairMapper equipmentRepairMapper;

    @Override
    public PageResult<Equipment> page(Integer page, Integer pageSize, String keyword) {
        PageHelper.startPage(page, pageSize);
        List<Equipment> rows = equipmentMapper.list(keyword);
        Page<Equipment> p = (Page<Equipment>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public void add(Equipment equipment) {
        equipmentMapper.add(equipment);
    }

    @Override
    public void update(Equipment equipment) {
        equipmentMapper.update(equipment);
    }

    // ── 报修流程 ──

    @Override
    public List<EquipmentRepair> listRepairs(Integer equipmentId) {
        return equipmentRepairMapper.list(equipmentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reportRepair(EquipmentRepair repair) {
        // 器材状态变为 REPORTED（报修）
        equipmentMapper.updateStatus(repair.getEquipmentId(), "REPORTED");
        equipmentRepairMapper.add(repair);
        log.info("器材 {} 报修已提交", repair.getEquipmentName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditRepair(Integer id, String auditStatus, String auditorName) {
        EquipmentRepair repair = new EquipmentRepair();
        repair.setId(id);
        repair.setAuditStatus(auditStatus);
        repair.setAuditorName(auditorName);

        if ("APPROVED".equals(auditStatus)) {
            repair.setStatus("AUDITING");
            // 器材状态同步
            equipmentRepairMapper.audit(repair);
        } else {
            repair.setStatus("REPORTED");
            equipmentRepairMapper.audit(repair);
            // 审核驳回，器材恢复正常
            // 找到原始器材 ID
            List<EquipmentRepair> repairs = equipmentRepairMapper.list(null);
            for (EquipmentRepair r : repairs) {
                if (r.getId().equals(id)) {
                    equipmentMapper.updateStatus(r.getEquipmentId(), "NORMAL");
                    break;
                }
            }
        }
        log.info("报修单 {} 审核结果: {}", id, auditStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startRepair(Integer id) {
        EquipmentRepair repair = new EquipmentRepair();
        repair.setId(id);
        equipmentRepairMapper.startRepair(repair);
        log.info("报修单 {} 开始维修", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishRepair(Integer id, String repairDesc, BigDecimal repairCost) {
        EquipmentRepair repair = new EquipmentRepair();
        repair.setId(id);
        repair.setRepairDesc(repairDesc);
        repair.setRepairCost(repairCost);
        equipmentRepairMapper.finishRepair(repair);

        // 器材恢复
        List<EquipmentRepair> repairs = equipmentRepairMapper.list(null);
        for (EquipmentRepair r : repairs) {
            if (r.getId().equals(id)) {
                equipmentMapper.updateStatus(r.getEquipmentId(), "NORMAL");
                break;
            }
        }
        log.info("报修单 {} 维修完成", id);
    }
}
