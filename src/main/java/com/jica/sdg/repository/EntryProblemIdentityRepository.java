package com.jica.sdg.repository;

import com.jica.sdg.model.EntryProblemIdentity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryProblemIdentityRepository extends CrudRepository<EntryProblemIdentity, Integer> {
}
