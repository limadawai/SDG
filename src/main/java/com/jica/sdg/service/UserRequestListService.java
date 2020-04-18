package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.model.UserRequestList;
import com.jica.sdg.repository.SdgIndicatorRepository;
import com.jica.sdg.repository.UserRequestListRepository;

@Service
public class UserRequestListService implements IUserRequestListService{
	
	@Autowired
	UserRequestListRepository repo;

	@Override
	public List<UserRequestList> findAll() {
		return (List<UserRequestList>) repo.findAll();
	}

	@Override
	public void saveUserRequestList(UserRequestList u) {
		repo.save(u);
	}

	@Override
	public Optional<UserRequestList> findOne(String id) {
		return (Optional<UserRequestList>) repo.findById(id);
	}

	@Override
	public void deleteUserRequestList(String id) {
		repo.deleteById(id);
	}

	@Override
	public List<UserRequestList> findAllNew() {
		return (List<UserRequestList>) repo.findAllNew();
	}

	@Override
	public List<UserRequestList> findAllNewProv(String id_prov) {
		return repo.findAllNewProv(id_prov);
	}

	@Override
	public List<UserRequestList> findAllProv(String id_prov) {
		return repo.findAllProv(id_prov);
	}
}
