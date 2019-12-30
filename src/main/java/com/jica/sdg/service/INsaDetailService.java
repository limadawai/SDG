package com.jica.sdg.service;

import com.jica.sdg.model.Nsadetail;

import java.util.List;
import java.util.Optional;

public interface INsaDetailService {

    List<Nsadetail> findAll();
    
    List<Nsadetail> findId(String id);

    void saveNsaDetail(Nsadetail ins);
    
//    Optional<Nsaprofile> findOne(String id);
//    
    void deleteIdNsa(String id);
    
    void deleteNsaDetail(Long id);
}
