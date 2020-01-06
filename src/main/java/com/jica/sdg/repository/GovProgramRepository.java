package com.jica.sdg.repository;

import com.jica.sdg.model.GovProgram;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GovProgramRepository extends CrudRepository<GovProgram, String> {
	@Query(value = "select * from gov_program where id_role = :id_role and id_monper = :id_monper ",nativeQuery = true)
	public List<GovProgram> findAll(@Param("id_role") String id_role, @Param("id_monper") String id_monper); 
}
