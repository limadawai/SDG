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
import com.jica.sdg.service.IMonPeriodService;
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
        Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
        model.addAttribute("monPer", monPeriodService.findAll(id_prov));
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
        		a.setId_goals(id_goals);
        		a.setId_indicator(id_indicator);
        		a.setId_monper(id_monper);
        		a.setId_prov(id_prov);
        		a.setId_role(Integer.parseInt(id_role));
        		a.setId_target(id_target);
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
    
    @PostMapping(path = "admin/manajemen/save-assignmentGov/{id_monper}/{id_prov}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveAssignGov(@RequestBody Map<String, Object> payload,@PathVariable("id_monper") String id_mon,@PathVariable("id_prov") String id_pro) {
    	JSONObject jsonObject = new JSONObject(payload);
        JSONObject catatan = jsonObject.getJSONObject("gov");
        JSONArray c = catatan.getJSONArray("gov");
        assignGovService.deleteAssign(id_mon, id_pro);
        for (int i = 0 ; i < c.length(); i++) {
        	JSONObject obj = c.getJSONObject(i);
        	String 	id_program = obj.getString("id_program");
        	String 	id_activity = obj.getString("id_activity");
        	String 	id_gov_indicator = obj.getString("id_gov_indicator");
        	String 	id_role = obj.getString("id_role");
        	String 	id_monper = obj.getString("id_monper");
        	String 	id_prov = obj.getString("id_prov");
        	
        	if(!id_role.equals("")) {
        		AssignGovIndicator a = new AssignGovIndicator();
        		a.setId_program(id_program);
        		a.setId_activity(id_activity);
        		a.setId_monper(id_monper);
        		a.setId_prov(id_prov);
        		a.setId_role(Integer.parseInt(id_role));
        		a.setId_gov_indicator(id_gov_indicator);
        		assignGovService.saveAssignGovIndicator(a);
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
    
    @PostMapping(path = "admin/manajemen/save-assignmentNsa/{id_monper}/{id_prov}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveAssignNsa(@RequestBody Map<String, Object> payload,@PathVariable("id_monper") String id_mon,@PathVariable("id_prov") String id_pro) {
    	JSONObject jsonObject = new JSONObject(payload);
        JSONObject catatan = jsonObject.getJSONObject("nsa");
        JSONArray c = catatan.getJSONArray("nsa");
        assignNsaService.deleteAssign(id_mon, id_pro);
        for (int i = 0 ; i < c.length(); i++) {
        	JSONObject obj = c.getJSONObject(i);
        	String 	id_program = obj.getString("id_program");
        	String 	id_activity = obj.getString("id_activity");
        	String 	id_nsa_indicator = obj.getString("id_nsa_indicator");
        	String 	id_role = obj.getString("id_role");
        	String 	id_monper = obj.getString("id_monper");
        	String 	id_prov = obj.getString("id_prov");
        	
        	if(!id_role.equals("")) {
        		AssignNsaIndicator a = new AssignNsaIndicator();
        		a.setId_program(id_program);
        		a.setId_activity(id_activity);
        		a.setId_monper(id_monper);
        		a.setId_prov(id_prov);
        		a.setId_role(Integer.parseInt(id_role));
        		a.setId_nsa_indicator(id_nsa_indicator);
        		assignNsaService.saveAssignNsaIndicator(a);
        	}
        }
	}

    @GetMapping("admin/manajemen/request")
    public String requestlist(Model model, HttpSession session) {
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/manajemen_request_list";
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
