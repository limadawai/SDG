package com.jica.sdg.service;

import com.jica.sdg.model.Nsadetail;
import com.jica.sdg.repository.NsadetailRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.jica.sdg.model.SdgGoals;
//import com.jica.sdg.repository.SdgGoalsRepository;

@Service
public class NsaDetailService implements INsaDetailService{
	
	@Autowired
	NsadetailRepository nsaDetailRepo;
	
	@Override
	public List<Nsadetail> findAll() {
		return (List<Nsadetail>) nsaDetailRepo.findAll();
	}
	
	@Override
	public List<Nsadetail> findId(String id) {
		return (List<Nsadetail>) nsaDetailRepo.findId(id);
	}
	
	@Override
	public List<Nsadetail> findIdNsa(String id) {
		return (List<Nsadetail>) nsaDetailRepo.findIdNsa(id);
	}
	
	@Override
	public void saveNsaDetail(Nsadetail nsa) {
		nsaDetailRepo.save(nsa);
	}
        
	@Override
	public void deleteIdNsa(Integer id) {
		nsaDetailRepo.deleteIdNsa(id);
	}

//	@Override
//	public Optional<Nsaprofile> findOne(String id_nsa) {
//		return (Optional<Nsaprofile>) nsaProfileRepo.findById(id_nsa);
//	}
//        
//        @Override
	public void deleteNsaDetail(Long id) {
		nsaDetailRepo.deleteById(id);
	}
        
}
