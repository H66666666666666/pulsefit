package com.itbin.service;

import com.itbin.pojo.Booking;
import java.util.List;

public interface BookingService {
    List<Booking> listByMember(Integer memberId);
    void book(Integer courseId, Integer memberId, String memberName);
    void cancel(Integer bookingId);
    void signIn(Integer bookingId);
}
