package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.CheckinLog;
import com.itbin.pojo.MemberCard;
import com.itbin.pojo.Result;
import com.itbin.mapper.CheckinLogMapper;
import com.itbin.service.MemberCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member-cards")
public class MemberCardController {
    @Autowired
    private MemberCardService memberCardService;

    @Autowired
    private CheckinLogMapper checkinLogMapper;

    @GetMapping
    public Result getByMember(Integer memberId) {
        MemberCard card = memberCardService.findByMemberId(memberId);
        return Result.success(card);
    }

    @Log
    @PostMapping
    public Result add(@RequestBody MemberCard card) {
        memberCardService.add(card);
        return Result.success();
    }

    @Log
    @PutMapping
    public Result update(@RequestBody MemberCard card) {
        memberCardService.update(card);
        return Result.success();
    }

    @GetMapping("/checkin-logs")
    public Result checkinLogs(Integer memberId) {
        return Result.success(checkinLogMapper.listByMember(memberId));
    }
}
