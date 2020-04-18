package com.jica.sdg.repository;

import com.jica.sdg.model.RanRad;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MonPeriodRepository extends CrudRepository<RanRad, Integer> {
	@Query(value = "select * from ran_rad where id_prov = :id_prov order by start_year",nativeQuery = true)
	public List<RanRad> findAllMonPeriod(@Param("id_prov") String id_prov); 
	
	@Query(value = "select count(*) from ran_rad WHERE id_prov = :id_prov and (start_year BETWEEN :start and :end or end_year BETWEEN :start and :end)",nativeQuery = true)
	public Integer cekPeriode(@Param("id_prov") String id_prov, @Param("start") Integer start, @Param("end") Integer end);
}
