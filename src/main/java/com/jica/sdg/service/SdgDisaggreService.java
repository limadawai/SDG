package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class SdgDisaggreService implements ISdgDisaggreService{
	
	@Autowired
	SdgDisaggreRepository sdgDisaggreRepository;

	@Override
	public List<SdgDisaggre> findAll(Integer id_indicator) {
		return (List<SdgDisaggre>) sdgDisaggreRepository.findAllDisaggre(id_indicator);
	}

	@Override
	public void saveSdgDisaggre(SdgDisaggre sdg) {
		sdgDisaggreRepository.save(sdg);
	}

	@Override
	public Optional<SdgDisaggre> findOne(Integer id) {
		return (Optional<SdgDisaggre>) sdgDisaggreRepository.findById(id);
	}

	@Override
	public void deleteSdgDisaggre(Integer id) {
		sdgDisaggreRepository.deleteById(id);
	}

	@Override
	public Integer countDisaggre(Integer id_indicator) {
		return sdgDisaggreRepository.countDisaggre(id_indicator);
	}
	
	
}
