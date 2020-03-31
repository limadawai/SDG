package com.jica.sdg.service;

import com.jica.sdg.model.EntryGovBudget;
import com.jica.sdg.model.EntryGovIndicator;
import com.jica.sdg.model.EntryNsaBudget;
import com.jica.sdg.model.EntryNsaIndicator;
import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntrySdgIndicatorJoin;
import com.jica.sdg.model.SdgIndicatorTarget;
import com.jica.sdg.repository.EntryGovBudgetRepository;
import com.jica.sdg.repository.EntrySdgRepository;
import com.jica.sdg.repository.EntryGovIndicatorRepository;
import com.jica.sdg.repository.EntryNsaBudgetRepository;
import com.jica.sdg.repository.EntryNsaIndicatorRepository;
import com.jica.sdg.repository.SdgIndicatorTargetRepository;
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
	
	@Autowired
	SdgIndicatorTargetRepository sdgIndicatorTargetRepo;
	
	@Autowired
	EntryGovBudgetRepository entryGovBudgetRepo;
	
	@Autowired
	EntryNsaBudgetRepository entryNsaBudgetRepo;
	
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
            //entryGovIndicator.setDate_created(date);
            entryGovIndicatorRepo.save(entryGovIndicator);
	}
        
	@Override
	public void saveEntryGovBudget(EntryGovBudget entryGovBudget) {
            Date date = new Date();
            //esdg.setApproval_date(date);
//            esdg.setShow_report_date(date);
//            entryGovBudget.setDate_created(date);
            entryGovBudgetRepo.save(entryGovBudget);
	}
        
	@Override
	public void saveEntryNsaBudget(EntryNsaBudget entryNsaBudget) {
            Date date = new Date();
            //esdg.setApproval_date(date);
//            esdg.setShow_report_date(date);
//            entryNsaBudget.setDate_created(date);
            entryNsaBudgetRepo.save(entryNsaBudget);
	}
        
	@Override
	public void saveEntryNsaIndicator(EntryNsaIndicator entryNsaIndicator) {
            Date date = new Date();
            //esdg.setApproval_date(date);
//            esdg.setShow_report_date(date);
//            entryNsaIndicator.setDate_created(date);
            entryNsaIndicatorRepo.save(entryNsaIndicator);
	}
        
	@Override
	public void saveSdgIndicatorTargetEntry(SdgIndicatorTarget sdgIndicatorTarget) {
//            Date date = new Date();
            //esdg.setApproval_date(date);
//            esdg.setShow_report_date(date);
//            entryNsaIndicator.setDate_created(date);
            sdgIndicatorTargetRepo.save(sdgIndicatorTarget);
	}
        
	@Override
	public void deleteSdgIndicatorTargetEntry(int id) {
            sdgIndicatorTargetRepo.deleteById(id);
//            sdgIndicatorTargetRepo.deleteEntrySdgIndicator(id);
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

	@Override
	public void updateNew1(Integer id, Integer nilai) {
		entrySdgRepo.updateNew1(id, nilai);
	}

	@Override
	public void updateNew2(Integer id, Integer nilai) {
		entrySdgRepo.updateNew2(id, nilai);
	}

	@Override
	public void updateNew3(Integer id, Integer nilai) {
		entrySdgRepo.updateNew3(id, nilai);
	}

	@Override
	public void updateNew4(Integer id, Integer nilai) {
		entrySdgRepo.updateNew4(id, nilai);
	}

	@Override
	public Optional<EntrySdg> findOne(Long id) {
		return (Optional<EntrySdg>) entrySdgRepo.findById(id);
	}

	@Override
	public Optional<EntryGovIndicator> findOneGovInd(Integer id) {
		return entryGovIndicatorRepo.findById(id);
	}

	@Override
	public Optional<EntryGovBudget> findOneGovBud(Integer id) {
            return entryGovBudgetRepo.findById(id);
	}
	
	@Override
	public Optional<EntryNsaIndicator> findOneNsaInd(Integer id) {
		return entryNsaIndicatorRepo.findById(id);
	}
	
	@Override
	public Optional<EntryNsaBudget> findOneNsaBud(Integer id) {
            return entryNsaBudgetRepo.findById(id);
	}
        
}
