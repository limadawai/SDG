package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.SdgTarget;
import com.jica.sdg.repository.SdgTargetRepository;

@Service
public class SdgTargetService implements ISdgTargetService{
	
	@Autowired
	SdgTargetRepository sdgtargetRepo;
	
	@Override
	public List<SdgTarget> findAll(String id) {
		return (List<SdgTarget>) sdgtargetRepo.findAllTarget(id);
	}
	
	@Override
	public void saveSdgTarget(SdgTarget sdg) {
		sdgtargetRepo.save(sdg);
	}

	@Override
	public Optional<SdgTarget> findOne(String id) {
		return (Optional<SdgTarget>) sdgtargetRepo.findById(id);
	}
}
