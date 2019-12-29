package com.jica.sdg.repository;

import com.jica.sdg.model.GovProgram;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GovProgramRepository extends CrudRepository<GovProgram, String> {
}
