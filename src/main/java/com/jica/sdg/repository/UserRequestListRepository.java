package com.jica.sdg.repository;

import com.jica.sdg.model.UserRequestList;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRequestListRepository extends CrudRepository<UserRequestList, String> {
	@Query(value = "select * from user_request_list where status = 'new'",nativeQuery = true)
	public List<UserRequestList> findAllNew();
	
	@Query(value = "select * from user_request_list where status = 'new' and id_prov = :id_prov",nativeQuery = true)
	public List<UserRequestList> findAllNewProv(@Param("id_prov") String id_prov);
	
	@Query(value = "select * from user_request_list where id_prov = :id_prov",nativeQuery = true)
	public List<UserRequestList> findAllProv(@Param("id_prov") String id_prov);
}
