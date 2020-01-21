package com.jica.sdg.service;

import com.jica.sdg.model.SdgGoals;

import java.util.List;
import java.util.Optional;

public interface ISdgGoalsService {

    List<SdgGoals> findAll();

    void saveSdgGoals(SdgGoals sdg);
    
    Optional<SdgGoals> findOne(int id);
    
    void deleteSdgGoals(int id);
}
