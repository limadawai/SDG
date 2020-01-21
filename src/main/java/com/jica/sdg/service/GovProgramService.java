package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovProgram;
import com.jica.sdg.repository.GovProgramRepository;

@Service
public class GovProgramService implements IGovProgramService{
	
	@Autowired
	GovProgramRepository govProgRepo;
	
	@Override
	public List<GovProgram> findAll() {
		return (List<GovProgram>) govProgRepo.findAll();
	}
	
	@Override
	public void saveGovProgram(GovProgram gov) {
		govProgRepo.save(gov);
	}

	@Override
	public Optional<GovProgram> findOne(Integer id) {
		return (Optional<GovProgram>) govProgRepo.findById(id);
	}

	@Override
	public void deleteGovProgram(Integer id) {
		govProgRepo.deleteById(id);
	}

	@Override
	//public List<GovProgram> findAllBy(String id_role,String id_monper) {
	public List<GovProgram> findAllBy(String id_monper) {
		return (List<GovProgram>) govProgRepo.findAll(id_monper);
	}
}
