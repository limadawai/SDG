package com.jica.sdg.repository;

import com.jica.sdg.model.Nsadetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface NsadetailRepository extends CrudRepository<Nsadetail, Long> {

    @Query(value = "select * from nsa_detail where id_role = :id ",nativeQuery = true)
    public List<Nsadetail> findId(@Param("id") String id);

    @Query(value = "select * from nsa_detail where id_nsa = :id ",nativeQuery = true)
    public List<Nsadetail> findIdNsa(@Param("id") String id);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from nsa_detail where id_nsa = :id ",nativeQuery = true)
    void deleteIdNsa(@Param("id") Integer id);

}
