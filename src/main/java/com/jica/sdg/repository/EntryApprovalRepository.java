package com.jica.sdg.repository;

import com.jica.sdg.model.EntryApproval;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EntryApprovalRepository extends CrudRepository<EntryApproval, Integer> {
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_approval set approval = :approval, description = :description WHERE id = :id",nativeQuery = true)
    void updateApproval(@Param("approval") String approval, @Param("description") String description, @Param("id") Integer id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from entry_approval WHERE id_role = :id_role and id_monper = :id_monper and year = :year and type = :type and approval <> 2",nativeQuery = true)
    void deleteApproval(@Param("id_role") Integer id_role, @Param("id_monper") Integer id_monper, @Param("year") Integer year, @Param("type") String type);
    
}
