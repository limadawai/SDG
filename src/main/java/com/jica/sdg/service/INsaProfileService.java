package com.jica.sdg.service;

import com.jica.sdg.model.Nsaprofile;

import java.util.List;
import java.util.Optional;

public interface INsaProfileService {

    List<Nsaprofile> findAll();
    
    List<Nsaprofile> findId(String id);

    void saveNsaProfil(Nsaprofile ins);
    
//    Optional<Nsaprofile> findOne(String id);
//    
    void deleteNsaProfil(String id);
}
