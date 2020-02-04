package com.jica.sdg.service;

import java.util.List;
import java.util.Map;
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
	public List<GovIndicator> findAll(Integer id_program, Integer id_activity) {
		return (List<GovIndicator>) govIndicatorRepo.findAllIndicator(id_program, id_activity);
	}

	@Override
	public void saveGovIndicator(GovIndicator gov) {
		govIndicatorRepo.save(gov);
	}

	@Override
	public Optional<GovIndicator> findOne(Integer id) {
		return (Optional<GovIndicator>) govIndicatorRepo.findById(id);
	}

	@Override
	public void deleteGovIndicator(Integer id) {
		govIndicatorRepo.deleteById(id);
	}

	@Override
	public List<GovIndicator> findAllByRole(Integer id_role) {
		return (List<GovIndicator>) govIndicatorRepo.findAllByRole(id_role);
	}

	@Override
	public List findAllIndi(Integer id_program, Integer id_activity) {
		List list = govIndicatorRepo.findAllIndi(id_program, id_activity);
		return list;
	}

	@Override
	public Integer countIndicator(Integer id_program, Integer id_activity) {
		return govIndicatorRepo.countIndicator(id_program, id_activity);
	}

}
