package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.Member;
import com.itbin.pojo.MemberQueryParam;
import com.itbin.pojo.PageResult;
import com.itbin.pojo.Result;
import com.itbin.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping
    public Result page(MemberQueryParam queryParam) {
        log.info("分页查询会员，参数：{}", queryParam);
        PageResult<Member> pageResult = memberService.page(queryParam);
        return Result.success(pageResult);
    }

    @Log
    @PostMapping
    public Result add(@RequestBody Member member) {
        log.info("新增会员，信息：{}", member);
        memberService.add(member);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer id) {
        log.info("查询会员信息：{}", id);
        Member member = memberService.findById(id);
        return Result.success(member);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Member member) {
        log.info("修改会员信息：{}", member);
        memberService.update(member);
        return Result.success();
    }

    @Log
    @DeleteMapping
    public Result delete(Integer[] ids) {
        log.info("批量删除会员：{}", Arrays.toString(ids));
        memberService.deleteMemberById(ids);
        return Result.success();
    }

    @PutMapping("/service-alert/{id}/{points}")
    public Result adjustServiceAlert(@PathVariable("id") Integer id, @PathVariable("points") Short points) {
        log.info("调整服务提醒，会员id：{}，风险积分：{}", id, points);
        memberService.adjustServiceAlert(id, points);
        return Result.success();
    }
}
