package com.jica.sdg.repository;

import com.jica.sdg.model.AssignGovIndicator;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AssignGovIndicatorRepository extends CrudRepository<AssignGovIndicator, Integer> {
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from assign_gov_indicator WHERE id_monper = :id_monper and id_prov= :id_prov",nativeQuery = true)
    void deleteAssign(@Param("id_monper") String id_monper, @Param("id_prov") String id_prov);
	
	@Query(value = "select id_role from assign_gov_indicator where id_program = :id_program and id_activity= :id_activity and id_gov_indicator= :id_gov_indicator and id_monper= :id_monper and id_prov= :id_prov",nativeQuery = true)
    int idRole(@Param("id_program") String id_program, 
    			@Param("id_activity") String id_activity, 
    			@Param("id_gov_indicator") String id_gov_indicator,
    			@Param("id_monper") String id_monper,
    			@Param("id_prov") String id_prov);
}
