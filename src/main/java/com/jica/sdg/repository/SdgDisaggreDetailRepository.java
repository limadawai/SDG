package com.jica.sdg.repository;

import com.jica.sdg.model.SdgDisaggreDetail;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SdgDisaggreDetailRepository extends CrudRepository<SdgDisaggreDetail, Integer> {
	@Query(value = "select * from sdg_ranrad_disaggre_detail where id_disaggre = :id_disaggre",nativeQuery = true)
	public List<SdgDisaggreDetail> findAllDisaggreDetail(@Param("id_disaggre") Integer id_disaggre); 
}
