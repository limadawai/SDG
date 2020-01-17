package com.jica.sdg.service;

import com.jica.sdg.model.Nsaprofile;
import com.jica.sdg.model.Role;

import java.util.List;
import java.util.Optional;

public interface INsaProfileService {

    List<Nsaprofile> findAll();
    
    List<Nsaprofile> findId(String id);

    void saveNsaProfil(Nsaprofile ins);
    
    List<Role> findRoleNsa();
    List<Role> findRoleAll();
    List<Role> findRoleGov();
//    
    void deleteNsaProfil(Integer id);
}
