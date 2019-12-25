package com.jica.sdg.service;

import com.jica.sdg.model.SdgDisaggre;

import java.util.List;
import java.util.Optional;

public interface ISdgDisaggreService {

    List<SdgDisaggre> findAll(String id_indicator);

    void saveSdgDisaggre(SdgDisaggre sdg);
    
    Optional<SdgDisaggre> findOne(String id);
}
