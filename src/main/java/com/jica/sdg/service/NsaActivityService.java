package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovActivity;
import com.jica.sdg.model.NsaActivity;
import com.jica.sdg.repository.GovActivityRepository;
import com.jica.sdg.repository.NsaActivityRepository;

@Service
public class NsaActivityService implements INsaActivityService{
	
	@Autowired
	NsaActivityRepository nsaActivityRepo;
	
	@Override
	public List<NsaActivity> findAll(String id_program) {
		return (List<NsaActivity>) nsaActivityRepo.findAllGovActivity(id_program);
	}
	
	@Override
	public void saveNsaActivity(NsaActivity gov) {
		nsaActivityRepo.save(gov);
	}

	@Override
	public Optional<NsaActivity> findOne(String id) {
		return (Optional<NsaActivity>) nsaActivityRepo.findById(id);
	}

	@Override
	public void deleteNsaActivity(String id) {
		nsaActivityRepo.deleteById(id);
	}
}
