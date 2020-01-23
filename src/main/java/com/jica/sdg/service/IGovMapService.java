package com.jica.sdg.service;

import com.jica.sdg.model.GovMap;

import java.util.List;
import java.util.Optional;

public interface IGovMapService {

    List<GovMap> findAll(String id);
    
    List<GovMap> findAllBySdgInd(String id);

    void saveGovMap(GovMap sdg);
    
    Optional<GovMap> findOne(Integer id);
    
    void deleteGovMap(Integer id);
    
    void deleteGovMapBySdgInd(String id);
}
