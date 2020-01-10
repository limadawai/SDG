package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.model.Unit;
import com.jica.sdg.repository.SdgDisaggreDetailRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;
import com.jica.sdg.repository.UnitRepository;

@Service
public class UnitService implements IUnitService{
	
	@Autowired
	UnitRepository repo;

	@Override
	public List<Unit> findAll() {
		return (List<Unit>) repo.findAll();
	}

	@Override
	public void saveUnit(Unit unit) {
		repo.save(unit);
	}

	@Override
	public Optional<Unit> findOne(Integer id) {
		return (Optional<Unit>) repo.findById(id);
	}

	@Override
	public void deleteUnit(Integer id) {
		repo.deleteById(id);
	}
	
	
}
