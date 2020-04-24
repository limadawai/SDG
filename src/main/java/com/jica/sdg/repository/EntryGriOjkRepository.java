package com.jica.sdg.repository;

import com.jica.sdg.model.EntryGriojk;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryGriOjkRepository extends CrudRepository<EntryGriojk, Integer> {
}
