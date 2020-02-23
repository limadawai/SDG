package com.jica.sdg.repository;

import com.jica.sdg.model.EntryBestPractice;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryBestPracticeRepository extends CrudRepository<EntryBestPractice, Integer> {
}
