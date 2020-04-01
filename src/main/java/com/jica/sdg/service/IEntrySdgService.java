package com.jica.sdg.service;

import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntryGovIndicator;
import com.jica.sdg.model.EntryGovBudget;
import com.jica.sdg.model.EntryNsaBudget;
import com.jica.sdg.model.EntryNsaIndicator;
import com.jica.sdg.model.EntrySdgIndicatorJoin;
import com.jica.sdg.model.SdgIndicatorTarget;

import java.util.List;
import java.util.Optional;

public interface IEntrySdgService {

    List<EntrySdg> findAllEntrySdg();
    
    Optional<EntrySdg> findOne(Long id);
    
    Optional<EntryGovIndicator> findOneGovInd(Integer id);
    Optional<EntryGovBudget> findOneGovBud(Integer id);
    
    Optional<EntryNsaIndicator> findOneNsaInd(Integer id);
    Optional<EntryNsaBudget> findOneNsaBud(Integer id);
    
//    List<Nsadetail> findId(String id);
//
    void saveEntrySdg(EntrySdg esdg);
    
    void saveEntryGovBudget(EntryGovBudget entryGovBudget);
    
    void saveEntryGovIndicator(EntryGovIndicator entryGovIndicator);
    
    void saveEntryNsaBudget(EntryNsaBudget entryNsaBudget);
    
    void saveEntryNsaIndicator(EntryNsaIndicator entryNsaIndicator);
    
    void saveSdgIndicatorTargetEntry(SdgIndicatorTarget sdgIndicatorTarget);
    
    void deleteSdgIndicatorTargetEntry(int id);
    
    void updateEntrySdg(String id_sdg_indicator, Integer achievement1, Integer achievement2, Integer achievement3, Integer achievement4, Integer year_entry, Integer id_role, Integer id_monper);
//    
////    Optional<Nsaprofile> findOne(String id);
////    
//    void deleteIdNsa(String id);
//    
    void deleteEntrySdg(String id);
    
    void updateNew1(Integer id, Integer nilai);
    void updateNew2(Integer id, Integer nilai);
    void updateNew3(Integer id, Integer nilai);
    void updateNew4(Integer id, Integer nilai);
}
