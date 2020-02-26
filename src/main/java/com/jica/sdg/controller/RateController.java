package com.jica.sdg.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import com.jica.sdg.service.EntryProblemIdentifyService;
import com.jica.sdg.service.IEntryApprovalService;
import com.jica.sdg.service.IEntrySdgService;
import com.jica.sdg.service.IGovActivityService;
import com.jica.sdg.service.IGovProgramService;
import com.jica.sdg.service.INsaActivityService;
import com.jica.sdg.service.INsaProgramService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRanRadService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.ISdgIndicatorService;
import com.jica.sdg.service.NsaProfileService;
import com.jica.sdg.service.SdgFundingService;
import com.jica.sdg.service.SdgGoalsService;
import com.jica.sdg.service.SdgTargetService;
import com.jica.sdg.service.UnitService;

@Controller
public class RateController {
	
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

    //entry SDG
    @GetMapping("admin/rate")
    public String entri_sdg(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Rate");
        
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
        return "admin/rate/rate_index";
    }
    
    @GetMapping("admin/x/val/{id_prov}/{period}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object>  val_rate_sdg(@PathVariable("id_prov") String id_prov, @PathVariable("period") String period, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
        String sql  = "select '00000' as id_role, 'Government' as nm_role, 'Government' as cat_role, '1' as kode, \n" +
                    "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_indicator' and period = :period) as show_report \n" +
                    "union all\n" +
                    "select a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report\n" +
                    "from ref_role a\n" +
                    "where a.cat_role = 'Government' and a.id_prov = :id_prov \n" +
                    "union all\n" +
                    "select '11111' as id, 'Non Government' as nm, 'NSA' as ket, '1' as kode, \n" +
                    "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_indicator' and period = :period) as show_report \n" +
                    "union all\n" +
                    "select a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report \n" +
                    "from ref_role a\n" +
                    "where a.cat_role = 'NSA' and a.id_prov = :id_prov ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get-cek-app/{id_role}/{id_monper}/{year}/{period}")
    public @ResponseBody Map<String, Object>  cek_app(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year, @PathVariable("period") String period) {
        String sql  = "select count(*) as cek from entry_approval where (type = 'entry_gov_indicator' or type = 'entry_nsa_indicator') and id_role = :id_role and id_monper = :id_monper and year = :year and periode = :period ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get-cek-sho/{id_monper}/{year}/{period}/{type}")
    public @ResponseBody Map<String, Object>  cek_sho(@PathVariable("id_monper") String id_monper,@PathVariable("year") String year, @PathVariable("period") String period, @PathVariable("type") String type) {
        String sql  = "select count(*) as cek from entry_show_report where id_monper = :id_monper and year = :year and type = :type and period = :period ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get-cek-data-all/{id_role}/{year}/{period}/{type}/{tb}/{tb2}")
    public @ResponseBody Map<String, Object>  cek_data_all(@PathVariable("id_role") String id_role,@PathVariable("year") String year, @PathVariable("period") String period, @PathVariable("type") String type, @PathVariable("tb") String tb, @PathVariable("tb2") String tb2) {
        
        String sql  = "select \n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b. id, b.id_activity, c.id_role, b.nm_indicator, b.nm_indicator_eng, d.achievement"+period+" \n" +
                    "from "+tb2+" b\n" +
                    "left join "+tb+" c on b.id_activity = c.id\n" +
                    "inner join (select * from "+type+" where year_entry = :year and achievement"+period+" != 0 ) d on b.id = d.id_assign\n" +
                    "where c.id_role = :id_role \n" +
                    ") as a\n" +
                    ") as isi,\n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b. id, b.id_activity, c.id_role, b.nm_indicator, b.nm_indicator_eng, d.achievement"+period+" \n" +
                    "from "+tb2+" b\n" +
                    "left join "+tb+" c on b.id_activity = c.id\n" +
                    "left join (select * from "+type+" where year_entry = :year) d on b.id = d.id_assign\n" +
                    "where c.id_role = :id_role\n" +
                    ") as a\n" +
                    ") as semua";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("period", period);
        query.setParameter("id_role", id_role);
        query.setParameter("year", year);
//        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
}
