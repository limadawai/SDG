package com.jica.sdg.service;

import com.jica.sdg.model.PhilanthropyCollaboration;
import com.jica.sdg.repository.PhilanthropyRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.jica.sdg.model.SdgGoals;
//import com.jica.sdg.repository.SdgGoalsRepository;

@Service
public class PhilanthropyService implements IPhilanthropyService{
	
	@Autowired
	PhilanthropyRepository philanthropyRepo;
	
//	@Override
//	public List<NsaCollaboration> findAll() {
//            return (List<NsaCollaboration>) nsaCollaborationRepo.findAll();
//	}
	
//	@Override
//	public List<NsaCollaboration> findId(String id) {
//            return (List<NsaCollaboration>) nsaCollaborationRepo.findId(id);
//	}
	
	@Override
	public void savePhilanthropyCollaboration(PhilanthropyCollaboration phy) {
            philanthropyRepo.save(phy);
	}

//	@Override
//	public Optional<NsaCollaboration> findOne(Long id) {
//            return (Optional<NsaCollaboration>) nsaCollaborationRepo.findById(id);
//        }
//        
        @Override
	public void deletePhilantropi(Integer id) {
            philanthropyRepo.deleteById(id);
	}
        
}
