package com.itbin.service;

import com.itbin.pojo.MemberCard;

public interface MemberCardService {
    MemberCard findByMemberId(Integer memberId);
    void add(MemberCard card);
    void update(MemberCard card);
    void deductAndCheckin(Integer memberId, String memberName, Integer courseId, String courseName);
}
