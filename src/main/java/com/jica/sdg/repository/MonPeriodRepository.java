package com.jica.sdg.repository;

import com.jica.sdg.model.RanRad;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MonPeriodRepository extends CrudRepository<RanRad, Integer> {
	@Query(value = "select * from mon_period where id_prov = :id_prov",nativeQuery = true)
	public List<RanRad> findAllMonPeriod(@Param("id_prov") String id_prov); 
}
