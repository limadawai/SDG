package com.jica.sdg.controller;

import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.Submenu;
import com.jica.sdg.model.TahunMap;
import com.jica.sdg.model.Unit;
import com.jica.sdg.service.IMenuService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.ISubmenuService;
import com.jica.sdg.service.ProvinsiService;
import com.jica.sdg.model.User;
import com.jica.sdg.service.*;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.json.JSONObject;

@Controller
public class AdminController {
	
	@Autowired
	ProvinsiService prov;
	
	@Autowired
	MonPeriodService monPeriodService;
	
	@Autowired
	RoleService roleService;

	@Autowired
        UserService userService;
        
        @Autowired
	private EntityManager em;

    //*********************** Menu Dari DB ***********************
    @Autowired
    IMenuService menuService;
    @CrossOrigin(origins = "http://sdgsemonevdev.com")
    @GetMapping("admin/menu")
    public @ResponseBody List<Menu> menuList(HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id[] = list.get().getMenu().split(",");
    	Integer size = id.length;
    	Integer [] arr = new Integer [size];
        for(int i=0; i<size; i++) {
           arr[i] = Integer.parseInt(id[i]);
        }
        Iterable<Integer> ids = Arrays.asList(arr);
        List<Menu> list1 = menuService.findAllByList(ids);
        return list1;
    }

    @Autowired
    ISubmenuService submenuService;
    @GetMapping("admin/submenu")
    public @ResponseBody List<Submenu> submenuList(@RequestParam int id, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list1 = roleService.findOne(id_role);
    	String ids[] = list1.get().getSubmenu().split(",");
    	List<String> id_submenu = Arrays.asList(ids);
        //List<Submenu> list = submenuService.findSubmenu(id);
    	List<Submenu> list = submenuService.findSubmenuByRole(id, id_submenu);
        return list;
    }

    //*********************** Dashboard ***********************
    
    @RequestMapping("default")
    public String defaultAfterLogin(HttpServletRequest request) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	System.out.print(auth.getName());
        if (auth.getName().equals("guest")) {
            return "redirect:/request-user";
        }
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("request-user")
    public String ReqUser(Model model, HttpSession session) {
    	model.addAttribute("listprov", prov.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
		model.addAttribute("name", session.getAttribute("name"));
		model.addAttribute("privilege", "guest");
        return "req-user";
    }

    @GetMapping("admin/dashboard")
    public String dashboard(Model model, Authentication auth, HttpServletRequest request, HttpSession session) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        String uname = auth.getName();
        List<User> userData = userService.findOne(uname);
        request.getSession().setAttribute("id_user", userData.get(0).getId_user());
        request.getSession().setAttribute("id_role", userData.get(0).getId_role());
        request.getSession().setAttribute("username", userData.get(0).getUserName());
        request.getSession().setAttribute("name", userData.get(0).getName());

        String bhs = (String) session.getAttribute("bahasa");
        if (bhs == null) {bhs = "0";}
        model.addAttribute("lang", bhs);
        model.addAttribute("name", session.getAttribute("name"));
        
         Query query = em.createNativeQuery("SELECT a.id_sdg_indicator,b.value AS target  \n" +
                                            "    , (a.achievement1+a.achievement2+a.achievement3+a.achievement4) AS realisasi \n" +
                                            "    ,c.id_prov \n" +
                                            "    ,d.id_map \n" +
                                            "    ,d.nm_prov \n" +
                                            "    ,b.year \n" +
                                            "    ,g.nm_goals\n" +
                                            "    ,f.nm_target\n" +
                                            "    ,e.nm_indicator\n"+
                                            "    ,(SELECT COUNT(*) FROM (SELECT * FROM gov_map)  AS tgovmap WHERE id_prov = c.id_prov) AS gov   " +
                                            "    ,(SELECT COUNT(*) FROM (SELECT * FROM nsa_map) AS tnsa_map WHERE id_prov = c.id_prov) AS non_gov \n" +
                                            "     FROM entry_sdg a JOIN sdg_indicator_target b ON a.id_monper = b.id_monper and a.id_sdg_indicator = b.id_sdg_indicator AND a.id_role = b.id_role AND a.year_entry = b.year  \n" +
                                            "     JOIN ref_role c ON a.id_role = c.id_role \n" +
                                            "     JOIN ref_province d ON c.id_prov = d.id_prov \n" +
                                            "     JOIN sdg_indicator e ON a.id_sdg_indicator = e.id\n" +
                                            "     JOIN sdg_target f ON e.id_target = f.id\n" +
                                            "     JOIN sdg_goals g ON f.id_goals = g.id\n" +
                                            "     JOIN entry_approval h on a.id_role = h.id_role and a.id_monper = h.id_monper and h.type='entry_sdg' " +
                                            " WHERE b.year = YEAR(NOW()) AND (h.approval = '2' OR  h.approval = '4')");
        
            List list =  query.getResultList();
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list);
            
            String sql = "SELECT min(start_year) as awal_tahun, max(end_year) as akhir_tahun from ran_rad where status ='on Going' or status = 'completed'";
            Query list2 = em.createNativeQuery(sql);
            Map<String, Object> hasiltahun = new HashMap<>();            
            hasiltahun.put("tahunmap",list2.getResultList());
            
            
            Query query3 = em.createNativeQuery("SELECT DISTINCT a.id,a.nm_goals AS nm,LPAD(a.id,3,'0') AS id_parent,'1' AS LEVEL ,a.id_goals AS id_text  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                                                "UNION \n" +
                                                "SELECT DISTINCT  b.id,b.nm_target AS nm,CONCAT(LPAD(a.id,3,'0'),'.',LPAD(b.id,3,'0')) AS id_parent,'2' AS LEVEL ,CONCAT(a.id_goals,'-',b.id_target) AS id_text FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                                                "UNION \n" +
                                                "SELECT DISTINCT  c.id,c.nm_indicator AS nm,CONCAT(LPAD(a.id,3,'0') ,'.',LPAD(b.id,3,'0'),'.',LPAD(c.id,3,'0')) AS id_parent,'3' AS LEVEL ,CONCAT(a.id_goals,'-',b.id_target,'-',c.id_indicator) AS id_text  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                                                "ORDER BY id_parent");
        
            List list3 =  query3.getResultList();
            Map<String, Object> filtersdg = new HashMap<>();
            filtersdg.put("data",list3);
            
            model.addAttribute("map",hasil);
            model.addAttribute("tahunmap",hasiltahun);
            model.addAttribute("filtersdg",filtersdg);
         return "admin/dashboard";
    }
    
       @GetMapping("admin/dashboard/get-map/{tahun}/{indicator}")
        public @ResponseBody Map<String, Object> getUnit(@PathVariable("tahun") String tahun,@PathVariable("indicator") String indicator) {
        String where = "";
            if(!indicator.equals("0")){
                where = "AND b.id_sdg_indicator = '"+indicator+"'";
            }
        Query query = em.createNativeQuery("SELECT a.id_sdg_indicator,b.value AS target  \n" +
                                            "    , (a.achievement1+a.achievement2+a.achievement3+a.achievement4) AS realisasi \n" +
                                            "    ,c.id_prov \n" +
                                            "    ,d.id_map \n" +
                                            "    ,d.nm_prov \n" +
                                            "    ,b.year \n" +
                                            "    ,g.nm_goals\n" +
                                            "    ,f.nm_target\n" +
                                            "    ,e.nm_indicator\n"+
                                            "    ,(SELECT COUNT(*) FROM (SELECT * FROM gov_map)  AS tgovmap WHERE id_prov = c.id_prov) AS gov   " +
                                            "    ,(SELECT COUNT(*) FROM (SELECT * FROM nsa_map) AS tnsa_map WHERE id_prov = c.id_prov) AS non_gov \n" +
                                            "     FROM entry_sdg a JOIN sdg_indicator_target b ON a.id_monper = b.id_monper and a.id_sdg_indicator = b.id_sdg_indicator AND a.id_role = b.id_role AND a.year_entry = b.year  \n" +
                                            "     JOIN ref_role c ON a.id_role = c.id_role \n" +
                                            "     JOIN ref_province d ON c.id_prov = d.id_prov \n" +
                                            "     JOIN sdg_indicator e ON a.id_sdg_indicator = e.id\n" +
                                            "     JOIN sdg_target f ON e.id_target = f.id\n" +
                                            "     JOIN sdg_goals g ON f.id_goals = g.id\n"
                                          + "     JOIN entry_approval h on a.id_role = h.id_role and a.id_monper = h.id_monper and h.type='entry_sdg' " +
                                            "     WHERE b.year = '"+tahun+"' "+where+" AND (h.approval = '2' OR  h.approval = '4') ");
            List list =  query.getResultList();
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list);
            return hasil;
	}

    @PostMapping("admin/bahasa")
    public @ResponseBody void bahasa(@RequestParam("bhs") String bhs, HttpServletRequest request, HttpSession session) {
        request.getSession().setAttribute("bahasa", bhs);
    }

    @PostMapping("admin/english")
    public @ResponseBody void english(@RequestParam("bhs") String bhs, HttpServletRequest request, HttpSession session) {
        request.getSession().setAttribute("bahasa", bhs);
    }
    
    //*********************** RAN RAD ***********************

    @GetMapping("admin/ran_rad/gov/program")
    public String gov_program(Model model, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
    	String privilege = list.get().getPrivilege();
    	model.addAttribute("provMap", prov.findAllProvinsi());
    	if(privilege.equals("SUPER")) {
    		model.addAttribute("prov", prov.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = prov.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("prov", foundUpdateObject1));
    	}
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        model.addAttribute("monPer", monPeriodService.findAll(id_prov));
        model.addAttribute("role", roleService.findByProvince(id_prov));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("privilege", privilege);
        String bhs = (String) session.getAttribute("bahasa");
        if (bhs == null) {bhs = "0";}
        model.addAttribute("lang", bhs);
        return "admin/ran_rad/gov/program";
    }

    @GetMapping("admin/ran_rad/non-gov/program")
    public String nongov_program(Model model, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
    	String privilege = list.get().getPrivilege();
    	if(privilege.equals("SUPER")) {
    		model.addAttribute("prov", prov.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = prov.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("prov", foundUpdateObject1));
    	}
    	if(privilege.equals("SUPER") || privilege.equals("ADMIN")) {
    		model.addAttribute("role", roleService.findRoleNonGov(id_prov));
    	}else {
    		Optional<Role> list1 = roleService.findOne(id_role);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("role", foundUpdateObject1));
    	}
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        model.addAttribute("monPer", monPeriodService.findAll(id_prov));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("privilege", privilege);
        String bhs = (String) session.getAttribute("bahasa");
        if (bhs == null) {bhs = "0";}
        model.addAttribute("lang", bhs);
//        System.out.println(privilege);
        return "admin/ran_rad/non-gov/program";
    }

    @GetMapping("admin/ran_rad")
    public String ran_goals(Model model, HttpSession session) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
    	String privilege = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
    		model.addAttribute("prov", prov.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = prov.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("prov", foundUpdateObject1));
    	}
        //tester brooo google gg
        String bhs = (String) session.getAttribute("bahasa");
        if (bhs == null) {bhs = "0";}
        model.addAttribute("lang", bhs);
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("privilege", privilege);
        return "admin/ran_rad/monper";
    }

}
