package com.jica.sdg.repository;

import com.jica.sdg.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByUserName(String userName);
    
    @Query(value = "select a.* from ref_user a left join ref_role b on a.id_role = b.id_role where b.id_prov = :id_prov",nativeQuery = true)
	public List<User> findByProvince(@Param("id_prov") String id_prov); 

}
