package com.jica.sdg.service;

import com.jica.sdg.model.MonPeriod;

import java.util.List;
import java.util.Optional;

public interface IMonPeriodService {

    List<MonPeriod> findAll(String id);

    void saveMonPeriod(MonPeriod sdg);
    
    Optional<MonPeriod> findOne(Integer id);
    
    void deleteMonPeriod(Integer id);
}
