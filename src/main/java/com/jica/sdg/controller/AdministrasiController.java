package com.jica.sdg.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

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

import com.jica.sdg.model.AssignSdgIndicator;
import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.Unit;
import com.jica.sdg.model.User;
import com.jica.sdg.model.UserRequestList;
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
import com.jica.sdg.service.IUserService;
import com.jica.sdg.service.MenuService;
import com.jica.sdg.service.SubmenuService;

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
    
    @Autowired
    private EntityManager em;

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

    @GetMapping("admin/manajemen/list-role/{id_prov}/{cat}/{prev}")
    public @ResponseBody Map<String, Object> roles(HttpSession session, @PathVariable("id_prov") String id_prov, @PathVariable("cat") String cat, @PathVariable("prev") String prev) {
    	List<Role> listRole;
    	if(id_prov.equals("all") && cat.equals("all")) {
    		listRole = roleService.findAll();
    	}else if(!id_prov.equals("all") && cat.equals("all")){
    		listRole = roleService.findByProvince(id_prov);
    	}else if(id_prov.equals("all") && !cat.equals("all")){
    		listRole = roleService.findByCat(cat,prev);
    	}else {
    		listRole = roleService.findByProvince(id_prov,cat,prev);
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", listRole);
        return hasil;
    }
    
    @GetMapping("admin/manajemen/list-unit")
    public @ResponseBody Map<String, Object> units(HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> listRole = roleService.findOne(id_role);
    	String privilege = listRole.get().getPrivilege();
    	String id_prov = listRole.get().getId_prov();
    	String sql;
    	if(privilege.equals("SUPER")) {
    		sql = "select * from ref_unit";
    	}else {
    		sql = "select a.* from ref_unit a left join ref_role b on a.id_role = b.id_role where b.id_prov = '"+id_prov+"' or a.id_role = 1";
    	}
        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Unit> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(
                        new Unit((Integer)row[0], (String) row[1], (Integer)row[2], (Integer)row[3])
            );
        }
        hasil.put("content",result);
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
    		listRole = roleService.findByProvinceUserForm(id_prov);
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", listRole);
        return hasil;
    }
    
    @PostMapping("admin/manajemen/cek-role")
    public @ResponseBody Map<String, Object> cekRoles(HttpSession session, @RequestBody Map<String, Object> payload) {
    	JSONObject jsonObunit = new JSONObject(payload);
        String id_prov = jsonObunit.get("id_prov").toString();  
        String nm_role = jsonObunit.get("nm_role").toString();
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
    
        @PostMapping(path = "admin/manajemen/save-unit", consumes = "application/json", produces = "application/json")
	@ResponseBody
        @Transactional
	public void saveUnit(@RequestBody Map<String, Object> payload,HttpSession session) {
        Integer id_role = (Integer) session.getAttribute("id_role");
        JSONObject jsonObunit = new JSONObject(payload);
        String nm_unit           = jsonObunit.get("nm_unit").toString();  
        String id_unit           = jsonObunit.get("id_unit").toString();
        String calculation       = jsonObunit.get("calculation").toString();
            if(id_unit.equals("")){
                em.createNativeQuery("INSERT INTO ref_unit (nm_unit,id_role,calculation) values ('"+nm_unit+"','"+id_role+"','"+calculation+"')").executeUpdate();
            }else{
                em.createNativeQuery("UPDATE ref_unit set nm_unit = '"+nm_unit+"', calculation = '"+calculation+"' where id_unit ='"+id_unit+"'").executeUpdate();
            }
        
	}
        
    @GetMapping("admin/manajemen/get-role/{id}")
    public @ResponseBody Map<String, Object> getRole(@PathVariable("id") Integer id) {
        Optional<Role> list = roleService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/manajemen/get-unit/{id}")
    public @ResponseBody Map<String, Object> getUnit(@PathVariable("id") Integer id) {
        String sql = "select * from ref_unit where id_unit = '"+id+"'";
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Unit> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(
                        new Unit((Integer)row[0], (String) row[1], (Integer)row[2], (Integer)row[3])
            );
        }
        hasil.put("content",result);
        return hasil;
    }
    
    @DeleteMapping("admin/manajemen/delete-role/{id}")
    @ResponseBody
    public void deleteRole(@PathVariable("id") Integer id) {
        roleService.deleteRole(id);
    }
    
    @DeleteMapping("admin/manajemen/delete-unit/{id}")
    @ResponseBody    
    @Transactional
    public void deleteUnit(@PathVariable("id") Integer id) {
        em.createNativeQuery("delete from ref_unit where id_unit ='"+id+"'").executeUpdate();
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
        	String 	id_activity = obj.getString("id_activity");
        	String 	id_role = obj.getString("id_role");
        	
        	if(!id_role.equals("")) {
        		if(!id_program.equals("")) {
        			nsaProgService.updateRole(Integer.parseInt(id_role), Integer.parseInt(id_program));
        		}
        		if(!id_activity.equals("")) {
        			nsaActivityService.updateRole(Integer.parseInt(id_role), Integer.parseInt(id_activity));
        		}
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
        List list = provinsiService.findAllProvinsi();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", list);
        return hasil;
    }
    
    @GetMapping("admin/manajemen/list-request/{new}")
    public @ResponseBody Map<String, Object> req(@PathVariable("new") String ok, HttpSession session) {
    	List list;
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> listRole = roleService.findOne(id_role);
    	String privilege = listRole.get().getPrivilege();
    	String id_prov = listRole.get().getId_prov();
    	if(ok.equals("1")) {
    		if(privilege.equals("SUPER")) {
    			//list = userReqService.findAllNew();
    			String sql = "select b.acronym, a.date, a.level, a.type, a.institution, a.name, a.contact, a.status, a.id, a.detail from user_request_list a left join ref_province b on a.id_prov = b.id_prov where a.status = 'new'";
    	        Query query = em.createNativeQuery(sql);
    	        list   = query.getResultList();
    		}else {
    			//list = userReqService.findAllNewProv(id_prov);
    			String sql = "select b.acronym, a.date, a.level, a.type, a.institution, a.name, a.contact, a.status, a.id, a.detail from user_request_list a left join ref_province b on a.id_prov = b.id_prov where a.status = 'new' and a.id_prov = '"+id_prov+"' ";
    	        Query query = em.createNativeQuery(sql);
    	        list   = query.getResultList();
    		}
    	}else {
    		if(privilege.equals("SUPER")) {
    			//list = userReqService.findAll();
    			String sql = "select b.acronym, a.date, a.level, a.type, a.institution, a.name, a.contact, a.status, a.id, a.detail from user_request_list a left join ref_province b on a.id_prov = b.id_prov ";
    	        Query query = em.createNativeQuery(sql);
    	        list   = query.getResultList();
    		}else {
    			//list = userReqService.findAllProv(id_prov);
    			String sql = "select b.acronym, a.date, a.level, a.type, a.institution, a.name, a.contact, a.status, a.id, a.detail from user_request_list a left join ref_province b on a.id_prov = b.id_prov where a.id_prov = '"+id_prov+"' ";
    	        Query query = em.createNativeQuery(sql);
    	        list   = query.getResultList();
    		}
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", list);
        return hasil;
    }
    
    @PostMapping(path = "admin/ubah-status-req", consumes = "application/json", produces = "application/json")
	@ResponseBody
    @Transactional
	public void change(@RequestBody Map<String, Object> payload) {
        JSONObject jsonObapproval = new JSONObject(payload);
        String status = jsonObapproval.get("status").toString();  
        String id     = jsonObapproval.get("id").toString();
        em.createNativeQuery("UPDATE user_request_list set status = '"+status+"' where id ='"+id+"'").executeUpdate();
    }
    
    @PostMapping(path = "save-user-req", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveUserReqRole(@RequestBody UserRequestList rol) {
    	rol.setDate(new Date());
    	userReqService.saveUserRequestList(rol);
	}

    @GetMapping("admin/role/provinsi")
    public @ResponseBody List<Provinsi> provinsi() {
        List<Provinsi> list = provinsiService.findAllProvinsi();
        return list;
    }

    @GetMapping("admin/manajemen/menu")
    public @ResponseBody void menusubmenu() {
        List<Menu> listMenu = menuService.findAllMenu();
    }
    
    
    @GetMapping("admin/management/unit")
    public String unitmanajemen(Model model, HttpSession session) {
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
		model.addAttribute("id_role", id_role);
        return "admin/role_manajemen/manajemen_unit";
    }

}
