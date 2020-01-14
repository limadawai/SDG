package com.jica.sdg.service;

import com.jica.sdg.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    List<Role> findAll();
    
    Optional<Role> findOne(Integer id);
    
    List<Role> findByProvince(String id_prov);
    
    void saveRole(Role rol);
    
    void deleteRole(Integer id);
    
    Integer cekRole(String id_prov, String nm_role);
}
