package com.jica.sdg.service;

import com.jica.sdg.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    List<Role> findAllGrid();
    
    List<Role> findAll();
    
    List<Role> findAllNonSuper();
    
    Optional<Role> findOne(Integer id);
    
    List<Role> findByProvince(String id_prov);
    
    List<Role> findByProvinceUserForm(String id_prov);
    
    List<Role> findByProvince(String id_prov, String cat, String prev);
    
    List<Role> findByCat(String cat,String prev);
    
    void saveRole(Role rol);
    
    void deleteRole(Integer id);
    
    Integer cekRole(String id_prov, String nm_role);
    
    Integer cekJmlRole(String id_prov, String cat_role);
    
    List<Role> findRoleGov();
    
    List<Role> findRoleNonGov(String id_prov);
    
    List<Role> findRoleGov(String id_prov);

    List<Role> catRole(int id_role);

}
