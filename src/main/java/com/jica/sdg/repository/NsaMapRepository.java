package com.jica.sdg.repository;

import com.jica.sdg.model.GovMap;
import com.jica.sdg.model.NsaMap;
import com.jica.sdg.model.SdgDisaggreDetail;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NsaMapRepository extends CrudRepository<NsaMap, Integer> {
	@Query(value = "select * from sdg_ranrad_disaggre_detail where id_disaggre = :id_disaggre",nativeQuery = true)
	public List<SdgDisaggreDetail> findAllDisaggreDetail(@Param("id_disaggre") String id_disaggre); 
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from nsa_map WHERE id_indicator = :id_indicator",nativeQuery = true)
    void deleteBySdgInd(@Param("id_indicator") String id_indicator);
	
	@Query(value = "select * from nsa_map where id_indicator = :id_indicator",nativeQuery = true)
	public List<NsaMap> getIdBySdgInd(@Param("id_indicator") String id_indicator); 
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from nsa_map WHERE id_nsa_indicator = :id_nsa_indicator",nativeQuery = true)
    void deleteByNsaInd(@Param("id_nsa_indicator") Integer id_nsa_indicator);
	
	@Query(value = "select * from nsa_map where id_nsa_indicator = :id_nsa_indicator",nativeQuery = true)
	public List<NsaMap> getIdByNsaInd(@Param("id_nsa_indicator") Integer id_nsa_indicator);
}
