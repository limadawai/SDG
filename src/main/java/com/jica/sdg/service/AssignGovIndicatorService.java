package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.AssignGovIndicator;
import com.jica.sdg.repository.AssignGovIndicatorRepository;

@Service
public class AssignGovIndicatorService implements IAssignGovIndicatorService{
	
	@Autowired
	AssignGovIndicatorRepository repo;

	@Override
	public List<AssignGovIndicator> findAll(String id) {
		return (List<AssignGovIndicator>) repo.findAll();
	}

	@Override
	public void saveAssignGovIndicator(AssignGovIndicator assign) {
		repo.save(assign);
	}

	@Override
	public Optional<AssignGovIndicator> findOne(Integer id) {
		return (Optional<AssignGovIndicator>) repo.findById(id);
	}

	@Override
	public void deleteAssignGovIndicator(Integer id) {
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
}
