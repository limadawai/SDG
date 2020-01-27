package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.NsaProgram;
import com.jica.sdg.repository.NsaProgramRepository;

@Service
public class NsaProgramService implements INsaProgramService{
	
	@Autowired
	NsaProgramRepository nsaProgRepo;
	
	@Override
	public List<NsaProgram> findAll() {
		return (List<NsaProgram>) nsaProgRepo.findAll();
	}
	
	@Override
	public void saveNsaProgram(NsaProgram gov) {
		nsaProgRepo.save(gov);
	}

	@Override
	public Optional<NsaProgram> findOne(Integer id) {
		return (Optional<NsaProgram>) nsaProgRepo.findById(id);
	}

	@Override
	public void deleteNsaProgram(Integer id) {
		nsaProgRepo.deleteById(id);
	}

	@Override
	public List<NsaProgram> findAllBy(String id_role, String id_monper) {
		return (List<NsaProgram>) nsaProgRepo.findAll(id_role, id_monper);
	}

	@Override
	public List<NsaProgram> findAllByMonperProv(Integer id_monper, String id_prov) {
		return (List<NsaProgram>) nsaProgRepo.findAllByMonperProv(id_monper, id_prov);
	}

	@Override
	public void updateRole(Integer id_role, Integer id) {
		nsaProgRepo.UpdateRole(id_role, id);
	}
}
