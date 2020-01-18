package com.jica.sdg.service;

import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntryGovIndicator;
import com.jica.sdg.model.EntryNsaIndicator;
import com.jica.sdg.model.EntrySdgIndicatorJoin;

import java.util.List;
import java.util.Optional;

public interface IEntrySdgService {

    List<EntrySdg> findAllEntrySdg();
    
//    List<Nsadetail> findId(String id);
//
    void saveEntrySdg(EntrySdg esdg);
    
    void saveEntryGovIndicator(EntryGovIndicator entryGovIndicator);
    
    void saveEntryNsaIndicator(EntryNsaIndicator entryNsaIndicator);
    
    void updateEntrySdg(String id_sdg_indicator, Integer achievement1, Integer achievement2, Integer achievement3, Integer achievement4, Integer year_entry, Integer id_role, Integer id_monper);
//    
////    Optional<Nsaprofile> findOne(String id);
////    
//    void deleteIdNsa(String id);
//    
    void deleteEntrySdg(String id);
}
