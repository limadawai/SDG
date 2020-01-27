package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovMap;
import com.jica.sdg.model.NsaMap;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.repository.GovMapRepository;
import com.jica.sdg.repository.NsaMapRepository;
import com.jica.sdg.repository.SdgDisaggreDetailRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class NsaMapService implements INsaMapService{
	
	@Autowired
	NsaMapRepository repo;

	@Override
	public List<NsaMap> findAll(String id) {
		return (List<NsaMap>) repo.findAll();
	}

	@Override
	public void saveNsaMap(NsaMap sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<NsaMap> findOne(Integer id) {
		return (Optional<NsaMap>) repo.findById(id);
	}

	@Override
	public void deleteNsaMap(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public void deleteNsaMapBySdgInd(String id) {
		repo.deleteBySdgInd(id);
	}

	@Override
	public List<NsaMap> findAllBySdgInd(String id) {
		return (List<NsaMap>) repo.findAll();
	}

	@Override
	public List<NsaMap> findAllByNsaInd(Integer id) {
		return (List<NsaMap>) repo.getIdByNsaInd(id);
	}

	@Override
	public void deleteNsaMapByNsaInd(Integer id) {
		repo.deleteByNsaInd(id);
	}	
}
