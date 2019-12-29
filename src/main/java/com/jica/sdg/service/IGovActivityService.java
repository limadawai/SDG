package com.jica.sdg.service;

import com.jica.sdg.model.GovActivity;

import java.util.List;
import java.util.Optional;

public interface IGovActivityService {

    List<GovActivity> findAll(String id);

    void saveGovActivity(GovActivity gov);
    
    Optional<GovActivity> findOne(String id);
    
    void deleteGovActivity(String id);
}
