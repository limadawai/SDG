package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovMap;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.repository.GovMapRepository;
import com.jica.sdg.repository.SdgDisaggreDetailRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class GovMapService implements IGovMapService{
	
	@Autowired
	GovMapRepository repo;

	@Override
	public List<GovMap> findAll(String id) {
		return (List<GovMap>) repo.findAll();
	}

	@Override
	public void saveGovMap(GovMap sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<GovMap> findOne(Integer id) {
		return (Optional<GovMap>) repo.findById(id);
	}

	@Override
	public void deleteGovMap(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public void deleteGovMapBySdgInd(String id) {
		repo.deleteBySdgInd(id);
	}

	@Override
	public List<GovMap> findAllBySdgInd(String id) {
		return (List<GovMap>) repo.getIdBySdgInd(id);
	}

	@Override
	public List<GovMap> getAllByIdProv(String id_prov) {
		List list = (List<GovMap>) repo.getAllByIdProv(id_prov);
		return list;
	}
}
