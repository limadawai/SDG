package com.jica.sdg.repository;

import com.jica.sdg.model.GovFunding;
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
public interface GovFundingRepository extends CrudRepository<GovFunding, Integer> { 
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from gov_funding WHERE id_gov_indicator = :id_indicator and id_monper = :id_monper",nativeQuery = true)
    void deleteByGovInd(@Param("id_indicator") Integer id_indicator, @Param("id_monper") Integer id_monper);
	
	@Query(value = "select * from gov_funding where id_gov_indicator = :id_gov_indicator and id_monper = :id_monper",nativeQuery = true)
	public List<GovFunding> findByGovMon(@Param("id_gov_indicator") Integer id_gov_indicator, @Param("id_monper") Integer id_monper); 
}
