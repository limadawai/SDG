package com.jica.sdg.repository;

import com.jica.sdg.model.BestPractice;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BestPracticeRepository extends CrudRepository<BestPractice, Integer> {
}
