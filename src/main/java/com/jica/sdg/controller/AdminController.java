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

import javax.servlet.ServletContext;
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
        
    @Autowired
    private ServletContext context;

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
		model.addAttribute("context", context.getContextPath());
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
        
         Query query = em.createNativeQuery("SELECT a.id_sdg_indicator,b.value AS target  \r\n" + 
        		 ",Case when j.calculation = '1' Then ("
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '1') = 0 THEN 0 else COALESCE(a.new_value1,a.achievement1,0) end"
          		+ "+"
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '2') = 0 THEN 0 else COALESCE(a.new_value2,a.achievement2,0) end"
          		+ "+"
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '3') = 0 THEN 0 else COALESCE(a.new_value3,a.achievement3,0) end"
          		+ "+"
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '4') = 0 THEN 0 else COALESCE(a.new_value4,a.achievement4,0) end"
          		+ ") else "
          		+ "COALESCE("
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '4') = 0 THEN null else a.new_value4 end,"
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '3') = 0 THEN null else a.new_value3 end,"
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '2') = 0 THEN null else a.new_value2 end,"
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '1') = 0 THEN null else a.new_value1 end,"
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '4') = 0 THEN null else a.achievement4 end,"
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '3') = 0 THEN null else a.achievement3 end,"
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '2') = 0 THEN null else a.achievement2 end,"
          		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '1') = 0 THEN null else a.achievement1 end"
          		+ ") end AS realisasi \r\n" + 
         		",d.id_prov \r\n" + 
         		",d.id_map \r\n" + 
         		",d.nm_prov \r\n" + 
         		",a.year_entry \r\n" + 
         		",g.nm_goals\r\n" + 
         		",f.nm_target\r\n" + 
         		",e.nm_indicator\r\n" + 
         		",g.nm_goals_eng\r\n" + 
         		",f.nm_target_eng\r\n" + 
         		",e.nm_indicator_eng\r\n" + 
         		",(SELECT COUNT(*) FROM (SELECT * FROM gov_map)  AS tgovmap WHERE id_prov = i.id_prov and id_monper = a.id_monper) AS gov   \r\n" + 
         		",(SELECT COUNT(*) FROM (SELECT * FROM nsa_map) AS tnsa_map WHERE id_prov = i.id_prov and id_monper = a.id_monper) AS non_gov \r\n" + 
         		",IFNULL(a.achievement1,0),IFNULL(a.achievement2,0),IFNULL(a.achievement3,0),IFNULL(a.achievement4,0)\r\n" + 
         		",case when b.value is not null then ((IFNULL(a.achievement1,0)+IFNULL(a.achievement2,0)+IFNULL(a.achievement3,0)+IFNULL(a.achievement4,0))/b.value)*100 else '' end as persenTotal \r\n" + 
         		",case when b.value is not null then (IFNULL(a.achievement1,0)/b.value)*100 else '' end as persen1\r\n" + 
         		",case when b.value is not null then (IFNULL(a.achievement2,0)/b.value)*100 else '' end as persen2\r\n" + 
         		",case when b.value is not null then (IFNULL(a.achievement3,0)/b.value)*100 else '' end as persen3\r\n" + 
         		",case when b.value is not null then (IFNULL(a.achievement4,0)/b.value)*100 else '' end as persen4\r\n" + 
         		"FROM entry_sdg a \r\n" + 
         		"left JOIN sdg_indicator_target b ON a.id_monper = b.id_monper and a.id_sdg_indicator = b.id_sdg_indicator AND a.id_role = b.id_role AND a.year_entry = b.year  \r\n" + 
         		"left JOIN ref_role c ON a.id_role = c.id_role \r\n" + 
         		"left JOIN ran_rad i on a.id_monper = i.id_monper\r\n" + 
         		"left JOIN ref_province d ON i.id_prov = d.id_prov \r\n" + 
         		"JOIN sdg_indicator e ON a.id_sdg_indicator = e.id\r\n" + 
         		"JOIN sdg_target f ON e.id_target = f.id\r\n" + 
         		"JOIN sdg_goals g ON f.id_goals = g.id\r\n" + 
         		"JOIN entry_approval h on (CASE WHEN a.id_role is null THEN h.id_role is null ELSE a.id_role = h.id_role END) and a.id_monper = h.id_monper and h.type='entry_sdg' and a.year_entry = h.year\r\n" + 
         		"left join ref_unit j on e.unit = j.id_unit "+
         		"WHERE a.year_entry = YEAR(NOW()) AND (h.approval = '4')");
        
            List list =  query.getResultList();
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list);
            
            String sql = "SELECT min(start_year) as awal_tahun, max(end_year) as akhir_tahun from ran_rad where status ='on Going' or status = 'completed'";
            Query list2 = em.createNativeQuery(sql);
            Map<String, Object> hasiltahun = new HashMap<>();            
            hasiltahun.put("tahunmap",list2.getResultList());
            
            String start_year;
            String status = "";
            String id_monper = "";
            List<Object[]> rows = list2.getResultList();
            start_year = rows.get(0)[0].toString();
            String sqlran = "SELECT status,id_monper FROM ran_rad WHERE '"+start_year+"' BETWEEN start_year and end_year and id_prov = 000 limit 1";
            Query listran = em.createNativeQuery(sqlran);
            List<Object[]> rowsran = listran.getResultList();
            if(!rowsran.isEmpty()) {
            	status = rowsran.get(0)[0].toString();
            	id_monper = rowsran.get(0)[1].toString();
            }

            Query query3;
            if(status.equals("completed")) {
            	query3 = em.createNativeQuery("SELECT DISTINCT a.id_old,a.nm_goals AS nm,LPAD(a.id_old,3,'0') AS id_parent,'1' AS LEVEL ,a.id_goals AS id_text,CONCAT(a.id_goals,'.0','.0') AS id_sort  FROM history_sdg_goals a JOIN history_sdg_target b ON a.id_old = b.id_goals and a.id_monper = b.id_monper JOIN history_sdg_indicator c ON b.id_old = c.id_target and b.id_monper = c.id_monper where a.id_monper = '"+id_monper+"' \n" +
                        "UNION \n" +
                        "SELECT DISTINCT  b.id_old,b.nm_target AS nm,CONCAT(LPAD(a.id_old,3,'0'),'.',LPAD(b.id_old,3,'0')) AS id_parent,'2' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.0') AS id_sort FROM history_sdg_goals a JOIN history_sdg_target b ON a.id_old = b.id_goals and a.id_monper = b.id_monper JOIN history_sdg_indicator c ON b.id_old = c.id_target and b.id_monper = c.id_monper where a.id_monper = '"+id_monper+"'\n" +
                        "UNION \n" +
                        "SELECT DISTINCT  c.id_old,c.nm_indicator AS nm,CONCAT(LPAD(a.id_old,3,'0') ,'.',LPAD(b.id_old,3,'0'),'.',LPAD(c.id_old,3,'0')) AS id_parent,'3' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_sort  FROM history_sdg_goals a JOIN history_sdg_target b ON a.id_old = b.id_goals and a.id_monper = b.id_monper JOIN history_sdg_indicator c ON b.id_old = c.id_target and b.id_monper = c.id_monper where a.id_monper = '"+id_monper+"'\n" +
                        "ORDER BY cast(substring_index(id_sort,'.',1) as unsigned),\r\n" + 
                        "cast(substring_index(substring_index(id_sort,'.',2),'.',-1) as unsigned),\r\n" + 
                        "cast(substring_index(substring_index(id_sort,'.',3),'.',-1) as unsigned)");
            }else {
            	query3 = em.createNativeQuery("SELECT DISTINCT a.id,a.nm_goals AS nm,LPAD(a.id,3,'0') AS id_parent,'1' AS LEVEL ,a.id_goals AS id_text,CONCAT(a.id_goals,'.0','.0') AS id_sort  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                        "UNION \n" +
                        "SELECT DISTINCT  b.id,b.nm_target AS nm,CONCAT(LPAD(a.id,3,'0'),'.',LPAD(b.id,3,'0')) AS id_parent,'2' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.0') AS id_sort FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                        "UNION \n" +
                        "SELECT DISTINCT  c.id,c.nm_indicator AS nm,CONCAT(LPAD(a.id,3,'0') ,'.',LPAD(b.id,3,'0'),'.',LPAD(c.id,3,'0')) AS id_parent,'3' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_sort  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                        "ORDER BY cast(substring_index(id_sort,'.',1) as unsigned),\r\n" + 
                        "cast(substring_index(substring_index(id_sort,'.',2),'.',-1) as unsigned),\r\n" + 
                        "cast(substring_index(substring_index(id_sort,'.',3),'.',-1) as unsigned)");
            }
            
            List list3 =  query3.getResultList();
            Map<String, Object> filtersdg = new HashMap<>();
            filtersdg.put("data",list3);
            
            model.addAttribute("map",hasil);
            model.addAttribute("tahunmap",hasiltahun);
            model.addAttribute("filtersdg",filtersdg);
            model.addAttribute("id_user",userData.get(0).getId_user());
         return "admin/dashboard";
    }
    
    @GetMapping("admin/dashboard/get-sdg-map/{tahun}")
    public @ResponseBody Map<String, Object> getSdgMap(@PathVariable("tahun") String tahun) {
    	String sqlran = "SELECT status,id_monper FROM ran_rad WHERE '"+tahun+"' BETWEEN start_year and end_year and id_prov = 000 limit 1";
        Query listran = em.createNativeQuery(sqlran);
        String status = "";
        String id_monper = "";
        List<Object[]> rowsran = listran.getResultList();
        if(!rowsran.isEmpty()) {
        	status = rowsran.get(0)[0].toString();
        	id_monper = rowsran.get(0)[1].toString();
        }

        Query query3;
        Query query4;
        if(status.equals("completed")) {
        	query3 = em.createNativeQuery("SELECT DISTINCT a.id_old,a.nm_goals AS nm,LPAD(a.id_old,3,'0') AS id_parent,'1' AS LEVEL ,a.id_goals AS id_text,CONCAT(a.id_goals,'.0','.0') AS id_sort  FROM history_sdg_goals a JOIN history_sdg_target b ON a.id_old = b.id_goals and a.id_monper = b.id_monper JOIN history_sdg_indicator c ON b.id_old = c.id_target and b.id_monper = c.id_monper where a.id_monper = '"+id_monper+"' \n" +
                    "UNION \n" +
                    "SELECT DISTINCT  b.id_old,b.nm_target AS nm,CONCAT(LPAD(a.id_old,3,'0'),'.',LPAD(b.id_old,3,'0')) AS id_parent,'2' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.0') AS id_sort FROM history_sdg_goals a JOIN history_sdg_target b ON a.id_old = b.id_goals and a.id_monper = b.id_monper JOIN history_sdg_indicator c ON b.id_old = c.id_target and b.id_monper = c.id_monper where a.id_monper = '"+id_monper+"'\n" +
                    "UNION \n" +
                    "SELECT DISTINCT  c.id_old,c.nm_indicator AS nm,CONCAT(LPAD(a.id_old,3,'0') ,'.',LPAD(b.id_old,3,'0'),'.',LPAD(c.id_old,3,'0')) AS id_parent,'3' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_sort  FROM history_sdg_goals a JOIN history_sdg_target b ON a.id_old = b.id_goals and a.id_monper = b.id_monper JOIN history_sdg_indicator c ON b.id_old = c.id_target and b.id_monper = c.id_monper where a.id_monper = '"+id_monper+"'\n" +
                    "ORDER BY cast(substring_index(id_sort,'.',1) as unsigned),\r\n" + 
                    "cast(substring_index(substring_index(id_sort,'.',2),'.',-1) as unsigned),\r\n" + 
                    "cast(substring_index(substring_index(id_sort,'.',3),'.',-1) as unsigned)");
        	query4 = em.createNativeQuery("SELECT DISTINCT a.id_old,a.nm_goals_eng AS nm,LPAD(a.id_old,3,'0') AS id_parent,'1' AS LEVEL ,a.id_goals AS id_text,CONCAT(a.id_goals,'.0','.0') AS id_sort  FROM history_sdg_goals a JOIN history_sdg_target b ON a.id_old = b.id_goals and a.id_monper = b.id_monper JOIN history_sdg_indicator c ON b.id_old = c.id_target and b.id_monper = c.id_monper where a.id_monper = '"+id_monper+"' \n" +
                    "UNION \n" +
                    "SELECT DISTINCT  b.id_old,b.nm_target_eng AS nm,CONCAT(LPAD(a.id_old,3,'0'),'.',LPAD(b.id_old,3,'0')) AS id_parent,'2' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.0') AS id_sort FROM history_sdg_goals a JOIN history_sdg_target b ON a.id_old = b.id_goals and a.id_monper = b.id_monper JOIN history_sdg_indicator c ON b.id_old = c.id_target and b.id_monper = c.id_monper where a.id_monper = '"+id_monper+"'\n" +
                    "UNION \n" +
                    "SELECT DISTINCT  c.id_old,c.nm_indicator_eng AS nm,CONCAT(LPAD(a.id_old,3,'0') ,'.',LPAD(b.id_old,3,'0'),'.',LPAD(c.id_old,3,'0')) AS id_parent,'3' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_sort  FROM history_sdg_goals a JOIN history_sdg_target b ON a.id_old = b.id_goals and a.id_monper = b.id_monper JOIN history_sdg_indicator c ON b.id_old = c.id_target and b.id_monper = c.id_monper where a.id_monper = '"+id_monper+"'\n" +
                    "ORDER BY cast(substring_index(id_sort,'.',1) as unsigned),\r\n" + 
                    "cast(substring_index(substring_index(id_sort,'.',2),'.',-1) as unsigned),\r\n" + 
                    "cast(substring_index(substring_index(id_sort,'.',3),'.',-1) as unsigned)");
        }else {
        	query3 = em.createNativeQuery("SELECT DISTINCT a.id,a.nm_goals AS nm,LPAD(a.id,3,'0') AS id_parent,'1' AS LEVEL ,a.id_goals AS id_text,CONCAT(a.id_goals,'.0','.0') AS id_sort  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                    "UNION \n" +
                    "SELECT DISTINCT  b.id,b.nm_target AS nm,CONCAT(LPAD(a.id,3,'0'),'.',LPAD(b.id,3,'0')) AS id_parent,'2' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.0') AS id_sort FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                    "UNION \n" +
                    "SELECT DISTINCT  c.id,c.nm_indicator AS nm,CONCAT(LPAD(a.id,3,'0') ,'.',LPAD(b.id,3,'0'),'.',LPAD(c.id,3,'0')) AS id_parent,'3' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_sort  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                    "ORDER BY cast(substring_index(id_sort,'.',1) as unsigned),\r\n" + 
                    "cast(substring_index(substring_index(id_sort,'.',2),'.',-1) as unsigned),\r\n" + 
                    "cast(substring_index(substring_index(id_sort,'.',3),'.',-1) as unsigned)");
        	query4 = em.createNativeQuery("SELECT DISTINCT a.id,a.nm_goals_eng AS nm,LPAD(a.id,3,'0') AS id_parent,'1' AS LEVEL ,a.id_goals AS id_text,CONCAT(a.id_goals,'.0','.0') AS id_sort  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                    "UNION \n" +
                    "SELECT DISTINCT  b.id,b.nm_target_eng AS nm,CONCAT(LPAD(a.id,3,'0'),'.',LPAD(b.id,3,'0')) AS id_parent,'2' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.0') AS id_sort FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                    "UNION \n" +
                    "SELECT DISTINCT  c.id,c.nm_indicator_eng AS nm,CONCAT(LPAD(a.id,3,'0') ,'.',LPAD(b.id,3,'0'),'.',LPAD(c.id,3,'0')) AS id_parent,'3' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_text,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_sort  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                    "ORDER BY cast(substring_index(id_sort,'.',1) as unsigned),\r\n" + 
                    "cast(substring_index(substring_index(id_sort,'.',2),'.',-1) as unsigned),\r\n" + 
                    "cast(substring_index(substring_index(id_sort,'.',3),'.',-1) as unsigned)");
        }
        List list =  query3.getResultList();
        List listEng =  query4.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        hasil.put("contentEng",listEng);
        return hasil;
}
    
       @GetMapping("admin/dashboard/get-map/{tahun}/{indicator}")
        public @ResponseBody Map<String, Object> getUnit(@PathVariable("tahun") String tahun,@PathVariable("indicator") String indicator) {
        String where = "";
        
        String sqlran = "SELECT status,id_monper FROM ran_rad WHERE '"+tahun+"' BETWEEN start_year and end_year and id_prov = 000 limit 1";
        Query listran = em.createNativeQuery(sqlran);
        String status = "";
        String id_monper = "";
        Query query;
        List<Object[]> rowsran = listran.getResultList();
        if(!rowsran.isEmpty()) {
        	status = rowsran.get(0)[0].toString();
        	id_monper = rowsran.get(0)[1].toString();
        }
        if(!indicator.equals("0")){
            where = "AND a.id_sdg_indicator = '"+indicator+"'";
        }
        if(status.equals("completed")) {
            query = em.createNativeQuery("SELECT a.id_sdg_indicator,b.value AS target  \n" +
            		",Case when j.calculation = '1' Then ("
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '1') = 0 THEN 0 else COALESCE(a.new_value1,a.achievement1,0) end"
             		+ "+"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '2') = 0 THEN 0 else COALESCE(a.new_value2,a.achievement2,0) end"
             		+ "+"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '3') = 0 THEN 0 else COALESCE(a.new_value3,a.achievement3,0) end"
             		+ "+"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '4') = 0 THEN 0 else COALESCE(a.new_value4,a.achievement4,0) end"
             		+ ") else "
             		+ "COALESCE("
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '4') = 0 THEN null else a.new_value4 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '3') = 0 THEN null else a.new_value3 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '2') = 0 THEN null else a.new_value2 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '1') = 0 THEN null else a.new_value1 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '4') = 0 THEN null else a.achievement4 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '3') = 0 THEN null else a.achievement3 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '2') = 0 THEN null else a.achievement2 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '1') = 0 THEN null else a.achievement1 end"
             		+ ") end AS realisasi \r\n" + 
	                "    ,d.id_prov \n" +
	                "    ,d.id_map \n" +
	                "    ,d.nm_prov \n" +
	                "    ,a.year_entry \n" +
	                "    ,g.nm_goals\n" +
	                "    ,f.nm_target\n" +
	                "    ,e.nm_indicator\n"+
	                "    ,g.nm_goals_eng\n" +
	                "    ,f.nm_target_eng\n" +
	                "    ,e.nm_indicator_eng\n"+
	                ",(SELECT COUNT(*) FROM (SELECT * FROM gov_map)  AS tgovmap WHERE id_prov = i.id_prov and id_monper = a.id_monper and id_indicator = a.id_sdg_indicator) AS gov   \r\n" + 
	         		",(SELECT COUNT(*) FROM (SELECT * FROM nsa_map) AS tnsa_map WHERE id_prov = i.id_prov and id_monper = a.id_monper and id_indicator = a.id_sdg_indicator) AS non_gov \r\n" + 
	                "    ,IFNULL(a.achievement1,''),IFNULL(a.achievement2,''),IFNULL(a.achievement3,''),IFNULL(a.achievement4,'')"+
	                "    ,case when b.value is not null then ((IFNULL(a.achievement1,0)+IFNULL(a.achievement2,0)+IFNULL(a.achievement3,0)+IFNULL(a.achievement4,0))/b.value)*100 else '' end as persenTotal\n "+
	                "    ,case when b.value is not null then (IFNULL(a.achievement1,0)/b.value)*100 else '' end as persen1\n "+
	                "    ,case when b.value is not null then (IFNULL(a.achievement2,0)/b.value)*100 else '' end as persen2\n "+
	                "    ,case when b.value is not null then (IFNULL(a.achievement3,0)/b.value)*100 else '' end as persen3\n "+
	                "    ,case when b.value is not null then (IFNULL(a.achievement4,0)/b.value)*100 else '' end as persen4\n "+
	                "     FROM entry_sdg a "
	                + "   left JOIN sdg_indicator_target b ON a.id_monper = b.id_monper and a.id_sdg_indicator = b.id_sdg_indicator AND a.id_role = b.id_role AND a.year_entry = b.year  \n" +
	                "     left JOIN ref_role c ON a.id_role = c.id_role \n" +
	                "	  left JOIN ran_rad i on a.id_monper = i.id_monper\r\n" + 
	                "     JOIN ref_province d ON i.id_prov = d.id_prov \n" +
	                "     JOIN history_sdg_indicator e ON a.id_sdg_indicator = e.id_old and e.id_monper = '"+id_monper+"'\n" +
	                "     JOIN history_sdg_target f ON e.id_target = f.id_old and f.id_monper = '"+id_monper+"'\n" +
	                "     JOIN history_sdg_goals g ON f.id_goals = g.id_old and g.id_monper = '"+id_monper+"'\n"
	              + "     JOIN entry_approval h on (CASE WHEN a.id_role is null THEN h.id_role is null ELSE a.id_role = h.id_role END) and a.id_monper = h.id_monper and h.type='entry_sdg' and a.year_entry = h.year " +
	              "left join ref_unit j on e.unit = j.id_unit "+ 
	              "     WHERE a.year_entry = '"+tahun+"' "+where+" AND (h.approval = '4') ");
            
        }else {
            query = em.createNativeQuery("SELECT a.id_sdg_indicator,b.value AS target  \r\n" + 
            		",Case when j.calculation = '1' Then ("
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '1') = 0 THEN 0 else COALESCE(a.new_value1,a.achievement1,0) end"
             		+ "+"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '2') = 0 THEN 0 else COALESCE(a.new_value2,a.achievement2,0) end"
             		+ "+"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '3') = 0 THEN 0 else COALESCE(a.new_value3,a.achievement3,0) end"
             		+ "+"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '4') = 0 THEN 0 else COALESCE(a.new_value4,a.achievement4,0) end"
             		+ ") else "
             		+ "COALESCE("
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '4') = 0 THEN null else a.new_value4 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '3') = 0 THEN null else a.new_value3 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '2') = 0 THEN null else a.new_value2 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '1') = 0 THEN null else a.new_value1 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '4') = 0 THEN null else a.achievement4 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '3') = 0 THEN null else a.achievement3 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '2') = 0 THEN null else a.achievement2 end,"
             		+ "case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = a.year_entry and type = 'entry_sdg' and period = '1') = 0 THEN null else a.achievement1 end"
             		+ ") end AS realisasi \r\n" +  
	        		",d.id_prov \r\n" + 
	        		",d.id_map \r\n" + 
	        		",d.nm_prov \r\n" + 
	        		",a.year_entry \r\n" + 
	        		",g.nm_goals\r\n" + 
	        		",f.nm_target\r\n" + 
	        		",e.nm_indicator\r\n" + 
	        		",g.nm_goals_eng\r\n" + 
	        		",f.nm_target_eng\r\n" + 
	        		",e.nm_indicator_eng\r\n" + 
	        		",(SELECT COUNT(*) FROM (SELECT * FROM gov_map)  AS tgovmap WHERE id_prov = i.id_prov and id_monper = a.id_monper and id_indicator = a.id_sdg_indicator) AS gov   \r\n" + 
	         		",(SELECT COUNT(*) FROM (SELECT * FROM nsa_map) AS tnsa_map WHERE id_prov = i.id_prov and id_monper = a.id_monper and id_indicator = a.id_sdg_indicator) AS non_gov \r\n" +
	        		",IFNULL(a.achievement1,0),IFNULL(a.achievement2,0),IFNULL(a.achievement3,0),IFNULL(a.achievement4,0)\r\n" + 
	        		",case when b.value is not null then ((IFNULL(a.achievement1,0)+IFNULL(a.achievement2,0)+IFNULL(a.achievement3,0)+IFNULL(a.achievement4,0))/b.value)*100 else '' end as persenTotal \r\n" + 
	        		",case when b.value is not null then (IFNULL(a.achievement1,0)/b.value)*100 else '' end as persen1\r\n" + 
	        		",case when b.value is not null then (IFNULL(a.achievement2,0)/b.value)*100 else '' end as persen2\r\n" + 
	        		",case when b.value is not null then (IFNULL(a.achievement3,0)/b.value)*100 else '' end as persen3\r\n" + 
	        		",case when b.value is not null then (IFNULL(a.achievement4,0)/b.value)*100 else '' end as persen4\r\n" + 
	        		"FROM entry_sdg a \r\n" + 
	        		"left JOIN sdg_indicator_target b ON a.id_monper = b.id_monper and a.id_sdg_indicator = b.id_sdg_indicator AND a.id_role = b.id_role AND a.year_entry = b.year  \r\n" + 
	        		"left JOIN ref_role c ON a.id_role = c.id_role \r\n" + 
	        		"left JOIN ran_rad i on a.id_monper = i.id_monper\r\n" + 
	        		"left JOIN ref_province d ON i.id_prov = d.id_prov \r\n" + 
	        		"JOIN sdg_indicator e ON a.id_sdg_indicator = e.id\r\n" + 
	        		"JOIN sdg_target f ON e.id_target = f.id\r\n" + 
	        		"JOIN sdg_goals g ON f.id_goals = g.id\r\n" + 
	        		"JOIN entry_approval h on (CASE WHEN a.id_role is null THEN h.id_role is null ELSE a.id_role = h.id_role END) and a.id_monper = h.id_monper and h.type='entry_sdg' and a.year_entry = h.year\r\n" + 
	        		"left join ref_unit j on e.unit = j.id_unit "+
	        		"WHERE a.year_entry = '"+tahun+"' "+where+" AND (h.approval = '4')");
           
        }
        
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
