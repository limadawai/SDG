package com.jica.sdg.repository;

import com.jica.sdg.model.EntryProblemIdentify;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryProblemIdentifyRepository extends CrudRepository<EntryProblemIdentify, Integer> {

    @Query(value = "select a.id, a.id_goals, b.nm_goals from entry_problem_identify a left join sdg_goals b on b.id_goals = a.id_goals", nativeQuery = true)
    public List findGoals();

    @Query(value = "select a.*, b.nm_goals from entry_problem_identify a left join sdg_goals b on b.id_goals = a.id_goals where id_role = :id_role", nativeQuery = true)
    public List findGoalsByRole(@Param("id_role") String id_role);

}
