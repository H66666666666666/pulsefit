package com.itbin.service.impl;

import com.itbin.mapper.CheckinLogMapper;
import com.itbin.mapper.MemberCardMapper;
import com.itbin.pojo.CheckinLog;
import com.itbin.pojo.MemberCard;
import com.itbin.service.MemberCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MemberCardServiceImpl implements MemberCardService {

    @Autowired
    private MemberCardMapper memberCardMapper;

    @Autowired
    private CheckinLogMapper checkinLogMapper;

    @Override
    public MemberCard findByMemberId(Integer memberId) {
        return memberCardMapper.findByMemberId(memberId);
    }

    @Override
    public void add(MemberCard card) {
        card.setRemainingTimes(card.getTotalTimes());
        memberCardMapper.add(card);
    }

    @Override
    public void update(MemberCard card) {
        memberCardMapper.update(card);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductAndCheckin(Integer memberId, String memberName, Integer courseId, String courseName) {
        MemberCard card = memberCardMapper.findByMemberId(memberId);
        if (card == null) {
            throw new RuntimeException("该会员暂无会员卡，请先购卡");
        }
        if (!"ACTIVE".equals(card.getStatus())) {
            throw new RuntimeException("会员卡状态异常：" + card.getStatus());
        }
        if (card.getRemainingTimes() <= 0) {
            throw new RuntimeException("会员卡剩余次数不足");
        }

        // 扣减次数
        memberCardMapper.deductTimes(card.getId());

        // 记录签到日志
        CheckinLog entry = new CheckinLog();
        entry.setMemberId(memberId);
        entry.setMemberName(memberName);
        entry.setCardId(card.getId());
        entry.setCourseId(courseId);
        entry.setCourseName(courseName);
        entry.setTimesConsumed(1);
        entry.setRemainingAfter(card.getRemainingTimes() - 1);
        checkinLogMapper.add(entry);
        log.info("会员 {} 签到消费成功，剩余次数: {}", memberName, card.getRemainingTimes() - 1);
    }
}
