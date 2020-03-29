package com.jica.sdg.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jica.sdg.model.EntryApproval;
import com.jica.sdg.model.EntryGriojk;
import com.jica.sdg.model.EntryShowReport;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.Unit;
import com.jica.sdg.service.IEntryApprovalService;
import com.jica.sdg.service.IEntrySdgDetailService;
import com.jica.sdg.service.IEntrySdgService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.NsaProfileService;
import java.util.ArrayList;

@Controller
public class ApprovalController {
	@Autowired
    IEntryApprovalService approvalService;
	
	@Autowired
    private EntityManager em;
	
	@Autowired
    NsaProfileService nsaProfilrService;
	
	@Autowired
    IRoleService roleService;
	
	@Autowired
    IProvinsiService provinsiService;
	
	@Autowired
	IEntrySdgService sdgService;
	
	@Autowired
	IEntrySdgDetailService sdgDetailService;
	
	
	@PostMapping(path = "admin/save-approval", consumes = "application/json", produces = "application/json")
	@ResponseBody
	@Transactional
	public void saveApproval(@RequestBody EntryApproval app) {
		if(app.getId()==null) {
			app.setApproval_date(new Date());
		}

		Query query;
		if(app.getType().equals("entry_sdg")) {
			query = em.createNativeQuery("update entry_sdg set new_value1 = null, new_value2 = null, new_value3 = null, new_value4 = null where year_entry=:year and id_monper=:id_monper and id_role = :id_role");
			query.setParameter("year", app.getYear());
	        query.setParameter("id_monper", app.getId_monper());
	        query.setParameter("id_role", app.getId_role());
	        query.executeUpdate();
	        
	        query = em.createNativeQuery("update entry_sdg_detail set new_value1 = null, new_value2 = null, new_value3 = null, new_value4 = null where year_entry=:year and id_monper=:id_monper and id_role = :id_role");
			query.setParameter("year", app.getYear());
	        query.setParameter("id_monper", app.getId_monper());
	        query.setParameter("id_role", app.getId_role());
	        query.executeUpdate();
		}else if(app.getType().equals("entry_gov_budget")) {
			query = em.createNativeQuery("update entry_gov_budget set new_value1 = null, new_value2 = null, new_value3 = null, new_value4 = null where year_entry=:year and id_monper=:id_monper");
			query.setParameter("year", app.getYear());
	        query.setParameter("id_monper", app.getId_monper());
	        query.executeUpdate();
		}else if(app.getType().equals("entry_gov_indicator")) {
			query = em.createNativeQuery("update entry_gov_indicator set new_value1 = null, new_value2 = null, new_value3 = null, new_value4 = null where year_entry=:year and id_monper=:id_monper");
			query.setParameter("year", app.getYear());
	        query.setParameter("id_monper", app.getId_monper());
	        query.executeUpdate();
		}else if(app.getType().equals("entry_nsa_budget")) {
			query = em.createNativeQuery("update entry_nsa_budget set new_value1 = null, new_value2 = null, new_value3 = null, new_value4 = null where year_entry=:year and id_monper=:id_monper");
			query.setParameter("year", app.getYear());
	        query.setParameter("id_monper", app.getId_monper());
	        query.executeUpdate();
		}else if(app.getType().equals("entry_nsa_indicator")) {
			query = em.createNativeQuery("update entry_nsa_indicator set new_value1 = null, new_value2 = null, new_value3 = null, new_value4 = null where year_entry=:year and id_monper=:id_monper");
			query.setParameter("year", app.getYear());
	        query.setParameter("id_monper", app.getId_monper());
	        query.executeUpdate();
		}
		
		approvalService.deleteApproveGovBudget(app.getId_role(), app.getId_monper(), app.getYear(), app.getType(), app.getPeriode());
		approvalService.save(app);
	}
	
    @GetMapping("admin/get-approve/{type}/{year}/{id_monper}/{periode}/{id_role}")
    public @ResponseBody Map<String, Object> getApprove(@PathVariable("type") String type, @PathVariable("year") Integer year, @PathVariable("id_monper") Integer id_monper, @PathVariable("periode") Integer periode, @PathVariable("id_role") Integer id_role) {
        String sql = "select id, approval from entry_approval where type=:type and year=:year and id_monper=:id_monper and periode = :periode and id_role = :id_role";
        Query query = em.createNativeQuery(sql);
        query.setParameter("year", year);
        query.setParameter("id_monper", id_monper);
        query.setParameter("type", type);
        query.setParameter("periode", periode);
        query.setParameter("id_role", id_role);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/unapply/{type}/{year}/{id_monper}/{periode}/{id_role}")
    @ResponseBody    
    @Transactional
    public void deleteUnit(@PathVariable("type") String type, @PathVariable("year") Integer year, @PathVariable("id_monper") Integer id_monper, @PathVariable("periode") Integer periode, @PathVariable("id_role") Integer id_role) {
    	Query query = em.createNativeQuery("delete from entry_approval where type=:type and year=:year and id_monper=:id_monper and periode = :periode and id_role = :id_role");
    	query.setParameter("year", year);
        query.setParameter("id_monper", id_monper);
        query.setParameter("type", type);
        query.setParameter("periode", periode);
        query.setParameter("id_role", id_role);
        query.executeUpdate();
    } 
	
	@GetMapping("admin/data-approval/sdg-indicator-monitoring")
    public String entri_sdg(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        
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
        return "admin/approval/entry_sdg";
    }
	
    @GetMapping("admin/list-role-approval/{id_monper}/{year}/{type}/{period}")
    public @ResponseBody Map<String, Object> listRoleApproval(@PathVariable("id_monper") String id_monper, @PathVariable("year") String year, @PathVariable("type") String type, @PathVariable("period") String period) {
        String sql = "select a.id, a.id_role, b.nm_role, a.approval, a.id_monper, a.description, a.periode from entry_approval a left join ref_role b on a.id_role = b.id_role where a.id_monper=:id_monper and a.year=:year and a.type=:type and a.periode=:period order by a.approval ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        query.setParameter("type", type);
        query.setParameter("period", period);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
    @GetMapping("admin/list-get-cek-show-report/{id_monper}/{year}/{sts}/{type}/{period}")
    public @ResponseBody Map<String, Object> listcekshowreport(@PathVariable("id_monper") String id_monper, @PathVariable("year") String year, @PathVariable("sts") String sts, @PathVariable("type") String type, @PathVariable("period") String period) {
        String sql = "select count(*) as totalcek from entry_show_report where id_monper = :id_monper and year = :year and show_report = :sts and type = :type and period = :period";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        query.setParameter("sts", sts);
        query.setParameter("type", type);
        query.setParameter("period", period);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
	@PostMapping(path = "admin/update-approval", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void updateApproval(@RequestBody Map<String, Object> recive) {
		String approval = recive.get("approval").toString();
		String description = recive.get("description").toString();
		Integer id = Integer.parseInt(recive.get("id").toString());
		approvalService.updateApproval(approval, description, id);
	}
	
	@PostMapping(path = "admin/show-report-sdg", consumes = "application/json", produces = "application/json")
	@ResponseBody
	@Transactional
	public void showReport(@RequestBody Map<String, Object> payload) {
		JSONObject jsonObunit = new JSONObject(payload);
        String id_monper = jsonObunit.get("id_monper").toString();  
        String tahun = jsonObunit.get("tahun").toString();
        String period = jsonObunit.get("period").toString();
        Query query = em.createNativeQuery("UPDATE entry_approval set approval = '4' where type='entry_sdg' and id_monper = :id_monper and year = :year and periode = :periode");
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", tahun);
        query.setParameter("periode", period);
        query.executeUpdate();
        
        EntryShowReport rp = new EntryShowReport();
        rp.setId_monper(Integer.parseInt(id_monper));
        rp.setYear(Integer.parseInt(tahun));
        rp.setShow_report("1");
        rp.setShow_report_date(new Date());
        rp.setType("entry_sdg");
        rp.setPeriod(period);
        approvalService.saveshow(rp);
	}

	@PostMapping(path = "admin/update-new-sdg", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void updateNewSdg(@RequestBody Map<String, Object> payload) {
		JSONObject jsonObject = new JSONObject(payload);
        JSONObject catatan = jsonObject.getJSONObject("sdg");
        JSONArray c = catatan.getJSONArray("sdg");
        for (int i = 0 ; i < c.length(); i++) {
        	JSONObject obj = c.getJSONObject(i);
        	String 	nilai = obj.getString("new_val_nilai");
        	String 	id = obj.getString("new_val_id");
        	String 	period = obj.getString("new_val_period");
        	String 	id_disaggre = obj.getString("new_val_id_disaggre");
        	if(id_disaggre.equals("null") || id_disaggre.equals("")) {
        		if(!id.equals("null") && !id.equals("")) {
        			if(period.equals("1")) {
            			sdgService.updateNew1(Integer.parseInt(id), (nilai.equals("")?null:Integer.parseInt(nilai)));
            		}else if(period.equals("2")) {
            			sdgService.updateNew2(Integer.parseInt(id), (nilai.equals("")?null:Integer.parseInt(nilai)));
            		}else if(period.equals("3")) {
            			sdgService.updateNew3(Integer.parseInt(id), (nilai.equals("")?null:Integer.parseInt(nilai)));
            		}else if(period.equals("4")) {
            			sdgService.updateNew4(Integer.parseInt(id), (nilai.equals("")?null:Integer.parseInt(nilai)));
            		}
        		}
    		}else {
    			if(period.equals("1")) {
        			sdgDetailService.updateNew1(Integer.parseInt(id_disaggre), (nilai.equals("")?null:Integer.parseInt(nilai)));
        		}else if(period.equals("2")) {
        			sdgDetailService.updateNew2(Integer.parseInt(id_disaggre), (nilai.equals("")?null:Integer.parseInt(nilai)));
        		}else if(period.equals("3")) {
        			sdgDetailService.updateNew3(Integer.parseInt(id_disaggre), (nilai.equals("")?null:Integer.parseInt(nilai)));
        		}else if(period.equals("4")) {
        			sdgDetailService.updateNew4(Integer.parseInt(id_disaggre), (nilai.equals("")?null:Integer.parseInt(nilai)));
        		}
    		}
        }
	}
        
        
    // approval gov
    @GetMapping("admin/data-approval/gov-program-monitoring")
    public String entry_gov(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        
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
        return "admin/approval/entry_gov";
    }
    
    
    
    
    // approval nsa
    @GetMapping("admin/data-approval/nongov-program-monitoring")
    public String entry_nongov(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        
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
        return "admin/approval/entry_nsa";
    }
    @GetMapping("admin/home-approval/gri-ojk")
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
        model.addAttribute("lang", session.getAttribute("bahasa"));
		model.addAttribute("name", session.getAttribute("name"));
		model.addAttribute("id_prov", id_prov);
		model.addAttribute("privilege", privilege);
		model.addAttribute("id_role", id_role);
        return "admin/approval/gri_ojk";
        
    }
    @GetMapping("admin/get-approval/gri-ojk/{id}")
    public @ResponseBody Map<String, Object> getUnit(@PathVariable("id") Integer id) {
        String sql = "select * from entry_gri_ojk where id = '"+id+"'";
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<EntryGriojk> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(
                        new EntryGriojk((Integer)row[0], (String) row[1],(Integer)row[2], (String) row[3], (String) row[4],(String)row[5], (Integer)row[6])
            );
        }
        hasil.put("content",result);
        return hasil;
    }
    
    @GetMapping("admin/set-unUprove/gri-ojk/{id}")
    @Transactional
    public @ResponseBody Map<String, Object> setUnUprove(@PathVariable("id") Integer id) {
        String sql = "Update entry_gri_ojk set approval = NULL where id  = '"+id+"'";
        em.createNativeQuery(sql).executeUpdate();        
        return null;
    }
    
    @GetMapping("admin/set-approve-all/gri-ojk/")
    @Transactional
    public @ResponseBody Map<String, Object> setUnUprove() {
        String sql = "Update entry_gri_ojk set approval = 2 where approval IS NULL ";
        em.createNativeQuery(sql).executeUpdate();        
        return null;
    }
    
    
    @PostMapping(path = "admin/save-approval/gri-ojk", consumes = "application/json", produces = "application/json")
	@ResponseBody
        @Transactional
	public void saveUnit(@RequestBody Map<String, Object> payload,HttpSession session) {
        Integer id_role = (Integer) session.getAttribute("id_role");
        JSONObject jsonObapproval = new JSONObject(payload);
        String description           = jsonObapproval.get("description").toString();  
        String approval              = jsonObapproval.get("approval").toString();  
        String id                    = jsonObapproval.get("id").toString();
           em.createNativeQuery("UPDATE entry_gri_ojk set description = '"+description+"',approval = '"+approval+"' where id ='"+id+"'").executeUpdate();
        
	}
        
        
    // approval best practice
    @GetMapping("admin/data-approval/best-practice")
    public String entry_best_practice(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        
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
        return "admin/approval/best_practice";
    }
    
    @GetMapping("admin/data-approval/problem-identify")
    public String entry_problem_identify(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        
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
        Query query3 = em.createNativeQuery("SELECT DISTINCT a.id,a.nm_goals AS nm,LPAD(a.id,3,'0') AS id_parent,'1' AS LEVEL ,a.id_goals AS id_text ,'#' AS id_parent2 FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                                                "	UNION \n" +
                                                "	SELECT DISTINCT  CONCAT(a.id,'.',b.id) AS id,b.nm_target AS nm,CONCAT(LPAD(a.id,3,'0'),'.',LPAD(b.id,3,'0')) AS id_parent,'2' AS LEVEL ,CONCAT(a.id_goals,'-',b.id_target) AS id_text ,a.id AS id_parent2 FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                                                "	UNION \n" +
                                                "	SELECT DISTINCT  CONCAT(a.id,'.',b.id,'.',c.id) AS id,c.nm_indicator AS nm,CONCAT(LPAD(a.id,3,'0') ,'.',LPAD(b.id,3,'0'),'.',LPAD(c.id,3,'0')) AS id_parent,'3' AS LEVEL ,CONCAT(a.id_goals,'-',b.id_target,'-',c.id_indicator) AS id_text ,CONCAT(a.id,'.',b.id) AS id_parent2  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                                                "	ORDER BY id_parent");
        
        List list3 =  query3.getResultList();
        Map<String, Object> filtersdg = new HashMap<>();
        filtersdg.put("data",list3);
        model.addAttribute("filtersdg",filtersdg);
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        return "admin/approval/problem_identify";
    }
    
    @GetMapping("admin/count-pesan")
    public @ResponseBody Map<String, Object> getPesan(HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> role = roleService.findOne(id_role);
    	String privilege = role.get().getPrivilege();
    	String sql;
    	if(privilege.equals("SUPER")) {
    		sql  = "select count(id) from entry_approval where approval = '3' and (read_date is null or read_date='') ";
    	}else {
    		sql  = "select count(id) from entry_approval where id_role ='"+id_role+"' and approval = '3' and (read_date is null or read_date='') ";
    	}
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/read-message")
    @Transactional
    public String readPesan(HttpSession session,Model model) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> role = roleService.findOne(id_role);
    	String privilege = role.get().getPrivilege();
    	if(privilege.equals("SUPER")) {
    		model.addAttribute("data", approvalService.getAllMessage());
    	}else {
    		model.addAttribute("data", approvalService.getMessageByRole(id_role));
    		String sql  = "select count(id) from entry_approval where id_role ='"+id_role+"' and approval = '3' and (read_date is null or read_date='') ";
            Query query = em.createNativeQuery(sql);
            List list   = query.getResultList();
            if(!list.get(0).toString().equals("0")) {
            	em.createNativeQuery("UPDATE entry_approval set read_date = NOW() where id_role ='"+id_role+"' and approval = '3' and (read_date is null or read_date='') ").executeUpdate();
            }
    	}        
    	model.addAttribute("name", session.getAttribute("name"));
    	model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/message";
    }
    
}
