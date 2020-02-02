package com.jica.sdg.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jica.sdg.model.EntryApproval;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.service.IEntryApprovalService;
import com.jica.sdg.service.IEntrySdgService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.NsaProfileService;

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
	
	@PostMapping(path = "admin/save-approval", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveApproval(@RequestBody EntryApproval app) {
		if(app.getId()==null) {
			app.setApproval_date(new Date());
		}
		approvalService.save(app);
	}
	
	@GetMapping("admin/get-approve/{id_form_type}/{type}/{year}/{period}")
	public @ResponseBody Map<String, Object> getApprove(@PathVariable("id_form_type") Integer id_form_type, @PathVariable("type") String type, @PathVariable("year") Integer year, @PathVariable("period") Integer period) {
		if(type.equals("entry_sdg")) {
		}
		String sql = "select id, approval from entry_approval where id_form_type=:id_form_type and type=:type and year=:year and period=:period";
		Query query = em.createNativeQuery(sql);
		query.setParameter("id_form_type", id_form_type);
        query.setParameter("year", year);
        query.setParameter("period", period);
        query.setParameter("type", type);
		List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
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
	
	@GetMapping("admin/list-role-approval/{id_monper}/{year}/{type}")
	public @ResponseBody Map<String, Object> listRoleApproval(@PathVariable("id_monper") Integer id_monper, @PathVariable("year") Integer year, @PathVariable("type") String type) {
		String sql = "select a.id, a.id_role, b.nm_role, a.approval, a.period, a.description from entry_approval a left join ref_role b on a.id_role = b.id_role where id_monper=:id_monper and year=:year and type=:type";
		Query query = em.createNativeQuery(sql);
		query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        query.setParameter("type", type);
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
        	if(!nilai.equals("")) {
        		if(period.equals("1")) {
        			sdgService.updateNew1(Integer.parseInt(id), Integer.parseInt(nilai));
        		}else if(period.equals("2")) {
        			sdgService.updateNew2(Integer.parseInt(id), Integer.parseInt(nilai));
        		}else if(period.equals("3")) {
        			sdgService.updateNew3(Integer.parseInt(id), Integer.parseInt(nilai));
        		}else if(period.equals("4")) {
        			sdgService.updateNew4(Integer.parseInt(id), Integer.parseInt(nilai));
        		}
        	}
        }
	}
}
