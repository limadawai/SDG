package com.jica.sdg.repository;

import com.jica.sdg.model.NsaIndicator;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NsaIndicatorRepository extends CrudRepository<NsaIndicator, String> {
	@Query(value = "select * from nsa_indicator where id_program = :id_program and id_activity = :id_activity",nativeQuery = true)
	public List<NsaIndicator> findAllIndicator(@Param("id_program") String id_program, @Param("id_activity") String id_activity); 
}
