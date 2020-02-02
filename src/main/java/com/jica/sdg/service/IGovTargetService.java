package com.jica.sdg.service;

import com.jica.sdg.model.GovTarget;
import com.jica.sdg.model.SdgDisaggreDetail;

import java.util.List;
import java.util.Optional;

public interface IGovTargetService {

    List<GovTarget> findAll();
    
    List<GovTarget> findByGovYear(Integer id_gov_indicator, Integer year);

    void saveGovTarget(GovTarget gov);
    
    Optional<GovTarget> findOne(Integer id);
    
    void deleteGovTarget(Integer id);
    
    void deleteByGovInd(Integer id);
}
