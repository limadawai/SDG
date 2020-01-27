package com.jica.sdg.controller;

import com.jica.sdg.model.AssignGovIndicator;
import com.jica.sdg.model.AssignNsaIndicator;
import com.jica.sdg.model.AssignSdgIndicator;
import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.User;
import com.jica.sdg.service.IAssignGovIndicatorService;
import com.jica.sdg.service.IAssignNsaIndicatorService;
import com.jica.sdg.service.IAssignSdgIndicatorService;
import com.jica.sdg.service.IGovActivityService;
import com.jica.sdg.service.IMonPeriodService;
import com.jica.sdg.service.INsaActivityService;
import com.jica.sdg.service.INsaProgramService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.IUserRequestListService;
import com.jica.sdg.service.MenuService;
import com.jica.sdg.service.SubmenuService;
import com.jica.sdg.service.IUserService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    
    @Autowired
	IMonPeriodService monPeriodService;
    
    @Autowired
    IAssignSdgIndicatorService assignSdgService;
    
    @Autowired
    IAssignGovIndicatorService assignGovService;
    
    @Autowired
    IAssignNsaIndicatorService assignNsaService;

    @Autowired
    MenuService menuService;

    @Autowired
    SubmenuService submenuService;
    
    @Autowired
    IUserRequestListService userReqService;
    
    @Autowired
    IGovActivityService govActivityService;
    
    @Autowired
    INsaProgramService nsaProgService;
    
    @Autowired
    INsaActivityService nsaActivityService;

    //*********************** Manajemen Role & User ***********************
    @GetMapping("admin/management/role")
    public String rolemanajemen(Model model, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
    	String privilege = list.get().getPrivilege();
    	if(privilege.equals("SUPER")) {
    		model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("lang", session.getAttribute("bahasa"));
		model.addAttribute("name", session.getAttribute("name"));
		model.addAttribute("id_prov", id_prov);
		model.addAttribute("privilege", privilege);
        return "admin/role_manajemen/manajemen_role";
    }

    @GetMapping("admin/manajemen/list-role/{id_prov}")
    public @ResponseBody Map<String, Object> roles(HttpSession session, @PathVariable("id_prov") String id_prov) {
    	List<Role> listRole;
    	if(id_prov.equals("all")) {
    		listRole = roleService.findAll();
    	}else {
    		listRole = roleService.findByProvince(id_prov);
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", listRole);
        return hasil;
    }
    
    @GetMapping("admin/manajemen/list-role-nsa/{id_prov}")
    public @ResponseBody Map<String, Object> rolesNsa(HttpSession session, @PathVariable("id_prov") String id_prov) {
    	List<Role> listRole;
    	if(id_prov.equals("all")) {
    		listRole = roleService.findAll();
    	}else {
    		listRole = roleService.findRoleNonGov(id_prov);
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", listRole);
        return hasil;
    }
    
    @GetMapping("admin/manajemen/list-role-gov/{id_prov}")
    public @ResponseBody Map<String, Object> rolesGov(HttpSession session, @PathVariable("id_prov") String id_prov) {
    	List<Role> listRole;
    	if(id_prov.equals("all")) {
    		listRole = roleService.findAll();
    	}else {
    		listRole = roleService.findRoleGov(id_prov);
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", listRole);
        return hasil;
    }
    
    @GetMapping("admin/manajemen/list-role-user/{id_prov}")
    public @ResponseBody Map<String, Object> rolesUser(HttpSession session, @PathVariable("id_prov") String id_prov) {
    	List<Role> listRole;
    	if(id_prov.equals("all")) {
    		listRole = roleService.findAll();
    	}else {
    		listRole = roleService.findByProvince(id_prov);
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", listRole);
        return hasil;
    }
    
    @GetMapping("admin/manajemen/cek-role/{id_prov}/{nm_role}")
    public @ResponseBody Map<String, Object> cekRoles(HttpSession session, @PathVariable("id_prov") String id_prov, @PathVariable("nm_role") String nm_role) {
    	Integer cek = roleService.cekRole(id_prov, nm_role);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("cek", cek);
        return hasil;
    }
    
    @GetMapping("admin/manajemen/cek-jml-role/{id_prov}/{cat_role}")
    public @ResponseBody Map<String, Object> cekJmlRoles(HttpSession session, @PathVariable("id_prov") String id_prov, @PathVariable("cat_role") String cat_role) {
    	Integer cek = roleService.cekJmlRole(id_prov, cat_role);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("cek", cek);
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

    @GetMapping("admin/management/user")
    public String usermanajemen(Model model, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
    	String privilege = list.get().getPrivilege();
    	model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("listRole", roleService.findAll());
        model.addAttribute("lang", session.getAttribute("bahasa"));
		model.addAttribute("name", session.getAttribute("name"));
		model.addAttribute("privilege", privilege);
		model.addAttribute("id_prov", id_prov);
        return "admin/role_manajemen/manajemen_user";
    }
    
    @GetMapping("admin/manajemen/cek-username/{username}")
    public @ResponseBody Map<String, Object> cekRoles(HttpSession session, @PathVariable("username") String username) {
    	Integer cek = userService.cekUsername(username);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("cek", cek);
        return hasil;
    }
    
    @GetMapping("admin/manajemen/list-user/{id_prov}")
    public @ResponseBody Map<String, Object> user(HttpSession session, @PathVariable("id_prov") String id_prov) {
    	List listUser;
    	if(id_prov.equals("all")) {
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
    	if(user.getId_user() != null) {
    		Optional<User> list = userService.findOne(user.getId_user());
        	if(user.getPassword().equals(list.get().getPassword())) {
        		user.setPassword(list.get().getPassword());
        	}else {
        		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            	user.setPassword(b.encode(user.getPassword()));
        	}
    	}else {
    		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        	user.setPassword(b.encode(user.getPassword()));
    	}
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

    @GetMapping("admin/management/assignment")
    public String assignment(Model model, HttpSession session) {
        model.addAttribute("lang", session.getAttribute("bahasa"));
		model.addAttribute("name", session.getAttribute("name"));
        Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
    	String privilege = list.get().getPrivilege();
        model.addAttribute("monPer", monPeriodService.findAll(id_prov));
        if(privilege.equals("SUPER")) {
    		model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        return "admin/role_manajemen/manajemen_assignment";
    }
    
    @GetMapping("admin/manajemen/get-id_roleSdg/{id_goals}/{id_target}/{id_indicator}/{id_monper}/{id_prov}")
    public @ResponseBody Map<String, Object> getIdRole(@PathVariable("id_goals") String id_goals,
    		@PathVariable("id_target") String id_target,
    		@PathVariable("id_indicator") String id_indicator,
    		@PathVariable("id_monper") String id_monper,
    		@PathVariable("id_prov") String id_prov) {
    	int idRole = assignSdgService.idRole(id_goals, id_target, id_indicator, id_monper, id_prov);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", idRole);
        return hasil;
    }
    
    @PostMapping(path = "admin/manajemen/save-assignmentSdg/{id_monper}/{id_prov}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveAssign(@RequestBody Map<String, Object> payload,@PathVariable("id_monper") String id_mon,@PathVariable("id_prov") String id_pro) {
    	JSONObject jsonObject = new JSONObject(payload);
        JSONObject catatan = jsonObject.getJSONObject("sdg");
        JSONArray c = catatan.getJSONArray("sdg");
        assignSdgService.deleteAssign(id_mon, id_pro);
        for (int i = 0 ; i < c.length(); i++) {
        	JSONObject obj = c.getJSONObject(i);
        	String 	id_goals = obj.getString("id_goals");
        	String 	id_target = obj.getString("id_target");
        	String 	id_indicator = obj.getString("id_indicator");
        	String 	id_role = obj.getString("id_role");
        	String 	id_monper = obj.getString("id_monper");
        	String 	id_prov = obj.getString("id_prov");
        	
        	if(!id_role.equals("")) {
        		AssignSdgIndicator a = new AssignSdgIndicator();
        		a.setId_goals(Integer.parseInt(id_goals));
        		a.setId_indicator(Integer.parseInt(id_indicator));
        		a.setId_monper(Integer.parseInt(id_monper));
        		a.setId_prov(id_prov);
        		a.setId_role(Integer.parseInt(id_role));
        		a.setId_target(Integer.parseInt(id_target));
        		assignSdgService.saveAssignSdgIndicator(a);
        	}
        }
	}
    
    @GetMapping("admin/manajemen/get-id_roleGov/{id_program}/{id_activity}/{id_gov_indicator}/{id_monper}/{id_prov}")
    public @ResponseBody Map<String, Object> getIdRoleGov(@PathVariable("id_program") String id_program,
    		@PathVariable("id_activity") String id_activity,
    		@PathVariable("id_gov_indicator") String id_gov_indicator,
    		@PathVariable("id_monper") String id_monper,
    		@PathVariable("id_prov") String id_prov) {
    	int idRole = assignGovService.idRole(id_program, id_activity, id_gov_indicator, id_monper, id_prov);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", idRole);
        return hasil;
    }
    
    @PostMapping(path = "admin/manajemen/save-assignmentGov", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveAssignGov(@RequestBody Map<String, Object> payload) {
    	JSONObject jsonObject = new JSONObject(payload);
        JSONObject catatan = jsonObject.getJSONObject("gov");
        JSONArray c = catatan.getJSONArray("gov");
        for (int i = 0 ; i < c.length(); i++) {
        	JSONObject obj = c.getJSONObject(i);
        	String 	id = obj.getString("id");
        	String 	id_role = obj.getString("id_role");
        	
        	if(!id_role.equals("")) {
        		govActivityService.UpdateRole(Integer.parseInt(id_role), Integer.parseInt(id));
        	}
        }
	}
    
    @GetMapping("admin/manajemen/get-id_roleNsa/{id_program}/{id_activity}/{id_gov_indicator}/{id_monper}/{id_prov}")
    public @ResponseBody Map<String, Object> getIdRoleNsa(@PathVariable("id_program") String id_program,
    		@PathVariable("id_activity") String id_activity,
    		@PathVariable("id_gov_indicator") String id_gov_indicator,
    		@PathVariable("id_monper") String id_monper,
    		@PathVariable("id_prov") String id_prov) {
    	int idRole = assignNsaService.idRole(id_program, id_activity, id_gov_indicator, id_monper, id_prov);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", idRole);
        return hasil;
    }
    
    @PostMapping(path = "admin/manajemen/save-assignmentNsa", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveAssignNsa(@RequestBody Map<String, Object> payload) {
    	JSONObject jsonObject = new JSONObject(payload);
        JSONObject catatan = jsonObject.getJSONObject("nsa");
        JSONArray c = catatan.getJSONArray("nsa");
        for (int i = 0 ; i < c.length(); i++) {
        	JSONObject obj = c.getJSONObject(i);
        	String 	id_program = obj.getString("id");
        	String 	id_role = obj.getString("id_role");
        	
        	if(!id_role.equals("")) {
        		nsaProgService.updateRole(Integer.parseInt(id_role), Integer.parseInt(id_program));
        		nsaActivityService.updateRole(Integer.parseInt(id_role), Integer.parseInt(id_program));
        	}
        }
	}

    @GetMapping("admin/management/request")
    public String requestlist(Model model, HttpSession session) {
        model.addAttribute("lang", session.getAttribute("bahasa"));
		model.addAttribute("name", session.getAttribute("name"));
        return "admin/role_manajemen/manajemen_request_list";
    }

    @GetMapping("level")
    public @ResponseBody Map<String, Object> requestLevel() {
        System.out.println("lala");
        List list = provinsiService.findAllProvinsi();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", list);
        return hasil;
    }
    
    @GetMapping("admin/manajemen/list-request/{new}")
    public @ResponseBody Map<String, Object> req(@PathVariable("new") String ok) {
    	List list;
    	if(ok.equals("1")) {
    		list = userReqService.findAllNew();
    	}else {
    		list = userReqService.findAll();
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", list);
        return hasil;
    }

    @GetMapping("admin/role/provinsi")
    public @ResponseBody List<Provinsi> provinsi() {
        List<Provinsi> list = provinsiService.findAllProvinsi();
        return list;
    }

    @GetMapping("admin/manajemen/menu")
    public @ResponseBody void menusubmenu() {
        List<Menu> listMenu = menuService.findAllMenu();
        System.out.println(listMenu.size());
    }

}
