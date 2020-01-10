package com.jica.sdg.service;

import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntrySdgIndicatorJoin;
import com.jica.sdg.repository.EntrySdgRepository;
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
            esdg.setShow_report_date(date);
            esdg.setDate_created(date);
            entrySdgRepo.save(esdg);
	}
//
////	@Override
////	public Optional<Nsaprofile> findOne(String id_nsa) {
////		return (Optional<Nsaprofile>) nsaProfileRepo.findById(id_nsa);
////	}
////        
        @Override
	public void deleteEntrySdg(String id_nsa) {
		entrySdgRepo.deleteEntrySdg(id_nsa);
	}
        
}
