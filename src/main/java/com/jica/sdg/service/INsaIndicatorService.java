package com.jica.sdg.service;

import com.jica.sdg.model.NsaIndicator;

import java.util.List;
import java.util.Optional;

public interface INsaIndicatorService {

    List<NsaIndicator> findAll(Integer id_program, Integer id_activity);
    
    List findAllIndi(Integer id_program, Integer id_activity);

    void saveNsaIndicator(NsaIndicator gov);
    
    Optional<NsaIndicator> findOne(Integer id);
    
    void deleteNsaIndicator(Integer id);
    
    Integer countIndicator(Integer id_program, Integer id_activity);
}
