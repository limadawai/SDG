package com.jica.sdg.repository;

import com.jica.sdg.model.SdgTarget;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SdgTargetRepository extends CrudRepository<SdgTarget, String> {
	@Query(value = "select * from sdg_target where id_goals = :id_goals",nativeQuery = true)
	public List<SdgTarget> findAllTarget(@Param("id_goals") String id_goals); 
}