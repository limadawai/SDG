package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.Role;
import com.jica.sdg.repository.RoleRepository;

@Service
public class RoleService implements IRoleService{
	
	@Autowired
	RoleRepository role;

	@Override
	public List<Role> findAll() {
		return (List<Role>) role.findAll();
	}

}
