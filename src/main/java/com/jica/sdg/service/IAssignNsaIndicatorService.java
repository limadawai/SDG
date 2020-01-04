package com.jica.sdg.service;

import com.jica.sdg.model.AssignNsaIndicator;

import java.util.List;
import java.util.Optional;

public interface IAssignNsaIndicatorService {

    List<AssignNsaIndicator> findAll(String id);

    void saveAssignNsaIndicator(AssignNsaIndicator assign);
    
    Optional<AssignNsaIndicator> findOne(Integer id);
    
    void deleteAssignNsaIndicator(Integer id);
    
    void deleteAssign(String id_monper, String id_prov);
    
    int idRole(String id_program,String id_activity, String id_gov_indicator,String id_monper,String id_prov);
}
