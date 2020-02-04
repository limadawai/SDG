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
	public List<SdgTarget> findAll(int id) {
		return (List<SdgTarget>) sdgtargetRepo.findAllTarget(id);
	}
	
	@Override
	public void saveSdgTarget(SdgTarget sdg) {
		sdgtargetRepo.save(sdg);
	}

	@Override
	public Optional<SdgTarget> findOne(int id) {
		return (Optional<SdgTarget>) sdgtargetRepo.findById(id);
	}

	@Override
	public void deleteSdgTarget(int id) {
		sdgtargetRepo.deleteById(id);
	}

	@Override
	public List<SdgTarget> findAll(Integer id) {
		return (List<SdgTarget>) sdgtargetRepo.findAllTarget(id);
	}

	@Override
	public Optional<SdgTarget> findOne(Integer id) {
		return (Optional<SdgTarget>) sdgtargetRepo.findById(id);
	}

	@Override
	public void deleteSdgTarget(Integer id) {
		sdgtargetRepo.deleteById(id);
	}

	@Override
	public Integer countTarget(Integer id) {
		return sdgtargetRepo.countTarget(id);
	}
}
