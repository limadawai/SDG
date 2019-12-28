package com.jica.sdg.service;

import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.SdgTarget;

import java.util.List;
import java.util.Optional;

public interface IGovProgramService {

    List<GovProgram> findAll();

    void saveGovProgram(GovProgram gov);
    
    Optional<GovProgram> findOne(String id);
    
    void deleteGovProgram(String id);
}
