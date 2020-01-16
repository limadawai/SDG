package com.jica.sdg.repository;

import com.jica.sdg.model.EntryProblemIdentity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryProblemIdentifyRepository extends CrudRepository<EntryProblemIdentity, Integer> {

    @Query(value = "select a.*, b.nm_goals from entry_problem_identify a left join sdg_goals b on a.id_goals = b.id_goals", nativeQuery = true)
    public List findGoals();

}
