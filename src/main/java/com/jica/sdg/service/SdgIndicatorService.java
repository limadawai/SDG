package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.model.SdgTarget;
import com.jica.sdg.repository.SdgGoalsRepository;
import com.jica.sdg.repository.SdgIndicatorRepository;
import com.jica.sdg.repository.SdgTargetRepository;

@Service
public class SdgIndicatorService implements ISdgIndicatorService{
	
	@Autowired
	SdgIndicatorRepository sdgIndicatorRepo;

	@Override
	public List<SdgIndicator> findAll(String id_goals, String id_target) {
		return (List<SdgIndicator>) sdgIndicatorRepo.findAllIndicator(id_goals, id_target);
	}

	@Override
	public void saveSdgIndicator(SdgIndicator sdg) {
		sdgIndicatorRepo.save(sdg);
	}

	@Override
	public Optional<SdgIndicator> findOne(String id) {
		return (Optional<SdgIndicator>) sdgIndicatorRepo.findById(id);
	}
	
	
}
