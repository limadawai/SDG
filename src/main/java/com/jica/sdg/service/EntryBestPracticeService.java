package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.EntryBestPractice;
import com.jica.sdg.repository.EntryBestPracticeRepository;

@Service
public class EntryBestPracticeService implements IEntryBestPracticeService{
	
	@Autowired
	EntryBestPracticeRepository repo;

	@Override
	public List<EntryBestPractice> findAll() {
		return (List<EntryBestPractice>) repo.findAll();
	}

	@Override
	public void saveEntryBestPractice(EntryBestPractice sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<EntryBestPractice> findOne(Integer id) {
		return (Optional<EntryBestPractice>) repo.findById(id);
	}

	@Override
	public void deleteEntryBestPractice(Integer id) {
		repo.deleteById(id);
	}
	
}
