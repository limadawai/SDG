package com.jica.sdg.service;

import com.jica.sdg.model.NsaIndicator;

import java.util.List;
import java.util.Optional;

public interface INsaIndicatorService {

    List<NsaIndicator> findAll(String id_program, String id_activity);

    void saveNsaIndicator(NsaIndicator gov);
    
    Optional<NsaIndicator> findOne(String id);
    
    void deleteNsaIndicator(String id);
}
