package com.itbin.mapper;

import com.itbin.pojo.TrainingCamp;
import com.itbin.pojo.TrainingCampQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrainingCampMapper {
    List<TrainingCamp> list(TrainingCampQueryParam queryParam);

    void add(TrainingCamp trainingCamp);

    TrainingCamp findById(Integer id);

    void update(TrainingCamp trainingCamp);

    void deleteById(Integer id);

    List<TrainingCamp> findAll();
}
