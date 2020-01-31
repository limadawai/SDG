package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.SdgFunding;
import com.jica.sdg.repository.SdgDisaggreRepository;
import com.jica.sdg.repository.SdgFundingRepository;

@Service
public class SdgFundingService implements ISdgFundingService{
	
	@Autowired
	SdgFundingRepository sdgFundingRepository;

	@Override
	public List<SdgFunding> findAll(Integer id_indicator, Integer id_monper) {
		return (List<SdgFunding>) sdgFundingRepository.findAllFunding(id_indicator, id_monper);
	}

	@Override
	public void saveSdgFunding(SdgFunding sdg) {
		sdgFundingRepository.save(sdg);
	}

//	@Override
//	public Optional<SdgDisaggre> findOne(Integer id) {
//		return (Optional<SdgDisaggre>) sdgDisaggreRepository.findById(id);
//	}

	@Override
	public void deleteSdgFunding(Integer id) {
		sdgFundingRepository.deleteById(id);
	}

	@Override
	public List<SdgFunding> findAllByIdIndicator(int id_indicator) {
		List list = sdgFundingRepository.findAllByIdIndi(id_indicator);
		return list;
	}


}
