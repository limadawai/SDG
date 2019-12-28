package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovIndicator;
import com.jica.sdg.model.NsaIndicator;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.repository.GovIndicatorRepository;
import com.jica.sdg.repository.NsaIndicatorRepository;
import com.jica.sdg.repository.SdgIndicatorRepository;

@Service
public class NsaIndicatorService implements INsaIndicatorService{
	
	@Autowired
	NsaIndicatorRepository nsaIndicatorRepo;

	@Override
	public List<NsaIndicator> findAll(String id_program, String id_activity) {
		return (List<NsaIndicator>) nsaIndicatorRepo.findAllIndicator(id_program, id_activity);
	}

	@Override
	public void saveNsaIndicator(NsaIndicator gov) {
		nsaIndicatorRepo.save(gov);
	}

	@Override
	public Optional<NsaIndicator> findOne(String id) {
		return (Optional<NsaIndicator>) nsaIndicatorRepo.findById(id);
	}

	@Override
	public void deleteNsaIndicator(String id) {
		nsaIndicatorRepo.deleteById(id);
	}
	
	
}
