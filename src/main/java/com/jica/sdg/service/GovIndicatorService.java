package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovIndicator;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.repository.GovIndicatorRepository;
import com.jica.sdg.repository.SdgIndicatorRepository;

@Service
public class GovIndicatorService implements IGovIndicatorService{
	
	@Autowired
	GovIndicatorRepository govIndicatorRepo;

	@Override
	public List<GovIndicator> findAll(String id_program, String id_activity) {
		return (List<GovIndicator>) govIndicatorRepo.findAllIndicator(id_program, id_activity);
	}

	@Override
	public void saveGovIndicator(GovIndicator gov) {
		govIndicatorRepo.save(gov);
	}

	@Override
	public Optional<GovIndicator> findOne(String id) {
		return (Optional<GovIndicator>) govIndicatorRepo.findById(id);
	}

	@Override
	public void deleteGovIndicator(String id) {
		govIndicatorRepo.deleteById(id);
	}
	
	
}
