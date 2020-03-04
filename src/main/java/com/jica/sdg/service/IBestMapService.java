package com.jica.sdg.service;

import com.jica.sdg.model.BestMap;

import java.util.List;
import java.util.Optional;

public interface IBestMapService {

    List<BestMap> findAll(String id);
    
    List<BestMap> findAllBySdgInd(String id);
    
    List<BestMap> findAllByGovInd(Integer id);

    void saveGovMap(BestMap sdg);
    
    Optional<BestMap> findOne(Integer id);
    
    void deleteGovMap(Integer id);
    
    void deleteGovMapBySdgInd(Integer id);
    
    void deleteGovMapByGovInd(Integer id);
}
