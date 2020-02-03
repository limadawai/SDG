package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovTarget;
import com.jica.sdg.model.NsaTarget;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.repository.GovTargetRepository;
import com.jica.sdg.repository.NsaTargetRepository;
import com.jica.sdg.repository.SdgDisaggreDetailRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class NsaTargetService implements INsaTargetService{
	
	@Autowired
	NsaTargetRepository repo;

	@Override
	public List<NsaTarget> findAll() {
		return (List<NsaTarget>) repo.findAll();
	}

	@Override
	public void saveNsaTarget(NsaTarget sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<NsaTarget> findOne(Integer id) {
		return (Optional<NsaTarget>) repo.findById(id);
	}

	@Override
	public void deleteNsaTarget(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public void deleteByNsaInd(Integer id) {
		repo.deleteByNsaInd(id);
	}

	@Override
	public List<NsaTarget> findByNsaYear(Integer id_nsa_indicator, Integer year) {
		return (List<NsaTarget>) repo.findByNsaYear(id_nsa_indicator, year);
	}
	
	
}
