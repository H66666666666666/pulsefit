package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.EquipmentMapper;
import com.itbin.mapper.EquipmentRepairMapper;
import com.itbin.mapper.FinanceRecordMapper;
import com.itbin.pojo.Equipment;
import com.itbin.pojo.EquipmentRepair;
import com.itbin.pojo.FinanceRecord;
import com.itbin.pojo.PageResult;
import com.itbin.service.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentRepairMapper equipmentRepairMapper;

    @Autowired
    private FinanceRecordMapper financeRecordMapper;

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

        // 先查出维修记录，获取器材 ID
        EquipmentRepair current = equipmentRepairMapper.findById(id);
        if (current == null) {
            log.warn("报修单 {} 不存在", id);
            return;
        }

        if ("APPROVED".equals(auditStatus)) {
            repair.setStatus("AUDITING");
            equipmentRepairMapper.audit(repair);
            // 器材状态同步为审核中
            equipmentMapper.updateStatus(current.getEquipmentId(), "AUDITING");
        } else {
            repair.setStatus("REPORTED");
            equipmentRepairMapper.audit(repair);
            // 审核驳回，器材恢复正常
            equipmentMapper.updateStatus(current.getEquipmentId(), "NORMAL");
        }
        log.info("报修单 {} 审核结果: {}", id, auditStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startRepair(Integer id) {
        EquipmentRepair repair = new EquipmentRepair();
        repair.setId(id);
        equipmentRepairMapper.startRepair(repair);

        // 器材状态同步为维修中
        EquipmentRepair current = equipmentRepairMapper.findById(id);
        if (current != null) {
            equipmentMapper.updateStatus(current.getEquipmentId(), "REPAIRING");
        }
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

        // 器材恢复正常
        EquipmentRepair current = equipmentRepairMapper.findById(id);
        if (current != null) {
            equipmentMapper.updateStatus(current.getEquipmentId(), "NORMAL");

            // 维修费用自动计入支出管理
            if (repairCost != null && repairCost.compareTo(BigDecimal.ZERO) > 0) {
                FinanceRecord record = new FinanceRecord();
                record.setRecordType("EXPENSE");
                record.setCategory("EQUIPMENT_REPAIR");
                record.setAmount(repairCost);
                record.setDescription("器材维修: " + current.getEquipmentName() + " - " + (repairDesc != null ? repairDesc : ""));
                record.setRelatedId(id);
                record.setRecordDate(LocalDate.now());
                financeRecordMapper.add(record);
                log.info("维修费用 {} 已计入支出", repairCost);
            }
        }
        log.info("报修单 {} 维修完成", id);
    }
}
