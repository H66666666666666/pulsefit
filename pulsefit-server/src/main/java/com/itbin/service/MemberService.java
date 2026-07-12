package com.itbin.service;

import com.itbin.pojo.*;

public interface MemberService {
    PageResult<Member> page(MemberQueryParam queryParam);

    void add(Member member);

    Member findById(Integer id);

    void update(Member member);

    void deleteMemberById(Integer[] ids);

    void adjustServiceAlert(Integer id, Short points);
}
