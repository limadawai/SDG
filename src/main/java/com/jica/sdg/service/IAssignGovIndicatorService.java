package com.jica.sdg.service;

import com.jica.sdg.model.AssignGovIndicator;
import com.jica.sdg.model.AssignSdgIndicator;

import java.util.List;
import java.util.Optional;

public interface IAssignGovIndicatorService {

    List<AssignGovIndicator> findAll(String id);

    void saveAssignGovIndicator(AssignGovIndicator assign);
    
    Optional<AssignGovIndicator> findOne(Integer id);
    
    void deleteAssignGovIndicator(Integer id);
    
    void deleteAssign(String id_monper, String id_prov);
    
    int idRole(String id_program,String id_activity, String id_gov_indicator,String id_monper,String id_prov);
}
