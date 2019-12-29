package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.MonPeriod;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.repository.MonPeriodRepository;
import com.jica.sdg.repository.RoleRepository;
import com.jica.sdg.repository.SdgDisaggreDetailRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class RoleService implements IRoleService{
	
	@Autowired
	RoleRepository role;

	@Override
	public List<Role> findAll() {
		return (List<Role>) role.findAll();
	}	
	
}
