package com.jica.sdg.repository;

import com.jica.sdg.model.NsaCollaboration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface NsaCollaborationRepository extends CrudRepository<NsaCollaboration, Long> {

    @Query(value = "select * from nsa_collaboration where id_nsa_profil = :id ",nativeQuery = true)
    public List<NsaCollaboration> findId(@Param("id") String id);
    
    @Query(value = "select * from nsa_collaboration where id_program = :id",nativeQuery = true)
    public List<NsaCollaboration> findByNsaProg(@Param("id") String id);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update nsa_collaboration set id_philanthropy = :id_philanthropy where id = :id ",nativeQuery = true)
    void updateIdPhilanthropy(@Param("id_philanthropy") int id_philanthropy, @Param("id") int id);

}
