package com.jica.sdg.repository;

import com.jica.sdg.model.SdgIndicatorTarget;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SdgIndicatorTargetRepository extends CrudRepository<SdgIndicatorTarget, Integer> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from sdg_indicator_target where id = :id ",nativeQuery = true)
    void deleteEntrySdgIndicator(@Param("id") int id);
}
