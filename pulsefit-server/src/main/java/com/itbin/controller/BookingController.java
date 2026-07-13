package com.itbin.controller;

import com.itbin.anno.Log;
import com.itbin.pojo.Booking;
import com.itbin.pojo.Result;
import com.itbin.service.BookingService;
import com.itbin.service.MemberCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private MemberCardService memberCardService;

    @GetMapping
    public Result list(Integer memberId) {
        return Result.success(bookingService.listByMember(memberId));
    }

    @Log
    @PostMapping
    public Result book(@RequestBody Booking booking) {
        bookingService.book(booking.getCourseId(), booking.getMemberId(), booking.getMemberName());
        return Result.success();
    }

    @PutMapping("/{id}/cancel")
    public Result cancel(@PathVariable Integer id) {
        bookingService.cancel(id);
        return Result.success();
    }

    /**
     * 签到接口：扣减会员卡次数 + 记录签到
     */
    @Log
    @PostMapping("/{id}/checkin")
    public Result checkin(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        Integer memberId = (Integer) body.get("memberId");
        String memberName = (String) body.get("memberName");
        Integer courseId = (Integer) body.get("courseId");
        String courseName = (String) body.get("courseName");

        bookingService.signIn(id);
        memberCardService.deductAndCheckin(memberId, memberName, courseId, courseName);
        return Result.success();
    }
}
