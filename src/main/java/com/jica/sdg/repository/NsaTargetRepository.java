package com.jica.sdg.repository;

import com.jica.sdg.model.NsaTarget;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NsaTargetRepository extends CrudRepository<NsaTarget, Integer> { 
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from nsa_target WHERE id_nsa_indicator = :id_indicator",nativeQuery = true)
    void deleteByNsaInd(@Param("id_indicator") Integer id_indicator);
	
	@Query(value = "select * from nsa_target where id_nsa_indicator = :id_nsa_indicator and year = :year",nativeQuery = true)
	public List<NsaTarget> findByNsaYear(@Param("id_nsa_indicator") Integer id_nsa_indicator, @Param("year") Integer year); 
}
