package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.Equipment;
import com.itbin.pojo.EquipmentRepair;
import com.itbin.pojo.Result;
import com.itbin.service.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String keyword) {
        return Result.success(equipmentService.page(page, pageSize, keyword));
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        return Result.success(null); // frontend uses list
    }

    @Log
    @PostMapping
    public Result add(@RequestBody Equipment equipment) {
        equipmentService.add(equipment);
        return Result.success();
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Equipment equipment) {
        equipmentService.update(equipment);
        return Result.success();
    }

    // ═══ 报修流程 ═══

    @GetMapping("/repairs")
    public Result listRepairs(Integer equipmentId) {
        return Result.success(equipmentService.listRepairs(equipmentId));
    }

    @Log
    @PostMapping("/repairs")
    public Result report(@RequestBody EquipmentRepair repair) {
        equipmentService.reportRepair(repair);
        return Result.success();
    }

    @Log
    @PutMapping("/repairs/{id}/audit")
    public Result audit(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        equipmentService.auditRepair(id, body.get("auditStatus"), body.get("auditorName"));
        return Result.success();
    }

    @Log
    @PutMapping("/repairs/{id}/start")
    public Result startRepair(@PathVariable Integer id) {
        equipmentService.startRepair(id);
        return Result.success();
    }

    @Log
    @PutMapping("/repairs/{id}/finish")
    public Result finishRepair(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        equipmentService.finishRepair(id,
                (String) body.get("repairDesc"),
                new BigDecimal(body.get("repairCost").toString()));
        return Result.success();
    }
}
