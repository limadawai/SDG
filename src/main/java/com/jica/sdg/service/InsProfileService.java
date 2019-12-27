package com.jica.sdg.service;

import com.jica.sdg.model.Insprofile;
import com.jica.sdg.repository.InsProfileRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.jica.sdg.model.SdgGoals;
//import com.jica.sdg.repository.SdgGoalsRepository;

@Service
public class InsProfileService implements IInsProfileService{
	
	@Autowired
	InsProfileRepository insProfileRepo;
	
	@Override
	public List<Insprofile> findAll() {
		return (List<Insprofile>) insProfileRepo.findAll();
	}
	
	@Override
	public void saveInsProfil(Insprofile ins) {
		insProfileRepo.save(ins);
	}

	@Override
	public Optional<Insprofile> findOne(String id) {
		return (Optional<Insprofile>) insProfileRepo.findById(id);
	}
}
