package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.PageResult;
import com.itbin.pojo.Result;
import com.itbin.pojo.TrainingCamp;
import com.itbin.pojo.TrainingCampQueryParam;
import com.itbin.service.TrainingCampService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/training-camps")
public class TrainingCampController {
    @Autowired
    private TrainingCampService trainingCampService;

    @GetMapping
    public Result page(TrainingCampQueryParam queryParam) {
        log.info("分页查询训练营，参数：{}", queryParam);
        PageResult<TrainingCamp> pageResult = trainingCampService.page(queryParam);
        return Result.success(pageResult);
    }

    @Log
    @PostMapping
    public Result add(@RequestBody TrainingCamp trainingCamp) {
        log.info("新增训练营，信息：{}", trainingCamp);
        trainingCampService.add(trainingCamp);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer id) {
        log.info("查询训练营信息：{}", id);
        TrainingCamp trainingCamp = trainingCampService.findById(id);
        return Result.success(trainingCamp);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody TrainingCamp trainingCamp) {
        log.info("修改训练营信息：{}", trainingCamp);
        trainingCampService.update(trainingCamp);
        return Result.success();
    }

    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("根据id删除训练营数据：{}", id);
        trainingCampService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        log.info("查询全部训练营数据");
        return Result.success(trainingCampService.findAll());
    }
}
