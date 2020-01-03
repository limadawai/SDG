package com.jica.sdg.service;

import com.jica.sdg.model.Insprofile;
import com.jica.sdg.model.Role;

import java.util.List;
import java.util.Optional;

public interface IInsProfileService {

    List<Insprofile> findAll();
    
    List<Insprofile> findId(String id);
    
    List<Role> findRoleInstitusi();

    void saveInsProfil(Insprofile ins);
    
    Optional<Insprofile> findOne(Integer id);
    
    void deleteInsProfil(Integer id);
}
