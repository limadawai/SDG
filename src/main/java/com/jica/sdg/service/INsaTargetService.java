package com.jica.sdg.service;

import com.jica.sdg.model.NsaTarget;

import java.util.List;
import java.util.Optional;

public interface INsaTargetService {

    List<NsaTarget> findAll();
    
    List<NsaTarget> findByNsaYear(Integer id_nsa_indicator, Integer year);

    void saveNsaTarget(NsaTarget gov);
    
    Optional<NsaTarget> findOne(Integer id);
    
    void deleteNsaTarget(Integer id);
    
    void deleteByNsaInd(Integer id);
}
