package com.jica.sdg.repository;

import com.jica.sdg.model.EntryShowReport;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EntryShowReportRepository extends CrudRepository<EntryShowReport, Integer> {

    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from entry_show_report WHERE year = :year and id_monper = :id_monper and show_report = '1' and type = :type and period = :periode ",nativeQuery = true)
    void deleteShowreport(@Param("year") Integer year, @Param("id_monper") Integer id_monper, @Param("type") String type, @Param("periode") String periode);



}
