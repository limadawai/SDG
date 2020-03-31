package com.jica.sdg.repository;

import com.jica.sdg.model.EntryApproval;

import java.util.List;

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
    @Query(value = "delete from entry_approval WHERE id_role = :id_role and id_monper = :id_monper and year = :year and type = :type and periode = :periode and approval <> 2",nativeQuery = true)
    void deleteApproval(@Param("id_role") Integer id_role, @Param("id_monper") Integer id_monper, @Param("year") Integer year, @Param("type") String type, @Param("periode") String periode);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_approval set approval='4' WHERE id_monper = :id_monper and year = :year and type = :type and periode = :periode and (approval = '2' or approval = '1')",nativeQuery = true)
    void updatedoneApproval(@Param("id_monper") Integer id_monper, @Param("year") Integer year, @Param("type") String type, @Param("periode") String periode);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update entry_approval set approval='2' WHERE id_monper = :id_monper and year = :year and type = :type and periode = :periode and approval = '4' ",nativeQuery = true)
    void updateUNdoneApproval(@Param("id_monper") Integer id_monper, @Param("year") Integer year, @Param("type") String type, @Param("periode") String periode);

    @Query(value = "select * from entry_approval where approval = '3' order by id_monper, year, periode ",nativeQuery = true)
    public List<EntryApproval> getAllMessage();
    
    @Query(value = "select * from entry_approval where id_role =:id_role and approval = '3' order by id_monper, year, periode ",nativeQuery = true)
    public List<EntryApproval> getMessageByRole(@Param("id_role") Integer id_role);
    
    @Query(value = "select * from entry_approval a left join ref_role b on a.id_role=b.id_role where b.id_prov =:id_prov and a.approval = '3' order by a.id_monper, a.year, a.periode ",nativeQuery = true)
    public List<EntryApproval> getMessageByProv(@Param("id_prov") Integer id_prov);

}
