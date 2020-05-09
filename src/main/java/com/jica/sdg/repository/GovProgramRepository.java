package com.jica.sdg.repository;

import com.jica.sdg.model.GovProgram;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GovProgramRepository extends CrudRepository<GovProgram, Integer> {
	@Query(value = "select * from gov_program where id_monper = :id_monper order by CAST(internal_code AS UNSIGNED)",nativeQuery = true)
	public List<GovProgram> findAll(@Param("id_monper") String id_monper); 
}
