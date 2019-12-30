package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.NsaProgram;
import com.jica.sdg.repository.NsaProgramRepository;

@Service
public class NsaProgramService implements INsaProgramService{
	
	@Autowired
	NsaProgramRepository nsaProgRepo;
	
	@Override
	public List<NsaProgram> findAll() {
		return (List<NsaProgram>) nsaProgRepo.findAll();
	}
	
	@Override
	public void saveNsaProgram(NsaProgram gov) {
		nsaProgRepo.save(gov);
	}

	@Override
	public Optional<NsaProgram> findOne(String id) {
		return (Optional<NsaProgram>) nsaProgRepo.findById(id);
	}

	@Override
	public void deleteNsaProgram(String id) {
		nsaProgRepo.deleteById(id);
	}
}
