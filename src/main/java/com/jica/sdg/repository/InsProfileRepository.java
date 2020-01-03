package com.jica.sdg.repository;

import com.jica.sdg.model.Insprofile;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InsProfileRepository extends CrudRepository<Insprofile, Integer> {
    
    @Query(value = "select * from nsa_inst where id_role = :id ",nativeQuery = true)
    public List<Insprofile> findId(@Param("id") String id);
    
}
