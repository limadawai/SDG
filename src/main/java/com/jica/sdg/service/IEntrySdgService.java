package com.jica.sdg.service;

import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntrySdgIndicatorJoin;

import java.util.List;
import java.util.Optional;

public interface IEntrySdgService {

    List<EntrySdg> findAllEntrySdg();
    
//    List<Nsadetail> findId(String id);
//
    void saveEntrySdg(EntrySdg esdg);
//    
////    Optional<Nsaprofile> findOne(String id);
////    
//    void deleteIdNsa(String id);
//    
    void deleteEntrySdg(String id);
}
