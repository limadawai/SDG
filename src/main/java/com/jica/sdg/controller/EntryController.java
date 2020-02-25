package com.jica.sdg.controller;

import com.jica.sdg.model.BestPractice;
import com.jica.sdg.model.EntryBestPractice;
import com.jica.sdg.model.EntryProblemIdentify;
import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.Nsaprofile2;
import com.jica.sdg.model.Problemlist;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.model.SdgTarget;
import com.jica.sdg.model.Unit;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import com.jica.sdg.service.*;
import java.util.ArrayList;
import java.util.Date;

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
import javax.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @Autowired
    ISdgIndicatorService sdgIndicatorService;
    @Autowired
    ModelCrud modelCrud;
    
    @Autowired
    IBestPracticeService bestService;
    
    @Autowired
    IEntryBestPracticeService enbestService;
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
    public String indicator(Model model, @PathVariable("id") int id, @PathVariable("id_target") Integer id_target, HttpSession session) {
    	Optional<SdgGoals> list = sdgGoalsService.findOne(id);
    	Optional<SdgTarget> list1 = sdgTargetService.findOne(id_target);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("goals", foundUpdateObject));
        list1.ifPresent(foundUpdate -> model.addAttribute("target", foundUpdate));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("unit", unitService.findAll());
        return "admin/dataentry/problemindicator";
    }
    
    @GetMapping("admin/problem-identification/{id}/target/{id_target}/indicator/{id_indicator}/problem_identify")
    public String problem_identify(Model model, @PathVariable("id") int id, @PathVariable("id_target") int id_target, @PathVariable("id_indicator") int id_indicator, HttpSession session) {
//    public String disagre(Model model, @PathVariable("id") int id, @PathVariable("id_target") Integer id_target, @PathVariable("id_indicator") Integer id_indicator, HttpSession session) {
    	Optional<SdgGoals> list = sdgGoalsService.findOne(id);
    	Optional<SdgTarget> list1 = sdgTargetService.findOne(id_target);
    	Optional<SdgIndicator> list2 = sdgIndicatorService.findOne(id_indicator);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("goals", foundUpdateObject));
        list1.ifPresent(foundUpdate -> model.addAttribute("target", foundUpdate));
        list2.ifPresent(foundUpdate1 -> model.addAttribute("indicator", foundUpdate1));
        model.addAttribute("refcategory",modelCrud.getRefCategory());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/dataentry/problemidentify";
    }
    
    @PostMapping(path = "admin/problem-identification/save", consumes = "application/json", produces = "application/json")
	@ResponseBody
        @Transactional
	public void saveUnit(@RequestBody Map<String, Object> payload,HttpSession session) {
        JSONObject jsonObunit = new JSONObject(payload);
        String id               = jsonObunit.get("id").toString();  
        String id_cat           = jsonObunit.get("id_cat").toString();        
        String id_goals         = jsonObunit.get("id_goals").toString();
        String id_target        = jsonObunit.get("id_target").toString();
        String id_indicator     = jsonObunit.get("id_indicator").toString();
        String problem          = jsonObunit.get("problem").toString();
        String follow_up        = jsonObunit.get("follow_up").toString();
        String sqlGetGoals = "SELECT b.id_role,b.id_prov,b.id_monper FROM sdg_goals a JOIN assign_sdg_indicator b ON a.id = b.id_goals WHERE a.id ='"+id_goals+"'";
        Query list = em.createNativeQuery(sqlGetGoals);
        Map<String, Object> goalsMap = new HashMap<>();
        goalsMap.put("goalsMap",list.getResultList());
        JSONObject objGoals = new JSONObject(goalsMap); 
        JSONArray  arrayGoals = objGoals.getJSONArray("goalsMap");
        String id_role   = arrayGoals.getJSONArray(0).get(0).toString();
        String id_prov   = arrayGoals.getJSONArray(0).get(1).toString();
        String id_monper = arrayGoals.getJSONArray(0).get(2).toString();
            if(id.equals("")){
                Query query = em.createNativeQuery("INSERT INTO entry_problem_identify \n" +
                                                    "(id_goals,id_target,id_indicator,id_cat,problem,follow_up,id_prov,id_role,`year`,year_entry,created_by,date_created,summary,id_monper) \n" +
                                                    "VALUES(:id_goals,:id_target,:id_indicator,:id_cat,:problem,:follow_up,:id_prov,:id_role,DATE_FORMAT(NOW(), '%Y'),DATE_FORMAT(NOW(), '%Y'),'1',DATE_FORMAT(NOW(), '%Y-%m-%d '),'1',:id_monper)");
                query.setParameter("id_goals", id_goals)
                     .setParameter("id_target", id_target)
                     .setParameter("id_indicator", id_indicator)
                     .setParameter("id_cat", id_cat)
                     .setParameter("problem", problem)
                     .setParameter("follow_up", follow_up)
                     .setParameter("id_prov", id_prov)
                     .setParameter("id_role", id_role)
                     .setParameter("id_monper", id_monper)
                     .executeUpdate();
            }
            else{
                Query query = em.createNativeQuery("Update entry_problem_identify set id_cat=:id_cat,problem=:problem,follow_up=:follow_up where id = :id");
                 query.setParameter("id", id)
                     .setParameter("id_cat", id_cat)
                     .setParameter("problem", problem)
                     .setParameter("follow_up", follow_up)
                     .executeUpdate();
            }
        
	}
    
    @GetMapping("testcrud")
    public @ResponseBody Map<String, Object> testCrud(){
        String sql = "select * from ref_category";
//        modelCrud.hallo();
//        modelCrud.getRefCategory(sql);
        JSONObject objRanRad = new JSONObject(modelCrud.getRefCategory(sql)); 
        System.out.println(objRanRad);
        return null;
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
     
     @GetMapping("admin/list-entry-problem/{id_goals}/{id_target}/{id_indicator}")
     public @ResponseBody Map<String, Object> entryProblemList(@PathVariable("id_goals") String id_goals,@PathVariable("id_target") String id_target,@PathVariable("id_indicator") String id_indicator) {
        String sql = "SELECT a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat "
                     + "WHERE a.id_goals =  '"+id_goals+"' and a.id_target =  '"+id_target+"' and a.id_indicator =  '"+id_indicator+"' ";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Problemlist> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new Problemlist((Integer)row[0],(String)row[1],(String)row[2], (String)row[3],(String)row[4]));
        }
        hasil.put("content",result);
        return hasil;
    }
     
    @GetMapping("admin/problem/get-problem/{id}")
    public @ResponseBody Map<String, Object> getUnit(@PathVariable("id") Integer id) {
        String sql = "select id,id_cat,problem,follow_up from entry_problem_identify where id = '"+id+"'";
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<EntryProblemIdentify> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(
                        new EntryProblemIdentify(id,(String)row[1] , (String)row[2], (String)row[3])
            );
        }
        hasil.put("content",result);
        return hasil;
    }
    
    @DeleteMapping("admin/problem-identification/delete/{id}")
    @ResponseBody    
    @Transactional
    public void deleteUnit(@PathVariable("id") Integer id) {
        em.createNativeQuery("delete from entry_problem_identify where id ='"+id+"'").executeUpdate();
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
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String privilege    = list.get().getPrivilege();
    	String id_prov 		= list.get().getId_prov();
    	String cat_role		= list.get().getCat_role();
        model.addAttribute("title", "Best Practice");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        if(privilege.equals("SUPER")) {
        	model.addAttribute("listprov", provinsiService.findAllProvinsi());
        }else {
        	Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
        }
        model.addAttribute("privilege", privilege);
        model.addAttribute("cat_role", cat_role);
        return "admin/dataentry/practice";
    }

    @GetMapping("admin/best-practice/list-govmap/{id_prov}/{id_monper}/{id}")
    public @ResponseBody Map<String, Object> getOptionMonperList(@PathVariable("id_prov") String id_prov,@PathVariable("id_monper") String id_monper,@PathVariable("id") String id) {
        String sql  = "select a.id, b.nm_goals, b.nm_goals_eng, c.nm_target, c.nm_target_eng, d.nm_indicator, d.nm_indicator_eng, "
        		+ "b.id_goals, c.id_target, d.id_indicator "
        		+ "from gov_map as a "
        		+ "left join sdg_goals b on a.id_goals = b.id "
        		+ "left join sdg_target c on a.id_target = c.id "
        		+ "left join sdg_indicator d on a.id_indicator = d.id "
        		+ "where a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_gov_indicator = :id";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_monper", id_monper);
        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/best-practice/list-nsamap/{id_prov}/{id_monper}/{id}")
    public @ResponseBody Map<String, Object> getNsa(@PathVariable("id_prov") String id_prov,@PathVariable("id_monper") String id_monper,@PathVariable("id") String id) {
        String sql  = "select a.id, b.nm_goals, b.nm_goals_eng, c.nm_target, c.nm_target_eng, d.nm_indicator, d.nm_indicator_eng, "
        		+ "b.id_goals, c.id_target, d.id_indicator "
        		+ "from nsa_map as a "
        		+ "left join sdg_goals b on a.id_goals = b.id "
        		+ "left join sdg_target c on a.id_target = c.id "
        		+ "left join sdg_indicator d on a.id_indicator = d.id "
        		+ "where a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_nsa_indicator = :id";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_monper", id_monper);
        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-best/{id_role}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> listBest(@PathVariable("id_role") String id_role,@PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
        String sql  = "select a.id, b.nm_program, b.nm_program_eng, a.location, DATE_FORMAT(a.time_activity, \"%M, %d %Y %H:%i\"), a.background, a.implementation_process, "
        		+ "a.challenges_learning, a.id_indicator, c.nm_program as nsa_prog, c.nm_program_eng as nsa_prog_eng "
        		+ "from best_practice as a "
        		+ "left join gov_program b on a.id_program = b.id "
        		+ "left join nsa_program c on a.id_program = c.id "
        		+ "where a.id_role = :id_role and a.id_monper = :id_monper and a.year = :year";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-best-entry/{id_best}")
    public @ResponseBody Map<String, Object> listBest(@PathVariable("id_best") String id_best) {
        String sql  = "select a.*, c.nm_program as nsa_prog, c.nm_program_eng as nsa_prog_eng, b.id_indicator "
        		+ "from entry_best_practice as a "
        		+ "left join best_practice b on a.id_best_practice = b.id "
        		+ "left join nsa_program c on b.id_program = c.id "
        		+ "where a.id_best_practice = :id_best_practice";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_best_practice", id_best);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-best", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveBest(@RequestBody BestPractice best) {
    	bestService.saveBestPractice(best);
    }
    
    @PostMapping(path = "admin/save-best-entry", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveBest(@RequestBody EntryBestPractice best, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	best.setCreated_by(id_role);
    	best.setDate_created(new Date());
    	enbestService.saveEntryBestPractice(best);
    }
    
    @GetMapping("admin/get-best/{id}")
    public @ResponseBody Map<String, Object> getBest(@PathVariable("id") Integer id){
    	Optional<BestPractice> list = bestService.findOne(id);
    	Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/get-best-entry/{id}")
    public @ResponseBody Map<String, Object> getBestEntry(@PathVariable("id") Integer id){
    	Optional<EntryBestPractice> list = enbestService.findOne(id);
    	Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/best-practice/detail/{id}")
    public String bestpractice(Model model, HttpSession session, @PathVariable("id") Integer id) {
    	Optional<BestPractice> listRole = bestService.findOne(id);
    	Optional<Role> list = roleService.findOne(listRole.get().getId_role());
    	String privilege    = list.get().getPrivilege();
    	String id_prov 		= list.get().getId_prov();
    	String cat_role		= list.get().getCat_role();
        model.addAttribute("title", "Best Practice");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        if(privilege.equals("SUPER")) {
        	model.addAttribute("listprov", provinsiService.findAllProvinsi());
        }else {
        	Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
        }
        model.addAttribute("privilege", privilege);
        model.addAttribute("cat_role", cat_role);
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("id_role", listRole.get().getId_role());
        model.addAttribute("id_monper", listRole.get().getId_monper());
        model.addAttribute("year", listRole.get().getYear());
        model.addAttribute("id_best", id);
        model.addAttribute("id_indicator", listRole.get().getId_indicator());
        return "admin/dataentry/entry_practice";
    }
    
}
