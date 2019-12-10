package com.jica.sdg.repository;

import com.jica.sdg.model.Nsaprofil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NsaprofilRepository extends CrudRepository<Nsaprofil, Long> {

    @Query(value = "select * from menu", nativeQuery = true)
    List<Nsaprofil> findAllNsa();

    @Query(value = "select id_nsa, nm_nsa from nsa_profil", nativeQuery = true)
    List<Nsaprofil> findNama();

}
