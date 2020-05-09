package com.jica.sdg.repository;

import com.jica.sdg.model.GovIndicator;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GovIndicatorRepository extends CrudRepository<GovIndicator, Integer> {
	@Query(value = "select * from gov_indicator where id_program = :id_program and id_activity = :id_activity order by CAST(internal_code AS UNSIGNED)",nativeQuery = true)
	public List<GovIndicator> findAllIndicator(@Param("id_program") Integer id_program, @Param("id_activity") Integer id_activity);
	
	@Query(value = "select a.* from gov_indicator a left join gov_activity b on a.id_activity=b.id where b.id_role = :id_role order by CAST(a.internal_code AS UNSIGNED)",nativeQuery = true)
	public List<GovIndicator> findAllByRole(@Param("id_role") Integer id_role);
	
	@Query(value = "select a.id, a.id_gov_indicator,a.nm_indicator,b.id_unit,b.nm_unit,a.nm_indicator_eng, a.internal_code from gov_indicator a left join ref_unit b on a.unit=b.id_unit where a.id_program = :id_program and a.id_activity = :id_activity order by CAST(a.internal_code AS UNSIGNED)",nativeQuery = true)
	public List findAllIndi(@Param("id_program") Integer id_program, @Param("id_activity") Integer id_activity);
	
	@Query(value = "select count(*) from gov_indicator where id_program = :id_program and id_activity = :id_activity",nativeQuery = true)
	public Integer countIndicator(@Param("id_program") Integer id_program, @Param("id_activity") Integer id_activity);
	
}
