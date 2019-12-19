package com.jica.sdg.repository;

import com.jica.sdg.model.SdgGoals;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SdgGoalsRepository extends CrudRepository<SdgGoals, String> {
}
