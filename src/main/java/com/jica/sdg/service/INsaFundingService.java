package com.jica.sdg.service;

import com.jica.sdg.model.GovFunding;
import com.jica.sdg.model.GovTarget;
import com.jica.sdg.model.NsaFunding;
import com.jica.sdg.model.SdgDisaggreDetail;

import java.util.List;
import java.util.Optional;

public interface INsaFundingService {

    List<NsaFunding> findAll();
    
    List<NsaFunding> findByNsaMon(Integer id_nsa_indicator, Integer id_monper);

    void saveNsaFunding(NsaFunding gov);
    
    Optional<NsaFunding> findOne(Integer id);
    
    void deleteNsaFunding(Integer id);
    
    void deleteByNsaInd(Integer id_nsa_indicator, Integer id_monper);
}
