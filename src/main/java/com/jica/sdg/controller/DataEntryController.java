package com.jica.sdg.controller;

import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntrySdgIndicatorJoin;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.service.IEntrySdgService;
import com.jica.sdg.service.IProvinsiService;
import java.util.HashMap;

import com.jica.sdg.service.IRanRadService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.NsaProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    NsaProfileService nsaProfilrService;
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
                    "f.achievement1, f.achievement2, f.achievement3, f.achievement4, g.sdg_indicator\n" +
                    "from assign_sdg_indicator as a\n" +
                    "left join sdg_goals as b on a.id_goals = b.id_goals\n" +
                    "left join sdg_target as c on a.id_target = c.id_target\n" +
                    "left join sdg_indicator as d on a.id_indicator = d.id_indicator\n" +
                    "inner join \n" +
                    "(select id_sdg_indicator, id_role, year, value from sdg_indicator_target where id_role = :id_role and year = :year) as e on d.id_indicator = e.id_sdg_indicator \n" +
                    "inner join \n" +
                    "(select * from entry_sdg where year_entry = :year and id_role = :id_role and id_monper = :id_monper) as f on d.id_indicator = f.id_sdg_indicator \n" +
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
    public void saveNsaProfil(@RequestBody EntrySdg entrySdg) {
        String id_sdg_indicator = entrySdg.getId_sdg_indicator();
        int achievement1        = entrySdg.getAchievement1();
        int achievement2        = entrySdg.getAchievement2();
        int achievement3        = entrySdg.getAchievement3();
        int achievement4        = entrySdg.getAchievement4();
        int year_entry          = entrySdg.getYear_entry();
        int id_role             = entrySdg.getId_role();
        int id_monper           = entrySdg.getId_monper();
//        entrySdgService.deleteEntrySdg(id);
        entrySdgService.updateEntrySdg(id_sdg_indicator, achievement1, achievement2, achievement3, achievement4, year_entry, id_role, id_monper);
    }

    @GetMapping("admin/government-program-monitoring")
    public String govprogram(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/dataentry/govprogram";
    }

    @GetMapping("admin/government-activity-monitoring")
    public String govkegiatan(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/dataentry/govactivity";
    }

    // ****************** Problem Identification & Follow Up ******************
    @GetMapping("admin/problem-identification")
    public String problem(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Problem Identification & Follow Up");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("listRole", roleService.findAll());
        model.addAttribute("listranrad", ranRadService.findAll());
        return "admin/dataentry/problem";
    }

    // ****************** Best Practice ******************
    @GetMapping("admin/best-practice")
    public String bestpractice(Model model, HttpSession session) {
        model.addAttribute("title", "Best Practice");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("listRole", roleService.findAll());
        model.addAttribute("listranrad", ranRadService.findAll());
        return "admin/dataentry/practice";
    }

}
