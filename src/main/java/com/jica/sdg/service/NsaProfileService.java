package com.jica.sdg.service;

import com.jica.sdg.model.Nsaprofile;
import com.jica.sdg.model.Role;
import com.jica.sdg.repository.NsaprofileRepository;
import com.jica.sdg.repository.RoleRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.jica.sdg.model.SdgGoals;
//import com.jica.sdg.repository.SdgGoalsRepository;

@Service
public class NsaProfileService implements INsaProfileService{
	
	@Autowired
	NsaprofileRepository nsaProfileRepo;
	
	@Autowired
	RoleRepository nsaRoleRepo;
	
	@Override
	public List<Nsaprofile> findAll() {
		return (List<Nsaprofile>) nsaProfileRepo.findAll();
	}
	
	@Override
	public List<Nsaprofile> findId(Integer id) {
		return (List<Nsaprofile>) nsaProfileRepo.findId(id);
	}
	
	@Override
	public void saveNsaProfil(Nsaprofile nsa) {
		nsaProfileRepo.save(nsa);
	}

	@Override
	public List<Role> findRoleNsa() {
            return (List<Role>) nsaRoleRepo.findRoleNsa();
	}
        
	@Override
	public List<Role> findRoleAll() {
            return (List<Role>) nsaRoleRepo.findRoleAll();
	}
	@Override
	public List<Role> findRoleGov() {
            return (List<Role>) nsaRoleRepo.findRoleGov();
	}
//        
        @Override
	public void deleteNsaProfil(Integer id_nsa) {
		nsaProfileRepo.deleteById(id_nsa);
	}

		@Override
		public List<Role> findIdProv(String id) {
			// TODO Auto-generated method stub
			return (List<Role>) nsaRoleRepo.findNsaByProvince(id);
		}

		@Override
		public List<Role> findNsaAllProvince() {
			return (List<Role>) nsaRoleRepo.findNsaAllProvince();
		}
        
}
