package com.jica.sdg.repository;

import com.jica.sdg.model.Nsaprofile;
import com.jica.sdg.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface NsaprofileRepository extends CrudRepository<Nsaprofile, Integer> {

    @Query(value = "select * from nsa_profile where id_role = :id ",nativeQuery = true)
    public List<Nsaprofile> findId(@Param("id") Integer id);

    @Query(value = "select a.* from nsa_profile a left join ref_role b on a.id_role=b.id_role where b.id_prov = :id ",nativeQuery = true)
    public List<Nsaprofile> findIdProv(@Param("id") String id);


}
