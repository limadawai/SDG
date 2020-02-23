package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.BestPractice;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.repository.BestPracticeRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class BestPracticeService implements IBestPracticeService{
	
	@Autowired
	BestPracticeRepository repo;

	@Override
	public List<BestPractice> findAll() {
		return (List<BestPractice>) repo.findAll();
	}

	@Override
	public void saveBestPractice(BestPractice sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<BestPractice> findOne(Integer id) {
		return (Optional<BestPractice>) repo.findById(id);
	}

	@Override
	public void deleteBestPractice(Integer id) {
		repo.deleteById(id);
	}
	
}
