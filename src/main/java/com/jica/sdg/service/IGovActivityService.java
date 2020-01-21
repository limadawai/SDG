package com.jica.sdg.service;

import com.jica.sdg.model.GovActivity;

import java.util.List;
import java.util.Optional;

public interface IGovActivityService {

    List<GovActivity> findAll(Integer id);

    void saveGovActivity(GovActivity gov);
    
    Optional<GovActivity> findOne(Integer id);
    
    void deleteGovActivity(Integer id);
}
