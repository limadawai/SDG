package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.BestPracticeFile;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.repository.BestPracticeFileRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class BestPracticeFileService implements IBestPracticeFileService{
	
	@Autowired
	BestPracticeFileRepository repo;

	@Override
	public List<BestPracticeFile> findAll() {
		return (List<BestPracticeFile>) repo.findAll();
	}

	@Override
	public void saveBestPracticeFile(BestPracticeFile sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<BestPracticeFile> findOne(Integer id) {
		return (Optional<BestPracticeFile>) repo.findById(id);
	}

	@Override
	public void deleteBestPracticeFile(Integer id) {
		repo.deleteIdBest(id);
//		repo.deleteById(id);
	}
	
}
