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
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_sdg set achievement1 = :achievement1, achievement2 = :achievement2, achievement3 = :achievement3, achievement4 = :achievement4 where id_sdg_indicator = :id_sdg_indicator and year_entry = :year_entry and id_role = :id_role and id_monper = :id_monper ",nativeQuery = true)
    void updateEntrySdg(@Param("id_sdg_indicator") String id_sdg_indicator, @Param("achievement1") Integer achievement1, @Param("achievement2") Integer achievement2, @Param("achievement3") Integer achievement3, @Param("achievement4") Integer achievement4, @Param("year_entry") Integer year_entry, @Param("id_role") Integer id_role, @Param("id_monper") Integer id_monper);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_sdg set new_value1 = :nilai where id = :id",nativeQuery = true)
    void updateNew1(@Param("id") Integer id, @Param("nilai") Integer nilai);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_sdg set new_value2 = :nilai where id = :id",nativeQuery = true)
    void updateNew2(@Param("id") Integer id, @Param("nilai") Integer nilai);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_sdg set new_value3 = :nilai where id = :id",nativeQuery = true)
    void updateNew3(@Param("id") Integer id, @Param("nilai") Integer nilai);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_sdg set new_value4 = :nilai where id = :id",nativeQuery = true)
    void updateNew4(@Param("id") Integer id, @Param("nilai") Integer nilai);
    
}
