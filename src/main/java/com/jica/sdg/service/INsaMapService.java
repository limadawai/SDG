package com.jica.sdg.service;

import com.jica.sdg.model.NsaMap;

import java.util.List;
import java.util.Optional;

public interface INsaMapService {

    List<NsaMap> findAll(String id);
    
    List<NsaMap> findAllBySdgInd(String id);

    void saveNsaMap(NsaMap sdg);
    
    Optional<NsaMap> findOne(Integer id);
    
    void deleteNsaMap(Integer id);
    
    void deleteNsaMapBySdgInd(String id);
}
