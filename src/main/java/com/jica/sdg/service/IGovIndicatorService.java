package com.jica.sdg.service;

import com.jica.sdg.model.GovIndicator;

import java.util.List;
import java.util.Optional;

public interface IGovIndicatorService {

    List<GovIndicator> findAll(String id_program, String id_activity);
    
    List<GovIndicator> findAllByRole(Integer id_role);

    void saveGovIndicator(GovIndicator gov);
    
    Optional<GovIndicator> findOne(String id);
    
    void deleteGovIndicator(String id);
}
