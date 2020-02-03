package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovFunding;
import com.jica.sdg.model.NsaFunding;
import com.jica.sdg.repository.GovFundingRepository;
import com.jica.sdg.repository.NsaFundingRepository;

@Service
public class NsaFundingService implements INsaFundingService{
	
	@Autowired
	NsaFundingRepository repo;

	@Override
	public List<NsaFunding> findAll() {
		return (List<NsaFunding>) repo.findAll();
	}

	@Override
	public void saveNsaFunding(NsaFunding sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<NsaFunding> findOne(Integer id) {
		return (Optional<NsaFunding>) repo.findById(id);
	}

	@Override
	public void deleteNsaFunding(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public void deleteByNsaInd(Integer id_nsa_indicator, Integer id_monper) {
		repo.deleteByNsaInd(id_nsa_indicator, id_monper);
	}

	@Override
	public List<NsaFunding> findByNsaMon(Integer id_nsa_indicator, Integer id_monper) {
		return (List<NsaFunding>) repo.findByNsaMon(id_nsa_indicator, id_monper);
	}
	
	
}
