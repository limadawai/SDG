package com.jica.sdg.service;

import com.jica.sdg.model.SdgDisaggre;

import java.util.List;
import java.util.Optional;

public interface ISdgDisaggreService {

    List<SdgDisaggre> findAll(Integer id_indicator);

    void saveSdgDisaggre(SdgDisaggre sdg);
    
    Optional<SdgDisaggre> findOne(Integer id);
    
    void deleteSdgDisaggre(Integer id);
    
    Integer countDisaggre(Integer id_indicator);
}
