package com.jica.sdg.service;

import com.jica.sdg.model.GovActivity;

import java.util.List;
import java.util.Optional;

public interface IGovActivityService {

    List<GovActivity> findAll(Integer id, Integer id_role);
    
    List<GovActivity> findGovActivityByIdAndProv(Integer id, String id_prov);

    void saveGovActivity(GovActivity gov);
    
    Optional<GovActivity> findOne(Integer id);
    
    void deleteGovActivity(Integer id);
    
    void UpdateRole(Integer id_role, Integer id);
    
    Integer countGovActivity(Integer id);
}
