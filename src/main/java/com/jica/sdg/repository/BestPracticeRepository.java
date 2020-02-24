package com.jica.sdg.repository;

import com.jica.sdg.model.BestPractice;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BestPracticeRepository extends CrudRepository<BestPractice, Integer> {
}
