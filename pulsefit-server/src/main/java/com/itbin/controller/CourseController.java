package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.Course;
import com.itbin.pojo.Result;
import com.itbin.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String coachName, String status) {
        return Result.success(courseService.page(page, pageSize, coachName, status));
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        return Result.success(courseService.findById(id));
    }

    @Log
    @PostMapping
    public Result add(@RequestBody Course course) {
        courseService.add(course);
        return Result.success();
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Course course) {
        courseService.update(course);
        return Result.success();
    }

    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        courseService.deleteById(id);
        return Result.success();
    }
}
