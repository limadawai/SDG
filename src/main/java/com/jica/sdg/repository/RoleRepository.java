package com.jica.sdg.repository;

import com.jica.sdg.model.Role;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    
    @Query(value = "select * from ref_role where LOWER(cat_role) = 'nsa' ",nativeQuery = true)
    public List<Role> findRoleNsa();
    
    @Query(value = "select * from ref_role where LOWER(cat_role) = 'Institution' ",nativeQuery = true)
    public List<Role> findRoleInstitusi();
    
}
