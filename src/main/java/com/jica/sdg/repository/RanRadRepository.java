package com.jica.sdg.repository;

import com.jica.sdg.model.RanRad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RanRadRepository extends CrudRepository<RanRad, Integer> {
}
