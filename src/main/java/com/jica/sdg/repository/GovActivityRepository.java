package com.jica.sdg.repository;

import com.jica.sdg.model.GovActivity;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GovActivityRepository extends CrudRepository<GovActivity, Integer> {
	@Query(value = "select * from gov_activity where id_program = :id_program and id_role = :id_role order by CAST(internal_code AS UNSIGNED)",nativeQuery = true)
	public List<GovActivity> findAllGovActivity(@Param("id_program") Integer id_program, @Param("id_role") Integer id_role);
	
	@Query(value = "select a.* from gov_activity a left join ref_role b on a.id_role=b.id_role where a.id_program = :id_program and b.id_prov = :id_prov order by CAST(internal_code AS UNSIGNED)",nativeQuery = true)
	public List<GovActivity> findGovActivityByIdAndProv(@Param("id_program") Integer id_program, @Param("id_prov") String id_prov);
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update gov_activity set id_role = :id_role where id = :id",nativeQuery = true)
    void UpdateRole(@Param("id_role") Integer id_role, @Param("id") Integer id);
	
	@Query(value = "select count(*) from gov_activity where id_program = :id_program",nativeQuery = true)
	public Integer countGovActivity(@Param("id_program") Integer id_program);
}
