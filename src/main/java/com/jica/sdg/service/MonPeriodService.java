package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.RanRad;
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
	public List<RanRad> findAll(String id_prov) {
		return (List<RanRad>) monPer.findAllMonPeriod(id_prov);
	}

	@Override
	public void saveMonPeriod(RanRad sdg) {
		monPer.save(sdg);
	}

	@Override
	public Optional<RanRad> findOne(Integer id) {
		return (Optional<RanRad>) monPer.findById(id);
	}

	@Override
	public void deleteMonPeriod(Integer id) {
		monPer.deleteById(id);
	}

	@Override
	public Integer cekPeriode(String id_prov, Integer start, Integer end) {
		return monPer.cekPeriode(id_prov, start, end);
	}
	
	
}
