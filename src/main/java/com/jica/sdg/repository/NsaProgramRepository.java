package com.jica.sdg.repository;

import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.NsaProgram;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NsaProgramRepository extends CrudRepository<NsaProgram, Integer> {
	@Query(value = "select * from nsa_program where id_role = :id_role and id_monper = :id_monper order by CAST(internal_code AS UNSIGNED)",nativeQuery = true)
	public List<NsaProgram> findAll(@Param("id_role") String id_role, @Param("id_monper") String id_monper); 
	
	@Query(value = "select a.* from nsa_program a left join ref_role b on a.id_role=b.id_role where a.id_monper = :id_monper and b.id_prov = :id_prov order by CAST(a.internal_code AS UNSIGNED)",nativeQuery = true)
	public List<NsaProgram> findAllByMonperProv(@Param("id_monper") Integer id_monper, @Param("id_prov") String id_prov); 
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update nsa_program set id_role = :id_role where id = :id",nativeQuery = true)
    void UpdateRole(@Param("id_role") Integer id_role, @Param("id") Integer id);
}
