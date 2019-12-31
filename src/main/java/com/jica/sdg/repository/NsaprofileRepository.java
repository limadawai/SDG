package com.jica.sdg.repository;

import com.jica.sdg.model.Nsaprofile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface NsaprofileRepository extends CrudRepository<Nsaprofile, String> {

    @Query(value = "select * from nsa_profile where id_nsa = :id ",nativeQuery = true)
    public List<Nsaprofile> findId(@Param("id") String id);


}
