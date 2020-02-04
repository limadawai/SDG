package com.jica.sdg.service;

import com.jica.sdg.model.SdgIndicator;

import java.util.List;
import java.util.Optional;

public interface ISdgIndicatorService {

    List<SdgIndicator> findAll(Integer id_goals, Integer id_target);
    
    List findAllGrid(Integer id_goals, Integer id_target);
    
    List<SdgIndicator> findAll();

    void saveSdgIndicator(SdgIndicator sdg);
    
    Optional<SdgIndicator> findOne(Integer id);
    
    void deleteSdgIndicator(Integer id);
    
    Integer countIndicator(Integer id_goals, Integer id_target);
}
