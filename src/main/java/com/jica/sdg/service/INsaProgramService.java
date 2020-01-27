package com.jica.sdg.service;

import com.jica.sdg.model.NsaProgram;

import java.util.List;
import java.util.Optional;

public interface INsaProgramService {

    List<NsaProgram> findAll();
    
    List<NsaProgram> findAllBy(String id_role, String id_monper);
    
    List<NsaProgram> findAllByMonperProv(Integer id_monper, String id_prov);

    void saveNsaProgram(NsaProgram gov);
    
    Optional<NsaProgram> findOne(Integer id);
    
    void deleteNsaProgram(Integer id);
    
    void updateRole(Integer id_role, Integer id);
}
