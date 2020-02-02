package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovFunding;
import com.jica.sdg.repository.GovFundingRepository;

@Service
public class GovFundingService implements IGovFundingService{
	
	@Autowired
	GovFundingRepository repo;

	@Override
	public List<GovFunding> findAll() {
		return (List<GovFunding>) repo.findAll();
	}

	@Override
	public void saveGovFunding(GovFunding sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<GovFunding> findOne(Integer id) {
		return (Optional<GovFunding>) repo.findById(id);
	}

	@Override
	public void deleteGovFunding(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public void deleteByGovInd(Integer id_gov_indicator, Integer id_monper) {
		repo.deleteByGovInd(id_gov_indicator, id_monper);
	}

	@Override
	public List<GovFunding> findByGovMon(Integer id_gov_indicator, Integer id_monper) {
		return (List<GovFunding>) repo.findByGovMon(id_gov_indicator, id_monper);
	}
	
	
}
