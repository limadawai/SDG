package com.jica.sdg.service;

import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.SdgTarget;

import java.util.List;
import java.util.Optional;

public interface IGovProgramService {

    List<GovProgram> findAll();
    
    //List<GovProgram> findAllBy(String id_role, String id_monper);
    List<GovProgram> findAllBy(String id_monper);

    void saveGovProgram(GovProgram gov);
    
    Optional<GovProgram> findOne(Integer id);
    
    void deleteGovProgram(Integer id);
}
