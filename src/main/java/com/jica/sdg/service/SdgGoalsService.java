package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.repository.SdgGoalsRepository;

@Service
public class SdgGoalsService implements ISdgGoalsService{
	
	@Autowired
	SdgGoalsRepository sdgGoalsRepo;
	
	@Override
	public List<SdgGoals> findAll() {
		return (List<SdgGoals>) sdgGoalsRepo.findAllOrder();
	}
	
	@Override
	public void saveSdgGoals(SdgGoals sdg) {
		sdgGoalsRepo.save(sdg);
	}

	@Override
	public Optional<SdgGoals> findOne(int id) {
		return (Optional<SdgGoals>) sdgGoalsRepo.findById(id);
	}

	@Override
	public void deleteSdgGoals(int id) {
		sdgGoalsRepo.deleteById(id);
	}
}
