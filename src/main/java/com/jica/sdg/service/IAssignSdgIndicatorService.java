package com.jica.sdg.service;

import com.jica.sdg.model.AssignSdgIndicator;

import java.util.List;
import java.util.Optional;

public interface IAssignSdgIndicatorService {

    List<AssignSdgIndicator> findAll(String id);

    void saveAssignSdgIndicator(AssignSdgIndicator assign);
    
    Optional<AssignSdgIndicator> findOne(Integer id);
    
    void deleteAssignSdgIndicator(Integer id);
    
    void deleteAssign(String id_monper, String id_prov);
    
    void deleteAssign(String id_prov);
    
    int idRole(String id_goals,String id_target, String id_indicator,String id_monper,String id_prov);
}
