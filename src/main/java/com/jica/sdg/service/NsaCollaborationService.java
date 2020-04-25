package com.jica.sdg.service;

import com.jica.sdg.model.NsaCollaboration;
import com.jica.sdg.repository.NsaCollaborationRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.jica.sdg.model.SdgGoals;
//import com.jica.sdg.repository.SdgGoalsRepository;

@Service
public class NsaCollaborationService implements INsaCollaborationService{
	
	@Autowired
	NsaCollaborationRepository nsaCollaborationRepo;
	
	@Override
	public List<NsaCollaboration> findAll() {
            return (List<NsaCollaboration>) nsaCollaborationRepo.findAll();
	}
	
	@Override
	public List<NsaCollaboration> findId(String id) {
            return (List<NsaCollaboration>) nsaCollaborationRepo.findId(id);
	}
	
	@Override
	public void saveNsaCollaboration(NsaCollaboration col) {
            nsaCollaborationRepo.save(col);
	}
        
	@Override
	public void updateIdPhilanthropy(int id_philanthropy, int id) {
            nsaCollaborationRepo.updateIdPhilanthropy(id_philanthropy, id);
	}

	@Override
	public Optional<NsaCollaboration> findOne(Long id) {
            return (Optional<NsaCollaboration>) nsaCollaborationRepo.findById(id);
        }
        
        @Override
	public void deleteNsaCollaboration(Long id) {
            nsaCollaborationRepo.deleteById(id);
	}

		@Override
		public List<NsaCollaboration> findByProgram(String id) {
			return (List<NsaCollaboration>) nsaCollaborationRepo.findByNsaProg(id);
		}

        
}
