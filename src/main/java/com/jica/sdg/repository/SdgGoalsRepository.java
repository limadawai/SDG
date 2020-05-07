package com.jica.sdg.repository;

import com.jica.sdg.model.SdgGoals;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SdgGoalsRepository extends CrudRepository<SdgGoals, Integer> {
	@Query(value = "select * from sdg_goals order by CAST(id_goals AS UNSIGNED) ",nativeQuery = true)
	public List<SdgGoals> findAllOrder();
}

