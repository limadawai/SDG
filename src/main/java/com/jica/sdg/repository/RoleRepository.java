package com.jica.sdg.repository;

import com.jica.sdg.model.Role;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
	@Query(value = "select * from ref_role where id_prov = :id_prov",nativeQuery = true)
	public List<Role> findByProvince(@Param("id_prov") String id_prov); 
}
