package com.jica.sdg.controller;

import com.jica.sdg.model.EntryGovBudget;
import com.jica.sdg.model.EntryGovIndicator;
import com.jica.sdg.model.EntryNsaBudget;
import com.jica.sdg.model.EntryNsaIndicator;
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
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    
    @GetMapping("admin/x/val/{id_prov}/{period}/{id_monper}/{year}/{id_role}")
    public @ResponseBody Map<String, Object>  val_rate_sdg(@PathVariable("id_prov") String id_prov, @PathVariable("period") String period, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year, @PathVariable("id_role") String id_role) {
        Query query = em.createNativeQuery("");
//        System.out.println("id "+id_role);
        if(id_role.equals("999999")){
            String sql  = "select '00000' as id_role, 'Government' as nm_role, 'Government' as cat_role, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_indicator' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select distinct a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report\n" +
                        "from ref_role a\n" +
                        "inner join gov_activity as m on a.id_role = m.id_role \n" +
                        "where a.cat_role = 'Government' and a.id_role <> '1' and a.id_prov = :id_prov \n" +
                        "union all\n" +
                        "select '11111' as id, 'Non Government' as nm, 'NSA' as ket, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_nsa_indicator' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select distinct a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report \n" +
                        "from ref_role a\n" +
                        "inner join nsa_activity as m on a.id_role = m.id_role \n" +
                        "where a.cat_role = 'NSA' and a.id_role <> '1' and a.id_prov = :id_prov ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("period", period);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
        }else if(id_role.equals("9999991")){
            String sql  = "select '00000' as id_role, 'Government' as nm_role, 'Government' as cat_role, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_indicator' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select distinct a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report\n" +
                        "from ref_role a\n" +
                        "inner join gov_activity as m on a.id_role = m.id_role \n" +
                        "where a.cat_role = 'Government' and a.id_role <> '1' and a.id_prov = :id_prov \n";
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("period", period);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
        }else if(id_role.equals("9999992")){
            String sql  = "select '11111' as id, 'Non Government' as nm, 'NSA' as ket, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_nsa_indicator' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select distinct a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report \n" +
                        "from ref_role a\n" +
                        "inner join nsa_activity as m on a.id_role = m.id_role \n" +
                        "where a.cat_role = 'NSA' and a.id_role <> '1' and a.id_prov = :id_prov ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("period", period);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
        }else{
            String sql  = "select '00000' as id_role, 'Government' as nm_role, 'Government' as cat_role, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_indicator' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report\n" +
                        "from ref_role a\n" +
                        "where a.cat_role = 'Government' and a.id_prov = :id_prov and a.id_role = :id_role \n" +
                        "union all\n" +
                        "select '11111' as id, 'Non Government' as nm, 'NSA' as ket, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_nsa_indicator' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report \n" +
                        "from ref_role a\n" +
                        "where a.cat_role = 'NSA' and a.id_prov = :id_prov and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("period", period);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
            query.setParameter("id_role", id_role);
        }
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }  
    
    @GetMapping("admin/x/val_budget/{id_prov}/{period}/{id_monper}/{year}/{id_role}")
    public @ResponseBody Map<String, Object>  val_rate_sdg_budget(@PathVariable("id_prov") String id_prov, @PathVariable("period") String period, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year, @PathVariable("id_role") String id_role) {
        Query query = em.createNativeQuery("");
//        System.out.println("id "+id_role);
        if(id_role.equals("999999")){
            String sql  = "select '00000' as id_role, 'Government' as nm_role, 'Government' as cat_role, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_budget' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select distinct a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report\n" +
                        "from ref_role a\n" +
                        "inner join gov_activity as m on a.id_role = m.id_role \n" +
                        "where a.cat_role = 'Government' and a.id_role <> '1' and a.id_prov = :id_prov \n" +
                        "union all\n" +
                        "select '11111' as id, 'Non Government' as nm, 'NSA' as ket, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_nsa_budget' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select distinct a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report \n" +
                        "from ref_role a\n" +
                        "inner join nsa_activity as m on a.id_role = m.id_role \n" +
                        "where a.cat_role = 'NSA' and a.id_role <> '1' and a.id_prov = :id_prov ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("period", period);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
        }else if(id_role.equals("9999991")){
            String sql  = "select '00000' as id_role, 'Government' as nm_role, 'Government' as cat_role, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_budget' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select distinct a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report\n" +
                        "from ref_role a\n" +
                        "inner join gov_activity as m on a.id_role = m.id_role \n" +
                        "where a.cat_role = 'Government' and a.id_role <> '1' and a.id_prov = :id_prov \n" ;
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("period", period);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
        }else if(id_role.equals("9999992")){
            String sql  = "select '11111' as id, 'Non Government' as nm, 'NSA' as ket, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_nsa_budget' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select distinct a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report \n" +
                        "from ref_role a\n" +
                        "inner join nsa_activity as m on a.id_role = m.id_role \n" +
                        "where a.cat_role = 'NSA' and a.id_role <> '1' and a.id_prov = :id_prov ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("period", period);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
        }else{
            String sql  = "select '00000' as id_role, 'Government' as nm_role, 'Government' as cat_role, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_budget' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report\n" +
                        "from ref_role a\n" +
                        "where a.cat_role = 'Government' and a.id_prov = :id_prov and a.id_role = :id_role \n" +
                        "union all\n" +
                        "select '11111' as id, 'Non Government' as nm, 'NSA' as ket, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_nsa_budget' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report \n" +
                        "from ref_role a\n" +
                        "where a.cat_role = 'NSA' and a.id_prov = :id_prov and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("period", period);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
            query.setParameter("id_role", id_role);
        }
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
    
    @GetMapping("admin/get-cek-app-budget/{id_role}/{id_monper}/{year}/{period}")
    public @ResponseBody Map<String, Object>  cek_app_budget(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year, @PathVariable("period") String period) {
        String sql  = "select count(*) as cek from entry_approval where (type = 'entry_gov_budget' or type = 'entry_nsa_budget') and id_role = :id_role and id_monper = :id_monper and year = :year and periode = :period ";
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
    
    @GetMapping("admin/get-cek-app-approve/{id_role}/{id_monper}/{year}/{period}")
    public @ResponseBody Map<String, Object>  cek_app_approve(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year, @PathVariable("period") String period) {
        String sql  = "select count(*) as cek from entry_approval where (type = 'entry_gov_indicator' or type = 'entry_nsa_indicator') and id_role = :id_role and id_monper = :id_monper and year = :year and periode = :period and (approval = '2' or approval = '4')";
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
    
    @GetMapping("admin/get-cek-app-approve-budget/{id_role}/{id_monper}/{year}/{period}")
    public @ResponseBody Map<String, Object>  cek_app_approve_budget(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year, @PathVariable("period") String period) {
        String sql  = "select count(*) as cek from entry_approval where (type = 'entry_gov_budget' or type = 'entry_nsa_budget') and id_role = :id_role and id_monper = :id_monper and year = :year and periode = :period and (approval = '2' or approval = '4')";
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
    
    @GetMapping("admin/get-cek-data-all/{id_role}/{year}/{period}/{type}/{tb}/{tb2}/{isi_time}/{id_monper}")
    public @ResponseBody Map<String, Object>  cek_data_all(@PathVariable("id_role") String id_role,@PathVariable("year") String year, @PathVariable("period") String period, @PathVariable("type") String type, @PathVariable("tb") String tb, @PathVariable("tb2") String tb2, @PathVariable("isi_time") String isi_time, @PathVariable("id_monper") String id_monper) {
        String tg_date = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        String prog = "";
        System.out.println("isi tipe = "+type);
        if(type.equals("entry_gov_indicator")){
            prog = "gov_program";
        }else{
            prog = "nsa_program";
        }
        String sql  = "select \n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b. id, b.id_activity, c.id_role, b.nm_indicator, b.nm_indicator_eng, d.achievement"+period+" \n" +
                    "from "+tb2+" b\n" +
                    "left join "+tb+" c on b.id_activity = c.id\n" +
                    "inner join (select * from "+type+" where id_monper = :id_monper and year_entry = :year and achievement"+period+" != 0 "+tg_date+" ) d on b.id = d.id_assign\n" +
                    "inner join (select * from "+prog+" where id_monper = :id_monper) e on c.id_program = e.id \n" +
                    "where c.id_role = :id_role \n" +
                    ") as a\n" +
                    ") as isi,\n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b. id, b.id_activity, c.id_role, b.nm_indicator, b.nm_indicator_eng, d.achievement"+period+" \n" +
                    "from "+tb2+" b\n" +
                    "left join "+tb+" c on b.id_activity = c.id\n" +
                    "left join (select * from "+type+" where id_monper = :id_monper and year_entry = :year "+tg_date+") d on b.id = d.id_assign\n" +
                    "inner join (select * from "+prog+" where id_monper = :id_monper) e on c.id_program = e.id \n" +
                    "where c.id_role = :id_role\n" +
                    ") as a\n" +
                    ") as semua";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
        query.setParameter("id_role", id_role);
        query.setParameter("year", year);
//        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }  
    @GetMapping("admin/get-cek-data-all-budget/{id_role}/{year}/{period}/{type}/{tb}/{tb2}/{isi_time}/{id_monper}")
    public @ResponseBody Map<String, Object>  cek_data_all_budget(@PathVariable("id_role") String id_role,@PathVariable("year") String year, @PathVariable("period") String period, @PathVariable("type") String type, @PathVariable("tb") String tb, @PathVariable("tb2") String tb2, @PathVariable("isi_time") String isi_time, @PathVariable("id_monper") String id_monper) {
        String tg_date = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        
        String id_activity_1 = "";
        String prog = "";
        if(type.equals("entry_gov_budget")){
            id_activity_1 = "d.id_gov_activity";
            prog = "gov_program";
        }else{
            id_activity_1 = "d.id_nsa_activity";
            prog = "nsa_program";
        }
        String sql  = "select \n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b.id, b.id_activity, b.id_role, b.nm_activity, b.nm_activity_eng, d.achievement"+period+" \n" +
                    "from "+tb+" b\n" +
                    "inner join (select * from "+type+" where id_monper = :id_monper and year_entry = :year and achievement"+period+" != 0 "+tg_date+" ) d on b.id = "+id_activity_1+"\n" +
                    "inner join (select * from "+prog+" where id_monper = :id_monper ) e on b.id_program = e.id \n" +
                    " \n" +
                    "where b.id_role = :id_role  \n" +
                    ") as a\n" +
                    ") as isi,\n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b.id, b.id_activity, b.id_role, b.nm_activity, b.nm_activity_eng, d.achievement"+period+" \n" +
                    "from "+tb+" b\n" +
                    "left join (select * from "+type+" where id_monper = :id_monper and year_entry = :year "+tg_date+") d on b.id = "+id_activity_1+"\n" +
                    "inner join (select * from "+prog+" where id_monper = :id_monper) e on b.id_program = e.id \n" +
                    " \n" +
                    "where b.id_role = :id_role \n" +
                    ") as a\n" +
                    ") as semua";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
        query.setParameter("id_role", id_role);
        query.setParameter("year", year);
//        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }  
    
    @GetMapping("admin/get-cek-data-all-deadline/{id_role}/{year}/{period}/{type}/{tb}/{tb2}/{sts}/{isi_time}/{id_monper}")
    public @ResponseBody Map<String, Object>  cek_data_all_deadline(@PathVariable("id_role") String id_role,@PathVariable("year") int year, @PathVariable("period") String period, @PathVariable("type") String type, @PathVariable("tb") String tb, @PathVariable("tb2") String tb2, @PathVariable("sts") String sts_monper, @PathVariable("isi_time") String isi_time, @PathVariable("id_monper") String id_monper) {
        String tg_date = "";
        if(sts_monper.equals("yearly")){
            if(period.equals("1")){
                tg_date = "and date_created <= '"+(year+1)+"-01-15' ";
            }else{}
        }else if(sts_monper.equals("semesterly")){
            if(period.equals("1")){
                tg_date = "and date_created <= '"+year+"-07-15' ";
            }else if(period.equals("2")){
                tg_date = "and date_created2 <= '"+(year+1)+"-01-15' ";
            }else {}
        }else if(sts_monper.equals("quarterly")){
            if(period.equals("1")){
                tg_date = "and date_created <= '"+year+"-04-15' ";
            }else if(period.equals("2")){
                tg_date = "and date_created2 <= '"+year+"-07-15' ";
            }else if(period.equals("3")){
                tg_date = "and date_created3 <= '"+year+"-10-15' ";
            }else if(period.equals("4")){
                tg_date = "and date_created4 <= '"+(year+1)+"-01-15' ";
            }else{}
        }else{}
        
        String tg_date_1 = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        String prog = "";
        if(type.equals("entry_gov_indicator")){
            prog = "gov_program";
        }else{
            prog = "nsa_program";
        }
        System.out.println("tgdate = "+tg_date);
        String sql  = "select \n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b. id, b.id_activity, c.id_role, b.nm_indicator, b.nm_indicator_eng, d.achievement"+period+" \n" +
                    "from "+tb2+" b\n" +
                    "left join "+tb+" c on b.id_activity = c.id\n" +
                    "inner join (select * from "+type+" where year_entry = :year and achievement"+period+" != 0 "+tg_date+" "+tg_date_1+" ) d on b.id = d.id_assign\n" +
                    "inner join (select * from "+prog+" where id_monper = :id_monper) e on c.id_program = e.id \n" +
                    "where c.id_role = :id_role \n" +
                    ") as a\n" +
                    ") as isi,\n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b. id, b.id_activity, c.id_role, b.nm_indicator, b.nm_indicator_eng, d.achievement"+period+" \n" +
                    "from "+tb2+" b\n" +
                    "left join "+tb+" c on b.id_activity = c.id\n" +
                    "left join (select * from "+type+" where year_entry = :year "+tg_date_1+") d on b.id = d.id_assign\n" +
                    "inner join (select * from "+prog+" where id_monper = :id_monper) e on c.id_program = e.id \n" +
                    "where c.id_role = :id_role\n" +
                    ") as a\n" +
                    ") as semua";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
        query.setParameter("id_role", id_role);
        query.setParameter("year", year);
//        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get-cek-data-all-deadline-budget/{id_role}/{year}/{period}/{type}/{tb}/{tb2}/{sts}/{isi_time}/{id_monper}")
    public @ResponseBody Map<String, Object>  cek_data_all_deadline_budget(@PathVariable("id_role") String id_role,@PathVariable("year") int year, @PathVariable("period") String period, @PathVariable("type") String type, @PathVariable("tb") String tb, @PathVariable("tb2") String tb2, @PathVariable("sts") String sts_monper, @PathVariable("isi_time") String isi_time, @PathVariable("id_monper") String id_monper) {
        String tg_date = "";
        if(sts_monper.equals("yearly")){
            if(period.equals("1")){
                tg_date = "and date_created <= '"+(year+1)+"-01-15' ";
            }else{}
        }else if(sts_monper.equals("semesterly")){
            if(period.equals("1")){
                tg_date = "and date_created <= '"+year+"-07-15' ";
            }else if(period.equals("2")){
                tg_date = "and date_created2 <= '"+(year+1)+"-01-15' ";
            }else {}
        }else if(sts_monper.equals("quarterly")){
            if(period.equals("1")){
                tg_date = "and date_created <= '"+year+"-04-15' ";
            }else if(period.equals("2")){
                tg_date = "and date_created2 <= '"+year+"-07-15' ";
            }else if(period.equals("3")){
                tg_date = "and date_created3 <= '"+year+"-10-15' ";
            }else if(period.equals("4")){
                tg_date = "and date_created4 <= '"+(year+1)+"-01-15' ";
            }else{}
        }else{}
        
        String tg_date_1 = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        
        String id_activity_1 = "";
        String prog = "";
        if(type.equals("entry_gov_budget")){
            id_activity_1 = "d.id_gov_activity";
            prog = "gov_program";
        }else{
            id_activity_1 = "d.id_nsa_activity";
            prog = "nsa_program";
        }
        System.out.println("tgdate = "+tg_date);
        String sql  = "select \n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b.id, b.id_activity, b.id_role, b.nm_activity, b.nm_activity_eng, d.achievement"+period+" \n" +
                    "from "+tb+" b\n" +
                    "inner join (select * from "+type+" where year_entry = :year and achievement"+period+" != 0 "+tg_date+" "+tg_date_1+" ) d on b.id = "+id_activity_1+"\n" +
                    "inner join (select * from "+prog+" where id_monper = :id_monper) e on b.id_program = e.id \n" +
                    "where b.id_role = :id_role \n" +
                    ") as a\n" +
                    ") as isi,\n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b.id, b.id_activity, b.id_role, b.nm_activity, b.nm_activity_eng, d.achievement"+period+" \n" +
                    "from "+tb+" b\n" +
                    "left join (select * from "+type+" where year_entry = :year "+tg_date_1+") d on b.id = "+id_activity_1+"\n" +
                    "inner join (select * from "+prog+" where id_monper = :id_monper) e on b.id_program = e.id \n" +
                    "where b.id_role = :id_role\n" +
                    ") as a\n" +
                    ") as semua";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
        query.setParameter("id_role", id_role);
        query.setParameter("year", year);
//        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get_sub_prog_level1/{id_monper}/{id_role}/{catrole}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level1(@PathVariable("id_monper") String id_monper,@PathVariable("id_role") String id_role, @PathVariable("catrole") String catrole) {
        System.out.println("catrole = "+catrole+", id_monper = "+id_monper);
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select distinct a.id, a.nm_program, a.nm_program_eng, a.internal_code from gov_program a \n" +
                        "left join gov_activity b on a.id = b.id_program\n" +
                        "inner join gov_indicator c on b.id = c.id_activity\n" +
                        "where a.id_monper = :id_monper and b.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_monper", id_monper);
        }else if(catrole.equals("NSA")){
            String sql  = "select distinct a.id, a.nm_program, a.nm_program_eng, a.internal_code from nsa_program a \n" +
                        "inner join nsa_activity b on a.id = b.id_program \n" +
                        "inner join nsa_indicator c on b.id = c.id_activity \n" +
                        "where a.id_monper = :id_monper and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_monper", id_monper);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get_sub_prog_level2/{id_program}/{id_role}/{catrole}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level2(@PathVariable("id_program") String id_program, @PathVariable("id_role") String id_role, @PathVariable("catrole") String catrole) {
        
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select distinct a.id, a.nm_activity, a.nm_activity_eng, a.internal_code from gov_activity a\n" +
                        "inner join gov_indicator c on a.id = c.id_activity \n" +
                        "where a.id_program = :id_program and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_program", id_program);
        }else if(catrole.equals("NSA")){
            String sql  = "select distinct a.id, a.nm_activity, a.nm_activity_eng, a.internal_code from nsa_activity a\n" +
                        "inner join nsa_indicator c on a.id = c.id_activity \n" +
                        "where a.id_program = :id_program and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_program", id_program);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get_sub_prog_level2_budget/{id_program}/{id_role}/{catrole}/{period}/{id_monper}/{year}/{isi_time}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level2_budget(@PathVariable("id_program") String id_program, @PathVariable("id_role") String id_role, @PathVariable("catrole") String catrole, @PathVariable("period") String period, @PathVariable("id_monper") String id_monper, @PathVariable("year") String year, @PathVariable("isi_time") String isi_time) {
        String tg_date_1 = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select a.id, a.nm_activity, a.nm_activity_eng, "
                        + "'' as nama_unit,"
                        + "c.id as id_entry, case when ( COALESCE(NULLIF(c.new_value1,''),c.achievement1) is null) then 0 else COALESCE(NULLIF(c.new_value1,''),c.achievement1) end, case when (COALESCE(NULLIF(c.new_value2,''),c.achievement2) is null) then 0 else COALESCE(NULLIF(c.new_value2,''),c.achievement2) end, case when ( COALESCE(NULLIF(c.new_value3,''),c.achievement3) is null) then 0 else COALESCE(NULLIF(c.new_value3,''),c.achievement3) end, case when ( COALESCE(NULLIF(c.new_value4,''),c.achievement4) is null) then 0 else COALESCE(NULLIF(c.new_value4,''),c.achievement4) end,"
                        + "c.date_created, c.date_created2, c.date_created3, c.date_created4, a.internal_code "
                        + "from gov_activity a \n"
                        + "inner join (select * from entry_gov_budget where year_entry = :year and id_monper = :id_monper "+tg_date_1+") c on a.id = c.id_gov_activity " +
                        "where a.id_program = :id_program and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_program", id_program);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
        }else if(catrole.equals("NSA")){
            String sql  = "select a.id, a.nm_activity, a.nm_activity_eng, "
                        + "'' as nama_unit,"
                        + "c.id as id_entry, case when ( COALESCE(NULLIF(c.new_value1,''),c.achievement1) is null) then 0 else COALESCE(NULLIF(c.new_value1,''),c.achievement1) end, case when (COALESCE(NULLIF(c.new_value2,''),c.achievement2) is null) then 0 else COALESCE(NULLIF(c.new_value2,''),c.achievement2) end, case when ( COALESCE(NULLIF(c.new_value3,''),c.achievement3) is null) then 0 else COALESCE(NULLIF(c.new_value3,''),c.achievement3) end, case when ( COALESCE(NULLIF(c.new_value4,''),c.achievement4) is null) then 0 else COALESCE(NULLIF(c.new_value4,''),c.achievement4) end,"
                        + "c.date_created, c.date_created2, c.date_created3, c.date_created4, a.internal_code "
                        + "from nsa_activity a \n"
                        + "inner join (select * from entry_nsa_budget where year_entry = :year and id_monper = :id_monper "+tg_date_1+") c on a.id = c.id_nsa_activity " +
                        "where a.id_program = :id_program and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_program", id_program);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get_sub_prog_level3/{id_program}/{id_activity}/{id_role}/{catrole}/{period}/{id_monper}/{year}/{isi_time}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level3(@PathVariable("id_program") String id_program, @PathVariable("id_activity") String id_activity, @PathVariable("id_role") String id_role, @PathVariable("catrole") String catrole, @PathVariable("period") String period, @PathVariable("id_monper") String id_monper, @PathVariable("year") String year, @PathVariable("isi_time") String isi_time) {
        String tg_date_1 = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select a.id, a.nm_indicator, a.nm_indicator_eng, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = a.unit) as nama_unit, \n" +
                        "c.id as id_entry, case when (COALESCE(NULLIF(c.new_value1,''),c.achievement1) is null) then 0 else COALESCE(NULLIF(c.new_value1,''),c.achievement1) end, case when ( COALESCE(NULLIF(c.new_value2,''),c.achievement2) is null) then 0 else COALESCE(NULLIF(c.new_value2,''),c.achievement2) end, case when ( COALESCE(NULLIF(c.new_value3,''),c.achievement3) is null) then 0 else COALESCE(NULLIF(c.new_value3,''),c.achievement3) end, case when ( COALESCE(NULLIF(c.new_value4,''),c.achievement4) is null) then 0 else COALESCE(NULLIF(c.new_value4,''),c.achievement4) end,\n" +
                        "c.date_created, c.date_created2, c.date_created3, c.date_created4, a.internal_code \n" +
                        "from gov_indicator a\n" +
                        "left join gov_activity b on a.id_activity = b.id\n" +
                        "inner join (select * from entry_gov_indicator where year_entry = :year and id_monper = :id_monper "+tg_date_1+") c on a.id = c.id_assign\n" +
                        "where a.id_program = :id_program and a.id_activity = :id_activity and b.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_program", id_program);
            query.setParameter("id_activity", id_activity);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
        }else if(catrole.equals("NSA")){
            String sql  = "select a.id, a.nm_indicator, a.nm_indicator_eng, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = a.unit) as nama_unit,\n" +
                        "c.id as id_entry, case when (COALESCE(NULLIF(c.new_value1,''),c.achievement1) is null) then 0 else COALESCE(NULLIF(c.new_value1,''),c.achievement1) end, case when ( COALESCE(NULLIF(c.new_value2,''),c.achievement2) is null) then 0 else COALESCE(NULLIF(c.new_value2,''),c.achievement2) end, case when ( COALESCE(NULLIF(c.new_value3,''),c.achievement3) is null) then 0 else COALESCE(NULLIF(c.new_value3,''),c.achievement3) end, case when ( COALESCE(NULLIF(c.new_value4,''),c.achievement4) is null) then 0 else COALESCE(NULLIF(c.new_value4,''),c.achievement4) end, \n" +
                        "c.date_created, c.date_created2, c.date_created3, c.date_created4, a.internal_code \n" +
                        "from nsa_indicator a\n" +
                        "left join nsa_activity b on a.id_activity = b.id\n" +
                        "inner join (select * from entry_nsa_indicator where year_entry = :year and id_monper = :id_monper "+tg_date_1+") c on a.id = c.id_assign\n" +
                        "where a.id_program = :id_program and a.id_activity = :id_activity and b.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_program", id_program);
            query.setParameter("id_activity", id_activity);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    
    @GetMapping("admin/get_sub_prog_level1_catrole/{id_monper}/{catrole}/{id_prov}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level1_catrole(@PathVariable("id_monper") String id_monper, @PathVariable("catrole") String catrole, @PathVariable("id_prov") String id_prov) {
        System.out.println("catrole = "+catrole+", id_monper = "+id_monper);
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code as kode_program\n" +
                        "from ref_role a\n" +
                        "inner join gov_activity b on a.id_role = b.id_role\n" +
                        "left join ( select * from gov_program where id_monper = :id_monper ) c on b.id_program = c.id\n" +
                        "inner join gov_indicator d on b.id = d.id_activity\n" +
                        "where a.cat_role = :catrole and a.id_prov = :id_prov and c.id is not null";
            query = em.createNativeQuery(sql);
            query.setParameter("catrole", catrole);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prov", id_prov);
        }else if(catrole.equals("NSA")){
            String sql  = "select distinct b.id, b.nm_program, b.nm_program_eng, b.internal_code as kode_program\n" +
                        "from ref_role a\n" +
                        "inner join ( select * from nsa_program where id_monper = :id_monper ) b on a.id_role = b.id_role\n" +
                        "inner join nsa_activity c on b.id = c.id_program\n" +
                        "inner join nsa_indicator d on c.id = d.id_activity\n" +
                        "where a.cat_role = :catrole and a.id_prov = :id_prov and b.id is not null";
            query = em.createNativeQuery(sql);
            query.setParameter("catrole", catrole);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prov", id_prov);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    } 
    
    @GetMapping("admin/get_sub_prog_level2_catrole/{id_program}/{catrole}/{id_prov}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level2_catrole(@PathVariable("id_program") String id_program, @PathVariable("catrole") String catrole, @PathVariable("id_prov") String id_prov) {
        
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select distinct b.id, b.nm_activity, b.nm_activity_eng, b.internal_code as kode_activity\n" +
                        "from ref_role a\n" +
                        "inner join gov_activity b on a.id_role = b.id_role\n" +
                        "inner join gov_indicator c on b.id = c.id_activity\n" +
                        "where a.cat_role = :catrole and a.id_prov = :id_prov and b.id_program = :id_program and b.id is not null";
            query = em.createNativeQuery(sql);
            query.setParameter("catrole", catrole);
            query.setParameter("id_prov", id_prov);
            query.setParameter("id_program", id_program);
        }else if(catrole.equals("NSA")){
            String sql  = "select distinct b.id, b.nm_activity, b.nm_activity_eng, b.internal_code as kode_activity\n" +
                        "from ref_role a\n" +
                        "inner join nsa_activity b on a.id_role = b.id_role\n" +
                        "inner join nsa_indicator c on b.id = c.id_activity\n" +
                        "where a.cat_role = :catrole and a.id_prov = :id_prov and b.id_program = :id_program and b.id is not null";
            query = em.createNativeQuery(sql);
            query.setParameter("catrole", catrole);
            query.setParameter("id_prov", id_prov);
            query.setParameter("id_program", id_program);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get_sub_prog_level2_catrole_budget/{id_program}/{id_prov}/{catrole}/{period}/{id_monper}/{year}/{isi_time}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level2_catrole_budget(@PathVariable("id_program") String id_program, @PathVariable("id_prov") String id_prov, @PathVariable("catrole") String catrole, @PathVariable("period") String period, @PathVariable("id_monper") String id_monper, @PathVariable("year") String year, @PathVariable("isi_time") String isi_time) {
        String tg_date_1 = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select b.id, b.nm_activity, b.nm_activity_eng, '' as nama_unit, c.id as id_entry, \n" +
                        "case when ( COALESCE(NULLIF(c.new_value1,''),c.achievement1) is null) then 0 else COALESCE(NULLIF(c.new_value1,''),c.achievement1) end, case when (COALESCE(NULLIF(c.new_value2,''),c.achievement2) is null) then 0 else COALESCE(NULLIF(c.new_value2,''),c.achievement2) end, case when ( COALESCE(NULLIF(c.new_value3,''),c.achievement3) is null) then 0 else COALESCE(NULLIF(c.new_value3,''),c.achievement3) end, case when ( COALESCE(NULLIF(c.new_value4,''),c.achievement4) is null) then 0 else COALESCE(NULLIF(c.new_value4,''),c.achievement4) end,\n" +
                        "c.date_created, c.date_created2, c.date_created3, c.date_created4, b.internal_code as kode_activity \n" +
                        "from ref_role a\n" +
                        "inner join gov_activity b on a.id_role = b.id_role\n" +
                        "left join (select * from entry_gov_budget where year_entry = :year and id_monper = :id_monper "+tg_date_1+") c on b.id = c.id_gov_activity \n" +
                        "where a.cat_role = :catrole and a.id_prov = :id_prov and b.id_program = :id_program ";
            query = em.createNativeQuery(sql);
            query.setParameter("catrole", catrole);
            query.setParameter("id_prov", id_prov);
            query.setParameter("id_program", id_program);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
        }else if(catrole.equals("NSA")){
            String sql  = "select b.id, b.nm_activity, b.nm_activity_eng, '' as nama_unit, c.id as id_entry, \n" +
                        "case when ( COALESCE(NULLIF(c.new_value1,''),c.achievement1) is null) then 0 else COALESCE(NULLIF(c.new_value1,''),c.achievement1) end, case when (COALESCE(NULLIF(c.new_value2,''),c.achievement2) is null) then 0 else COALESCE(NULLIF(c.new_value2,''),c.achievement2) end, case when ( COALESCE(NULLIF(c.new_value3,''),c.achievement3) is null) then 0 else COALESCE(NULLIF(c.new_value3,''),c.achievement3) end, case when ( COALESCE(NULLIF(c.new_value4,''),c.achievement4) is null) then 0 else COALESCE(NULLIF(c.new_value4,''),c.achievement4) end,\n" +
                        "c.date_created, c.date_created2, c.date_created3, c.date_created4, b.internal_code as kode_activity\n" +
                        "from ref_role a\n" +
                        "inner join nsa_activity b on a.id_role = b.id_role\n" +
                        "left join (select * from entry_nsa_budget where year_entry = :year and id_monper = :id_monper "+tg_date_1+") c on b.id = c.id_nsa_activity \n" +
                        "where a.cat_role = :catrole and a.id_prov = :id_prov and b.id_program = :id_program ";
            query = em.createNativeQuery(sql);
            query.setParameter("catrole", catrole);
            query.setParameter("id_prov", id_prov);
            query.setParameter("id_program", id_program);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    
    @GetMapping("admin/get_sub_prog_level3_catrole/{id_program}/{id_activity}/{id_prov}/{catrole}/{period}/{id_monper}/{year}/{isi_time}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level3_catrole(@PathVariable("id_program") String id_program, @PathVariable("id_activity") String id_activity, @PathVariable("id_prov") String id_prov, @PathVariable("catrole") String catrole, @PathVariable("period") String period, @PathVariable("id_monper") String id_monper, @PathVariable("year") String year, @PathVariable("isi_time") String isi_time) {
        String tg_date_1 = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select c.id, c.nm_indicator, c.nm_indicator_eng,\n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = c.unit) as nama_unit,\n" +
                        "d.id as id_entry, case when (COALESCE(NULLIF(d.new_value1,''),d.achievement1) is null) then 0 else COALESCE(NULLIF(d.new_value1,''),d.achievement1) end, case when ( COALESCE(NULLIF(d.new_value2,''),d.achievement2) is null) then 0 else COALESCE(NULLIF(d.new_value2,''),d.achievement2) end, case when ( COALESCE(NULLIF(d.new_value3,''),d.achievement3) is null) then 0 else COALESCE(NULLIF(d.new_value3,''),d.achievement3) end, case when ( COALESCE(NULLIF(d.new_value4,''),d.achievement4) is null) then 0 else COALESCE(NULLIF(d.new_value4,''),d.achievement4) end,\n" +
                        "d.date_created, d.date_created2, d.date_created3, d.date_created4,\n" +
                        "c.internal_code as kode_indicator\n" +
                        "from ref_role a\n" +
                        "inner join gov_activity b on a.id_role = b.id_role\n" +
                        "inner join gov_indicator c on b.id = c.id_activity\n" +
                        "left join (select * from entry_gov_indicator where year_entry = :year and id_monper = :id_monper "+tg_date_1+") d on c.id = d.id_assign\n" +
                        "where a.cat_role = :catrole and a.id_prov = :id_prov and c.id_program = :id_program and c.id_activity = :id_activity ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("id_program", id_program);
            query.setParameter("id_activity", id_activity);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
            query.setParameter("catrole", catrole);
        }else if(catrole.equals("NSA")){
            String sql  = "select c.id, c.nm_indicator, c.nm_indicator_eng,\n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = c.unit) as nama_unit,\n" +
                        "d.id as id_entry, case when (COALESCE(NULLIF(d.new_value1,''),d.achievement1) is null) then 0 else COALESCE(NULLIF(d.new_value1,''),d.achievement1) end, case when ( COALESCE(NULLIF(d.new_value2,''),d.achievement2) is null) then 0 else COALESCE(NULLIF(d.new_value2,''),d.achievement2) end, case when ( COALESCE(NULLIF(d.new_value3,''),d.achievement3) is null) then 0 else COALESCE(NULLIF(d.new_value3,''),d.achievement3) end, case when ( COALESCE(NULLIF(d.new_value4,''),d.achievement4) is null) then 0 else COALESCE(NULLIF(d.new_value4,''),d.achievement4) end,\n" +
                        "d.date_created, d.date_created2, d.date_created3, d.date_created4,\n" +
                        "c.internal_code as kode_indicator\n" +
                        "from ref_role a\n" +
                        "inner join nsa_activity b on a.id_role = b.id_role\n" +
                        "inner join nsa_indicator c on b.id = c.id_activity\n" +
                        "left join (select * from entry_nsa_indicator where year_entry = :year and id_monper = :id_monper "+tg_date_1+") d on c.id = d.id_assign\n" +
                        "where a.cat_role = :catrole and a.id_prov = :id_prov and c.id_program = :id_program and c.id_activity = :id_activity ";
            query = em.createNativeQuery(sql);
            query.setParameter("catrole", catrole);
            query.setParameter("id_prov", id_prov);
            query.setParameter("id_program", id_program);
            query.setParameter("id_activity", id_activity);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }  
    
    
    @GetMapping("admin/get-jumlah-catrole/{year}/{period}/{type}/{tb}/{tb2}/{isi_time}/{id_monper}/{catrole}/{id_prov}")
    public @ResponseBody Map<String, Object>  get_jumlah_catrole(@PathVariable("year") String year, @PathVariable("period") String period, @PathVariable("type") String type, @PathVariable("tb") String tb, @PathVariable("tb2") String tb2, @PathVariable("isi_time") String isi_time, @PathVariable("id_monper") String id_monper, @PathVariable("catrole") String catrole, @PathVariable("id_prov") String id_prov) {
        String tg_date = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        String prog = "";
        String id_indi = "";
        if(type.equals("entry_gov_indicator")){
            prog = "gov_program";
            id_indi = "d.id_gov_indicator";
        }else{
            prog = "nsa_program";
            id_indi = "d.id_nsa_indicator";
        }
        String sql  = "select count(*) as tot from\n" +
                    "(\n" +
                    "select a.id_role, a.nm_role, a.cat_role, a.id_prov, b.id as id_acti, b.id_activity as kode_activity, \n" +
                    "b.nm_activity, b.nm_activity_eng, c.id as id_prog, c.id_program as kode_program, c.nm_program, c.nm_program_eng,\n" +
                    "d.id as id_indi, "+id_indi+" as kode_indicator, d.nm_indicator, d.nm_indicator_eng, e.nm_unit,\n" +
                    "f.id as id_entry, COALESCE(NULLIF(f.new_value"+period+",''),f.achievement"+period+") as realisasi\n" +
                    "from ref_role a\n" +
                    "inner join "+tb+" b on a.id_role = b.id_role\n" +
                    "left join ( select * from "+prog+" where id_monper = :id_monper ) c on b.id_program = c.id\n" +
                    "inner join "+tb2+" d on b.id = d.id_activity\n" +
                    "left join ref_unit e on d.unit = e.id_unit\n" +
                    "left join (select * from "+type+" where year_entry = :year and id_monper = :id_monper "+tg_date+") f on d.id = f.id_assign\n" +
                    "where a.cat_role = :catrole and a.id_prov = :id_prov \n" +
                    ") as jml";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
//        query.setParameter("id_role", id_role);
        query.setParameter("year", year);
        query.setParameter("catrole", catrole);
        query.setParameter("id_prov", id_prov);
//        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/get-jumlah-catrole-budget/{year}/{period}/{type}/{tb}/{tb2}/{isi_time}/{id_monper}/{catrole}/{id_prov}")
    public @ResponseBody Map<String, Object>  get_jumlah_catrole_budget(@PathVariable("year") String year, @PathVariable("period") String period, @PathVariable("type") String type, @PathVariable("tb") String tb, @PathVariable("tb2") String tb2, @PathVariable("isi_time") String isi_time, @PathVariable("id_monper") String id_monper, @PathVariable("catrole") String catrole, @PathVariable("id_prov") String id_prov) {
        String tg_date = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        String prog = "";
        String id_indi = "";
        if(type.equals("entry_gov_budget")){
            prog = "gov_program";
            id_indi = "f.id_gov_activity";
        }else{
            prog = "nsa_program";
            id_indi = "f.id_nsa_activity";
        }
        String sql  = "select count(*) as tot from\n" +
                    "(\n" +
                    "select a.id_role, a.nm_role, a.cat_role, a.id_prov, b.id as id_acti, b.id_activity as kode_activity, \n" +
                    "b.nm_activity, b.nm_activity_eng, c.id as id_prog, c.id_program as kode_program, c.nm_program, c.nm_program_eng,\n" +
                    "f.id as id_entry, COALESCE(NULLIF(f.new_value"+period+",''),f.achievement"+period+") as realisasi\n" +
                    "from ref_role a\n" +
                    "inner join "+tb+" b on a.id_role = b.id_role\n" +
                    "inner join ( select * from "+prog+" where id_monper = :id_monper ) c on b.id_program = c.id\n" +
                    "left join (select * from "+type+" where year_entry = :year and id_monper = :id_monper "+tg_date+") f on b.id = "+id_indi+" \n" +
                    "where a.cat_role = :catrole and a.id_prov = :id_prov \n" +
                    ") as jml";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
//        query.setParameter("id_role", id_role);
        query.setParameter("year", year);
        query.setParameter("catrole", catrole);
        query.setParameter("id_prov", id_prov);
//        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-submission/{dat_id_indicator}/{dat_achievement}/{dat_entry}/{period}/{catrole}/{id_monper}/{tahun}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Transactional
    public void saveBest(
                        @PathVariable("dat_id_indicator") String dat_id_indicator,
			@PathVariable("dat_achievement") String dat_achievement,
			@PathVariable("dat_entry") String dat_entry,
                        @PathVariable("period") String period,
                        @PathVariable("catrole") String catrole,
                        @PathVariable("id_monper") int id_monper,
                        @PathVariable("tahun") int tahun) {
        System.out.println("data "+dat_id_indicator+" - "+dat_achievement+" - "+dat_entry);
        Query query;
        Query query_d;
        if(catrole.equals("Government")){
            if(!dat_id_indicator.equals("0")) {
                String[] data_indicator      = dat_id_indicator.split(",");
                String[] data_achievement    = dat_achievement.split(",");
                String[] data_entry          = dat_entry.split(",");
                for(int i=0;i<data_entry.length;i++) {
                    System.out.println("coba : "+data_entry[i]);
                    if(!data_entry[i].equals("null")) {
                        System.out.println("tidak null : "+data_entry[i]);
                        query = em.createNativeQuery("update entry_gov_indicator set achievement"+period+" = :achievement where id=:id");
//                            query.setParameter("created_by", data_indicator[i]);  
                        query.setParameter("achievement", data_achievement[i]);
                        query.setParameter("id", data_entry[i]);
                        query.executeUpdate();
                    }else {
                        System.out.println("null : "+data_entry[i]);
                        if(period.equals("1")) {
                            System.out.println("ke 1");
                            EntryGovIndicator entryGovIndicator = new EntryGovIndicator();
//                            entryGovIndicator.setId(null);
                            entryGovIndicator.setId_assign(Integer.parseInt(data_indicator[i]));
                            entryGovIndicator.setAchievement1(Integer.parseInt(data_achievement[i]));
                            entryGovIndicator.setYear_entry(tahun);
                            entryGovIndicator.setDate_created(new Date());
                            entryGovIndicator.setId_monper(id_monper);
                            entrySdgService.saveEntryGovIndicator(entryGovIndicator);
                        }else if(period.equals("2")) {
                            System.out.println("ke 2");
                            EntryGovIndicator entryGovIndicator = new EntryGovIndicator();
//                            entryGovIndicator.setId(null);
                            entryGovIndicator.setId_assign(Integer.parseInt(data_indicator[i]));
                            entryGovIndicator.setAchievement2(Integer.parseInt(data_achievement[i]));
                            entryGovIndicator.setYear_entry(tahun);
                            entryGovIndicator.setDate_created(new Date());
                            entryGovIndicator.setId_monper(id_monper);
                            entrySdgService.saveEntryGovIndicator(entryGovIndicator);
                        }else if(period.equals("3")) {
                            System.out.println("ke 3");
                            EntryGovIndicator entryGovIndicator = new EntryGovIndicator();
//                            entryGovIndicator.setId(null);
                            entryGovIndicator.setId_assign(Integer.parseInt(data_indicator[i]));
                            entryGovIndicator.setAchievement3(Integer.parseInt(data_achievement[i]));
                            entryGovIndicator.setYear_entry(tahun);
                            entryGovIndicator.setDate_created(new Date());
                            entryGovIndicator.setId_monper(id_monper);
                            entrySdgService.saveEntryGovIndicator(entryGovIndicator);
                        }else if(period.equals("4")) {
                            System.out.println("ke 4");
                            EntryGovIndicator entryGovIndicator = new EntryGovIndicator();
//                            entryGovIndicator.setId(null);
                            entryGovIndicator.setId_assign(Integer.parseInt(data_indicator[i]));
                            entryGovIndicator.setAchievement4(Integer.parseInt(data_achievement[i]));
                            entryGovIndicator.setYear_entry(tahun);
                            entryGovIndicator.setDate_created(new Date());
                            entryGovIndicator.setId_monper(id_monper);
                            entrySdgService.saveEntryGovIndicator(entryGovIndicator);
                        }else{
                            System.out.println("ke gak ada");
                        };
                    }
                }
            }
            
        }else if(catrole.equals("NSA")){
            if(!dat_id_indicator.equals("0")) {
                String[] data_indicator      = dat_id_indicator.split(",");
                String[] data_achievement    = dat_achievement.split(",");
                String[] data_entry          = dat_entry.split(",");
                for(int i=0;i<data_entry.length;i++) {
                    System.out.println("coba : "+data_entry[i]);
                    if(!data_entry[i].equals("null")) {
                        System.out.println("tidak null : "+data_entry[i]);
                        query = em.createNativeQuery("update entry_nsa_indicator set achievement"+period+" = :achievement where id=:id");
//                            query.setParameter("created_by", data_indicator[i]);  
                        query.setParameter("achievement", data_achievement[i]);
                        query.setParameter("id", data_entry[i]);
                        query.executeUpdate();
                    }else {
                        System.out.println("null : "+data_entry[i]);
                        if(period.equals("1")) {
                            System.out.println("ke 1");
                            EntryNsaIndicator entryNsaIndicator = new EntryNsaIndicator();
//                            entryGovIndicator.setId(null);
                            entryNsaIndicator.setId_assign(Integer.parseInt(data_indicator[i]));
                            entryNsaIndicator.setAchievement1(Integer.parseInt(data_achievement[i]));
                            entryNsaIndicator.setYear_entry(tahun);
                            entryNsaIndicator.setDate_created(new Date());
                            entryNsaIndicator.setId_monper(id_monper);
                            entrySdgService.saveEntryNsaIndicator(entryNsaIndicator);
                        }else if(period.equals("2")) {
                            System.out.println("ke 2");
                            EntryNsaIndicator entryNsaIndicator = new EntryNsaIndicator();
//                            entryGovIndicator.setId(null);
                            entryNsaIndicator.setId_assign(Integer.parseInt(data_indicator[i]));
                            entryNsaIndicator.setAchievement2(Integer.parseInt(data_achievement[i]));
                            entryNsaIndicator.setYear_entry(tahun);
                            entryNsaIndicator.setDate_created(new Date());
                            entryNsaIndicator.setId_monper(id_monper);
                            entrySdgService.saveEntryNsaIndicator(entryNsaIndicator);
                        }else if(period.equals("3")) {
                            System.out.println("ke 3");
                            EntryNsaIndicator entryNsaIndicator = new EntryNsaIndicator();
//                            entryGovIndicator.setId(null);
                            entryNsaIndicator.setId_assign(Integer.parseInt(data_indicator[i]));
                            entryNsaIndicator.setAchievement3(Integer.parseInt(data_achievement[i]));
                            entryNsaIndicator.setYear_entry(tahun);
                            entryNsaIndicator.setDate_created(new Date());
                            entryNsaIndicator.setId_monper(id_monper);
                            entrySdgService.saveEntryNsaIndicator(entryNsaIndicator);
                        }else if(period.equals("4")) {
                            System.out.println("ke 4");
                            EntryNsaIndicator entryNsaIndicator = new EntryNsaIndicator();
//                            entryGovIndicator.setId(null);
                            entryNsaIndicator.setId_assign(Integer.parseInt(data_indicator[i]));
                            entryNsaIndicator.setAchievement4(Integer.parseInt(data_achievement[i]));
                            entryNsaIndicator.setYear_entry(tahun);
                            entryNsaIndicator.setDate_created(new Date());
                            entryNsaIndicator.setId_monper(id_monper);
                            entrySdgService.saveEntryNsaIndicator(entryNsaIndicator);
                        }else{
                            System.out.println("ke gak ada");
                        };
                    }
                }
            }
        }
        
    }
    
    @PostMapping(path = "admin/save-submission-budget/{dat_id_indicator}/{dat_achievement}/{dat_entry}/{period}/{catrole}/{id_monper}/{tahun}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Transactional
    public void saveBestbudget(
                        @PathVariable("dat_id_indicator") String dat_id_indicator,
			@PathVariable("dat_achievement") String dat_achievement,
			@PathVariable("dat_entry") String dat_entry,
                        @PathVariable("period") String period,
                        @PathVariable("catrole") String catrole,
                        @PathVariable("id_monper") int id_monper,
                        @PathVariable("tahun") int tahun) {
        System.out.println("data "+dat_id_indicator+" - "+dat_achievement+" - "+dat_entry);
        Query query;
        Query query_d;
        if(catrole.equals("Government")){
            if(!dat_id_indicator.equals("0")) {
                String[] data_indicator      = dat_id_indicator.split(",");
                String[] data_achievement    = dat_achievement.split(",");
                String[] data_entry          = dat_entry.split(",");
                for(int i=0;i<data_entry.length;i++) {
                    System.out.println("coba : "+data_entry[i]);
                    if(!data_entry[i].equals("null")) {
                        System.out.println("tidak null : "+data_entry[i]);
                        query = em.createNativeQuery("update entry_gov_budget set achievement"+period+" = :achievement where id=:id");
//                            query.setParameter("created_by", data_indicator[i]);  
                        query.setParameter("achievement", data_achievement[i]);
                        query.setParameter("id", data_entry[i]);
                        query.executeUpdate();
                    }else {
                        System.out.println("null : "+data_entry[i]);
                        if(period.equals("1")) {
                            System.out.println("ke 1");
                            EntryGovBudget entryGovBudget = new EntryGovBudget();
//                            entryGovIndicator.setId(null);
                            entryGovBudget.setId_gov_activity(Integer.parseInt(data_indicator[i]));
                            entryGovBudget.setAchievement1(Integer.parseInt(data_achievement[i]));
                            entryGovBudget.setYear_entry(tahun);
                            entryGovBudget.setDate_created(new Date());
                            entryGovBudget.setId_monper(id_monper);
                            entrySdgService.saveEntryGovBudget(entryGovBudget);
                        }else if(period.equals("2")) {
                            System.out.println("ke 2");
                            EntryGovBudget entryGovBudget = new EntryGovBudget();
//                            entryGovIndicator.setId(null);
                            entryGovBudget.setId_gov_activity(Integer.parseInt(data_indicator[i]));
                            entryGovBudget.setAchievement2(Integer.parseInt(data_achievement[i]));
                            entryGovBudget.setYear_entry(tahun);
                            entryGovBudget.setDate_created(new Date());
                            entryGovBudget.setId_monper(id_monper);
                            entrySdgService.saveEntryGovBudget(entryGovBudget);
                        }else if(period.equals("3")) {
                            System.out.println("ke 3");
                            EntryGovBudget entryGovBudget = new EntryGovBudget();
//                            entryGovIndicator.setId(null);
                            entryGovBudget.setId_gov_activity(Integer.parseInt(data_indicator[i]));
                            entryGovBudget.setAchievement3(Integer.parseInt(data_achievement[i]));
                            entryGovBudget.setYear_entry(tahun);
                            entryGovBudget.setDate_created(new Date());
                            entryGovBudget.setId_monper(id_monper);
                            entrySdgService.saveEntryGovBudget(entryGovBudget);
                        }else if(period.equals("4")) {
                            System.out.println("ke 4");
                            EntryGovBudget entryGovBudget = new EntryGovBudget();
//                            entryGovIndicator.setId(null);
                            entryGovBudget.setId_gov_activity(Integer.parseInt(data_indicator[i]));
                            entryGovBudget.setAchievement4(Integer.parseInt(data_achievement[i]));
                            entryGovBudget.setYear_entry(tahun);
                            entryGovBudget.setDate_created(new Date());
                            entryGovBudget.setId_monper(id_monper);
                            entrySdgService.saveEntryGovBudget(entryGovBudget);
                        }else{
                            System.out.println("ke gak ada");
                        };
                    }
                }
            }
            
        }else if(catrole.equals("NSA")){
            if(!dat_id_indicator.equals("0")) {
                String[] data_indicator      = dat_id_indicator.split(",");
                String[] data_achievement    = dat_achievement.split(",");
                String[] data_entry          = dat_entry.split(",");
                for(int i=0;i<data_entry.length;i++) {
                    System.out.println("coba : "+data_entry[i]);
                    if(!data_entry[i].equals("null")) {
                        System.out.println("tidak null : "+data_entry[i]);
                        query = em.createNativeQuery("update entry_nsa_budget set achievement"+period+" = :achievement where id=:id");
//                            query.setParameter("created_by", data_indicator[i]);  
                        query.setParameter("achievement", data_achievement[i]);
                        query.setParameter("id", data_entry[i]);
                        query.executeUpdate();
                    }else {
                        System.out.println("null : "+data_entry[i]);
                        if(period.equals("1")) {
                            System.out.println("ke 1");
                            EntryNsaBudget entryNsaBudget = new EntryNsaBudget();
                            System.out.println("nilai = "+data_achievement[i]);
                            entryNsaBudget.setId_nsa_activity(Integer.parseInt(data_indicator[i]));
                            entryNsaBudget.setAchievement1(Integer.parseInt(data_achievement[i]));
                            entryNsaBudget.setYear_entry(tahun);
                            entryNsaBudget.setDate_created(new Date());
                            entryNsaBudget.setId_monper(id_monper);
                            entrySdgService.saveEntryNsaBudget(entryNsaBudget);
                        }else if(period.equals("2")) {
                            System.out.println("ke 2");
                            EntryNsaBudget entryNsaBudget = new EntryNsaBudget();
//                            entryGovIndicator.setId(null);
                            entryNsaBudget.setId_nsa_activity(Integer.parseInt(data_indicator[i]));
                            entryNsaBudget.setAchievement2(Integer.parseInt(data_achievement[i]));
                            entryNsaBudget.setYear_entry(tahun);
                            entryNsaBudget.setDate_created(new Date());
                            entryNsaBudget.setId_monper(id_monper);
                            entrySdgService.saveEntryNsaBudget(entryNsaBudget);
                        }else if(period.equals("3")) {
                            System.out.println("ke 3");
                            EntryNsaBudget entryNsaBudget = new EntryNsaBudget();
//                            entryGovIndicator.setId(null);
                            entryNsaBudget.setId_nsa_activity(Integer.parseInt(data_indicator[i]));
                            entryNsaBudget.setAchievement3(Integer.parseInt(data_achievement[i]));
                            entryNsaBudget.setYear_entry(tahun);
                            entryNsaBudget.setDate_created(new Date());
                            entryNsaBudget.setId_monper(id_monper);
                            entrySdgService.saveEntryNsaBudget(entryNsaBudget);
                        }else if(period.equals("4")) {
                            System.out.println("ke 4");
                            EntryNsaBudget entryNsaBudget = new EntryNsaBudget();
//                            entryGovIndicator.setId(null);
                            entryNsaBudget.setId_nsa_activity(Integer.parseInt(data_indicator[i]));
                            entryNsaBudget.setAchievement4(Integer.parseInt(data_achievement[i]));
                            entryNsaBudget.setYear_entry(tahun);
                            entryNsaBudget.setDate_created(new Date());
                            entryNsaBudget.setId_monper(id_monper);
                            entrySdgService.saveEntryNsaBudget(entryNsaBudget);
                        }else{
                            System.out.println("ke gak ada");
                        };
                    }
                }
            }
        }
        
    }
    
}
