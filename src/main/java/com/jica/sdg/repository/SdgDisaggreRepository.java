package com.jica.sdg.repository;

import com.jica.sdg.model.SdgDisaggre;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SdgDisaggreRepository extends CrudRepository<SdgDisaggre, String> {
	@Query(value = "select * from sdg_ranrad_disaggre where id_indicator = :id_indicator",nativeQuery = true)
	public List<SdgDisaggre> findAllDisaggre(@Param("id_indicator") String id_indicator); 
}