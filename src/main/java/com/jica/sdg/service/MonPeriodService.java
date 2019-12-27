package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.MonPeriod;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.repository.MonPeriodRepository;
import com.jica.sdg.repository.SdgDisaggreDetailRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class MonPeriodService implements IMonPeriodService{
	
	@Autowired
	MonPeriodRepository monPer;

	@Override
	public List<MonPeriod> findAll(String id_prov) {
		return (List<MonPeriod>) monPer.findAllMonPeriod(id_prov);
	}

	@Override
	public void saveMonPeriod(MonPeriod sdg) {
		monPer.save(sdg);
	}

	@Override
	public Optional<MonPeriod> findOne(Integer id) {
		return (Optional<MonPeriod>) monPer.findById(id);
	}
	
	
}
