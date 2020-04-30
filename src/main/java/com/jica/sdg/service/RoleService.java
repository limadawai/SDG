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
	public List<Role> findAllGrid() {
		return (List<Role>) role.findAllGrid();
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

	@Override
	public Integer cekRole(String id_prov, String nm_role) {
		return role.cekNmRole(id_prov, nm_role);
	}

	@Override
	public List<Role> findAll() {
		return  (List<Role>) role.findAll();
	}

	@Override
	public Integer cekJmlRole(String id_prov, String cat_role) {
		return role.cekRole(id_prov);
	}

	@Override
	public List<Role> findRoleGov() {
		return (List<Role>) role.findRoleGov();
	}

	@Override
	public List<Role> findRoleNonGov(String id_prov) {
		return (List<Role>) role.findRoleNonGov(id_prov);
	}

	public List<Role> catRole(int id_role) {
		return (List<Role>) role.findCatRole(id_role);
	}

	public List<Role> findRoleGov(String id_prov) {
		return (List<Role>) role.findRoleGov(id_prov);
	}

	@Override
	public List<Role> findByProvince(String id_prov, String cat, String prev) {
		return (List<Role>) role.findByProvince(id_prov,cat,prev);
	}

	@Override
	public List<Role> findByCat(String cat,String prev) {
		return (List<Role>) role.findByCat(cat,prev);
	}

	@Override
	public List<Role> findAllNonSuper() {
		return (List<Role>) role.findAllNonSuper();
	}

	@Override
	public List<Role> findByProvinceUserForm(String id_prov) {
		return role.findByProvinceUserForm(id_prov);
	}

}
