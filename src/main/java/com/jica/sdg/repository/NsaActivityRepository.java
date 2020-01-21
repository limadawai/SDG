package com.jica.sdg.repository;

import com.jica.sdg.model.NsaActivity;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NsaActivityRepository extends CrudRepository<NsaActivity, Integer> {
	@Query(value = "select * from nsa_activity where id_program = :id_program",nativeQuery = true)
	public List<NsaActivity> findAllGovActivity(@Param("id_program") Integer id_program); 
}
