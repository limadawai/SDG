package com.jica.sdg.repository;

import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntrySdgIndicatorJoin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface EntrySdgRepository extends CrudRepository<EntrySdg, Long> {

    @Query(value = "select a.*, b.achievement1, b.approval  from sdg_indicator as a left join entry_sdg as b on a.id_indicator = b.id_sdg_indicator ",nativeQuery = true)
    public List<EntrySdg> findAllEntrySdg();
    
    @Query(value = "select * from nsa_collaboration where id_nsa_profil = :id ",nativeQuery = true)
    public List<EntrySdg> findId(@Param("id") String id);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from entry_sdg where id_sdg_indicator = :id ",nativeQuery = true)
    void deleteEntrySdg(@Param("id") String id);

}
