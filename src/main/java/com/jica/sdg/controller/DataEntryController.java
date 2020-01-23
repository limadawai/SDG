package com.jica.sdg.controller;

import com.jica.sdg.model.EntryGovIndicator;
import com.jica.sdg.model.EntryNsaIndicator;
import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntrySdgIndicatorJoin;
import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.NsaProgram;

import com.jica.sdg.model.EntryProblemIdentify;
import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import com.jica.sdg.service.*;

import java.util.*;

import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.RanRad;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.model.SdgIndicatorTarget;
import com.jica.sdg.service.IEntrySdgService;
import com.jica.sdg.service.ISdgIndicatorService;
import com.jica.sdg.service.IGovProgramService;
import com.jica.sdg.service.INsaProgramService;
import com.jica.sdg.service.IProvinsiService;
import java.util.HashMap;

import com.jica.sdg.service.IRanRadService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.NsaProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.servlet.http.HttpSession;
//import org.springframework.data.jpa.repository.Query;

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
    INsaProgramService nsaProgService;
    
    @Autowired
    ISdgIndicatorService sdgIndicatorService;

    //entry SDG
    @GetMapping("admin/sdg-indicator-monitoring")
    public String entri_sdg(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
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
    
    @GetMapping("admin/list-entry-sdg/{id_prov}/{id_role}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> listEntrySdg(@PathVariable("id_prov") String id_prov, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
        String sql  = "select a.id_goals, a.id_target, a.id_indicator, b.nm_goals, c.nm_target, d.nm_indicator, d.unit, d.increment_decrement, e.value,\n" +
                    "f.achievement1, f.achievement2, f.achievement3, f.achievement4, g.sdg_indicator, f.id as id_target_1, b.id_goals as kode_goals, b.nm_goals_eng, \n" +
                    "c.id_target as kode_target, c.nm_target_eng, d.id_indicator as kode_indicator, d.nm_indicator_eng \n" +
                    "from assign_sdg_indicator as a\n" +
                    "left join sdg_goals as b on a.id_goals = b.id \n" +
                    "left join sdg_target as c on a.id_target = c.id \n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id \n" +
                    "left join \n" +
                    "(select id_sdg_indicator, id_role, year, value from sdg_indicator_target where id_role = :id_role and year = :year) as e on d.id = e.id_sdg_indicator \n" +
                    "left join \n" +
                    "(select * from entry_sdg where year_entry = :year and id_role = :id_role and id_monper = :id_monper) as f on d.id = f.id_sdg_indicator \n" +
                    "left join ran_rad as g on a.id_monper = g.id_monper \n" +
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
                    "(select gov_prog from ran_rad where id_monper = :id_monper ) as ket_ran_rad , a.nm_activity_eng, c.value as nilai_target, a.id \n" +
                    "from gov_activity as a\n" +
                    "left join entry_gov_indicator as b on a.id = b.id_assign\n" +
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
    
    @PostMapping(path = "admin/save-entry-gov_prog", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntryGovProg(@RequestBody EntryGovIndicator entryGovIndicator) {
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
                    "(select nsa_prog from ran_rad where id_monper = :id_monper) as ket_ran_rad, a.nm_activity_eng, c.value as nilai_target, a.id \n" +
                    "from nsa_activity as a\n" +
                    "left join entry_nsa_indicator as b on a.id = b.id_assign\n" +
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
    
    @PostMapping(path = "admin/save-entry-non-gov_prog", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveEntryNonGovProg(@RequestBody EntryNsaIndicator entryNsaIndicator) {
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
                    "c.id_target as kode_target, c.nm_target_eng, d.id_indicator as kode_indicator, d.nm_indicator_eng \n" +
                    "from assign_sdg_indicator as a\n" +
                    "left join sdg_goals as b on a.id_goals = b.id \n" +
                    "left join sdg_target as c on a.id_target = c.id \n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id \n" +
                    "left join \n" +
                    "(select id_sdg_indicator, id_role, year, value from sdg_indicator_target where id_role = :id_role and year = :year) as e on d.id = e.id_sdg_indicator \n" +
                    "left join ran_rad as g on a.id_monper = g.id_monper \n" +
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
    

}
