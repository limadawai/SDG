package com.jica.sdg.repository;

import com.jica.sdg.model.GovIndicator;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GovIndicatorRepository extends CrudRepository<GovIndicator, String> {
	@Query(value = "select * from gov_indicator where id_program = :id_program and id_activity = :id_activity",nativeQuery = true)
	public List<GovIndicator> findAllIndicator(@Param("id_program") String id_program, @Param("id_activity") String id_activity);
	
	@Query(value = "select a.* from gov_indicator a left join gov_program b on a.id_program=b.id_program where b.id_role = :id_role",nativeQuery = true)
	public List<GovIndicator> findAllByRole(@Param("id_role") Integer id_role);
}
