package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovActivity;
import com.jica.sdg.repository.GovActivityRepository;

@Service
public class GovActivityService implements IGovActivityService{
	
	@Autowired
	GovActivityRepository govActivityRepo;
	
	@Override
	public List<GovActivity> findAll(Integer id_program) {
		return (List<GovActivity>) govActivityRepo.findAllGovActivity(id_program);
	}
	
	@Override
	public void saveGovActivity(GovActivity gov) {
		govActivityRepo.save(gov);
	}

	@Override
	public Optional<GovActivity> findOne(Integer id) {
		return (Optional<GovActivity>) govActivityRepo.findById(id);
	}

	@Override
	public void deleteGovActivity(Integer id) {
		govActivityRepo.deleteById(id);
	}
}
