package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.GovActivity;
import com.jica.sdg.model.NsaActivity;
import com.jica.sdg.repository.GovActivityRepository;
import com.jica.sdg.repository.NsaActivityRepository;

@Service
public class NsaActivityService implements INsaActivityService{
	
	@Autowired
	NsaActivityRepository nsaActivityRepo;
	
	@Override
	public List<NsaActivity> findAll(Integer id_program, Integer id_role) {
		return (List<NsaActivity>) nsaActivityRepo.findAllGovActivity(id_program,id_role);
	}
	
	@Override
	public List<NsaActivity> findAll(Integer id_program) {
		return (List<NsaActivity>) nsaActivityRepo.findAllGovActivity(id_program);
	}
	
	@Override
	public void saveNsaActivity(NsaActivity gov) {
		nsaActivityRepo.save(gov);
	}

	@Override
	public Optional<NsaActivity> findOne(Integer id) {
		return (Optional<NsaActivity>) nsaActivityRepo.findById(id);
	}

	@Override
	public void deleteNsaActivity(Integer id) {
		nsaActivityRepo.deleteById(id);
	}

	@Override
	public void updateRole(Integer id_role, Integer id) {
		nsaActivityRepo.UpdateRole(id_role, id);
	}

	@Override
	public Integer countNsaActivity(Integer id) {
		return nsaActivityRepo.countNsaActivity(id);
	}
}
