package com.jica.sdg.repository;

import com.jica.sdg.model.AssignSdgIndicator;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AssignSdgIndicatorRepository extends CrudRepository<AssignSdgIndicator, Integer> {
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from assign_sdg_indicator WHERE id_monper = :id_monper and id_prov= :id_prov",nativeQuery = true)
    void deleteAssign(@Param("id_monper") String id_monper, @Param("id_prov") String id_prov);
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from assign_sdg_indicator WHERE id_prov= :id_prov",nativeQuery = true)
    void deleteAssign(@Param("id_prov") String id_prov);
	
	@Query(value = "select id_role from assign_sdg_indicator where id_goals = :id_goals and id_target= :id_target and id_indicator= :id_indicator and id_monper= :id_monper and id_prov= :id_prov",nativeQuery = true)
    int idRole(@Param("id_goals") String id_goals, 
    			@Param("id_target") String id_target, 
    			@Param("id_indicator") String id_indicator,
    			@Param("id_monper") String id_monper,
    			@Param("id_prov") String id_prov);
}
