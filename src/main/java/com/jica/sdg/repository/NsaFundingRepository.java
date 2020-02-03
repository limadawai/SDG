package com.jica.sdg.repository;

import com.jica.sdg.model.NsaFunding;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NsaFundingRepository extends CrudRepository<NsaFunding, Integer> { 
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from nsa_funding WHERE id_nsa_indicator = :id_indicator and id_monper = :id_monper",nativeQuery = true)
    void deleteByNsaInd(@Param("id_indicator") Integer id_indicator, @Param("id_monper") Integer id_monper);
	
	@Query(value = "select * from nsa_funding where id_nsa_indicator = :id_nsa_indicator and id_monper = :id_monper",nativeQuery = true)
	public List<NsaFunding> findByNsaMon(@Param("id_nsa_indicator") Integer id_nsa_indicator, @Param("id_monper") Integer id_monper); 
}
