package com.jica.sdg.controller;

import com.jica.sdg.model.EntryApproval;
import com.jica.sdg.model.EntryGovBudget;
import com.jica.sdg.model.EntryGovIndicator;
import com.jica.sdg.model.EntryGriojk;
import com.jica.sdg.model.EntryNsaBudget;
import com.jica.sdg.model.EntryNsaIndicator;
import com.jica.sdg.model.EntryShowReport;
import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntrySdgDetail;
import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.NsaProgram;

import com.jica.sdg.model.GovActivity;
import com.jica.sdg.model.NsaActivity;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import com.jica.sdg.service.*;

import java.util.*;

import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.RanRad;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgFunding;
import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.model.SdgIndicatorTarget;
import com.jica.sdg.model.SdgTarget;
import com.jica.sdg.model.Unit;
import com.jica.sdg.repository.EntryGriOjkRepository;
import com.jica.sdg.service.IEntrySdgService;
import com.jica.sdg.service.ISdgIndicatorService;
import com.jica.sdg.service.IGovProgramService;
import com.jica.sdg.service.INsaProgramService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRanRadService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.NsaProfileService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
//import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DataEntryController {

    //*********************** NSA ***********************

    @Autowired
    IProvinsiService provinsiService;
    
    @Autowired
    IEntrySdgService entrySdgService;
    
    @Autowired
    private EntityManager em;

    @Autowired
    IRoleService roleService;

    @Autowired
    IRanRadService ranRadService;

    @Autowired
    EntryProblemIdentifyService identifyService;

    @Autowired
    NsaProfileService nsaProfilrService;

    @Autowired
    SdgGoalsService goalsService;

    @Autowired
    EntryProblemIdentifyRepository repository;
    
    @Autowired
    IGovProgramService govProgService;
    
    @Autowired
    IGovActivityService govActService;
    
    @Autowired
    IEntryGriOjkService entryGriOjkService;
    
    @Autowired
    EntryGriOjkRepository entryGirOjkRepo;
    
    @Autowired
    INsaProgramService nsaProgService;
    
    @Autowired
    INsaActivityService nsaActService;
    
    @Autowired
    ISdgIndicatorService sdgIndicatorService;
    
    @Autowired
    SdgGoalsService sdgGoalsService;
    
    @Autowired
    SdgTargetService sdgTargetService;
//    
    @Autowired
    UnitService unitService;
    
    @Autowired
    SdgFundingService sdgFundingService;
    
    @Autowired
    IEntryApprovalService approvalService;
    
    @Autowired
    IEntrySdgDetailService entrySdgDetailService;

    //entry SDG
    @GetMapping("admin/sdg-indicator-monitoring")
    public String entri_sdg(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
    	model.addAttribute("listNsaProfile", roleService.findByProvince(id_prov));
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        return "admin/dataentry/entry_sdg";
    }
    
    @GetMapping("admin/list-get-option-monper/{id}")
    public @ResponseBody Map<String, Object> getOptionMonperList(@PathVariable("id") String id) {
        String sql  = "select * from ran_rad as a where a.id_prov = :id and a.status = 'on Going' order by start_year";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List list   = query.getResultList();
        
        String sql1  = "select * from ran_rad as a where a.id_prov = :id and (a.status = 'on Going' or a.status = 'created') order by start_year";
        Query query1 = em.createNativeQuery(sql1);
        query1.setParameter("id", id);
        List list1   = query1.getResultList();
        
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        hasil.put("target",list1);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-monper-report/{id}")
    public @ResponseBody Map<String, Object> getOptionMonperReportList(@PathVariable("id") String id) {
        String sql  = "select * from ran_rad as a where a.id_prov = :id and (a.status = 'on Going' or a.status = 'completed') order by start_year";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-monper-eva/{id}")
    public @ResponseBody Map<String, Object> getOptionMonperReportListEva(@PathVariable("id") String id) {
        String sql  = "select * from ran_rad as a where a.id_prov = :id and (a.status = 'on Going' or a.status = 'completed') order by start_year desc";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-category/{id_prov}/{id_role}/{id_monper}")
    public @ResponseBody Map<String, Object> getOptionCategoryList(@PathVariable("id_prov") String id_prov,@PathVariable("id_role") String id_role,@PathVariable("id_monper") String id_monper) {
        String wheremonper = "";
        String whereidrole ="";
        if(!id_monper.equals("*")){
          wheremonper = " AND  a.id_monper = '"+id_monper+"'";  
        }
        
        if(!id_role.equals("*")){
          whereidrole = " AND a.id_role = '"+id_role+"'";  
        }
        String sql  = " SELECT Distinct b.id_cat,b.nm_cat FROM entry_problem_identify a JOIN ref_category b ON a.id_cat = b.id_cat WHERE  a.id_prov = :id_prov "+whereidrole+wheremonper ;
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-goals/{id_prov}/{id_role}/{id_monper}/{id_category}")
    public @ResponseBody Map<String, Object> getOptionGoalsList(@PathVariable("id_prov") String id_prov,@PathVariable("id_role") String id_role,@PathVariable("id_monper") String id_monper,@PathVariable("id_category") String id_category) {
    	Optional<RanRad> monper = ranRadService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	
    	String whereidrole ="";
        String wheremonper = "";
        String whereidcategory ="";
        if(!id_monper.equals("*")){
          wheremonper = " AND  a.id_monper = '"+id_monper+"'";  
        }
        
        if(!id_role.equals("*")){
          whereidrole = " AND c.id_role = '"+id_role+"'";  
        }
        
        if(!id_category.equals("*")){
          whereidcategory = " AND c.id_cat =  '"+id_category+"'";  
        }
        
        String sql;
        if(status.equals("completed")) {
        	sql  = " SELECT DISTINCT b.id_old,b.nm_goals,b.id_goals  FROM entry_problem_identify_map a \n" +
                    "JOIN history_sdg_goals b ON a.id_goals = b.id_old and b.id_monper = '"+id_monper+"' \n" +
                    "LEFT JOIN entry_problem_identify c ON a.id_relation_entry_problem_identify = c.id_relation  \n" +
                    "WHERE a.id_prov = :id_prov "+whereidrole+wheremonper+whereidcategory;
        }else {
        	sql  = " SELECT DISTINCT b.id,b.nm_goals,b.id_goals  FROM entry_problem_identify_map a \n" +
                    "JOIN sdg_goals b ON a.id_goals = b.id \n" +
                    "LEFT JOIN entry_problem_identify c ON a.id_relation_entry_problem_identify = c.id_relation  \n" +
                    "WHERE a.id_prov = :id_prov "+whereidrole+wheremonper+whereidcategory;
        }
        
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-goals/{id_prov}/{id_role}/{id_monper}/{id_category}/{id_goals}")
    public @ResponseBody Map<String, Object> getOptionTargetList(@PathVariable("id_prov") String id_prov,@PathVariable("id_role") String id_role,@PathVariable("id_monper") String id_monper,@PathVariable("id_category") String id_category,@PathVariable("id_goals") String id_goals) {
    	Optional<RanRad> monper = ranRadService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	
    	String whereidrole ="";
        String wheremonper = "";
        String whereidcategory ="";
        String whereidgoals ="";
        if(!id_monper.equals("*")){
          wheremonper = " AND  a.id_monper = '"+id_monper+"'";  
        }
        
        if(!id_role.equals("*")){
          whereidrole = " AND c.id_role = '"+id_role+"'";  
        }
        
        if(!id_category.equals("*")){
          whereidcategory = " AND c.id_cat =  '"+id_category+"'";  
        }        
        if(!id_goals.equals("*")){
          whereidgoals = "  and a.id_goals =  '"+id_goals+"'";  
        }
        String sql;
        if(status.equals("completed")) {
        	sql  =   " SELECT DISTINCT b.id_old,b.nm_target,b.id_target  FROM entry_problem_identify_map a \n" +
                    " JOIN history_sdg_target b ON a.id_target = b.id_old and b.id_monper = '"+id_monper+"' \n" +
                    " LEFT JOIN entry_problem_identify c ON a.id_relation_entry_problem_identify = c.id_relation    \n" +
                    " WHERE a.id_prov = :id_prov "+whereidrole+wheremonper+whereidcategory+whereidgoals;
        }else {
        	sql  =   " SELECT DISTINCT b.id,b.nm_target,b.id_target  FROM entry_problem_identify_map a \n" +
                    " JOIN sdg_target b ON a.id_target = b.id\n" +
                    " LEFT JOIN entry_problem_identify c ON a.id_relation_entry_problem_identify = c.id_relation    \n" +
                    " WHERE a.id_prov = :id_prov "+whereidrole+wheremonper+whereidcategory+whereidgoals;
        }
        
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
     @GetMapping("admin/list-get-option-goals/{id_prov}/{id_role}/{id_monper}/{id_category}/{id_goals}/{id_target}")
    public @ResponseBody Map<String, Object> getOptionIndicatorList(@PathVariable("id_prov") String id_prov,@PathVariable("id_role") String id_role,@PathVariable("id_monper") String id_monper,@PathVariable("id_category") String id_category,@PathVariable("id_goals") String id_goals,@PathVariable("id_target") String id_target) {
    	Optional<RanRad> monper = ranRadService.findOne(Integer.parseInt(id_monper));
     	String status = (monper.isPresent())?monper.get().getStatus():"";
     	
    	String whereidrole ="";
        String wheremonper = "";
        String whereidcategory ="";
        String whereidgoals ="";
        String whereidtarget ="";
        if(!id_monper.equals("*")){
          wheremonper = " AND  a.id_monper = '"+id_monper+"'";  
        }
        
        if(!id_role.equals("*")){
          whereidrole = " AND c.id_role = '"+id_role+"'";  
        }
        
        if(!id_category.equals("*")){
          whereidcategory = " AND c.id_cat =  '"+id_category+"'";  
        }        
        if(!id_goals.equals("*")){
          whereidgoals = "  and a.id_goals =  '"+id_goals+"'";  
        }
        
        if(!id_target.equals("*")){
          whereidtarget = "and a.id_target =  '"+id_target+"'";  
        }
        String sql;
        if(status.equals("completed")) {
        	sql  =   " SELECT DISTINCT b.id_old,b.nm_indicator,b.id_indicator  FROM entry_problem_identify_map a \n" +
                    " JOIN history_sdg_indicator b ON a.id_indicator = b.id_old and b.id_monper = '"+id_monper+"' \n" +
                    " LEFT JOIN entry_problem_identify c ON a.id_relation_entry_problem_identify = c.id_relation \n " +
                    " WHERE a.id_prov = :id_prov "+whereidrole+wheremonper+whereidcategory+whereidgoals+whereidtarget;
        }else {
        	sql  =   " SELECT DISTINCT b.id,b.nm_indicator,b.id_indicator  FROM entry_problem_identify_map a \n" +
                    " JOIN sdg_indicator b ON a.id_indicator = b.id\n" +
                    " LEFT JOIN entry_problem_identify c ON a.id_relation_entry_problem_identify = c.id_relation \n " +
                    " WHERE a.id_prov = :id_prov "+whereidrole+wheremonper+whereidcategory+whereidgoals+whereidtarget;
        }
        
        
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    
    @GetMapping("admin/list-get-sts-monper/{id_monper}")
    public @ResponseBody Map<String, Object> getGetStsMonper(@PathVariable("id_monper") String id_monper) {
        String sql  = "select sdg_indicator from ran_rad as a where a.id_monper = :id_monper ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-sts-monper-all/{id_prov}/{id_monper}")
    public @ResponseBody Map<String, Object> getGetStsMonperAll(@PathVariable("id_prov") String id_prov,@PathVariable("id_monper") String id_monper) {
    	String sql;
    	if(id_monper.equals("0")) {
    		sql  = "select sdg_indicator, id_monper, start_year, end_year from ran_rad as a where (a.status = 'on Going' or a.status = 'completed') and a.id_prov=:id_prov order by start_year asc ";
    	}else {
    		sql  = "select sdg_indicator, id_monper, start_year, end_year from ran_rad as a where a.id_prov=:id_prov and a.id_monper = '"+id_monper+"' order by start_year asc ";
    	} 
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-sdg/{id_prov}/{id_role}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> listEntrySdg(@PathVariable("id_prov") String id_prov, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
    	Query query;
    	if(id_role.equals("0")) {
    		String sql  = "select b.id as id_goals, c.id as id_target, d.id as id_indicator, b.nm_goals, c.nm_target, d.nm_indicator,CASE when h.nm_unit is null then '' else h.nm_unit end as nm_unit, d.increment_decrement, e.value,\r\n" + 
    				"f.achievement1, f.achievement2, f.achievement3, f.achievement4, g.sdg_indicator, f.id as id_target_1, b.id_goals as kode_goals, b.nm_goals_eng, \r\n" + 
    				"c.id_target as kode_target, c.nm_target_eng, d.id_indicator as kode_indicator, d.nm_indicator_eng, \r\n" + 
    				"f.new_value1, f.new_value2, f.new_value3, f.new_value4, '' as id_disaggre, '' as nm_disaggre, '' as nm_disaggre_eng, '' as desc_disaggre, '' as desc_disaggre_eng, '' as iddisaggre, '' as iddetaildis, '' as identrysdgdetail, +\r\n" + 
    				"'' as achi1, '' as achi2, '' as achi3, '' as achi4, \r\n" + 
    				"'' as new1, '' as new2, '' as new3, '' as new4, CASE when l.nm_role is null then 'Unassigned' else l.nm_role end, l.id_role \r\n" + 
    				"from sdg_indicator as d\r\n" + 
    				"left join assign_sdg_indicator as a on a.id_indicator = d.id AND a.id_prov = :id_prov \r\n" + 
    				"left join ran_rad g on a.id_monper = g.id_monper and g.id_prov = a.id_prov\r\n" + 
    				"left join sdg_target as c on d.id_target = c.id \r\n" +
    				"left join sdg_goals as b on d.id_goals = b.id \r\n" + 
    				"left join sdg_indicator_target as e on e.id_monper = :id_monper and d.id = e.id_sdg_indicator and e.year = :year\r\n" + 
    				"left join entry_sdg as f on d.id = f.id_sdg_indicator and f.year_entry = :year and f.id_monper =:id_monper\r\n" + 
    				"left join ref_unit as h on d.unit = h.id_unit \r\n" + 
    				"left join ref_role as l on a.id_role = l.id_role \r\n" + 
    				"";
        	sql  += "union select b.id as id_goals, c.id as id_target, d.id as id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, CASE when h.nm_unit is null then '' else h.nm_unit end as nm_unit, d.increment_decrement, e.value,\r\n" + 
        			"f.achievement1, f.achievement2, f.achievement3, f.achievement4, g.sdg_indicator, f.id as id_target_1, b.id_goals as kode_goals, b.nm_goals_eng, \r\n" + 
        			"c.id_target as kode_target, c.nm_target_eng, d.id_indicator as kode_indicator, d.nm_indicator_eng, \r\n" + 
        			"f.new_value1, f.new_value2, f.new_value3, f.new_value4, i.id_disaggre, i.nm_disaggre, i.nm_disaggre_eng, j.desc_disaggre, j.desc_disaggre_eng, i.id as iddisaggre, j.id as iddetaildis, k.id as identrysdgdetail, +\r\n" + 
        			"k.achievement1 as achi1, k.achievement2 as achi2, k.achievement3 as achi3, k.achievement4 as achi4, \r\n" + 
        			"k.new_value1 as new1, k.new_value2 as new2, k.new_value3 as new3, k.new_value4 as new4, CASE when l.nm_role is null then 'Unassigned' else l.nm_role end, l.id_role \r\n" + 
        			"from sdg_indicator as d\r\n" + 
        			"left join assign_sdg_indicator as a on a.id_indicator = d.id AND a.id_prov = :id_prov \r\n" + 
        			"left join ran_rad g on a.id_monper = g.id_monper and g.id_prov = a.id_prov\r\n" + 
        			"left join sdg_target as c on d.id_target = c.id \r\n" +
        			"left join sdg_goals as b on d.id_goals = b.id \r\n" + 
        			"left join sdg_indicator_target as e on e.id_monper = :id_monper and d.id = e.id_sdg_indicator and e.year = :year\r\n" + 
        			"left join entry_sdg as f on d.id = f.id_sdg_indicator and f.year_entry = :year and f.id_monper =:id_monper\r\n" + 
        			"left join ref_unit as h on d.unit = h.id_unit \r\n" + 
        			"right join sdg_ranrad_disaggre as i on i.id_indicator = d.id \r\n" + 
        			"left join sdg_ranrad_disaggre_detail as j on j.id_disaggre = i.id \r\n" + 
        			"left join entry_sdg_detail as k on j.id_disaggre = k.id_disaggre and j.id = k.id_disaggre_detail and k.year_entry = :year and k.id_monper = :id_monper\r\n" + 
        			"left join ref_role as l on a.id_role = l.id_role \r\n" + 
        			"ORDER BY CAST(kode_goals AS UNSIGNED),CAST(kode_target AS UNSIGNED),CAST(kode_indicator AS UNSIGNED),31,32";
	        query = em.createNativeQuery(sql);
	        query.setParameter("id_prov", id_prov);
	        query.setParameter("id_monper", id_monper);
	        query.setParameter("year", year);
        }else {
        	String role;
        	if(id_role.equals("all")) {
        		role = "";
        	}else if(id_role.equals("unassign")) {
        		role = "id_role is null ";
        	}else {
        		role = "id_role = '"+id_role+"'";
        	}
        	String sql  = "select b.id as id_goals, c.id as id_target, d.id as id_indicator, b.nm_goals, c.nm_target, d.nm_indicator,CASE when h.nm_unit is null then '' else h.nm_unit end as nm_unit, d.increment_decrement, e.value,\r\n" + 
    				"f.achievement1, f.achievement2, f.achievement3, f.achievement4, g.sdg_indicator, f.id as id_target_1, b.id_goals as kode_goals, b.nm_goals_eng, \r\n" + 
    				"c.id_target as kode_target, c.nm_target_eng, d.id_indicator as kode_indicator, d.nm_indicator_eng, \r\n" + 
    				"f.new_value1, f.new_value2, f.new_value3, f.new_value4, '' as id_disaggre, '' as nm_disaggre, '' as nm_disaggre_eng, '' as desc_disaggre, '' as desc_disaggre_eng, '' as iddisaggre, '' as iddetaildis, '' as identrysdgdetail, +\r\n" + 
    				"'' as achi1, '' as achi2, '' as achi3, '' as achi4, \r\n" + 
    				"'' as new1, '' as new2, '' as new3, '' as new4, CASE when l.nm_role is null then 'Unassigned' else l.nm_role end, l.id_role \r\n" + 
    				"from sdg_indicator as d\r\n" + 
    				"left join assign_sdg_indicator as a on a.id_indicator = d.id AND a.id_prov = :id_prov \r\n" + 
    				"left join ran_rad g on a.id_monper = g.id_monper and g.id_prov = a.id_prov\r\n" +
    				"left join sdg_target as c on d.id_target = c.id \r\n" + 
    				"left join sdg_goals as b on d.id_goals = b.id \r\n" + 
    				"left join sdg_indicator_target as e on e.id_monper = :id_monper and d.id = e.id_sdg_indicator and e.year = :year \r\n" + 
    				"left join entry_sdg as f on d.id = f.id_sdg_indicator and f.year_entry = :year and f.id_monper =:id_monper \r\n" + 
    				"left join ref_unit as h on d.unit = h.id_unit \r\n" + 
    				"left join ref_role as l on a.id_role = l.id_role \r\n" + 
    				"where l."+role+ " ";
        	sql  += "union select b.id as id_goals, c.id as id_target, d.id as id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, CASE when h.nm_unit is null then '' else h.nm_unit end as nm_unit, d.increment_decrement, e.value,\r\n" + 
        			"f.achievement1, f.achievement2, f.achievement3, f.achievement4, g.sdg_indicator, f.id as id_target_1, b.id_goals as kode_goals, b.nm_goals_eng, \r\n" + 
        			"c.id_target as kode_target, c.nm_target_eng, d.id_indicator as kode_indicator, d.nm_indicator_eng, \r\n" + 
        			"f.new_value1, f.new_value2, f.new_value3, f.new_value4, i.id_disaggre, i.nm_disaggre, i.nm_disaggre_eng, j.desc_disaggre, j.desc_disaggre_eng, i.id as iddisaggre, j.id as iddetaildis, k.id as identrysdgdetail, +\r\n" + 
        			"k.achievement1 as achi1, k.achievement2 as achi2, k.achievement3 as achi3, k.achievement4 as achi4, \r\n" + 
        			"k.new_value1 as new1, k.new_value2 as new2, k.new_value3 as new3, k.new_value4 as new4, CASE when l.nm_role is null then 'Unassigned' else l.nm_role end, l.id_role \r\n" + 
        			"from sdg_indicator as d\r\n" + 
        			"left join assign_sdg_indicator as a on a.id_indicator = d.id AND a.id_prov = :id_prov \r\n" + 
        			"left join ran_rad g on a.id_monper = g.id_monper and g.id_prov = a.id_prov\r\n" + 
        			"left join sdg_target as c on d.id_target = c.id \r\n" + 
        			"left join sdg_goals as b on d.id_goals = b.id \r\n" + 
        			"left join sdg_indicator_target as e on e.id_monper = :id_monper and d.id = e.id_sdg_indicator and e.year = :year \r\n" + 
        			"left join entry_sdg as f on d.id = f.id_sdg_indicator and f.year_entry = :year and f.id_monper =:id_monper \r\n" + 
        			"left join ref_unit as h on d.unit = h.id_unit \r\n" + 
        			"right join sdg_ranrad_disaggre as i on i.id_indicator = d.id \r\n" + 
        			"left join sdg_ranrad_disaggre_detail as j on j.id_disaggre = i.id \r\n" + 
        			"left join entry_sdg_detail as k on j.id_disaggre = k.id_disaggre and j.id = k.id_disaggre_detail and k.year_entry = :year and k.id_monper = :id_monper \r\n" + 
        			"left join ref_role as l on a.id_role = l.id_role \r\n" + 
        			"where l."+role+ " "+
        			"ORDER BY CAST(kode_goals AS UNSIGNED),CAST(kode_target AS UNSIGNED),CAST(kode_indicator AS UNSIGNED),31,32";
	        query = em.createNativeQuery(sql);
	        query.setParameter("id_prov", id_prov);
	        query.setParameter("id_monper", id_monper);
	        query.setParameter("year", year);
        }
    	
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-gov/{id_prov}/{id_role}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> listEntryGov(@PathVariable("id_prov") String id_prov, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
    	String role = (id_role.equals("0"))?"":" and a.id_role = '"+id_role+"' ";
    	Query query;
    	String sql = "select b.id_program, a.id_activity, c.id_gov_indicator, b.nm_program, "
    			+ "b.nm_program_eng, a.nm_activity, a.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng, "
    			+ "f.nm_role, d.nm_unit, e.value, h.achievement1, h.achievement2, h.achievement3, h.achievement4, "
    			+ "i.achievement1 as achi1, i.achievement2 as achi2, i.achievement3 as achi3, i.achievement4 as achi4, "
    			+ "h.id, i.id as idbud, c.id as idind, a.id as idact, b.internal_code as intid_program, a.internal_code as intid_activity, c.internal_code as intid_gov_indicator "
    			+ "from gov_activity as a "
    			+ "left join gov_program b on a.id_program = b.id "
    			+ "left join gov_indicator c on a.id_program = c.id_program and a.id = c.id_activity "
    			+ "left join ref_unit d on c.unit = d.id_unit "
    			+ "left join gov_target e on e.id_gov_indicator = c.id and year = :year "
    			+ "left join ref_role f on a.id_role = f.id_role "
    			+ "left join ran_rad g on f.id_prov = g.id_prov and b.id_monper = g.id_monper "
    			+ "left join entry_gov_indicator h on h.id_assign = c.id and h.year_entry = :year and h.id_monper = g.id_monper "
    			+ "left join entry_gov_budget i on i.id_gov_activity = a.id and i.year_entry = :year and i.id_monper = g.id_monper "
    			+ "where g.id_monper = :id_monper and g.id_prov = :id_prov and c.id is not null "+role
    			+ "order by a.id_role, b.id, c.id, a.id ";
        query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
    	
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-nsa/{id_prov}/{id_role}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> listEntryNsa(@PathVariable("id_prov") String id_prov, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
    	String role = (id_role.equals("0"))?"":" and a.id_role = '"+id_role+"' ";
    	Query query;
    	String sql = "select b.id_program, a.id_activity, c.id_nsa_indicator, b.nm_program, "
    			+ "b.nm_program_eng, a.nm_activity, a.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng, "
    			+ "f.nm_role, d.nm_unit, e.value, h.achievement1, h.achievement2, h.achievement3, h.achievement4, "
    			+ "i.achievement1 as achi1, i.achievement2 as achi2, i.achievement3 as achi3, i.achievement4 as achi4, "
    			+ "h.id, i.id as idbud, c.id as idind, a.id as idact, b.internal_code as intid_program, a.internal_code as intid_activity, c.internal_code as intid_nsa_indicator "
    			+ "from nsa_activity as a "
    			+ "left join nsa_program b on a.id_program = b.id "
    			+ "left join nsa_indicator c on a.id_program = c.id_program and a.id = c.id_activity "
    			+ "left join ref_unit d on c.unit = d.id_unit "
    			+ "left join nsa_target e on e.id_nsa_indicator = c.id and year = :year "
    			+ "left join ref_role f on a.id_role = f.id_role "
    			+ "left join ran_rad g on f.id_prov = g.id_prov and b.id_monper = g.id_monper "
    			+ "left join entry_nsa_indicator h on h.id_assign = c.id and h.year_entry = :year and h.id_monper = g.id_monper "
    			+ "left join entry_nsa_budget i on i.id_nsa_activity = a.id and i.year_entry = :year and i.id_monper = g.id_monper "
    			+ "where g.id_monper = :id_monper and g.id_prov = :id_prov and c.id is not null  "+role
    			+ "order by b.id, c.id, a.id ";
        query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
    	
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-gov-bud/{id_prov}/{id_role}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> listEntryGovBud(@PathVariable("id_prov") String id_prov, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
    	String role = (id_role.equals("0"))?"":" and a.id_role = '"+id_role+"' ";
    	Query query;
    	String sql = "select b.id_program, a.id_activity, '' as id_gov_indicator, b.nm_program, "
    			+ "b.nm_program_eng, a.nm_activity, a.nm_activity_eng, '' as nm_indicator, '' as nm_indicator_eng, "
    			+ "f.nm_role, '' as nm_unit, '' as value, '' as achievement1, '' as achievement2, '' as achievement3, '' as achievement4, "
    			+ "i.achievement1 as achi1, i.achievement2 as achi2, i.achievement3 as achi3, i.achievement4 as achi4, "
    			+ "'' as id, i.id as idbud, '' as idind, a.id as idact, b.internal_code as inid_program, a.internal_code as inid_activity "
    			+ "from gov_activity as a "
    			+ "left join gov_program b on a.id_program = b.id "
    			+ "left join ref_role f on a.id_role = f.id_role "
    			+ "left join ran_rad g on f.id_prov = g.id_prov and b.id_monper = g.id_monper "
    			+ "left join entry_gov_budget i on i.id_gov_activity = a.id and i.year_entry = :year and i.id_monper = g.id_monper "
    			+ "where g.id_monper = :id_monper and g.id_prov = :id_prov "+role
    			+ "order by a.id_role, b.id, a.id ";
        query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
    	
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-nsa-bud/{id_prov}/{id_role}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> listEntryNsaBud(@PathVariable("id_prov") String id_prov, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
    	String role = (id_role.equals("0"))?"":" and a.id_role = '"+id_role+"' ";
    	Query query;
    	String sql = "select b.id_program, a.id_activity, '' as id_nsa_indicator, b.nm_program, "
    			+ "b.nm_program_eng, a.nm_activity, a.nm_activity_eng, '' as nm_indicator, '' as nm_indicator_eng, "
    			+ "f.nm_role, '' as nm_unit, '' as value, '' as achievement1, '' as achievement2, '' as achievement3, '' as achievement4, "
    			+ "i.achievement1 as achi1, i.achievement2 as achi2, i.achievement3 as achi3, i.achievement4 as achi4, "
    			+ "'' as id, i.id as idbud, '' as idind, a.id as idact, b.internal_code as intid_program, a.internal_code as intid_activity "
    			+ "from nsa_activity as a "
    			+ "left join nsa_program b on a.id_program = b.id "
    			+ "left join ref_role f on a.id_role = f.id_role "
    			+ "left join ran_rad g on f.id_prov = g.id_prov and b.id_monper = g.id_monper "
    			+ "left join entry_nsa_budget i on i.id_nsa_activity = a.id and i.year_entry = :year and i.id_monper = g.id_monper "
    			+ "where g.id_monper = :id_monper and g.id_prov = :id_prov "+role
    			+ "order by b.id, a.id ";
        query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
    	
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-sdg-report/{id_prov}/{id_role}/{id_monper}/{sdg}")
    public @ResponseBody Map<String, Object> listEntrySdgReport(@PathVariable("id_prov") String id_prov, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("sdg") String sdg) {
    	Query query;
    	String role="";
    	String sql;
    	if(id_role.equals("all")) {
			role = "";
		}else {
			role = " and n.id_role = '"+id_role+"' ";
		}
    	if(sdg.equals("0")) {
    		sql  = "select a.id_goals, a.id_target, a.id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, h.nm_unit, d.increment_decrement, \n" +
                    "b.nm_goals_eng, \n" +
                    "c.nm_target_eng, d.nm_indicator_eng, \n" +
                    "i.id_disaggre, i.nm_disaggre, i.nm_disaggre_eng, j.desc_disaggre, j.desc_disaggre_eng, i.id as iddisaggre, j.id as iddetaildis, "
                    + "'' as id_role, '' as nm_role, '' as cat_role, b.id_goals as idgol, c.id_target as idtarget, d.id_indicator as idindicator "+
                    "from nsa_map as a \n" +
                    "left join sdg_goals as b on a.id_goals = b.id \n" +
                    "left join sdg_target as c on a.id_target = c.id \n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id \n" +
                    "left join ref_unit as h on d.unit = h.id_unit \n" +
                    "left join sdg_ranrad_disaggre as i on i.id_indicator = d.id \n" +
                    "left join sdg_ranrad_disaggre_detail as j on j.id_disaggre = i.id \n" +
                    "left join nsa_indicator m on a.id_nsa_indicator = m.id "+
                    "left join nsa_activity n on m.id_activity = n.id " +
                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and (a.id_target is not null and a.id_indicator is not null) "+role+" ";
    		sql  += "union select a.id_goals, a.id_target, a.id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, h.nm_unit, d.increment_decrement, \n" +
                    "b.nm_goals_eng, \n" +
                    "c.nm_target_eng, d.nm_indicator_eng, \n" +
                    "i.id_disaggre, i.nm_disaggre, i.nm_disaggre_eng, j.desc_disaggre, j.desc_disaggre_eng, i.id as iddisaggre, j.id as iddetaildis, "
                    + "'' as id_role, '' as nm_role, '' as cat_role, b.id_goals as idgol, c.id_target as idtarget, d.id_indicator as idindicator "+
                    "from gov_map as a \n" +
                    "left join sdg_goals as b on a.id_goals = b.id \n" +
                    "left join sdg_target as c on a.id_target = c.id \n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id \n" +
                    "left join ref_unit as h on d.unit = h.id_unit \n" +
                    "left join sdg_ranrad_disaggre as i on i.id_indicator = d.id \n" +
                    "left join sdg_ranrad_disaggre_detail as j on j.id_disaggre = i.id \n" +
                    "left join gov_indicator m on a.id_gov_indicator = m.id "+
                    "left join gov_activity n on m.id_activity = n.id " +
                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and (a.id_target is not null and a.id_indicator is not null) "+role+" ";
    		sql  += "union select a.id_goals, a.id_target, a.id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, h.nm_unit, d.increment_decrement, \n" +
                    "b.nm_goals_eng, \n" +
                    "c.nm_target_eng, d.nm_indicator_eng, \n" +
                    "'0' as id_disaggre, '0' as nm_disaggre, '0' as nm_disaggre_eng, '0' as desc_disaggre, '0' as desc_disaggre_eng, '0' as iddisaggre, '0' as iddetaildis, "
                    + "'' as id_role, '' as nm_role, '' as cat_role, b.id_goals as idgol, c.id_target as idtarget, d.id_indicator as idindicator "+
                    "from nsa_map as a \n" +
                    "left join sdg_goals as b on a.id_goals = b.id \n" +
                    "left join sdg_target as c on a.id_target = c.id \n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id \n" +
                    "left join ref_unit as h on d.unit = h.id_unit \n" +
                    "left join nsa_indicator m on a.id_nsa_indicator = m.id "+
                    "left join nsa_activity n on m.id_activity = n.id " +
                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and (a.id_target is not null and a.id_indicator is not null) "+role+" ";
    		sql  += "union select a.id_goals, a.id_target, a.id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, h.nm_unit, d.increment_decrement, \n" +
                    "b.nm_goals_eng, \n" +
                    "c.nm_target_eng, d.nm_indicator_eng, \n" +
                    "'0' as id_disaggre, '0' as nm_disaggre, '0' as nm_disaggre_eng, '0' as desc_disaggre, '0' as desc_disaggre_eng, '0' as iddisaggre, '0' as iddetaildis, "
                    + "'' as id_role, '' as nm_role, '' as cat_role, b.id_goals as idgol, c.id_target as idtarget, d.id_indicator as idindicator "+
                    "from gov_map as a \n" +
                    "left join sdg_goals as b on a.id_goals = b.id \n" +
                    "left join sdg_target as c on a.id_target = c.id \n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id \n" +
                    "left join ref_unit as h on d.unit = h.id_unit \n" +
                    "left join gov_indicator m on a.id_gov_indicator = m.id "+
                    "left join gov_activity n on m.id_activity = n.id " +
                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and (a.id_target is not null and a.id_indicator is not null) "+role+" order by 1,2,3,17,18 ";
            query = em.createNativeQuery(sql);
    	}else {
    		String[] arrOfStr = sdg.split(","); 
    		StringBuffer goals = new StringBuffer();
    		StringBuffer target = new StringBuffer();
    		StringBuffer indicator = new StringBuffer();
    		if(arrOfStr.length>0) {
    			for (int i = 0; i < arrOfStr.length; i++) {
        			String[] arrOfStr1 = arrOfStr[i].split("---");
        			int cek=1;
        			for(int j=0;j<arrOfStr1.length;j++) {
        				cek = (cek==4)?1:cek;
        				System.out.println(cek);
        				if(!arrOfStr1[j].equals("0") && cek==1) {
        					goals.append("'"+arrOfStr1[j]+"',");
        				}
        				if(!arrOfStr1[j].equals("0") && cek==2) {
        					target.append("'"+arrOfStr1[j]+"',");
        				}
        				if(!arrOfStr1[j].equals("0") && cek==3) {
        					indicator.append("'"+arrOfStr1[j]+"',");
        				}
        				cek = cek+1;
        			}
        		}
    		}else{
    			String[] arrOfStr1 = sdg.split("---");
    			int cek=1;
    			for(int j=0;j<arrOfStr1.length;j++) {
    				cek = (cek==4)?1:cek;
    				System.out.println(cek);
    				if(!arrOfStr1[j].equals("0") && cek==1) {
    					goals.append("'"+arrOfStr1[j]+"',");
    				}
    				if(!arrOfStr1[j].equals("0") && cek==2) {
    					target.append("'"+arrOfStr1[j]+"',");
    				}
    				if(!arrOfStr1[j].equals("0") && cek==3) {
    					indicator.append("'"+arrOfStr1[j]+"',");
    				}
    				cek = cek+1;
    			}
    		}
    		String hasilgoals = (goals.length()==0)?"":goals.substring(0, goals.length() - 1);
    		String hasiltarget = (target.length()==0)?"":target.substring(0, target.length() - 1);
    		String hasilindicator = (indicator.length()==0)?"":indicator.substring(0, indicator.length() - 1);
    		
    		String gol = (hasilgoals.equals(""))?"":" a.id_goals in("+hasilgoals+") ";
    		String tar = (hasiltarget.equals(""))?"":" a.id_target in("+hasiltarget+") ";
    		String ind = (hasilindicator.equals(""))?"":" a.id_indicator in("+hasilindicator+") ";
    		tar = (gol.equals("") || tar.equals(""))?tar:" or "+tar;
    		ind = ((gol.equals("") && tar.equals("")) || ind.equals(""))?ind:" or "+ind;
    		sql  = "select a.id_goals, a.id_target, a.id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, h.nm_unit, d.increment_decrement, \n" +
                    "b.nm_goals_eng, \n" +
                    "c.nm_target_eng, d.nm_indicator_eng, \n" +
                    "i.id_disaggre, i.nm_disaggre, i.nm_disaggre_eng, j.desc_disaggre, j.desc_disaggre_eng, i.id as iddisaggre, j.id as iddetaildis, "
                    + "l.id_role, l.nm_role, l.cat_role, b.id_goals as idgol, c.id_target as idtarget, d.id_indicator as idindicator "+
                    "from ran_rad as g \n" +
                    "left join assign_sdg_indicator as a on a.id_prov = g.id_prov \n" +
                    "left join sdg_goals as b on a.id_goals = b.id \n" +
                    "left join sdg_target as c on a.id_target = c.id \n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id \n" +
                    "left join ref_unit as h on d.unit = h.id_unit \n" +
                    "left join sdg_ranrad_disaggre as i on i.id_indicator = d.id \n" +
                    "left join sdg_ranrad_disaggre_detail as j on j.id_disaggre = i.id \n" +
                    "left join ref_role as l on a.id_role = l.id_role \n" +
                    "where g.id_monper = '"+id_monper+"' and g.id_prov = '"+id_prov+"' "+role+" and ("+gol+" "+tar+" "+ind+")  ";
    		sql  += "union select a.id_goals, a.id_target, a.id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, h.nm_unit, d.increment_decrement, \n" +
                    "b.nm_goals_eng, \n" +
                    "c.nm_target_eng, d.nm_indicator_eng, \n" +
                    "'0' as id_disaggre, '0' as nm_disaggre, '0' as nm_disaggre_eng, '0' as desc_disaggre, '0' as desc_disaggre_eng, '0' as iddisaggre, '0' as iddetaildis, "
                    + "l.id_role, l.nm_role, l.cat_role, b.id_goals as idgol, c.id_target as idtarget, d.id_indicator as idindicator "+
                    "from ran_rad as g \n" +
                    "left join assign_sdg_indicator as a on a.id_prov = g.id_prov \n" +
                    "left join sdg_goals as b on a.id_goals = b.id \n" +
                    "left join sdg_target as c on a.id_target = c.id \n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id \n" +
                    "left join ref_unit as h on d.unit = h.id_unit \n" +
                    "left join ref_role as l on a.id_role = l.id_role \n" +
                    "where g.id_monper = '"+id_monper+"' and g.id_prov = '"+id_prov+"' "+role+" and ("+gol+" "+tar+" "+ind+") order by 1,2,3,17,18 ";
            query = em.createNativeQuery(sql);
            System.out.print(sql);
    	}
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        hasil.put("query",sql);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-sdg-isi/{id_indicator}/{year}/{id_role}/{id_disaggre}/{id_disaggre_detail}/{id_monper}")
    public @ResponseBody Map<String, Object> listEntrySdgIsi(@PathVariable("id_indicator") String id_indicator, 
    		@PathVariable("year") String year, 
    		@PathVariable("id_role") String id_role, 
    		@PathVariable("id_monper") String id_monper, 
    		@PathVariable("id_disaggre") String id_disaggre,
    		@PathVariable("id_disaggre_detail") String id_disaggre_detail) {
    	Query query;
    	String sql;
    	sql  = "select b.value, a.achievement1, a.achievement2, a.achievement3, a.achievement4, "
    			+ "a.new_value1, a.new_value2, a.new_value3, a.new_value4, "
    			+ "c.achievement1 as achi1, c.achievement2 as achi2, c.achievement3 as achi3, c.achievement4 as achi4,"
    			+ "c.new_value1 as new1, c.new_value2 as new2, c.new_value3 as new3, c.new_value4 as new4, id_disaggre, id_disaggre_detail, e.nm_role "
    			+ "from assign_sdg_indicator d "
    			+ "left join entry_sdg a on a.id_sdg_indicator = d.id_indicator and a.id_role = d.id_role and a.id_monper = d.id_monper and a.year_entry = '"+year+"' "
    			+ "left join sdg_indicator_target b on a.id_monper = b.id_monper and b.id_sdg_indicator = d.id_indicator and b.id_role = d.id_role and b.year = '"+year+"' "
    			+ "left join entry_sdg_detail c on c.id_disaggre = '"+id_disaggre+"' and c.id_disaggre_detail = '"+id_disaggre_detail+"' and c.year_entry = '"+year+"' and c.id_role = d.id_role and c.id_monper = d.id_monper "
    			+ "left join ref_role e on d.id_role = e.id_role "
                //+ "where d.id_indicator = '"+id_indicator+"' and d.id_role = '"+id_role+"' and d.id_monper = '"+id_monper+"'";
    			+ "where d.id_indicator = '"+id_indicator+"' and d.id_monper = '"+id_monper+"'";
        query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        hasil.put("query",sql);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-entry-sdg", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntrySdg(@RequestBody EntrySdg entrySdg, HttpSession session) {
    	entrySdg.setCreated_by((Integer) session.getAttribute("id_role"));
        entrySdgService.saveEntrySdg(entrySdg);
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    @PostMapping(path = "admin/save-entry-sdg-detail", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntrySdgDetail(@RequestBody EntrySdgDetail entrySdg, HttpSession session) {
    	entrySdg.setCreated_by((Integer) session.getAttribute("id_role"));
        entrySdgDetailService.saveEntrySdgDetail(entrySdg);
    }

    
    
    @GetMapping("admin/government-program-monitoring")
    public String govprogram(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
//        model.addAttribute("listprov", provinsiService.findAllProvinsi());
//        model.addAttribute("lang", session.getAttribute("bahasa"));
//        model.addAttribute("name", session.getAttribute("name"));
        
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleGov());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        return "admin/dataentry/entry_gov";
    }
    
    @GetMapping("admin/government-program-monitoring/gov/program/{id_program}/{id_prov_1}/{id_role_1}/{monper}/{tahun}/activity")
    public String gov_kegiatan(Model model, @PathVariable("id_program") Integer id_program, @PathVariable("id_prov_1") String id_prov_1, @PathVariable("id_role_1") String id_role_1, @PathVariable("monper") String monper, @PathVariable("tahun") String tahun, HttpSession session) {
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleGov());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        model.addAttribute("id_prov_1", id_prov_1);
        model.addAttribute("id_role_1", id_role_1);
        model.addAttribute("monper_1", monper);
        model.addAttribute("tahun_1", tahun);
        model.addAttribute("id_program_1", id_program);
        
    	Optional<GovProgram> govprog = govProgService.findOne(id_program);
        model.addAttribute("title", "SDG Indicators Monitoring");
        govprog.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        return "admin/dataentry/govactivity";
    }
    
    @GetMapping("admin/list-entry-gov-activity/{id_program}/{id_role}/{id_monper}/{tahun}")
    public @ResponseBody Map<String, Object> listEntryGovActivity(@PathVariable("id_program") String id_program, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("tahun") String tahun) {
        String sql  = "select a.id_activity, a.id_program, a.id_role, a.internal_code, a.nm_activity,\n" +
                    "b.id as id_entrygov, b.achievement1, b.achievement2, b.achievement3, b.achievement4, b.year_entry, b.id_monper, \n" +
                    "(select gov_prog_bud from ran_rad where id_monper = :id_monper ) as ket_ran_rad , a.nm_activity_eng, c.value as nilai_target, a.id \n" +
                    "from gov_activity as a\n" +
                    "left join (select * from entry_gov_budget where id_monper = :id_monper and year_entry = :tahun ) as b on a.id = b.id_gov_activity \n" +
                    "left join (select * from gov_target where id_role = :id_role and year = :tahun )  as c on a.id = c.id_gov_indicator\n" +
                    "where a.id_program = :id_program and a.id_role = :id_role ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_program", id_program);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("tahun", tahun);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-gov-activity_approve/{id_role}/{id_monper}/{tahun}")
    public @ResponseBody Map<String, Object> listEntryGovActivityApprove(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("tahun") String tahun) {
        String sql  = "select a.id_activity, a.id_program, a.id_role, a.internal_code, a.nm_activity,\n" +
                    "b.id as id_entrygov, b.achievement1, b.achievement2, b.achievement3, b.achievement4, b.year_entry, b.id_monper, \n" +
                    "(select gov_prog_bud from ran_rad where id_monper = :id_monper ) as ket_ran_rad , a.nm_activity_eng, c.value as nilai_target, a.id, \n" +
                    "e.nm_program, e.nm_program_eng, e.id_program as id_prog, \n" +
                    "b.new_value1, b.new_value2, b.new_value3, b.new_value4, e.internal_code as intprog, a.internal_code as intact \n" +
                    "from gov_activity as a\n" +
                    "left join (select * from gov_program ) as e on a.id_program = e.id \n" +
                    "left join (select * from entry_gov_budget where id_monper = :id_monper and year_entry = :tahun ) as b on a.id = b.id_gov_activity \n" +
                    "left join (select * from gov_target where id_role = :id_role and year = :tahun )  as c on a.id = c.id_gov_indicator\n" +
                    "where a.id_role = :id_role ";
        
        Query query = em.createNativeQuery(sql);
//        query.setParameter("id_program", id_program);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("tahun", tahun);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-entry-gov_prog", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntryGovProg(@RequestBody EntryGovBudget entryGovBudget) {
//        String id_sdg_indicator = entrySdg.getId_sdg_indicator();
//        int achievement1        = entrySdg.getAchievement1();
//        int achievement2        = entrySdg.getAchievement2();
//        int achievement3        = entrySdg.getAchievement3();
//        int achievement4        = entrySdg.getAchievement4();
//        int year_entry          = entrySdg.getYear_entry();
//        int id_role             = entrySdg.getId_role();
//        int id_monper           = entrySdg.getId_monper();
        entrySdgService.saveEntryGovBudget(entryGovBudget);
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    @PostMapping(path = "admin/save-approve-gov_prog", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveApproveGovProg(@RequestBody EntryApproval entryApproval) {
        String type             = entryApproval.getType();
        String periode          = entryApproval.getPeriode();
        int year                = entryApproval.getYear();
        int id_role             = entryApproval.getId_role();
        int id_monper           = entryApproval.getId_monper();
        if(entryApproval.getId()==null) {
            entryApproval.setApproval_date(new Date());
	}
        approvalService.deleteApproveGovBudget(id_role, id_monper, year, type, periode);
        approvalService.save(entryApproval);
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    @PostMapping(path = "admin/done-approve-gov_prog1", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void doneApproveGovProg1(@RequestBody EntryApproval entryApproval) {
//        System.out.println(entryApproval.getPeriode());
//        System.out.println(id_monper+' '+ year+" "+ type+" "+ periode);
        String type             = entryApproval.getType();
        String periode          = entryApproval.getPeriode();
        int year                = entryApproval.getYear();
//        int id_role             = entryApproval.getId_role();
        int id_monper           = entryApproval.getId_monper();
        
        EntryShowReport rp = new EntryShowReport();
//                rp.setId();
        rp.setId_monper(id_monper);
        rp.setYear(year);
        rp.setShow_report("1");
        rp.setShow_report_date(new Date());
        rp.setType(type);
        rp.setPeriod(periode);
        approvalService.updatedoneApproveGovBudget(id_monper, year, type, periode);
        approvalService.saveshow(rp);
//        approvalService.save(entryApproval);
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    @PostMapping(path = "admin/done-approve-gov_prog1_unshow", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void doneApproveGovProg1unshow(@RequestBody EntryApproval entryApproval) {
//        System.out.println(entryApproval.getPeriode());
//        System.out.println(id_monper+' '+ year+" "+ type+" "+ periode);
        String type             = entryApproval.getType();
        String periode          = entryApproval.getPeriode();
        int year                = entryApproval.getYear();
//        int id_role             = entryApproval.getId_role();
        int id_monper           = entryApproval.getId_monper();
        
        EntryShowReport rp = new EntryShowReport();
//                rp.setId();
        rp.setId_monper(id_monper);
        rp.setYear(year);
        rp.setShow_report("1");
        rp.setShow_report_date(new Date());
        rp.setType(type);
        rp.setPeriod("1");
        approvalService.updateundoneApproveGovBudget(id_monper, year, type, periode);
        approvalService.deleteshow(year, id_monper, type, periode);
//        approvalService.save(entryApproval);
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    @PostMapping(path = "admin/delete-approve-gov_prog", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void deleteApproveGovProg(@RequestBody EntryApproval entryApproval) {
        String type             = entryApproval.getType();
        String periode          = entryApproval.getPeriode();
        int year                = entryApproval.getYear();
        int id_role             = entryApproval.getId_role();
        int id_monper           = entryApproval.getId_monper();
//        if(entryApproval.getId()==null) {
//            entryApproval.setApproval_date(new Date());
//	}
        approvalService.deleteApproveGovBudget(id_role, id_monper, year, type, periode);
//        approvalService.save(entryApproval);
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    @GetMapping("admin/get-status-approve/{id_role}/{id_monper}/{year}/{type}/{periode}")
    public @ResponseBody Map<String, Object> getStatusApprove(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("year") String year, @PathVariable("type") String type, @PathVariable("periode") String periode) {
    	List list;
    	String sql  = "select approval from entry_approval as a where a.id_role = :id_role and a.id_monper = :id_monper and a.year = :year and a.type = :type and periode = :periode ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        query.setParameter("type", type);
        query.setParameter("periode", periode);
        list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/cek-show-report/{id_monper}/{year}/{type}")
    public @ResponseBody Map<String, Object> cekShowReport(@PathVariable("id_monper") String id_monper, @PathVariable("year") String year, @PathVariable("type") String type) {
    	List list;
    	String sql  = " select "
    			+ " (select count(id) from entry_show_report as a where a.id_monper = :id_monper and a.year = :year and a.type = :type and a.period = 1) as cek1, "
    	+ " (select count(id) from entry_show_report as a where a.id_monper = :id_monper and a.year = :year and a.type = :type and a.period = 2) as cek2, "
    	+ " (select count(id) from entry_show_report as a where a.id_monper = :id_monper and a.year = :year and a.type = :type and a.period = 3) as cek3, "
    	+ " (select count(id) from entry_show_report as a where a.id_monper = :id_monper and a.year = :year and a.type = :type and a.period = 4) as cek4, "
    	+ " (select count(id) from entry_show_report as a where a.id_monper = :id_monper and a.year = :year and a.type = :type and a.period = 0) as cek5 ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        query.setParameter("type", type);

        list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("cek",list);
        return hasil;
    }
    
    @GetMapping("admin/cek-show-report-problem/{id_monper}/{year}/{type}")
    public @ResponseBody Map<String, Object> cekShowReportproblem(@PathVariable("id_monper") String id_monper, @PathVariable("year") String year, @PathVariable("type") String type) {
    	List list;
    	String sql  = " select "
    			+ " (select count(id) from entry_show_report as a where a.id_monper = :id_monper and a.year = :year and a.type = :type ) as cek1 ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        query.setParameter("type", type);

        list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("cek",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-entry-gov_prog_indicator/{achiev}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Transactional
    public void saveEntryGovProgIndicator(@RequestBody EntryGovIndicator entryGovIndicator,@PathVariable("achiev") String achiev, HttpSession session) {
//        String id_sdg_indicator = entrySdg.getId_sdg_indicator();
//        int achievement1        = entrySdg.getAchievement1();
//        int achievement2        = entrySdg.getAchievement2();
//        int achievement3        = entrySdg.getAchievement3();
//        int achievement4        = entrySdg.getAchievement4();
//        int year_entry          = entrySdg.getYear_entry();
//        int id_role             = entrySdg.getId_role();
//        int id_monper           = entrySdg.getId_monper();
    	if(entryGovIndicator.getId()!=null) {
    		Optional<EntryGovIndicator> list = entrySdgService.findOneGovInd((entryGovIndicator.getId()));
        	if(list.isPresent()) {
        		list.get().setId(entryGovIndicator.getId());
            	list.get().setId_assign(entryGovIndicator.getId_assign());
            	list.get().setAchievement1(entryGovIndicator.getAchievement1());
            	list.get().setAchievement2(entryGovIndicator.getAchievement2());
            	list.get().setAchievement3(entryGovIndicator.getAchievement3());
            	list.get().setAchievement4(entryGovIndicator.getAchievement4());
            	list.get().setNew_value1(entryGovIndicator.getNew_value1());
            	list.get().setNew_value2(entryGovIndicator.getNew_value2());
            	list.get().setNew_value3(entryGovIndicator.getNew_value3());
            	list.get().setNew_value4(entryGovIndicator.getNew_value4());
            	list.get().setYear_entry(entryGovIndicator.getYear_entry());
            	list.get().setId_monper(entryGovIndicator.getId_monper());
        		list.ifPresent(foundUpdateObject ->entrySdgService.saveEntryGovIndicator(foundUpdateObject));
        		System.out.print("1");
        	}else {
        		entrySdgService.saveEntryGovIndicator(entryGovIndicator);
        		System.out.print("2");
        	}
    	}else {
    		entrySdgService.saveEntryGovIndicator(entryGovIndicator);
    		System.out.print("3");
    	}
    	
        Query query;
        if(achiev.equals("1")) {
        	query = em.createNativeQuery("update entry_gov_indicator set created_by = :created_by, date_created = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryGovIndicator.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("2")) {
    		query = em.createNativeQuery("update entry_gov_indicator set created_by2 = :created_by, date_created2 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryGovIndicator.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("3")) {
    		query = em.createNativeQuery("update entry_gov_indicator set created_by3 = :created_by, date_created3 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryGovIndicator.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("4")) {
    		query = em.createNativeQuery("update entry_gov_indicator set created_by4 = :created_by, date_created4 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryGovIndicator.getId());
	        query.executeUpdate();
    	}
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    @PostMapping(path = "admin/save-entry-gov_prog_budget/{achiev}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Transactional
    public void saveEntryGovProgBudget(@RequestBody EntryGovBudget entryGovBudget,@PathVariable("achiev") String achiev, HttpSession session) {
//        String id_sdg_indicator = entrySdg.getId_sdg_indicator();
//        int achievement1        = entrySdg.getAchievement1();
//        int achievement2        = entrySdg.getAchievement2();
//        int achievement3        = entrySdg.getAchievement3();
//        int achievement4        = entrySdg.getAchievement4();
//        int year_entry          = entrySdg.getYear_entry();
//        int id_role             = entrySdg.getId_role();
//        int id_monper           = entrySdg.getId_monper();
    	if(entryGovBudget.getId()!=null) {
    		Optional<EntryGovBudget> list = entrySdgService.findOneGovBud((entryGovBudget.getId()));
        	if(list.isPresent()) {
                list.get().setId(entryGovBudget.getId());
            	list.get().setId_gov_activity(entryGovBudget.getId_gov_activity());
            	list.get().setAchievement1(entryGovBudget.getAchievement1());
            	list.get().setAchievement2(entryGovBudget.getAchievement2());
            	list.get().setAchievement3(entryGovBudget.getAchievement3());
            	list.get().setAchievement4(entryGovBudget.getAchievement4());
            	list.get().setYear_entry(entryGovBudget.getYear_entry());
            	list.get().setId_monper(entryGovBudget.getId_monper());
                list.ifPresent(foundUpdateObject ->entrySdgService.saveEntryGovBudget(foundUpdateObject));
                System.out.print("1");
        	}else {
                entrySdgService.saveEntryGovBudget(entryGovBudget);
                System.out.print("2");
        	}
    	}else {
    		entrySdgService.saveEntryGovBudget(entryGovBudget);
    		 System.out.print("3");
    	}
    	   System.out.println("achie = "+achiev);
        Query query;
        if(achiev.equals("1")) {
        	query = em.createNativeQuery("update entry_gov_budget set created_by = :created_by, date_created = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryGovBudget.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("2")) {
    		query = em.createNativeQuery("update entry_gov_budget set created_by2 = :created_by, date_created2 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryGovBudget.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("3")) {
    		query = em.createNativeQuery("update entry_gov_budget set created_by3 = :created_by, date_created3 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryGovBudget.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("4")) {
    		query = em.createNativeQuery("update entry_gov_budget set created_by4 = :created_by, date_created4 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryGovBudget.getId());
	        query.executeUpdate();
    	}
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    @GetMapping("admin/list-cek-catrole-best/{id_role}")
    public @ResponseBody Map<String, Object> getcekcatrolebest(@PathVariable("id_role") String id_role) {
        String sql  = "select a.cat_role from ref_role as a where a.id_role = :id_role ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-sts-monper-gov/{id_monper}")
    public @ResponseBody Map<String, Object> getGetStsMonperGov(@PathVariable("id_monper") String id_monper) {
        String sql  = "select gov_prog_bud from ran_rad as a where a.id_monper = :id_monper ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-sts-monper-sdg/{id_monper}")
    public @ResponseBody Map<String, Object> getGetStsMonpersdg(@PathVariable("id_monper") String id_monper) {
        String sql  = "select sdg_indicator from ran_rad as a where a.id_monper = :id_monper ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-sts-monper-nongov/{id_monper}")
    public @ResponseBody Map<String, Object> getGetStsMonpernonGov(@PathVariable("id_monper") String id_monper) {
        String sql  = "select nsa_prog_bud from ran_rad as a where a.id_monper = :id_monper ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-sts-monper-gov_act/{id_monper}")
    public @ResponseBody Map<String, Object> getGetStsMonperGovact(@PathVariable("id_monper") String id_monper) {
        String sql  = "select gov_prog from ran_rad as a where a.id_monper = :id_monper ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-sts-monper-nongov_act/{id_monper}")
    public @ResponseBody Map<String, Object> getGetStsMonpernonGovact(@PathVariable("id_monper") String id_monper) {
        String sql  = "select nsa_prog from ran_rad as a where a.id_monper = :id_monper ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/government-program-monitoring/gov/program/{id_program}/{id_prov_1}/{id_role_1}/{monper}/{tahun}/activity/{id_activity}/indicator")
    public String gov_kegiatan_indicator(Model model, @PathVariable("id_program") Integer id_program, @PathVariable("id_prov_1") String id_prov_1, @PathVariable("id_role_1") String id_role_1, @PathVariable("monper") String monper, @PathVariable("tahun") String tahun, @PathVariable("id_activity") Integer id_activity, HttpSession session) {
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleGov());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        model.addAttribute("id_prov_1", id_prov_1);
        model.addAttribute("id_role_1", id_role_1);
        model.addAttribute("monper_1", monper);
        model.addAttribute("tahun_1", tahun);
        model.addAttribute("id_program_1", id_program);
        model.addAttribute("id_activity_1", id_activity);
        
    	Optional<GovProgram> govprog = govProgService.findOne(id_program);
    	Optional<GovActivity> govact  = govActService.findOne(id_activity);
        model.addAttribute("title", "SDG Indicators Monitoring");
        govprog.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        govact.ifPresent(foundUpdateObject -> model.addAttribute("govAct", foundUpdateObject));
        return "admin/dataentry/govindicator";
    }
    
    @GetMapping("admin/list-entry-gov-indicator/{id_program}/{id_activity}/{id_role}/{id_monper}/{tahun}")
    public @ResponseBody Map<String, Object> listEntryGovIndicator(@PathVariable("id_program") String id_program, @PathVariable("id_activity") String id_activity, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("tahun") String tahun) {
        String sql  = "select a.id_activity, a.id_program, a.id_gov_indicator, a.internal_code, a.nm_indicator,\n" +
                    "b.id as id_entrygov, b.achievement1, b.achievement2, b.achievement3, b.achievement4, b.year_entry, b.id_monper, \n" +
                    "(select gov_prog from ran_rad where id_monper = :id_monper ) as ket_ran_rad , a.nm_indicator_eng, c.value as nilai_target, a.id, d.nm_unit \n" +
                    "from gov_indicator as a\n" +
                    "left join (select * from entry_gov_indicator where id_monper = :id_monper and year_entry = :tahun ) as b on a.id = b.id_assign \n" +
                    "left join (select * from gov_target where id_role = :id_role and year = :tahun )  as c on a.id = c.id_gov_indicator\n" +
                    "left join (select * from ref_unit )  as d on a.unit = d.id_unit\n" +
//                    "where a.id_program = :id_program and a.id_activity = :id_activity and a.id_role = :id_role ";
                    "where a.id_program = :id_program and a.id_activity = :id_activity ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_program", id_program);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("tahun", tahun);
        query.setParameter("id_activity", id_activity);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-gov-indicator_approve/{id_role}/{id_monper}/{tahun}")
    public @ResponseBody Map<String, Object> listEntryGovIndicator_approve(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("tahun") String tahun) {
        String sql  = "select a.id_activity, a.id_program, a.id_gov_indicator, a.internal_code, a.nm_indicator,\n" +
                    "b.id as id_entrygov, b.achievement1, b.achievement2, b.achievement3, b.achievement4, b.year_entry, b.id_monper, \n" +
                    "(select gov_prog from ran_rad where id_monper = :id_monper ) as ket_ran_rad , a.nm_indicator_eng, c.value as nilai_target, a.id ,\n" +
                    "d.nm_activity, d.nm_activity_eng, d.id_activity as id_acty, e.nm_program, e.nm_program_eng, e.id_program as id_prog, \n" +
                    "b.new_value1, b.new_value2, b.new_value3, b.new_value4, e.internal_code as intprog, d.internal_code as intact, a.internal_code as intind \n" +
                    "from gov_indicator as a \n" +
                    "left join (select * from gov_activity where id_role = :id_role ) as d on a.id_activity = d.id \n" +
                    "left join (select * from gov_program ) as e on a.id_program = e.id \n" +
                    "left join (select * from entry_gov_indicator where id_monper = :id_monper and year_entry = :tahun ) as b on a.id = b.id_assign \n" +
                    "left join ("
                + "select z.* from gov_target z \n" +
                    "left join gov_indicator v on z.id_gov_indicator = v.id\n" +
                    "left join gov_activity m on v.id_activity = m.id\n" +
                    "where m.id_role = :id_role and z.year = :tahun "
                + ")  as c on a.id = c.id_gov_indicator \n" +
                    "where d.id_role = :id_role ";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("id_program", id_program);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("tahun", tahun);
//        query.setParameter("id_activity", id_activity);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-govProg-entry/{id_monper}/{id_role}")
    public @ResponseBody Map<String, Object> govProg(@PathVariable("id_monper") Integer id_monper, @PathVariable("id_role") Integer id_role) {
    	String sql = "select DISTINCT b.* from gov_activity a left join gov_program b on a.id_program = b.id where b.id_monper = :id_monper and a.id_role = :id_role";
        Query list = em.createNativeQuery(sql);
        list.setParameter("id_role", id_role);
        list.setParameter("id_monper", id_monper);
        List<Object[]> rows = list.getResultList();
        List<GovProgram> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
        	result.add(
        			new GovProgram(
        					(Integer)row[0], 
        					(String) row[1], 
        					(String)row[2], 
        					(String)row[3], 
        					(Integer)row[4], 
        					(String)row[5], 
        					Integer.parseInt(row[6].toString()), 
        					(Date)row[7], 
        					(Integer)row[8])
        			);
        }
        hasil.put("content",result);
        return hasil;
    }

    //non gov
    @GetMapping("admin/non-government-program-monitoring")
    public String nongovprogram(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
//        model.addAttribute("listprov", provinsiService.findAllProvinsi());
//        model.addAttribute("lang", session.getAttribute("bahasa"));
//        model.addAttribute("name", session.getAttribute("name"));
        
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleNsa());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        //return "admin/dataentry/nongovprogram";
        return "admin/dataentry/entry_nongov";
    }
    
    @GetMapping("admin/non-government-program-monitoring/gov/program/{id_program}/{id_prov_1}/{id_role_1}/{monper}/{tahun}/activity")
    public String non_gov_kegiatan(Model model, @PathVariable("id_program") Integer id_program, @PathVariable("id_prov_1") String id_prov_1, @PathVariable("id_role_1") String id_role_1, @PathVariable("monper") String monper, @PathVariable("tahun") String tahun, HttpSession session) {
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleNsa());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        model.addAttribute("id_prov_1", id_prov_1);
        model.addAttribute("id_role_1", id_role_1);
        model.addAttribute("monper_1", monper);
        model.addAttribute("tahun_1", tahun);
        model.addAttribute("id_program_1", id_program);
        
    	Optional<NsaProgram> nongovprog = nsaProgService.findOne(id_program);
        model.addAttribute("title", "SDG Indicators Monitoring");
        nongovprog.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        return "admin/dataentry/nongovactivity";
    }
    
    @GetMapping("admin/list-entry-non-gov-activity/{id_program}/{id_role}/{id_monper}/{tahun}")
    public @ResponseBody Map<String, Object> listEntryNonGovActivity(@PathVariable("id_program") String id_program, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("tahun") String tahun) {
        String sql  = "select a.id_activity, a.id_program, a.id_role, a.internal_code, a.nm_activity,\n" +
                    "b.id as id_entrynsa, b.achievement1, b.achievement2, b.achievement3, b.achievement4, b.year_entry, b.id_monper, \n" +
                    "(select nsa_prog_bud from ran_rad where id_monper = :id_monper) as ket_ran_rad, a.nm_activity_eng, c.value as nilai_target, a.id \n" +
                    "from nsa_activity as a\n" +
                    "left join (select * from entry_nsa_budget where id_monper = :id_monper and year_entry = :tahun ) as b on a.id = b.id_nsa_activity\n" +
                    "left join (select * from nsa_target where id_role = :id_role and year = :tahun )  as c on a.id = c.id_nsa_indicator \n" +
                    "where a.id_program = :id_program and a.id_role = :id_role ";
        
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_program", id_program);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("tahun", tahun);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-non-gov-activity_approve/{id_role}/{id_monper}/{tahun}")
    public @ResponseBody Map<String, Object> listEntryNonGovActivityApprove(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("tahun") String tahun) {
        String sql  = "select a.id_activity, a.id_program, a.id_role, a.internal_code, a.nm_activity,\n" +
                    "b.id as id_entrynsa, b.achievement1, b.achievement2, b.achievement3, b.achievement4, b.year_entry, b.id_monper, \n" +
                    "(select nsa_prog_bud from ran_rad where id_monper = :id_monper) as ket_ran_rad, a.nm_activity_eng, c.value as nilai_target, a.id, \n" +
                    "e.nm_program, e.nm_program_eng, e.id_program as id_prog, \n" +
                    "b.new_value1, b.new_value2, b.new_value3, b.new_value4, e.internal_code as intprog, a.internal_code as intact \n" +
                    "from nsa_activity as a\n" +
                    "left join (select * from nsa_program ) as e on a.id_program = e.id \n" +
                    "left join (select * from entry_nsa_budget where id_monper = :id_monper and year_entry = :tahun ) as b on a.id = b.id_nsa_activity\n" +
                    "left join (select * from nsa_target where id_role = :id_role and year = :tahun )  as c on a.id = c.id_nsa_indicator \n" +
                    "where a.id_role = :id_role ";
        
        
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("tahun", tahun);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-non-gov-indicator/{id_program}/{id_role}/{id_monper}/{tahun}")
    public @ResponseBody Map<String, Object> listEntryNonGovIndicator(@PathVariable("id_program") String id_program, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("tahun") String tahun) {
        String sql  = "select a.id_activity, a.id_program, a.id_role, a.internal_code, a.nm_activity,\n" +
                    "b.id as id_entrynsa, b.achievement1, b.achievement2, b.achievement3, b.achievement4, b.year_entry, b.id_monper, \n" +
                    "(select nsa_prog from ran_rad where id_monper = :id_monper) as ket_ran_rad, a.nm_activity_eng, c.value as nilai_target, a.id \n" +
                    "from nsa_activity as a\n" +
                    "left join (select * from entry_nsa_indicator where id_monper = :id_monper and year_entry = :tahun ) as b on a.id = b.id_assign\n" +
                    "left join (select * from nsa_target where id_role = :id_role and year = :tahun )  as c on a.id = c.id_nsa_indicator \n" +
                    "where a.id_program = :id_program and a.id_role = :id_role ";
        
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_program", id_program);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("tahun", tahun);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    
    @PostMapping(path = "admin/save-entry-non-gov_prog_indicator/{achiev}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Transactional
    public void saveEntryNonGovProgIndicator(@RequestBody EntryNsaIndicator entryNsaIndicator,@PathVariable("achiev") String achiev, HttpSession session) {
//        String id_sdg_indicator = entrySdg.getId_sdg_indicator();
//        int achievement1        = entrySdg.getAchievement1();
//        int achievement2        = entrySdg.getAchievement2();
//        int achievement3        = entrySdg.getAchievement3();
//        int achievement4        = entrySdg.getAchievement4();
//        int year_entry          = entrySdg.getYear_entry();
//        int id_role             = entrySdg.getId_role();
//        int id_monper           = entrySdg.getId_monper();
    	if(entryNsaIndicator.getId()!=null) {
    		Optional<EntryNsaIndicator> list = entrySdgService.findOneNsaInd((entryNsaIndicator.getId()));
        	if(list.isPresent()) {
        		list.get().setId(entryNsaIndicator.getId());
            	list.get().setId_assign(entryNsaIndicator.getId_assign());
            	list.get().setAchievement1(entryNsaIndicator.getAchievement1());
            	list.get().setAchievement2(entryNsaIndicator.getAchievement2());
            	list.get().setAchievement3(entryNsaIndicator.getAchievement3());
            	list.get().setAchievement4(entryNsaIndicator.getAchievement4());
            	list.get().setNew_value1(entryNsaIndicator.getNew_value1());
            	list.get().setNew_value2(entryNsaIndicator.getNew_value2());
            	list.get().setNew_value3(entryNsaIndicator.getNew_value3());
            	list.get().setNew_value4(entryNsaIndicator.getNew_value4());
            	list.get().setYear_entry(entryNsaIndicator.getYear_entry());
            	list.get().setId_monper(entryNsaIndicator.getId_monper());
        		list.ifPresent(foundUpdateObject ->entrySdgService.saveEntryNsaIndicator(foundUpdateObject));
        	}else {
        		entrySdgService.saveEntryNsaIndicator(entryNsaIndicator);
        	}
    	}else {
    		entrySdgService.saveEntryNsaIndicator(entryNsaIndicator);
    	}
    	
    	Query query;
        if(achiev.equals("1")) {
        	query = em.createNativeQuery("update entry_nsa_indicator set created_by = :created_by, date_created = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryNsaIndicator.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("2")) {
    		query = em.createNativeQuery("update entry_nsa_indicator set created_by2 = :created_by, date_created2 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryNsaIndicator.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("3")) {
    		query = em.createNativeQuery("update entry_nsa_indicator set created_by3 = :created_by, date_created3 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryNsaIndicator.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("4")) {
    		query = em.createNativeQuery("update entry_nsa_indicator set created_by4 = :created_by, date_created4 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryNsaIndicator.getId());
	        query.executeUpdate();
    	}
        
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    @PostMapping(path = "admin/save-entry-non-gov_prog_budget/{achiev}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Transactional
    public void saveEntryNonGovProgBudget(@RequestBody EntryNsaBudget entryNsaBudget,@PathVariable("achiev") String achiev, HttpSession session) {
//        String id_sdg_indicator = entrySdg.getId_sdg_indicator();
//        int achievement1        = entrySdg.getAchievement1();
//        int achievement2        = entrySdg.getAchievement2();
//        int achievement3        = entrySdg.getAchievement3();
//        int achievement4        = entrySdg.getAchievement4();
//        int year_entry          = entrySdg.getYear_entry();
//        int id_role             = entrySdg.getId_role();
//        int id_monper           = entrySdg.getId_monper();
    	if(entryNsaBudget.getId()!=null) {
    		Optional<EntryNsaBudget> list = entrySdgService.findOneNsaBud((entryNsaBudget.getId()));
        	if(list.isPresent()) {
                    list.get().setId(entryNsaBudget.getId());
                    list.get().setId_nsa_activity(entryNsaBudget.getId_nsa_activity());
                    list.get().setAchievement1(entryNsaBudget.getAchievement1());
                    list.get().setAchievement2(entryNsaBudget.getAchievement2());
                    list.get().setAchievement3(entryNsaBudget.getAchievement3());
                    list.get().setAchievement4(entryNsaBudget.getAchievement4());
                    list.get().setYear_entry(entryNsaBudget.getYear_entry());
                    list.get().setId_monper(entryNsaBudget.getId_monper());
                    list.ifPresent(foundUpdateObject ->entrySdgService.saveEntryNsaBudget(foundUpdateObject));
        	}else {
        		entrySdgService.saveEntryNsaBudget(entryNsaBudget);
        	}
    	}else {
    		entrySdgService.saveEntryNsaBudget(entryNsaBudget);
    	}
    	
    	Query query;
        if(achiev.equals("1")) {
        	query = em.createNativeQuery("update entry_nsa_budget set created_by = :created_by, date_created = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryNsaBudget.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("2")) {
    		query = em.createNativeQuery("update entry_nsa_budget set created_by2 = :created_by, date_created2 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryNsaBudget.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("3")) {
    		query = em.createNativeQuery("update entry_nsa_budget set created_by3 = :created_by, date_created3 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryNsaBudget.getId());
	        query.executeUpdate();
    	}else if(achiev.equals("4")) {
    		query = em.createNativeQuery("update entry_nsa_budget set created_by4 = :created_by, date_created4 = :date_created where id=:id");
			query.setParameter("created_by", (Integer) session.getAttribute("id_role"));
	        query.setParameter("date_created", new Date());
	        query.setParameter("id", entryNsaBudget.getId());
	        query.executeUpdate();
    	}
        
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    @PostMapping(path = "admin/save-entry-non-gov_prog", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntryNonGovProg(@RequestBody EntryNsaBudget entryNsaBudget) {
//        String id_sdg_indicator = entrySdg.getId_sdg_indicator();
//        int achievement1        = entrySdg.getAchievement1();
//        int achievement2        = entrySdg.getAchievement2();
//        int achievement3        = entrySdg.getAchievement3();
//        int achievement4        = entrySdg.getAchievement4();
//        int year_entry          = entrySdg.getYear_entry();
//        int id_role             = entrySdg.getId_role();
//        int id_monper           = entrySdg.getId_monper();
        entrySdgService.saveEntryNsaBudget(entryNsaBudget);
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }
    
    
    
    @GetMapping("admin/non-government-program-monitoring/gov/program/{id_program}/{id_prov_1}/{id_role_1}/{monper}/{tahun}/activity/{id_activity}/indicator")
    public String non_gov_kegiatan(Model model, @PathVariable("id_program") Integer id_program, @PathVariable("id_prov_1") String id_prov_1, @PathVariable("id_role_1") String id_role_1, @PathVariable("monper") String monper, @PathVariable("tahun") String tahun, @PathVariable("id_activity") Integer id_activity, HttpSession session) {
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleNsa());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        model.addAttribute("id_prov_1", id_prov_1);
        model.addAttribute("id_role_1", id_role_1);
        model.addAttribute("monper_1", monper);
        model.addAttribute("tahun_1", tahun);
        model.addAttribute("id_program_1", id_program);
        model.addAttribute("id_activity_1", id_activity);
        
    	Optional<NsaProgram> nongovprog = nsaProgService.findOne(id_program);
    	Optional<NsaActivity> nongovact = nsaActService.findOne(id_activity);
        model.addAttribute("title", "SDG Indicators Monitoring");
        nongovprog.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        nongovact.ifPresent(foundUpdateObject -> model.addAttribute("govAct", foundUpdateObject));
        return "admin/dataentry/nongovindicator";
    }
    
    @GetMapping("admin/list-entry-non_gov-indicator/{id_program}/{id_activity}/{id_role}/{id_monper}/{tahun}")
    public @ResponseBody Map<String, Object> listEntryNonGovIndicator(@PathVariable("id_program") String id_program, @PathVariable("id_activity") String id_activity, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("tahun") String tahun) {
        String sql  = "select a.id_activity, a.id_program, a.id_nsa_indicator, a.internal_code, a.nm_indicator,\n" +
                    "b.id as id_entrygov, b.achievement1, b.achievement2, b.achievement3, b.achievement4, b.year_entry, b.id_monper, \n" +
                    "(select nsa_prog from ran_rad where id_monper = :id_monper ) as ket_ran_rad , a.nm_indicator_eng, c.value as nilai_target, a.id, d.nm_unit \n" +
                    "from nsa_indicator as a\n" +
                    "left join (select * from entry_nsa_indicator where id_monper = :id_monper and year_entry = :tahun ) as b on a.id = b.id_assign \n" +
                    "left join (select * from nsa_target where id_role = :id_role and year = :tahun )  as c on a.id = c.id_nsa_indicator\n" +
                    "left join (select * from ref_unit )  as d on a.unit = d.id_unit\n" +
//                    "where a.id_program = :id_program and a.id_activity = :id_activity and a.id_role = :id_role ";
                    "where a.id_program = :id_program and a.id_activity = :id_activity ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_program", id_program);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("tahun", tahun);
        query.setParameter("id_activity", id_activity);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry-non_gov-indicator_approve/{id_role}/{id_monper}/{tahun}")
    public @ResponseBody Map<String, Object> listEntryNonGovIndicatorApprove(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("tahun") String tahun) {
        String sql  = "select a.id_activity, a.id_program, a.id_nsa_indicator, a.internal_code, a.nm_indicator,\n" +
                    "b.id as id_entrygov, b.achievement1, b.achievement2, b.achievement3, b.achievement4, b.year_entry, b.id_monper, \n" +
                    "(select nsa_prog from ran_rad where id_monper = :id_monper ) as ket_ran_rad , a.nm_indicator_eng, c.value as nilai_target, a.id, \n" +
                    "d.nm_activity, d.nm_activity_eng, d.id_activity as id_acty, e.nm_program, e.nm_program_eng, e.id_program as id_prog, \n" +
                    "b.new_value1, b.new_value2, b.new_value3, b.new_value4, e.internal_code as intprog, d.internal_code as intact, a.internal_code as intind \n" +
                    "from nsa_indicator as a\n" +
                    "left join (select * from nsa_activity where id_role = :id_role ) as d on a.id_activity = d.id \n" +
                    "left join (select * from nsa_program ) as e on a.id_program = e.id \n" +
                    "left join (select * from entry_nsa_indicator where id_monper = :id_monper and year_entry = :tahun ) as b on a.id = b.id_assign \n" +
                    "left join (select * from nsa_target where id_role = :id_role and year = :tahun )  as c on a.id = c.id_nsa_indicator \n" +
                    "where d.id_role = :id_role ";
        
        Query query = em.createNativeQuery(sql);
//        query.setParameter("id_program", id_program);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("tahun", tahun);
//        query.setParameter("id_activity", id_activity);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }

    
    ///
    @GetMapping("admin/government-activity-monitoring")
    public String govkegiatan(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/dataentry/govactivity";
    }

    //TARGET SDG
    @GetMapping("admin/entry/sdg-target")
    public String entri_sdg_target(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring Target");
//        model.addAttribute("listprov", provinsiService.findAllProvinsi());
//        model.addAttribute("listRole", roleService.findAll());
//        model.addAttribute("listranrad", ranRadService.findAll());
//        model.addAttribute("lang", session.getAttribute("bahasa"));
//        model.addAttribute("name", session.getAttribute("name"));
        
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleAll());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        return "admin/dataentry/entry_sdg_target";
    }
    
//    @GetMapping("admin/list-get-option-monper/{id}")
//    public @ResponseBody Map<String, Object> getOptionMonperList(@PathVariable("id") String id) {
//        String sql  = "select * from ran_rad as a where a.id_prov = :id ";
//        Query query = em.createNativeQuery(sql);
//        query.setParameter("id", id);
//        List list   = query.getResultList();
//        Map<String, Object> hasil = new HashMap<>();
//        
//        hasil.put("content",list);
//        return hasil;
//    }
    
    @GetMapping("admin/list-entry-sdg-target/{id_prov}/{id_role}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> listEntrySdgTarget(@PathVariable("id_prov") String id_prov, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
    	String role;
    	if(id_role.equals("all")) {
    		role = "";
    	}else if(id_role.equals("unassign")) {
    		role = " and a.id_role is null ";
    	}else {
    		role = " and a.id_role = '"+id_role+"'";
    	}
    	
    	String sql = "SELECT distinct b.id_goals, b.id_target, b.id, h.nm_goals, i.nm_target, b.nm_indicator, b.unit, b.increment_decrement, '' as value, "
    			+ " k.sdg_indicator, h.id_goals as kode_goals, h.nm_goals_eng, "
    			+ " i.id_target as kode_target, i.nm_target_eng, b.id_indicator as kode_indicator, b.nm_indicator_eng, j.nm_unit, "
    			+ " (select group_concat(concat(value,'---',year)) from sdg_indicator_target where id_sdg_indicator = b.id and year between k.start_year and k.end_year and id_monper = k.id_monper) as target,l.baseline, CASE when g.nm_role is null then 'Unassigned' else g.nm_role end, g.id_role "
    			+ " FROM sdg_indicator b "
    			+ " left join assign_sdg_indicator a on b.id = a.id_indicator and a.id_prov = :id_prov "
    			+ " left join ref_unit c on b.unit = c.id_unit "
    			+ " left join sdg_funding d on b.id = d.id_sdg_indicator and d.id_monper = :id_monper "
    			+ " left join ref_role g on a.id_role = g.id_role "
    			+ " left join sdg_goals as h on b.id_goals = h.id "
                + " left join sdg_target as i on b.id_target = i.id "
                + " left join ref_unit as j on b.unit = j.id_unit "
                + " left join ran_rad as k on k.id_monper = :id_monper "
                +"  left join sdg_funding as l on b.id = l.id_sdg_indicator and l.id_monper = k.id_monper "
    			+ " WHERE 1=1 "+role+" order by CAST(h.id_goals AS UNSIGNED),CAST(i.id_target AS UNSIGNED),CAST(b.id_indicator AS UNSIGNED)";
    	Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_monper", id_monper);
        System.out.println(id_prov+" "+id_monper+" "+year);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/get-sdgTargetIndicator/{id_indicator}/{id_role}/{year}/{id_monper}")
    public @ResponseBody Map<String, Object> getSdgTarget(@PathVariable("id_monper") String id_monper, @PathVariable("id_indicator") String id_indicator, @PathVariable("id_role") String id_role, @PathVariable("year") String year) {
        String sql  = "select id_sdg_indicator, id_role, year, value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_role = :id_role and year = :year";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        query.setParameter("id_indicator", id_indicator);
        query.setParameter("year", year);
        query.setParameter("id_monper", id_monper);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-sdgTargetIndicator/{id_indicator}/{id_role}/{id_monper}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	@Transactional
	public void saveSdgTarget(@RequestBody Map<String, Object> payload,@PathVariable("id_indicator") Integer id_indicator,@PathVariable("id_role") String id_role,@PathVariable("id_monper") String id_monper) {
    	JSONObject jsonObject = new JSONObject(payload);
        JSONObject catatan = jsonObject.getJSONObject("target");
        JSONArray c = catatan.getJSONArray("target");
        String role = id_role.equals("unassign")?null:"'"+id_role+"'";
        for (int i = 0 ; i < c.length(); i++) {
        	JSONObject obj = c.getJSONObject(i);
        	String year = obj.getString("year");
        	String value = obj.getString("nilai");
        	em.createNativeQuery("delete from sdg_indicator_target where id_sdg_indicator ='"+id_indicator+"' and year = '"+year+"' and id_monper = '"+id_monper+"'").executeUpdate();
        	if(!value.equals("")) {
        		em.createNativeQuery("INSERT INTO sdg_indicator_target (id_sdg_indicator,id_role,year,value,id_monper) values ('"+id_indicator+"',"+role+",'"+year+"','"+value+"','"+id_monper+"')").executeUpdate();
        	}
        }
    }
    
    @PostMapping(path = "admin/save-sdgFunding", consumes = "application/json", produces = "application/json")
	@ResponseBody
	@Transactional
	public void saveSdgTarget(@RequestBody Map<String, Object> payload) {
        JSONObject jsonObunit = new JSONObject(payload);
        String id_sdg_indicator = jsonObunit.get("id_sdg_indicator").toString();  
        String baseline = jsonObunit.get("baseline").toString();
        String funding_source = jsonObunit.get("funding_source").toString();
        String id_monper = jsonObunit.get("id_monper").toString();
        em.createNativeQuery("delete from sdg_funding where id_sdg_indicator ='"+id_sdg_indicator+"' and id_monper = '"+id_monper+"'").executeUpdate();
        em.createNativeQuery("INSERT INTO sdg_funding (id_sdg_indicator,baseline,funding_source,id_monper) values ('"+id_sdg_indicator+"','"+baseline+"','"+funding_source+"','"+id_monper+"')").executeUpdate();
    }
    
    @GetMapping("admin/get-sdgFunding/{id_indicator}/{id_monper}")
    public @ResponseBody Map<String, Object> getSdgTarget(@PathVariable("id_indicator") String id_indicator,@PathVariable("id_monper") String id_monper) {
        String sql  = "select baseline, funding_source from sdg_funding where id_sdg_indicator = :id_indicator and id_monper = :id_monper";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_indicator", id_indicator);
        query.setParameter("id_monper", id_monper);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
    @GetMapping("admin/entry/sdg-target/input/{id_indicator}/{id_prov_1}/{id_role_1}/{monper}/{tahun}")
    public String InputTargetSdg(Model model, @PathVariable("id_indicator") Integer id_indicator, @PathVariable("id_prov_1") String id_prov_1, @PathVariable("id_role_1") String id_role_1, @PathVariable("monper") String monper, @PathVariable("tahun") String tahun, HttpSession session) {
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleAll());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        model.addAttribute("id_prov_1", id_prov_1);
        model.addAttribute("id_role_1", id_role_1);
        model.addAttribute("monper_1", monper);
        model.addAttribute("tahun_1", tahun);
        model.addAttribute("id_indicator_1", id_indicator);
        
    	Optional<SdgIndicator> sdgIndicator = sdgIndicatorService.findOne(id_indicator);
    	Optional<SdgGoals> listgol = sdgGoalsService.findOne(Integer.parseInt(sdgIndicator.get().getId_goals()));
    	Optional<SdgTarget> list1 = sdgTargetService.findOne(Integer.parseInt(sdgIndicator.get().getId_target()));
        model.addAttribute("title", "SDG Indicators Monitoring");
        sdgIndicator.ifPresent(foundUpdateObject -> model.addAttribute("sdgInd", foundUpdateObject));
        listgol.ifPresent(foundUpdateObject -> model.addAttribute("goals", foundUpdateObject));
        list1.ifPresent(foundUpdate -> model.addAttribute("target", foundUpdate));
        return "admin/dataentry/entry_sdg_target_input";
    }
    
    @GetMapping("admin/list-entry-sdg-target-input/{id_indicator}/{id_role}")
    public @ResponseBody Map<String, Object> listEntrySdgTargetInput(@PathVariable("id_indicator") String id_indicator, @PathVariable("id_role") String id_role) {
        String sql  = "select id, id_sdg_indicator, id_role, year, value from sdg_indicator_target where id_role = :id_role and id_sdg_indicator = :id_indicator ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_indicator", id_indicator);
        query.setParameter("id_role", id_role);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/cek-tahun-target-sdg/{year}/{id_indicator}")
    public @ResponseBody Map<String, Object> cekTahunTargetSdg(@PathVariable("year") String year, @PathVariable("id_indicator") String id_indicator) {
        String sql  = "select id, id_sdg_indicator, id_role, year, value from sdg_indicator_target where year = :year and id_sdg_indicator = :id_indicator limit 1";
        Query query = em.createNativeQuery(sql);
        query.setParameter("year", year);
        query.setParameter("id_indicator", id_indicator);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-entry-sdg-indicator-target", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntrySdgIndicatorTarget(@RequestBody SdgIndicatorTarget sdgIndicatorTarget) {
        entrySdgService.saveSdgIndicatorTargetEntry(sdgIndicatorTarget);
    }
    
//    @GetMapping("admin/delete-entry-sdg-indicator-target/{id}")
//    public void saveEntrySdgIndicatorTarget(@PathVariable("id") int id) {
//        entrySdgService.deleteSdgIndicatorTargetEntry(id);
//    }
    
    @DeleteMapping("admin/delete-entry-sdg-indicator-target/{id}")
    @ResponseBody
    public void saveEntrySdgIndicatorTarget(@PathVariable("id") int id) {
        entrySdgService.deleteSdgIndicatorTargetEntry(id);
    }
    
    
    
    //funding SDG
    @GetMapping("admin/entry-funding/sdg-goals")
    public String goals(Model model, HttpSession session) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        String bhs = (String) session.getAttribute("bahasa");
        if (bhs == null) {bhs = "0";}
        model.addAttribute("lang", bhs);
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/dataentry/funding-sdg/goals";
    }
    
    @GetMapping("admin/entry-funding/sdg-goals/{id}/target")
    public String target(Model model, @PathVariable("id") int id, HttpSession session) {
        Optional<SdgGoals> list = sdgGoalsService.findOne(id);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("content", foundUpdateObject));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/dataentry/funding-sdg/target";
    }
    
    @GetMapping("admin/entry-funding/sdg-goals/{id}/target/{id_target}/indicator")
//    public String sdg(Model model, @PathVariable("id") int id, @PathVariable("id_target") int id_target, HttpSession session) {
    public String sdg(Model model, @PathVariable("id") int id, @PathVariable("id_target") Integer id_target, HttpSession session) {
    	Optional<SdgGoals> list = sdgGoalsService.findOne(id);
    	Optional<SdgTarget> list1 = sdgTargetService.findOne(id_target);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("goals", foundUpdateObject));
        list1.ifPresent(foundUpdate -> model.addAttribute("target", foundUpdate));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("unit", unitService.findAll());
        return "admin/dataentry/funding-sdg/sdgs_indicator";
    }
    
    @GetMapping("admin/entry-funding/sdg-goals/{id}/target/{id_target}/indicator/{id_indicator}/funding")
    public String disagre(Model model, @PathVariable("id") int id, @PathVariable("id_target") int id_target, @PathVariable("id_indicator") int id_indicator, HttpSession session) {
//    public String disagre(Model model, @PathVariable("id") int id, @PathVariable("id_target") Integer id_target, @PathVariable("id_indicator") Integer id_indicator, HttpSession session) {
    	Optional<SdgGoals> list = sdgGoalsService.findOne(id);
    	Optional<SdgTarget> list1 = sdgTargetService.findOne(id_target);
    	Optional<SdgIndicator> list2 = sdgIndicatorService.findOne(id_indicator);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("goals", foundUpdateObject));
        list1.ifPresent(foundUpdate -> model.addAttribute("target", foundUpdate));
        list2.ifPresent(foundUpdate1 -> model.addAttribute("indicator", foundUpdate1));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        
        
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleAll());
        Optional<Role> list3 = roleService.findOne(id_role);
    	String id_prov      = list3.get().getId_prov();
    	String privilege    = list3.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list4 = provinsiService.findOne(id_prov);
            list4.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        return "admin/dataentry/funding-sdg/funding";
    }
    
    @GetMapping("admin/list-funding/sdg-goals/{id_indicator}/{id_monper}")
    public @ResponseBody Map<String, Object> sdgDisaggreList(@PathVariable("id_indicator") Integer id_indicator, @PathVariable("id_monper") Integer id_monper) {
        List<SdgFunding> list = sdgFundingService.findAll(id_indicator, id_monper);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }

    @PostMapping(path = "admin/save-entry-funding-sdg", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntryFundingSdg(@RequestBody SdgFunding sdgFunding) {
        sdgFundingService.saveSdgFunding(sdgFunding);
    }
    
    @DeleteMapping("admin/delete-entry-funding-sdg/{id}")
    @ResponseBody
    public void saveEntryFundingSdg(@PathVariable("id") int id) {
        sdgFundingService.deleteSdgFunding(id);
    }
    
    @GetMapping("admin/home-entry/gri-ojk")
    public String listgriojk(Model model, HttpSession session) {
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
    	String sql = "select distinct company_name from entry_gri_ojk";
    	Query query = em.createNativeQuery(sql);
        List listcompany   = query.getResultList();
    	
        model.addAttribute("lang", session.getAttribute("bahasa"));
		model.addAttribute("name", session.getAttribute("name"));
		model.addAttribute("id_prov", id_prov);
		model.addAttribute("privilege", privilege);
		model.addAttribute("id_role", id_role);
		model.addAttribute("company_name", listcompany);
        return "admin/dataentry/gri_ojk";
        
    }
    @GetMapping("admin/list-entry/gri-ojk")
    public @ResponseBody Map<String, Object> units(HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> listRole = roleService.findOne(id_role);
    	String privilege = listRole.get().getPrivilege();
    	String id_prov = listRole.get().getId_prov();
    	String sql;
//    	if(privilege.equals("SUPER")) {
    		sql = "select * from entry_gri_ojk";
//    	}else {
//    		sql = "select a.* from ref_unit a left join ref_role b on a.id_role = b.id_role where b.id_prov = '"+id_prov+"' or a.id_role = 1";
//    	}
        
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry/gri-ojk-list/{year}/{comp}")
    public @ResponseBody Map<String, Object> Gri(@PathVariable("comp") String comp,@PathVariable("year") String year) {
    	String tahun = year.equals("all")?"":" and year = '"+year+"'";
    	String komp = comp.equals("all")?"":" and company_name = '"+comp+"'";
    	String sql = "select * from entry_gri_ojk where 1=1 "+tahun+" "+komp;
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-entry/gri-ojk/{year}")
    public @ResponseBody Map<String, Object> gri(HttpSession session,@PathVariable("year") String year) {
    	String tahun = year.equals("all")?"":" where year = '"+year+"'";
    	String sql = "select * from entry_gri_ojk "+tahun;
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-year-ojk")
    public @ResponseBody Map<String, Object> yearOjk() {
        String sql  = "select distinct year from entry_gri_ojk order by year";
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-entry/gri-ojk"/*, consumes = "application/json", produces = "application/json"*/)
	@ResponseBody
        @Transactional
	public ResponseEntity<?> saveGriOjk(/*@RequestBody Map<String, Object> payload
                                            ,*/HttpSession session
                                            ,@RequestParam("id") String id
                                            ,@RequestParam("company_name") String company_name
                                            ,@RequestParam("year") String year
                                            ,@RequestParam("files") MultipartFile[] uploadfiles, @RequestParam("file_pdf") MultipartFile file_pdf) throws IOException {
        Integer id_role = (Integer) session.getAttribute("id_role");
//        JSONObject jsonObunit = new JSONObject(payload); 
//        System.out.println(company_name);
//        String company_name      = jsonObunit.get("company_name").toString(); 
//        String year              = jsonObunit.get("year").toString(); 
//        String id                = jsonObunit.get("id").toString();
            EntryGriojk gri = new EntryGriojk();
            if(id.equals("")){
                UUID uuid1 = UUID.randomUUID();
                UUID uuid2 = UUID.randomUUID();
                String file1 =  company_name.toLowerCase()+"-"+year+".xls";
                String file2 =  uuid2.toString() + ".xls";
                gri.setCompany_name(company_name);
                int tahun = Integer.valueOf(year);
                gri.setYear(tahun);
                gri.setFile1(file1);
                gri.setFile2(file2);
                gri.setFile3(IOUtils.toByteArray(file_pdf.getInputStream()));
                gri.setDescription("");
                gri.setApproval(1);
                entryGriOjkService.saveEntryGriOjk(gri);
//                 UUID uuid1 = UUID.randomUUID();
//                 UUID uuid2 = UUID.randomUUID();
//                 String file1 =  company_name.toLowerCase()+"-"+year+".xls";
//                 String file2 =  uuid2.toString() + ".xls";
//                em.createNativeQuery("INSERT INTO entry_gri_ojk (company_name,year,file1,file2) values ('"+company_name+"','"+year+"','"+file1+"','"+file2+"')").executeUpdate();
                String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
                if (!StringUtils.isEmpty(uploadedFileName)) {
                    try {

                            saveUploadedFiles(Arrays.asList(uploadfiles),file1,file2,year,company_name);

                        } catch (IOException e) {
//                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                    
                }
            }else{
                em.createNativeQuery("UPDATE ref_unit set nm_unit = '"+company_name+"' where id_unit ='"+year+"'").executeUpdate();
            }
        return new ResponseEntity("Successfully uploaded - "
                + "test", HttpStatus.OK);
	}
        
    @GetMapping("admin/get-griojk/{id}")
    public @ResponseBody Map<String, Object> getGriojkval(@PathVariable("id") Integer id){
    	Optional<EntryGriojk> list = entryGriOjkService.findOne(id);
    	Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
        
        private void saveUploadedFiles(List<MultipartFile> files,String file1,String file2,String year,String company) throws IOException {
            int i = 1;
            for (MultipartFile file : files) {
                
                if (!file.isEmpty()) {
                    byte[] bytes = file.getBytes();
                    String uploadpath = System.getProperty("user.home");
                    String UPLOADED_FOLDER = "C://wa//";
                    String rename="";
                    if(i==1) {
                            rename = file1;
                    }else if(i==2){
                            rename = file2;
                    }
                    
                    File uploadhome = new File(System.getProperty("user.home")+"/upload");
                    if (!uploadhome.exists()) {
                        if (uploadhome.mkdir()) {
                            System.out.println("Directory is created!");
                        } else {
                            System.out.println("Failed to create directory!");
                        }
                    }
                    System.out.println(uploadpath);
                    System.out.println(uploadhome);
                    String fileExtension=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                    String fileName = StringUtils.cleanPath(rename).toLowerCase();
                    Path path = Paths.get(uploadhome +"/"+ fileName);
                    Files.write(path, bytes);
                    
                    String vImportExcell = uploadpath +"/"+ fileName;
                    importExcell(vImportExcell,year,company);
                }
                
                 i++;
            }

        }
        

    public  void importExcell(String path,String year,String company) throws FileNotFoundException, IOException {
        System.out.println("kesini lho");
       FileInputStream fis = new FileInputStream(path);
       DataFormatter formatter = new DataFormatter();
       Workbook wb = new HSSFWorkbook(fis);
       Sheet sheet1 = wb.getSheetAt(0);
       int i=0;
        for (Row row : sheet1) {
                String Kode = formatter.formatCellValue(wb.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("B")));
                String value = formatter.formatCellValue(wb.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("I")));
                if(!value.equals("VALUE")&&!value.equals("")){
                    String filename= company.toLowerCase()+"-"+year+".xls";
                   em.createNativeQuery("INSERT INTO trx_excell (year,kode,company_name,value,name_file) values ('"+year+"','"+Kode+"','"+company+"','"+value+"','"+filename+"')").executeUpdate(); 
//                   System.out.println(year+"=>"+company+"=>"+Kode+"=>"+value); 
                }
            i++;
        }
    }
        
        
        
       @DeleteMapping("admin/delete-entry/gri-ojk/{id}")
        @ResponseBody
        @Transactional
        public void deleteGriOjk(@PathVariable("id") Integer id) {
            em.createNativeQuery("delete from entry_gri_ojk where id ='" + id + "'").executeUpdate();
        }
        
        @RequestMapping(path = "/admin/export-entry/gri-ojk/{name}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFile(@PathVariable("name") String name, HttpServletResponse response,HttpSession session) throws FileNotFoundException {
    
        String path = System.getProperty("user.home")+"/upload";
        File f = new File (path+"/"+name);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(f));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+name);
//         headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+file);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
           return ResponseEntity.ok()
                .headers(headers)
                .contentLength(f.length())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
        }
        
}
