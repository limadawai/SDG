package com.jica.sdg.service;

import com.jica.sdg.model.SdgFunding;

import java.util.List;
import java.util.Optional;

public interface ISdgFundingService {

    List<SdgFunding> findAll(Integer id_indicator, Integer id_monper);

    void saveSdgFunding(SdgFunding sdg);
    
//    Optional<SdgDisaggre> findOne(Integer id);
    
    void deleteSdgFunding(Integer id);

    List<SdgFunding> findAllByIdIndicator(int id_indicator);
}
