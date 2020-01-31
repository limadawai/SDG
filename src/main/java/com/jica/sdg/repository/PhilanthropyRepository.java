package com.jica.sdg.repository;

import com.jica.sdg.model.PhilanthropyCollaboration;
import com.jica.sdg.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface PhilanthropyRepository extends CrudRepository<PhilanthropyCollaboration, Integer> {

//    @Query(value = "select * from nsa_profile where id_role = :id ",nativeQuery = true)
//    public List<Nsaprofile> findId(@Param("id") String id);

}
