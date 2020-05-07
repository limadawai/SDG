package com.jica.sdg.repository;

import com.jica.sdg.model.SdgTarget;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SdgTargetRepository extends CrudRepository<SdgTarget, Integer> {
	@Query(value = "select * from sdg_target where id = :id",nativeQuery = true)
	public List<SdgTarget> findAllTarget(@Param("id") int id);

	@Query(value = "select * from sdg_target where id_goals = :id_goals order by CAST(id_target AS UNSIGNED)",nativeQuery = true)
	public List<SdgTarget> findAllTarget(@Param("id_goals") Integer id_goals);
	
	@Query(value = "select count(*) from sdg_target where id_goals = :id_goals",nativeQuery = true)
	public Integer countTarget(@Param("id_goals") Integer id_goals);
}
