package com.jica.sdg.repository;

import com.jica.sdg.model.GovMap;
import com.jica.sdg.model.SdgDisaggreDetail;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GovMapRepository extends CrudRepository<GovMap, Integer> {
	@Query(value = "select * from sdg_ranrad_disaggre_detail where id_disaggre = :id_disaggre",nativeQuery = true)
	public List<SdgDisaggreDetail> findAllDisaggreDetail(@Param("id_disaggre") String id_disaggre); 
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from gov_map WHERE id_indicator = :id_indicator",nativeQuery = true)
    void deleteBySdgInd(@Param("id_indicator") Integer id_indicator);
	
	@Query(value = "select * from gov_map where id_indicator = :id_indicator",nativeQuery = true)
	public List<GovMap> getIdBySdgInd(@Param("id_indicator") String id_indicator);
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from gov_map WHERE id_gov_indicator = :id_gov_indicator",nativeQuery = true)
    void deleteByGovInd(@Param("id_gov_indicator") Integer id_gov_indicator);
	
	@Query(value = "select * from gov_map where id_gov_indicator = :id_gov_indicator",nativeQuery = true)
	public List<GovMap> getIdByGovInd(@Param("id_gov_indicator") Integer id_gov_indicator);

}
