package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.AssignNsaIndicator;
import com.jica.sdg.repository.AssignNsaIndicatorRepository;

@Service
public class AssignNsaIndicatorService implements IAssignNsaIndicatorService{
	
	@Autowired
	AssignNsaIndicatorRepository repo;

	@Override
	public List<AssignNsaIndicator> findAll(String id) {
		return (List<AssignNsaIndicator>) repo.findAll();
	}

	@Override
	public void saveAssignNsaIndicator(AssignNsaIndicator assign) {
		repo.save(assign);
	}

	@Override
	public Optional<AssignNsaIndicator> findOne(Integer id) {
		return (Optional<AssignNsaIndicator>) repo.findById(id);
	}

	@Override
	public void deleteAssignNsaIndicator(Integer id) {
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
