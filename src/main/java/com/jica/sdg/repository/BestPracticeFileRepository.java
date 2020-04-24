package com.jica.sdg.repository;

import com.jica.sdg.model.BestPracticeFile;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BestPracticeFileRepository extends CrudRepository<BestPracticeFile, Integer> {
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from best_practice_file WHERE id_best_practice = :id",nativeQuery = true)
    void deleteIdBest(@Param("id") Integer id);
    
}
