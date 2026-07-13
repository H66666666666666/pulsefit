package com.itbin.service.impl;

import com.itbin.mapper.BookingMapper;
import com.itbin.mapper.CourseMapper;
import com.itbin.pojo.Booking;
import com.itbin.pojo.Course;
import com.itbin.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Booking> listByMember(Integer memberId) {
        return bookingMapper.list(memberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void book(Integer courseId, Integer memberId, String memberName) {
        // 数据库唯一索引冲突检测：uk_member_course(member_id, course_id)
        // 同一会员不能重复预约同一课程
        Booking existing = bookingMapper.findByMemberAndCourse(memberId, courseId);
        if (existing != null && "BOOKED".equals(existing.getStatus())) {
            throw new RuntimeException("该课程已预约，请勿重复操作");
        }

        // 业务层乐观锁：检查课程是否还有名额
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        if (!"SCHEDULED".equals(course.getStatus())) {
            throw new RuntimeException("该课程当前不可预约");
        }
        if (course.getCurrentBooked() >= course.getMaxCapacity()) {
            throw new RuntimeException("课程预约名额已满");
        }

        Booking booking = new Booking();
        booking.setCourseId(courseId);
        booking.setMemberId(memberId);
        booking.setMemberName(memberName);
        bookingMapper.add(booking);

        // 更新课程预约人数
        courseMapper.incrementBooked(courseId);
        log.info("会员 {} 预约课程 {} 成功", memberName, course.getCourseName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Integer bookingId) {
        bookingMapper.cancel(bookingId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signIn(Integer bookingId) {
        bookingMapper.signIn(bookingId);
    }
}
