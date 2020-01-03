package com.jica.sdg.service;

import com.jica.sdg.model.Insprofile;
import com.jica.sdg.model.Role;
import com.jica.sdg.repository.InsProfileRepository;
import com.jica.sdg.repository.RoleRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.jica.sdg.model.SdgGoals;
//import com.jica.sdg.repository.SdgGoalsRepository;

@Service
public class InsProfileService implements IInsProfileService{
	
	@Autowired
	InsProfileRepository insProfileRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Override
	public List<Insprofile> findAll() {
		return (List<Insprofile>) insProfileRepo.findAll();
	}
	
	@Override
	public List<Insprofile> findId(String id) {
		return (List<Insprofile>) insProfileRepo.findId(id);
	}
	
	@Override
	public void saveInsProfil(Insprofile ins) {
		insProfileRepo.save(ins);
	}

	@Override
	public Optional<Insprofile> findOne(Integer id) {
		return (Optional<Insprofile>) insProfileRepo.findById(id);
	}
        
        @Override
	public List<Role> findRoleInstitusi() {
            return (List<Role>) roleRepo.findRoleInstitusi();
	}
        
        @Override
	public void deleteInsProfil(Integer id) {
		insProfileRepo.deleteById(id);
	}
        
}
