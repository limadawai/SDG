package com.jica.sdg.repository;

import com.jica.sdg.model.GovTarget;
import com.jica.sdg.model.SdgDisaggreDetail;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GovTargetRepository extends CrudRepository<GovTarget, Integer> { 
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from gov_target WHERE id_gov_indicator = :id_indicator",nativeQuery = true)
    void deleteByGovInd(@Param("id_indicator") Integer id_indicator);
	
	@Query(value = "select * from gov_target where id_gov_indicator = :id_gov_indicator and year = :year",nativeQuery = true)
	public List<GovTarget> findByGovYear(@Param("id_gov_indicator") Integer id_gov_indicator, @Param("year") Integer year); 
}
