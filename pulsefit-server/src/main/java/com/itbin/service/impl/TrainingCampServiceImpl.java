package com.itbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itbin.mapper.MemberMapper;
import com.itbin.mapper.TrainingCampMapper;
import com.itbin.pojo.*;
import com.itbin.service.TrainingCampService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TrainingCampServiceImpl implements TrainingCampService {

    @Autowired
    private TrainingCampMapper trainingCampMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public PageResult<TrainingCamp> page(TrainingCampQueryParam queryParam) {
        PageHelper.startPage(queryParam.getPage(), queryParam.getPageSize());
        List<TrainingCamp> rows = trainingCampMapper.list(queryParam);
        Page<TrainingCamp> p = (Page<TrainingCamp>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public void add(TrainingCamp trainingCamp) {
        trainingCamp.setCreateTime(LocalDateTime.now());
        trainingCamp.setUpdateTime(LocalDateTime.now());
        trainingCampMapper.add(trainingCamp);
    }

    @Override
    public TrainingCamp findById(Integer id) {
        return trainingCampMapper.findById(id);
    }

    @Override
    public void update(TrainingCamp trainingCamp) {
        trainingCamp.setUpdateTime(LocalDateTime.now());
        trainingCampMapper.update(trainingCamp);
    }

    @Override
    public void deleteById(Integer id) {
        Integer memberCount = memberMapper.countMembersByCampId(id);
        if (memberCount > 0) {
            throw new RuntimeException("对不起, 该训练营下有会员, 不能直接删除");
        }
        trainingCampMapper.deleteById(id);
    }

    @Override
    public List<TrainingCamp> findAll() {
        return trainingCampMapper.findAll();
    }
}
