package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.repository.SdgDisaggreDetailRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class SdgDisaggreDetailService implements ISdgDisaggreDetailService{
	
	@Autowired
	SdgDisaggreDetailRepository SdgDisaggreDetailRepository;

	@Override
	public List<SdgDisaggreDetail> findAll(Integer id) {
		return (List<SdgDisaggreDetail>) SdgDisaggreDetailRepository.findAllDisaggreDetail(id);
	}

	@Override
	public void saveSdgDisaggreDetail(SdgDisaggreDetail sdg) {
		SdgDisaggreDetailRepository.save(sdg);
	}

	@Override
	public Optional<SdgDisaggreDetail> findOne(Integer id) {
		return (Optional<SdgDisaggreDetail>) SdgDisaggreDetailRepository.findById(id);
	}

	@Override
	public void deleteSdgDisaggreDetail(Integer id) {
		SdgDisaggreDetailRepository.deleteById(id);
	}
	
	
}
