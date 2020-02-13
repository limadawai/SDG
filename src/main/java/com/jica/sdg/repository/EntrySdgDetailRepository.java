package com.jica.sdg.repository;

import com.jica.sdg.model.EntrySdgDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface EntrySdgDetailRepository extends CrudRepository<EntrySdgDetail, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_sdg_detail set achievement1 = :achievement1, achievement2 = :achievement2, achievement3 = :achievement3, achievement4 = :achievement4 where id_disaggre = :id_disaggre and id_disaggre_detail = :id_disaggre_detail and year_entry = :year_entry and id_role = :id_role and id_monper = :id_monper ",nativeQuery = true)
    void updateEntrySdg(@Param("id_disaggre") Integer id_disaggre, @Param("id_disaggre_detail") Integer id_disaggre_detail, @Param("achievement1") Integer achievement1, @Param("achievement2") Integer achievement2, @Param("achievement3") Integer achievement3, @Param("achievement4") Integer achievement4, @Param("year_entry") Integer year_entry, @Param("id_role") Integer id_role, @Param("id_monper") Integer id_monper);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_sdg_detail set new_value1 = :nilai where id = :id",nativeQuery = true)
    void updateNew1(@Param("id") Integer id, @Param("nilai") Integer nilai);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_sdg_detail set new_value2 = :nilai where id = :id",nativeQuery = true)
    void updateNew2(@Param("id") Integer id, @Param("nilai") Integer nilai);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_sdg_detail set new_value3 = :nilai where id = :id",nativeQuery = true)
    void updateNew3(@Param("id") Integer id, @Param("nilai") Integer nilai);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_sdg_detail set new_value4 = :nilai where id = :id",nativeQuery = true)
    void updateNew4(@Param("id") Integer id, @Param("nilai") Integer nilai);   
}
