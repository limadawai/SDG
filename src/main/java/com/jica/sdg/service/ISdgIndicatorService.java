package com.jica.sdg.service;

import com.jica.sdg.model.SdgIndicator;

import java.util.List;
import java.util.Optional;

public interface ISdgIndicatorService {

    List<SdgIndicator> findAll(String id_goals, String id_target);
    
    List findAllGrid(String id_goals, String id_target);
    
    List<SdgIndicator> findAll();

    void saveSdgIndicator(SdgIndicator sdg);
    
    Optional<SdgIndicator> findOne(String id);
    
    void deleteSdgIndicator(String id);
}
