package com.jica.sdg.repository;

import com.jica.sdg.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByUserName(String userName);
    
    @Query(value = "select a.id_user, a.username, b.nm_role, a.detail,a.name,b.id_prov from ref_user a left join ref_role b on a.id_role = b.id_role where b.id_prov = :id_prov and b.privilege != 'SUPER' order by a.username",nativeQuery = true)
	public List findByProvince(@Param("id_prov") String id_prov); 
    
    @Query(value = "select a.id_user, a.username, b.nm_role, a.detail,a.name,b.id_prov from ref_user a left join ref_role b on a.id_role = b.id_role order by a.username",nativeQuery = true)
	public List findAllGrid(); 
    
    @Query(value = "select count(*) from ref_user where username = :username",nativeQuery = true)
	public Integer cekUsername(@Param("username") String username);

}
