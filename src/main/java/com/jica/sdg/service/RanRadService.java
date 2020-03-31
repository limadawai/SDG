package com.jica.sdg.service;

import com.jica.sdg.model.RanRad;
import com.jica.sdg.repository.RanRadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RanRadService implements IRanRadService {

    @Autowired
    private RanRadRepository repository;

    @Override
    public List<RanRad> findAll() {
        List ranrad = (List<RanRad>) repository.findAll();
        return ranrad;
    }

    @Override
    public List<RanRad> findAllByIdProv(String id) {
        List<RanRad> list = repository.findByIdProv(id);
        return list;
    }

	@Override
	public Optional<RanRad> findOne(Integer id) {
		return repository.findById(id);
	}
}
