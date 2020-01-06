package com.jica.sdg.repository;

import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.NsaProgram;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NsaProgramRepository extends CrudRepository<NsaProgram, String> {
	@Query(value = "select * from nsa_program where id_role = :id_role and id_monper = :id_monper ",nativeQuery = true)
	public List<NsaProgram> findAll(@Param("id_role") String id_role, @Param("id_monper") String id_monper); 
}
