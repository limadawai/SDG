package com.jica.sdg.service;

import com.jica.sdg.model.GovIndicator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IGovIndicatorService {

    List<GovIndicator> findAll(Integer id_program, Integer id_activity);
    
    List<GovIndicator> findAllByRole(Integer id_role);
    
    List findAllIndi(Integer id_program, Integer id_activity);

    void saveGovIndicator(GovIndicator gov);
    
    Optional<GovIndicator> findOne(Integer id);
    
    void deleteGovIndicator(Integer id);
    
    Integer countIndicator(Integer id_program, Integer id_activity);
}
