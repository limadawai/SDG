package com.jica.sdg.repository;

import com.jica.sdg.model.Unit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends CrudRepository<Unit, Integer> {
}
