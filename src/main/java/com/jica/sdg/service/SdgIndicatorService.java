package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.repository.SdgIndicatorRepository;

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

	@Override
	public void deleteSdgIndicator(String id) {
		sdgIndicatorRepo.deleteById(id);
	}

	@Override
	public List<SdgIndicator> findAll() {
		return (List<SdgIndicator>) sdgIndicatorRepo.findAll();
	}

	@Override
	public List findAllGrid(String id_goals, String id_target) {
		List list = sdgIndicatorRepo.findAllGrid(id_goals, id_target);
		return list;
	}
}
