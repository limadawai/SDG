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

	@Override
	public Optional<Role> findOne(Integer id) {
		return (Optional<Role>) role.findById(id);
	}

	@Override
	public List<Role> findByProvince(String id_prov) {
		return (List<Role>) role.findByProvince(id_prov);
	}

	@Override
	public void saveRole(Role rol) {
		role.save(rol);
	}

	@Override
	public void deleteRole(Integer id) {
		role.deleteById(id);
	}

}
