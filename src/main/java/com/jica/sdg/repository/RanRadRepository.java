package com.jica.sdg.repository;

import com.jica.sdg.model.RanRad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RanRadRepository extends CrudRepository<RanRad, Integer> {

    @Query(value = "select * from ran_rad where id_prov = :id_prov", nativeQuery = true)
    public List<RanRad> findByIdProv(String id_prov);

}
