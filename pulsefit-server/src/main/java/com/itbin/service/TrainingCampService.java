package com.itbin.service;

import com.itbin.pojo.*;

import java.util.List;

public interface TrainingCampService {
    PageResult<TrainingCamp> page(TrainingCampQueryParam queryParam);

    void add(TrainingCamp trainingCamp);

    TrainingCamp findById(Integer id);

    void update(TrainingCamp trainingCamp);

    void deleteById(Integer id);

    List<TrainingCamp> findAll();
}
