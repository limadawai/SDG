package com.jica.sdg.repository;

import com.jica.sdg.model.NsaProgram;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NsaProgramRepository extends CrudRepository<NsaProgram, String> {
}
