package com.jica.sdg.service;

import com.jica.sdg.model.Insprofile;

import java.util.List;
import java.util.Optional;

public interface IInsProfileService {

    List<Insprofile> findAll();

    void saveInsProfil(Insprofile ins);
    
    Optional<Insprofile> findOne(String id);
}
