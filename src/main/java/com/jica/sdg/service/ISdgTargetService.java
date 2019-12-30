package com.jica.sdg.service;

import com.jica.sdg.model.SdgTarget;

import java.util.List;
import java.util.Optional;

public interface ISdgTargetService {

    List<SdgTarget> findAll(String id);

    void saveSdgTarget(SdgTarget sdg);
    
    Optional<SdgTarget> findOne(String id);
    
    void deleteSdgTarget(String id);
}
