package com.jica.sdg.repository;

import com.jica.sdg.model.UserRequestList;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRequestListRepository extends CrudRepository<UserRequestList, String> {
	@Query(value = "select * from user_request_list where status = 'new'",nativeQuery = true)
	public List<UserRequestList> findAllNew(); 
}
