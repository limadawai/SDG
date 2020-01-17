package com.jica.sdg.service;

import com.jica.sdg.model.EntryGovIndicator;
import com.jica.sdg.model.EntryNsaIndicator;
import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntrySdgIndicatorJoin;
import com.jica.sdg.repository.EntrySdgRepository;
import com.jica.sdg.repository.EntryGovIndicatorRepository;
import com.jica.sdg.repository.EntryNsaIndicatorRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.jica.sdg.model.SdgGoals;
//import com.jica.sdg.repository.SdgGoalsRepository;

@Service
public class EntrySdgService implements IEntrySdgService{
	
	@Autowired
	EntrySdgRepository entrySdgRepo;
	
	@Autowired
	EntryGovIndicatorRepository entryGovIndicatorRepo;
	
	@Autowired
	EntryNsaIndicatorRepository entryNsaIndicatorRepo;
	
	@Override
	public List<EntrySdg> findAllEntrySdg() {
		return (List<EntrySdg>) entrySdgRepo.findAllEntrySdg();
	}
	
//	@Override
//	public List<Nsaprofile> findId(String id) {
//		return (List<Nsaprofile>) nsaProfileRepo.findId(id);
//	}
//	
	@Override
	public void saveEntrySdg(EntrySdg esdg) {
            Date date = new Date();
            //esdg.setApproval_date(date);
//            esdg.setShow_report_date(date);
            esdg.setDate_created(date);
            entrySdgRepo.save(esdg);
	}
        
	@Override
	public void saveEntryGovIndicator(EntryGovIndicator entryGovIndicator) {
            Date date = new Date();
            //esdg.setApproval_date(date);
//            esdg.setShow_report_date(date);
            entryGovIndicator.setDate_created(date);
            entryGovIndicatorRepo.save(entryGovIndicator);
	}
        
	@Override
	public void saveEntryNsaIndicator(EntryNsaIndicator entryNsaIndicator) {
            Date date = new Date();
            //esdg.setApproval_date(date);
//            esdg.setShow_report_date(date);
            entryNsaIndicator.setDate_created(date);
            entryNsaIndicatorRepo.save(entryNsaIndicator);
	}
//
////	@Override
////	public Optional<Nsaprofile> findOne(String id_nsa) {
////		return (Optional<Nsaprofile>) nsaProfileRepo.findById(id_nsa);
////	}
////        
        @Override
	public void updateEntrySdg(String id_sdg_indicator, Integer achievement1, Integer achievement2, Integer achievement3, Integer achievement4, Integer year_entry, Integer id_role, Integer id_monper) {
		entrySdgRepo.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
	}
        
        @Override
	public void deleteEntrySdg(String id_nsa) {
		entrySdgRepo.deleteEntrySdg(id_nsa);
	}
        
}
