package com.jica.sdg.controller;

import com.jica.sdg.model.EntryApproval;
import com.jica.sdg.model.EntryGovBudget;
import com.jica.sdg.model.EntryGovIndicator;
import com.jica.sdg.model.EntryNsaBudget;
import com.jica.sdg.model.EntryNsaIndicator;
import com.jica.sdg.model.EntryShowReport;
import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.NsaProgram;

import com.jica.sdg.model.GovActivity;
import com.jica.sdg.model.NsaActivity;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import com.jica.sdg.service.*;

import java.util.*;

import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgFunding;
import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.model.SdgIndicatorTarget;
import com.jica.sdg.model.SdgTarget;
import com.jica.sdg.service.IEntrySdgService;
import com.jica.sdg.service.ISdgIndicatorService;
import com.jica.sdg.service.IGovProgramService;
import com.jica.sdg.service.INsaProgramService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRanRadService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.NsaProfileService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.servlet.http.HttpSession;
//import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

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
        String sql  = "select * from ran_rad as a where a.id_prov = :id ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
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
    
    @GetMapping("admin/list-entry-sdg/{id_prov}/{id_role}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> listEntrySdg(@PathVariable("id_prov") String id_prov, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
        String sql  = "select a.id_goals, a.id_target, a.id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, h.nm_unit, d.increment_decrement, e.value,\n" +
                    "f.achievement1, f.achievement2, f.achievement3, f.achievement4, g.sdg_indicator, f.id as id_target_1, b.id_goals as kode_goals, b.nm_goals_eng, \n" +
                    "c.id_target as kode_target, c.nm_target_eng, d.id_indicator as kode_indicator, d.nm_indicator_eng, \n" +
                    "f.new_value1, f.new_value2, f.new_value3, f.new_value4 \n" +
                    "from assign_sdg_indicator as a\n" +
                    "left join sdg_goals as b on a.id_goals = b.id \n" +
                    "left join sdg_target as c on a.id_target = c.id \n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id \n" +
                    "left join \n" +
                    "(select id_sdg_indicator, id_role, year, value from sdg_indicator_target where id_role = :id_role and year = :year) as e on d.id = e.id_sdg_indicator \n" +
                    "left join \n" +
                    "(select * from entry_sdg where year_entry = :year and id_role = :id_role and id_monper = :id_monper) as f on d.id = f.id_sdg_indicator \n" +
                    "left join ran_rad as g on a.id_monper = g.id_monper \n" +
                    "left join ref_unit as h on d.unit = h.id_unit \n" +
                    "where a.id_role = :id_role and a.id_monper = :id_monper and a.id_prov = :id_prov ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-entry-sdg", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntrySdg(@RequestBody EntrySdg entrySdg) {
        String id_sdg_indicator = entrySdg.getId_sdg_indicator();
        int achievement1        = entrySdg.getAchievement1();
        int achievement2        = entrySdg.getAchievement2();
        int achievement3        = entrySdg.getAchievement3();
        int achievement4        = entrySdg.getAchievement4();
        int year_entry          = entrySdg.getYear_entry();
        int id_role             = entrySdg.getId_role();
        int id_monper           = entrySdg.getId_monper();
        entrySdgService.saveEntrySdg(entrySdg);
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
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
        return "admin/dataentry/govprogram";
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
                    "b.new_value1, b.new_value2, b.new_value3, b.new_value4 \n" +
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
        rp.setPeriod("1");
        approvalService.updatedoneApproveGovBudget(id_monper, year, type, periode);
        approvalService.saveshow(rp);
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
        String sql  = "select approval from entry_approval as a where a.id_role = :id_role and a.id_monper = :id_monper and a.year = :year and a.type = :type and periode = :periode ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        query.setParameter("type", type);
        query.setParameter("periode", periode);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-entry-gov_prog_indicator", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntryGovProgIndicator(@RequestBody EntryGovIndicator entryGovIndicator) {
//        String id_sdg_indicator = entrySdg.getId_sdg_indicator();
//        int achievement1        = entrySdg.getAchievement1();
//        int achievement2        = entrySdg.getAchievement2();
//        int achievement3        = entrySdg.getAchievement3();
//        int achievement4        = entrySdg.getAchievement4();
//        int year_entry          = entrySdg.getYear_entry();
//        int id_role             = entrySdg.getId_role();
//        int id_monper           = entrySdg.getId_monper();
        entrySdgService.saveEntryGovIndicator(entryGovIndicator);
//        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
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
                    "b.new_value1, b.new_value2, b.new_value3, b.new_value4 \n" +
                    "from gov_indicator as a \n" +
                    "left join (select * from gov_activity where id_role = :id_role ) as d on a.id_activity = d.id \n" +
                    "left join (select * from gov_program ) as e on a.id_program = e.id \n" +
                    "left join (select * from entry_gov_indicator where id_monper = :id_monper and year_entry = :tahun ) as b on a.id = b.id_assign \n" +
                    "left join (select * from gov_target where id_role = :id_role and year = :tahun )  as c on a.id = c.id_gov_indicator \n" +
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
        return "admin/dataentry/nongovprogram";
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
                    "b.new_value1, b.new_value2, b.new_value3, b.new_value4 \n" +
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
    
    
    @PostMapping(path = "admin/save-entry-non-gov_prog_indicator", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntryNonGovProgIndicator(@RequestBody EntryNsaIndicator entryNsaIndicator) {
//        String id_sdg_indicator = entrySdg.getId_sdg_indicator();
//        int achievement1        = entrySdg.getAchievement1();
//        int achievement2        = entrySdg.getAchievement2();
//        int achievement3        = entrySdg.getAchievement3();
//        int achievement4        = entrySdg.getAchievement4();
//        int year_entry          = entrySdg.getYear_entry();
//        int id_role             = entrySdg.getId_role();
//        int id_monper           = entrySdg.getId_monper();
        entrySdgService.saveEntryNsaIndicator(entryNsaIndicator);
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
                    "b.new_value1, b.new_value2, b.new_value3, b.new_value4 \n" +
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
        String sql  = "select a.id_goals, a.id_target, a.id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, d.unit, d.increment_decrement, e.value,\n" +
                    "g.sdg_indicator, b.id_goals as kode_goals, b.nm_goals_eng, \n" +
                    "c.id_target as kode_target, c.nm_target_eng, d.id_indicator as kode_indicator, d.nm_indicator_eng, h.nm_unit \n" +
                    "from assign_sdg_indicator as a\n" +
                    "left join sdg_goals as b on a.id_goals = b.id \n" +
                    "left join sdg_target as c on a.id_target = c.id \n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id \n" +
                    "left join \n" +
                    "(select id_sdg_indicator, id_role, year, value from sdg_indicator_target where id_role = :id_role and year = :year) as e on d.id = e.id_sdg_indicator \n" +
                    "left join ran_rad as g on a.id_monper = g.id_monper \n" +
                    "left join ref_unit as h on d.unit = h.id_unit \n" +
                    "where a.id_role = :id_role and a.id_monper = :id_monper and a.id_prov = :id_prov ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/get-sdgTargetIndicator/{id_indicator}/{id_role}/{year}")
    public @ResponseBody Map<String, Object> getSdgTarget(@PathVariable("id_indicator") String id_indicator, @PathVariable("id_role") String id_role, @PathVariable("year") String year) {
        String sql  = "select id_sdg_indicator, id_role, year, value from sdg_indicator_target where id_sdg_indicator = :id_indicator and id_role = :id_role and year = :year";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        query.setParameter("id_indicator", id_indicator);
        query.setParameter("year", year);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-sdgTargetIndicator/{id_indicator}/{id_role}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	@Transactional
	public void saveSdgTarget(@RequestBody Map<String, Object> payload,@PathVariable("id_indicator") Integer id_indicator,@PathVariable("id_role") Integer id_role) {
    	JSONObject jsonObject = new JSONObject(payload);
        JSONObject catatan = jsonObject.getJSONObject("target");
        JSONArray c = catatan.getJSONArray("target");
        em.createNativeQuery("delete from sdg_indicator_target where id_sdg_indicator ='"+id_indicator+"' and id_role = '"+id_role+"'").executeUpdate();
        for (int i = 0 ; i < c.length(); i++) {
        	JSONObject obj = c.getJSONObject(i);
        	String year = obj.getString("year");
        	String value = obj.getString("nilai");
        	if(!value.equals("")) {
        		em.createNativeQuery("INSERT INTO sdg_indicator_target (id_sdg_indicator,id_role,year,value) values ('"+id_indicator+"','"+id_role+"','"+year+"','"+value+"')").executeUpdate();
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
        em.createNativeQuery("delete from sdg_funding where id_sdg_indicator ='"+id_sdg_indicator+"'").executeUpdate();
        em.createNativeQuery("INSERT INTO sdg_funding (id_sdg_indicator,baseline,funding_source) values ('"+id_sdg_indicator+"','"+baseline+"','"+funding_source+"')").executeUpdate();
    }
    
    @GetMapping("admin/get-sdgFunding/{id_indicator}")
    public @ResponseBody Map<String, Object> getSdgTarget(@PathVariable("id_indicator") String id_indicator) {
        String sql  = "select baseline, funding_source from sdg_funding where id_sdg_indicator = :id_indicator";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_indicator", id_indicator);
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
        model.addAttribute("title", "SDG Indicators Monitoring");
        sdgIndicator.ifPresent(foundUpdateObject -> model.addAttribute("sdgInd", foundUpdateObject));
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
}
