package com.jica.sdg.service;

import com.jica.sdg.model.NsaProgram;

import java.util.List;
import java.util.Optional;

public interface INsaProgramService {

    List<NsaProgram> findAll();
    
    List<NsaProgram> findAllBy(String id_role, String id_monper);

    void saveNsaProgram(NsaProgram gov);
    
    Optional<NsaProgram> findOne(String id);
    
    void deleteNsaProgram(String id);
}
