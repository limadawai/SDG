package com.jica.sdg.service;

import com.jica.sdg.model.EntrySdgDetail;
import com.jica.sdg.repository.EntrySdgDetailRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntrySdgDetailService implements IEntrySdgDetailService{
	
	@Autowired
	EntrySdgDetailRepository entrySdgRepo;
	
	@Override
	public void saveEntrySdgDetail(EntrySdgDetail esdg) {
        Date date = new Date();
        esdg.setDate_created(date);
        entrySdgRepo.save(esdg);
	}

	@Override
	public void updateNew1(Integer id, Integer nilai) {
		entrySdgRepo.updateNew1(id, nilai);
	}

	@Override
	public void updateNew2(Integer id, Integer nilai) {
		entrySdgRepo.updateNew2(id, nilai);
	}

	@Override
	public void updateNew3(Integer id, Integer nilai) {
		entrySdgRepo.updateNew3(id, nilai);
	}

	@Override
	public void updateNew4(Integer id, Integer nilai) {
		entrySdgRepo.updateNew4(id, nilai);
	}
    
}
