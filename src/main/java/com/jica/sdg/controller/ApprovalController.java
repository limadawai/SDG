package com.jica.sdg.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.jica.sdg.service.IEntryApprovalService;
import com.jica.sdg.service.IEntrySdgDetailService;
import com.jica.sdg.service.IEntrySdgService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.ModelCrud;
import com.jica.sdg.service.NsaProfileService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	@Autowired
    ModelCrud modelCrud;
	
	
	@PostMapping(path = "admin/save-approval", consumes = "application/json", produces = "application/json")
	@ResponseBody
	@Transactional
	public void saveApproval(@RequestBody EntryApproval app) throws ParseException {
		if(app.getId()==null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			Date date = new Date();
			Date date1=new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(dateFormat.format(date)); 
			app.setApproval_date(date1);
		}
		if(app.getId_role()==0) {
			String sql;
			if(app.getType().equals("entry_sdg")) {
				sql  = "select DISTINCT id_role from assign_sdg_indicator as a where a.id_monper = '"+app.getId_monper()+"'";
			}else if(app.getType().equals("entry_gov_budget") || app.getType().equals("entry_gov_indicator")) {
				sql = "select DISTINCT a.id_role "
		    			+ "from gov_activity as a "
		    			+ "left join gov_program b on a.id_program = b.id "
		    			+ "left join ref_role f on a.id_role = f.id_role "
		    			+ "left join ran_rad g on f.id_prov = g.id_prov and b.id_monper = g.id_monper "
		    			+ "where g.id_monper = '"+app.getId_monper()+"' "
		    			+ "order by a.id_role";
			}else if(app.getType().equals("entry_nsa_budget") || app.getType().equals("entry_nsa_indicator")) {
				sql = "select DISTINCT a.id_role "
		    			+ "from nsa_activity as a "
		    			+ "left join nsa_program b on a.id_program = b.id "
		    			+ "left join ref_role f on a.id_role = f.id_role "
		    			+ "left join ran_rad g on f.id_prov = g.id_prov and b.id_monper = g.id_monper "
		    			+ "where g.id_monper = '"+app.getId_monper()+"' "
		    			+ "order by a.id_role";
			}else{
				sql  = "select id_role from assign_sdg_indicator as a where a.id_monper = :id_monper and a.id_prov = :id_prov";
			}
	        Query query1 = em.createNativeQuery(sql);
	        List list   = query1.getResultList();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				EntryApproval apptemp = new EntryApproval();
				apptemp.setId_form_type(app.getId_form_type());
				apptemp.setId_role(Integer.parseInt(object.toString()));
				apptemp.setId_monper(app.getId_monper());
				apptemp.setYear(app.getYear());
				apptemp.setApproval(app.getApproval());
				apptemp.setType(app.getType());
				apptemp.setPeriode(app.getPeriode());
				apptemp.setApproval_date(new Date());
				
				Query query;
				if(app.getType().equals("entry_sdg")) {
					query = em.createNativeQuery("update entry_sdg set new_value1 = null, new_value2 = null, new_value3 = null, new_value4 = null where year_entry=:year and id_monper=:id_monper and id_role = :id_role");
					query.setParameter("year", app.getYear());
			        query.setParameter("id_monper", app.getId_monper());
			        query.setParameter("id_role", apptemp.getId_role());
			        query.executeUpdate();
			        
			        query = em.createNativeQuery("update entry_sdg_detail set new_value1 = null, new_value2 = null, new_value3 = null, new_value4 = null where year_entry=:year and id_monper=:id_monper and id_role = :id_role");
					query.setParameter("year", app.getYear());
			        query.setParameter("id_monper", app.getId_monper());
			        query.setParameter("id_role", apptemp.getId_role());
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
				approvalService.deleteApproveGovBudget(apptemp.getId_role(), app.getId_monper(), app.getYear(), app.getType(), app.getPeriode());
				approvalService.save(apptemp);
			}
			if(app.getType().equals("entry_sdg")) {
				EntryApproval apptemp = new EntryApproval();
				apptemp.setId_form_type(app.getId_form_type());
				apptemp.setId_role(null);
				apptemp.setId_monper(app.getId_monper());
				apptemp.setYear(app.getYear());
				apptemp.setApproval(app.getApproval());
				apptemp.setType(app.getType());
				apptemp.setPeriode(app.getPeriode());
				apptemp.setApproval_date(new Date());
				approvalService.deleteApproveGovBudget(apptemp.getId_role(), app.getId_monper(), app.getYear(), app.getType(), app.getPeriode());
				approvalService.save(apptemp);
			}
		}else {
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
		
	}
	
    @GetMapping("admin/get-approve/{type}/{year}/{id_monper}/{periode}/{id_role}")
    public @ResponseBody Map<String, Object> getApprove(@PathVariable("type") String type, @PathVariable("year") Integer year, @PathVariable("id_monper") Integer id_monper, @PathVariable("periode") Integer periode, @PathVariable("id_role") String id_role) {
    	List list;
    	Query query;
    	if(id_role.equals("0")) {
    		String sql = "select DISTINCT '' as id, approval from entry_approval where type=:type and year=:year and id_monper=:id_monper and periode = :periode";
            query = em.createNativeQuery(sql);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
            query.setParameter("type", type);
            query.setParameter("periode", periode);
        }else {
        	String role;
        	if(id_role.equals("all")) {
        		role = "";
        	}else if(id_role.equals("unassign")) {
        		role = "id_role is null ";
        	}else {
        		role = "id_role = '"+id_role+"'";
        	}
        	String sql = "select id, approval from entry_approval where type=:type and year=:year and id_monper=:id_monper and periode = :periode and "+role;
            query = em.createNativeQuery(sql);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
            query.setParameter("type", type);
            query.setParameter("periode", periode);
        }
    	
        list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/unapply/{type}/{year}/{id_monper}/{periode}/{id_role}")
    @ResponseBody    
    @Transactional
    public void deleteUnit(@PathVariable("type") String type, @PathVariable("year") Integer year, @PathVariable("id_monper") Integer id_monper, @PathVariable("periode") Integer periode, @PathVariable("id_role") Integer id_role) {
    	Query query;
    	if(id_role == 0) {
    		query = em.createNativeQuery("delete from entry_approval where type=:type and year=:year and id_monper=:id_monper and periode = :periode");
        	query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
            query.setParameter("type", type);
            query.setParameter("periode", periode);
            query.executeUpdate();
        }else {
        	query = em.createNativeQuery("delete from entry_approval where type=:type and year=:year and id_monper=:id_monper and periode = :periode and id_role = :id_role");
        	query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
            query.setParameter("type", type);
            query.setParameter("periode", periode);
            query.setParameter("id_role", id_role);
            query.executeUpdate();
        } 
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
    
    @GetMapping("admin/cek_sum_problem/{id_prov}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> cek_sum_problem(@PathVariable("id_prov") String id_prov, @PathVariable("id_monper") String id_monper, @PathVariable("year") String year) {
        String sql = "select count(*) as total from entry_problem_identify where id_prov = :id_prov and id_monper = :id_monper and year = :year and id_role = '999999' ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        query.setParameter("id_prov", id_prov);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/cek_sum_best_pract/{id_prov}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> cek_sum_best_pract(@PathVariable("id_prov") String id_prov, @PathVariable("id_monper") String id_monper, @PathVariable("year") String year) {
        String sql = "select count(*) as total from best_practice where id_monper = :id_monper and year = :year and id_role = '999999' ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
//        query.setParameter("id_prov", id_prov);
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
        Query query = em.createNativeQuery("UPDATE entry_approval set approval = '4' where type='entry_sdg' and id_monper = :id_monper and year = :year and periode = :periode and (approval = '2' or approval = '1')");
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
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
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
                "	SELECT DISTINCT  CONCAT(a.id,'.',b.id) AS id,b.nm_target AS nm,CONCAT(LPAD(a.id,3,'0'),'.',LPAD(b.id,3,'0')) AS id_parent,'2' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target) AS id_text ,a.id AS id_parent2 FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                "	UNION \n" +
                "	SELECT DISTINCT  CONCAT(a.id,'.',b.id,'.',c.id) AS id,c.nm_indicator AS nm,CONCAT(LPAD(a.id,3,'0') ,'.',LPAD(b.id,3,'0'),'.',LPAD(c.id,3,'0')) AS id_parent,'3' AS LEVEL ,CONCAT(a.id_goals,'.',b.id_target,'.',c.id_indicator) AS id_text ,CONCAT(a.id,'.',b.id) AS id_parent2  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                "	ORDER BY id_parent");
        
        List list3 =  query3.getResultList();
        Map<String, Object> filtersdg = new HashMap<>();
        filtersdg.put("data",list3);
        model.addAttribute("filtersdg",filtersdg);
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        model.addAttribute("refcategory",modelCrud.getRefCategory());
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
    	String id_prov = role.get().getId_prov();
    	if(privilege.equals("SUPER")) {
    		model.addAttribute("data", approvalService.getAllMessage());
    	}else if(privilege.equals("ADMIN")) {
    		model.addAttribute("data", approvalService.getMessageByProv(Integer.parseInt(id_prov)));
    	}else{
    		model.addAttribute("data", approvalService.getMessageByRole(id_role));
    		String sql  = "select count(id) from entry_approval where id_role ='"+id_role+"' and approval = '3' and (read_date is null or read_date='') ";
            Query query = em.createNativeQuery(sql);
            List list   = query.getResultList();
            if(!list.get(0).toString().equals("0")) {
            	em.createNativeQuery("UPDATE entry_approval set read_date = NOW() where id_role ='"+id_role+"' and approval = '3' and (read_date is null) ").executeUpdate();
            }
    	}        
    	model.addAttribute("name", session.getAttribute("name"));
    	model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/message";
    }
    
    @GetMapping("admin/list-message")
    public @ResponseBody Map<String, Object> listMessage(HttpSession session) {
    	Query query;
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> role = roleService.findOne(id_role);
    	String privilege = role.get().getPrivilege();
    	String id_prov = role.get().getId_prov();
    	if(privilege.equals("SUPER")) {
    		String sql  = " SELECT a.type,a.year,a.description, "
    				+ "CASE when a.type='entry_sdg' then b.sdg_indicator "
    				+ "when a.type='entry_gov_indicator' then b.gov_prog "
    				+ "when a.type='entry_gov_budget' then b.gov_prog_bud "
    				+ "when a.type='entry_nsa_indicator' then b.nsa_prog "
    				+ "when a.type='entry_nsa_budget' then b.nsa_prog_bud "
    				+ "ELSE '' END as period, "
    				+ "c.nm_role, a.periode,a.approval_date "
    				+ "from entry_approval a "
    				+ "left join ran_rad b on a.id_monper = b.id_monper "
    				+ "left join ref_role c on a.id_role = c.id_role "
    				+ "where a.approval = '3' order by a.id_monper, a.year, a.periode" ;
            query = em.createNativeQuery(sql);
    	}else if(privilege.equals("ADMIN")) {
    		String sql  = " SELECT a.type,a.year,a.description, "
    				+ "CASE when a.type='entry_sdg' then b.sdg_indicator "
    				+ "when a.type='entry_gov_indicator' then b.gov_prog "
    				+ "when a.type='entry_gov_budget' then b.gov_prog_bud "
    				+ "when a.type='entry_nsa_indicator' then b.nsa_prog "
    				+ "when a.type='entry_nsa_budget' then b.nsa_prog_bud "
    				+ "ELSE '' END as period, "
    				+ "c.nm_role, a.periode,a.approval_date "
    				+ "from entry_approval a "
    				+ "left join ran_rad b on a.id_monper = b.id_monper "
    				+ "left join ref_role c on a.id_role = c.id_role "
    				+ "where a.approval = '3' and c.id_prov = '"+id_prov+"' order by a.id_monper, a.year, a.periode" ;
    		query = em.createNativeQuery(sql);
    	}else{
    		String sql  = " SELECT a.type,a.year,a.description, "
    				+ "CASE when a.type='entry_sdg' then b.sdg_indicator "
    				+ "when a.type='entry_gov_indicator' then b.gov_prog "
    				+ "when a.type='entry_gov_budget' then b.gov_prog_bud "
    				+ "when a.type='entry_nsa_indicator' then b.nsa_prog "
    				+ "when a.type='entry_nsa_budget' then b.nsa_prog_bud "
    				+ "ELSE '' END as period, "
    				+ "c.nm_role, a.periode,a.approval_date "
    				+ "from entry_approval a "
    				+ "left join ran_rad b on a.id_monper = b.id_monper "
    				+ "left join ref_role c on a.id_role = c.id_role "
    				+ "where a.approval = '3' and a.id_role = '"+id_role+"' order by a.id_monper, a.year, a.periode" ;
    		query = em.createNativeQuery(sql);
    	} 
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
}
