package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.AssignSdgIndicator;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.repository.AssignSdgIndicatorRepository;
import com.jica.sdg.repository.SdgDisaggreDetailRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class AssignSdgIndicatorService implements IAssignSdgIndicatorService{
	
	@Autowired
	AssignSdgIndicatorRepository repo;

	@Override
	public List<AssignSdgIndicator> findAll(String id) {
		return (List<AssignSdgIndicator>) repo.findAll();
	}

	@Override
	public void saveAssignSdgIndicator(AssignSdgIndicator assign) {
		repo.save(assign);
	}

	@Override
	public Optional<AssignSdgIndicator> findOne(Integer id) {
		return (Optional<AssignSdgIndicator>) repo.findById(id);
	}

	@Override
	public void deleteAssignSdgIndicator(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public void deleteAssign(String id_monper, String id_prov) {
		repo.deleteAssign(id_monper, id_prov);
	}

	@Override
	public int idRole(String id_goals, String id_target, String id_indicator, String id_monper, String id_prov) {
		int id_role = repo.idRole(id_goals, id_target, id_indicator, id_monper, id_prov);
		return id_role;
	}

	@Override
	public void deleteAssign(String id_prov) {
		repo.deleteAssign(id_prov);
	}
}
