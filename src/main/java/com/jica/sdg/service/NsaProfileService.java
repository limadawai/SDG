package com.jica.sdg.service;

import com.jica.sdg.model.Nsaprofile;
import com.jica.sdg.repository.NsaprofileRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.jica.sdg.model.SdgGoals;
//import com.jica.sdg.repository.SdgGoalsRepository;

@Service
public class NsaProfileService implements INsaProfileService{
	
	@Autowired
	NsaprofileRepository nsaProfileRepo;
	
	@Override
	public List<Nsaprofile> findAll() {
		return (List<Nsaprofile>) nsaProfileRepo.findAll();
	}
	
	@Override
	public List<Nsaprofile> findId(String id) {
		return (List<Nsaprofile>) nsaProfileRepo.findId(id);
	}
	
	@Override
	public void saveNsaProfil(Nsaprofile nsa) {
		nsaProfileRepo.save(nsa);
	}

//	@Override
//	public Optional<Nsaprofile> findOne(String id_nsa) {
//		return (Optional<Nsaprofile>) nsaProfileRepo.findById(id_nsa);
//	}
//        
        @Override
	public void deleteNsaProfil(String id_nsa) {
		nsaProfileRepo.deleteById(id_nsa);
	}
        
}
