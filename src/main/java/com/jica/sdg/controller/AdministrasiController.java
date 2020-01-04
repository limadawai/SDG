package com.jica.sdg.controller;

import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.User;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class AdministrasiController {

    @Autowired
    IProvinsiService provinsiService;

    @Autowired
    IRoleService roleService;
    
    @Autowired
    IUserService userService;

    //*********************** Manajemen Role & User ***********************
    @GetMapping("admin/manajemen/role")
    public String rolemanajemen(Model model, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
    	if(id_prov.equals("000")) {
    		model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/manajemen_role";
    }

    @GetMapping("admin/manajemen/list-role/{id_prov}")
    public @ResponseBody Map<String, Object> roles(HttpSession session, @PathVariable("id_prov") String id_prov) {
    	List<Role> listRole;
    	if(id_prov.equals("000")) {
    		listRole = roleService.findAll();
    	}else {
    		listRole = roleService.findByProvince(id_prov);
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", listRole);
        return hasil;
    }
    
    @PostMapping(path = "admin/manajemen/save-role", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveRole(@RequestBody Role rol) {
    	roleService.saveRole(rol);
	}
    
    @GetMapping("admin/manajemen/get-role/{id}")
    public @ResponseBody Map<String, Object> getRole(@PathVariable("id") Integer id) {
        Optional<Role> list = roleService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/manajemen/delete-role/{id}")
	@ResponseBody
	public void deleteRole(@PathVariable("id") Integer id) {
    	roleService.deleteRole(id);
	}

    @GetMapping("admin/manajemen/user")
    public String usermanajemen(Model model, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
    	if(id_prov.equals("000")) {
    		model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/manajemen_user";
    }
    
    @GetMapping("admin/manajemen/list-user/{id_prov}")
    public @ResponseBody Map<String, Object> user(HttpSession session, @PathVariable("id_prov") String id_prov) {
    	List listUser;
    	if(id_prov.equals("000")) {
    		listUser = userService.findAllGrid();
    	}else {
    		listUser = userService.findByProvince(id_prov);
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", listUser);
        return hasil;
    }
    
    @PostMapping(path = "admin/manajemen/save-user", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveUser(@RequestBody User user) {
    	BCryptPasswordEncoder b = new BCryptPasswordEncoder();
    	user.setPassword(b.encode(user.getPassword()));
    	userService.saveUsere(user);
	}
    
    @GetMapping("admin/manajemen/get-user/{id}")
    public @ResponseBody Map<String, Object> getuser(@PathVariable("id") Integer id) {
        Optional<User> list = userService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/manajemen/delete-user/{id}")
	@ResponseBody
	public void deleteUser(@PathVariable("id") Integer id) {
    	userService.deleteUser(id);
	}

    @GetMapping("admin/manajemen/assignment")
    public String assignment(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/manajemen_assignment";
    }

    @GetMapping("admin/manajemen/request")
    public String requestlist(Model model, HttpSession session) {
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/manajemen_request_list";
    }

    @GetMapping("admin/role/provinsi")
    public @ResponseBody List<Provinsi> provinsi() {
        List<Provinsi> list = provinsiService.findAllProvinsi();
        return list;
    }

}
