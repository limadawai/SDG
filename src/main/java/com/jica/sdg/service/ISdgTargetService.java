package com.jica.sdg.service;

import com.jica.sdg.model.SdgTarget;

import java.util.List;
import java.util.Optional;

public interface ISdgTargetService {

    List<SdgTarget> findAll(int id);

    void saveSdgTarget(SdgTarget sdg);
    
    Optional<SdgTarget> findOne(int id);
    
    void deleteSdgTarget(int id);

    List<SdgTarget> findAll(Integer id);
    
    Optional<SdgTarget> findOne(Integer id);
    
    void deleteSdgTarget(Integer id);
    
    Integer countTarget(Integer id);
}
