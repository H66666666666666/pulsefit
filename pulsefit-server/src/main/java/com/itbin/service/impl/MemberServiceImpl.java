package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.MemberMapper;
import com.itbin.pojo.*;
import com.itbin.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public PageResult<Member> page(MemberQueryParam queryParam) {
        PageHelper.startPage(queryParam.getPage(), queryParam.getPageSize());
        List<Member> rows = memberMapper.list(queryParam);
        Page<Member> p = (Page<Member>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public void add(Member member) {
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());
        memberMapper.add(member);
    }

    @Override
    public Member findById(Integer id) {
        return memberMapper.findById(id);
    }

    @Override
    public void update(Member member) {
        member.setUpdateTime(LocalDateTime.now());
        memberMapper.update(member);
    }

    @Override
    public void deleteMemberById(Integer[] ids) {
        memberMapper.deleteMemberById(ids);
    }

    @Override
    public void adjustServiceAlert(Integer id, Short points) {
        Member member = memberMapper.findById(id);
        if (member == null) {
            throw new IllegalArgumentException("会员不存在");
        }
        if (points <= 0) {
            throw new IllegalArgumentException("风险积分必须为正数");
        }

        short newCount = (short) (member.getServiceAlertCount() + 1);
        short newScore = (short) (member.getRiskPoints() + points);

        if (newScore > 255) {
            throw new IllegalArgumentException("风险积分累计超过上限（255）");
        }

        member.setServiceAlertCount(newCount);
        member.setRiskPoints(newScore);
        member.setUpdateTime(LocalDateTime.now());
        memberMapper.update(member);
    }
}
