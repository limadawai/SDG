package com.jica.sdg.repository;

import com.jica.sdg.model.SdgFunding;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SdgFundingRepository extends CrudRepository<SdgFunding, Integer> {
    
	@Query(value = "select * from sdg_funding where id_sdg_indicator = :id_indicator and id_monper = :id_monper ",nativeQuery = true)
	public List<SdgFunding> findAllFunding(@Param("id_indicator") Integer id_indicator, @Param("id_monper") Integer id_monper);

	@Query(value = "SELECT * FROM sdg_funding WHERE id_sdg_indicator = :id_indicator", nativeQuery = true)
	public List<SdgFunding> findAllByIdIndi(@Param("id_indicator") int id_indicator);
}
