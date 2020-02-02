package com.jica.sdg.service;

import com.jica.sdg.model.GovFunding;
import com.jica.sdg.model.GovTarget;
import com.jica.sdg.model.SdgDisaggreDetail;

import java.util.List;
import java.util.Optional;

public interface IGovFundingService {

    List<GovFunding> findAll();
    
    List<GovFunding> findByGovMon(Integer id_gov_indicator, Integer id_monper);

    void saveGovFunding(GovFunding gov);
    
    Optional<GovFunding> findOne(Integer id);
    
    void deleteGovFunding(Integer id);
    
    void deleteByGovInd(Integer id_gov_indicator, Integer id_monper);
}
