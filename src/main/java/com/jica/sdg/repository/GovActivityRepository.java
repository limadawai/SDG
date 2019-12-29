package com.jica.sdg.repository;

import com.jica.sdg.model.GovActivity;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GovActivityRepository extends CrudRepository<GovActivity, String> {
	@Query(value = "select * from gov_activity where id_program = :id_program",nativeQuery = true)
	public List<GovActivity> findAllGovActivity(@Param("id_program") String id_program); 
}
