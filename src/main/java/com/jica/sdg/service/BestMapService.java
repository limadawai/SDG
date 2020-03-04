package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.BestMap;
import com.jica.sdg.repository.BestMapRepository;

@Service
public class BestMapService implements IBestMapService{
	
	@Autowired
	BestMapRepository repo;

	@Override
	public List<BestMap> findAll(String id) {
		return (List<BestMap>) repo.findAll();
	}

	@Override
	public void saveGovMap(BestMap sdg) {
		repo.save(sdg);
	}

	@Override
	public Optional<BestMap> findOne(Integer id) {
		return (Optional<BestMap>) repo.findById(id);
	}

	@Override
	public void deleteGovMap(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public void deleteGovMapBySdgInd(Integer id) {
		repo.deleteBySdgInd(id);
	}

	@Override
	public List<BestMap> findAllBySdgInd(String id) {
		return (List<BestMap>) repo.getIdBySdgInd(id);
	}

	@Override
	public void deleteGovMapByGovInd(Integer id) {
		repo.deleteByGovInd(id);
	}

	@Override
	public List<BestMap> findAllByGovInd(Integer id) {
		return (List<BestMap>) repo.getIdByGovInd(id);
	}

}
