package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovTarget;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.repository.GovTargetRepository;
import com.jica.sdg.repository.SdgDisaggreDetailRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class GovTargetService implements IGovTargetService{
	
	@Autowired
	GovTargetRepository repo;

	@Override
	public List<GovTarget> findAll() {
		return (List<GovTarget>) repo.findAll();
	}

	@Override
	public void saveGovTarget(GovTarget sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<GovTarget> findOne(Integer id) {
		return (Optional<GovTarget>) repo.findById(id);
	}

	@Override
	public void deleteGovTarget(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public void deleteByGovInd(Integer id) {
		repo.deleteByGovInd(id);
	}

	@Override
	public List<GovTarget> findByGovYear(Integer id_gov_indicator, Integer year) {
		return (List<GovTarget>) repo.findByGovYear(id_gov_indicator, year);
	}
	
	
}
