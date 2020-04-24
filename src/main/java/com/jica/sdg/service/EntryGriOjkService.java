package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.EntryGriojk;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.repository.EntryGriOjkRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class EntryGriOjkService implements IEntryGriOjkService{
	
	@Autowired
	EntryGriOjkRepository repo;

	@Override
	public List<EntryGriojk> findAll() {
		return (List<EntryGriojk>) repo.findAll();
	}

	@Override
	public void saveEntryGriOjk(EntryGriojk sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<EntryGriojk> findOne(Integer id) {
		return (Optional<EntryGriojk>) repo.findById(id);
	}

	@Override
	public void deleteEntryEntryGriOjk(Integer id) {
		repo.deleteById(id);
	}

	
}
