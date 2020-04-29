package com.jica.sdg.repository;

import com.jica.sdg.model.Role;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    
    @Query(value = "select * from ref_role where LOWER(cat_role) = 'nsa' ",nativeQuery = true)
    public List<Role> findRoleNsa();
    
    @Query(value = "select * from ref_role ",nativeQuery = true)
    public List<Role> findRoleAll();
    
    @Query(value = "select * from ref_role where LOWER(cat_role) = 'government' ",nativeQuery = true)
    public List<Role> findRoleGov();
    
    @Query(value = "select * from ref_role where LOWER(cat_role) = 'Institution' ",nativeQuery = true)
    public List<Role> findRoleInstitusi();
    
	@Query(value = "select * from ref_role where id_prov = :id_prov and cat_role != 'gri_ojk' and privilege != 'SUPER'",nativeQuery = true)
	public List<Role> findByProvince(@Param("id_prov") String id_prov);
	
	@Query(value = "select * from ref_role where id_prov = :id_prov and cat_role != 'gri_ojk'",nativeQuery = true)
	public List<Role> findByProvinceUserForm(@Param("id_prov") String id_prov);
	
	@Query(value = "select * from ref_role where id_prov = :id_prov and privilege != 'SUPER' and cat_role = :cat and privilege = :prev",nativeQuery = true)
	public List<Role> findByProvince(@Param("id_prov") String id_prov, @Param("cat") String cat,@Param("prev") String prev); 
	
	@Query(value = "select * from ref_role where id_role!=1 and cat_role = :cat and privilege = :prev",nativeQuery = true)
	public List<Role> findByCat(@Param("cat") String cat,@Param("prev") String prev); 
	
	@Query(value = "select * from ref_role where privilege != 'SUPER'",nativeQuery = true)
	public List<Role> findAllNonSuper(); 
	
	@Query(value = "select * from ref_role where id_role!=1",nativeQuery = true)
	public List<Role> findAllGrid();
	
	@Query(value = "select count(*) from ref_role where id_prov = :id_prov and nm_role = :nm_role",nativeQuery = true)
	public Integer cekNmRole(@Param("id_prov") String id_prov, @Param("nm_role") String nm_role);
	
	@Query(value = "select count(*) from ref_role where id_prov = :id_prov and cat_role = 'ADMIN' ",nativeQuery = true)
	public Integer cekRole(@Param("id_prov") String id_prov);
	
	@Query(value = "select * from ref_role where id_prov = :id_prov and cat_role = 'NSA'",nativeQuery = true)
	public List<Role> findNsaByProvince(@Param("id_prov") String id_prov);
	
	@Query(value = "select * from ref_role where id_prov != '000' and cat_role = 'NSA'",nativeQuery = true)
	public List<Role> findNsaAllProvince(); 
	
	@Query(value = "select * from ref_role where id_prov = :id_prov and (cat_role = 'Institution' or cat_role = 'NSA') and privilege='USER'",nativeQuery = true)
    public List<Role> findRoleNonGov(@Param("id_prov") String id_prov);
	
	@Query(value = "select * from ref_role where id_prov = :id_prov and cat_role = 'Government' and privilege='USER'",nativeQuery = true)
    public List<Role> findRoleGov(@Param("id_prov") String id_prov);

	@Query(value = "select cat_role from ref_role where id_role = :id_role", nativeQuery = true)
	public List<Role> findCatRole(@Param("id_role") int id_role);

}
