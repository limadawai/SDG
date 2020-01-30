package com.jica.sdg.controller;

import com.jica.sdg.model.EntryProblemIdentify;
import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.Nsaprofile2;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.SdgTarget;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import com.jica.sdg.service.*;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.Query;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EntryController {

    @Autowired
    EntryProblemIdentifyRepository repository;
    @Autowired
    ProvinsiService provinsiService;
    @Autowired
    RoleService roleService;
    @Autowired
    RanRadService ranRadService;
    @Autowired
    SdgGoalsService goalsService;
    @Autowired
    EntryProblemIdentifyService identifyService;
    @Autowired
    ISdgGoalsService sdgGoalsService;
    @Autowired
    NsaProfileService nsaProfilrService;
    @Autowired
    IGovProgramService govProgService;
    @Autowired
    private EntityManager em;
    @Autowired
    ISdgTargetService sdgTargetService;
    @Autowired
    IUnitService unitService;
    // ****************** Problem Identification & Follow Up ******************
    @GetMapping("admin/problem-identification")
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
        return "admin/dataentry/problemgoals";
    }
    @GetMapping("admin/problem-identification/{id}/target")
    public String target(Model model, @PathVariable("id") int id, HttpSession session) {
                Optional<SdgGoals> list = sdgGoalsService.findOne(id);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("content", foundUpdateObject));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/dataentry/problemtarget";
    }
     @GetMapping("admin/problem-identification/{id}/target/{id_target}/indicator")
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
        return "admin/ran_rad/sdg/sdgs_indicator";
    }

    @GetMapping("admin/list-problem/{id_monper}")
     public @ResponseBody Map<String, Object> govProgList(@PathVariable("id_monper") String id_monper) {
        String sql = "SELECT a.* FROM sdg_goals a JOIN assign_sdg_indicator b ON a.id = b.id_goals WHERE b.id_monper =  '"+id_monper+"'";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<SdgGoals> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new SdgGoals((Integer)row[0], (String) row[1], (String) row[2], (String) row[3]) );
        }
        hasil.put("content",result);
        return hasil;
    }
     

    @PostMapping("admin/save-problem")
    public String saveProblem(EntryProblemIdentify problem, Model model, HttpSession session) {
        repository.save(problem);
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "redirect:/admin/problem-identification";
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
