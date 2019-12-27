package com.jica.sdg.repository;

import com.jica.sdg.model.SdgIndicator;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SdgIndicatorRepository extends CrudRepository<SdgIndicator, String> {
	@Query(value = "select * from sdg_indicator where id_goals = :id_goals and id_target = :id_target",nativeQuery = true)
	public List<SdgIndicator> findAllIndicator(@Param("id_goals") String id_goals, @Param("id_target") String id_target); 
}
