package com.jica.sdg.controller;

import com.jica.sdg.model.BestMap;
import com.jica.sdg.model.BestPractice;
import com.jica.sdg.model.BestPracticeFile;
import com.jica.sdg.model.EntryBestPractice;
import com.jica.sdg.model.EntryProblemIdentify;
import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.Nsaprofile2;
import com.jica.sdg.model.Problemlist;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.RanRad;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.model.SdgTarget;
import com.jica.sdg.model.Unit;
import com.jica.sdg.model.UploadFileResponse;
import com.jica.sdg.repository.BestPracticeRepository;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import com.jica.sdg.service.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//import com.jica.sdg.service.FileStorageService;

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
    IBestPracticeFileService bestFileService;
    
    @Autowired
    BestPracticeRepository bestRepo;
    
    @Autowired
    IBestMapService bestMapService;
    
    @Autowired
    IEntryBestPracticeService enbestService;
    
//    
//    @Autowired
//    private FileStorageService fileStorageService;
    
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
        String sql = "SELECT DISTINCT  a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up,c.approval,a.id_role,a.id_monper,a.year  FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat "
                     + " JOIN entry_approval c ON  a.id_role = c.id_role AND a.id_monper = c.id_monper AND a.year = c.year AND c.type = 'entry_problem_identify' "
                     + "WHERE a.id_goals =  '"+id+"' and a.id_target =  '"+id_target+"' and a.id_indicator =  '"+id_indicator+"' ";        
        Query list3 = em.createNativeQuery(sql);
        List<Object[]> rows = list3.getResultList();
        model.addAttribute("count_data_app", rows.size());
        System.out.println(rows.size());
        
        return "admin/dataentry/problemidentify";
    }
    
    @PostMapping(path = "admin/problem-identification/save", consumes = "application/json", produces = "application/json")
	@ResponseBody
        @Transactional
	public void saveUnit(@RequestBody Map<String, Object> payload,HttpSession session) {
        JSONObject jsonObunit = new JSONObject(payload);
        String id               = jsonObunit.get("id").toString();  
        String id_cat           = jsonObunit.get("id_cat").toString();        
        String problem          = jsonObunit.get("problem").toString();
        String follow_up        = jsonObunit.get("follow_up").toString();
        UUID uuid = UUID.randomUUID();
        String id_relation      =  uuid.toString();
        String id_role   = jsonObunit.get("id_role").toString();
        String id_prov   = jsonObunit.get("id_prov").toString();
        String id_monper = jsonObunit.get("id_monper").toString();
        String tahun     = jsonObunit.get("tahun").toString();
        String sdgs_map  = jsonObunit.get("sdgs_map").toString();
        String id_relation_update  = jsonObunit.get("id_relation").toString();
        JSONArray  arraySdgsMap = jsonObunit.getJSONArray("sdgs_map");
         
            if(id.equals("")){
                Query query = em.createNativeQuery("INSERT INTO entry_problem_identify \n" +
                                                    "(id_cat,problem,follow_up,id_prov,id_role,`year`,year_entry,created_by,date_created,summary,id_monper,id_relation) \n" +
                                                    "VALUES(:id_cat,:problem,:follow_up,:id_prov,:id_role,:year,DATE_FORMAT(NOW(), '%Y'),'1',DATE_FORMAT(NOW(), '%Y-%m-%d'),'1',:id_monper,:id_relation)");
                query.setParameter("id_cat", id_cat)
                     .setParameter("problem", problem)
                     .setParameter("follow_up", follow_up)
                     .setParameter("id_prov", id_prov)
                     .setParameter("id_role", id_role)
                     .setParameter("id_monper", id_monper)
                     .setParameter("year", tahun)
                     .setParameter("id_relation", id_relation)
                     .executeUpdate();
                
                for(int i=0;i<arraySdgsMap.length();i++){
                    JSONObject  resultSdg = arraySdgsMap.getJSONObject(i);
                    String id_prov2     = resultSdg.getString("id_prov");
                    String id_monper2   = resultSdg.getString("id_monper");
                    String id_goals     = resultSdg.getString("id_goals");
                    String id_sdgs      = resultSdg.getString("id_sdgs");
                    if(id_goals.equals("")){
                        id_goals = null;
                    }
                    String id_target    = resultSdg.getString("id_target");
                    if(id_target.equals("")){
                        id_target = null;
                    }
                    String id_indicator = resultSdg.getString("id_indicator");
                    if(id_indicator.equals("")){
                        id_indicator = null;
                    }
                        Query query2 = em.createNativeQuery("INSERT into entry_problem_identify_map (id_prov,id_monper,id_goals,id_target,id_indicator,id_relation_entry_problem_identify,id_sdgs)"
                                                            + "Values(:id_prov,:id_monper,:id_goals,:id_target,:id_indicator,:id_relation_entry_problem_identify,:id_sdgs)");
                        query2.setParameter("id_prov", id_prov2)
                             .setParameter("id_monper", id_monper2)
                             .setParameter("id_goals", id_goals)
                             .setParameter("id_target", id_target)
                             .setParameter("id_indicator", id_indicator)
                             .setParameter("id_relation_entry_problem_identify", id_relation)
                             .setParameter("id_sdgs", id_sdgs)
                             .executeUpdate();

                 }
                
                
            }
            else{
                Query query = em.createNativeQuery("Update entry_problem_identify set id_cat=:id_cat,problem=:problem,follow_up=:follow_up where id = :id");
                 query.setParameter("id", id)                     
                     .setParameter("id_cat", id_cat)
                     .setParameter("problem", problem)
                     .setParameter("follow_up", follow_up)
                     .executeUpdate();
                 
                 em.createNativeQuery("delete from entry_problem_identify_map where id_relation_entry_problem_identify ='"+id_relation_update+"'").executeUpdate();
                 for(int i=0;i<arraySdgsMap.length();i++){
                    JSONObject  resultSdg = arraySdgsMap.getJSONObject(i);
                    String id_prov2     = resultSdg.getString("id_prov");
                    String id_monper2   = resultSdg.getString("id_monper");
                    String id_goals     = resultSdg.getString("id_goals");
                    String id_sdgs      = resultSdg.getString("id_sdgs");
                    if(id_goals.equals("")){
                        id_goals = null;
                    }
                    String id_target    = resultSdg.getString("id_target");
                    if(id_target.equals("")){
                        id_target = null;
                    }
                    String id_indicator = resultSdg.getString("id_indicator");
                    if(id_indicator.equals("")){
                        id_indicator = null;
                    }
                        Query query2 = em.createNativeQuery("INSERT into entry_problem_identify_map (id_prov,id_monper,id_goals,id_target,id_indicator,id_relation_entry_problem_identify,id_sdgs)"
                                                            + "Values(:id_prov,:id_monper,:id_goals,:id_target,:id_indicator,:id_relation_entry_problem_identify,:id_sdgs)");
                        query2.setParameter("id_prov", id_prov2)
                             .setParameter("id_monper", id_monper2)
                             .setParameter("id_goals", id_goals)
                             .setParameter("id_target", id_target)
                             .setParameter("id_indicator", id_indicator)
                             .setParameter("id_relation_entry_problem_identify", id_relation_update)
                             .setParameter("id_sdgs", id_sdgs)
                             .executeUpdate();

                 }
            }
        
	}
    
     @PostMapping(path = "admin/problem-identification/aply", consumes = "application/json", produces = "application/json")
	@ResponseBody
        @Transactional
	public void aplyProblemidentification(@RequestBody Map<String, Object> payload,HttpSession session) {
        JSONObject jsonObunit = new JSONObject(payload);
        String id_monper         = jsonObunit.get("id_monper").toString();  
        String tahun             = jsonObunit.get("tahun").toString();
        String id_role           = jsonObunit.get("id_role").toString();
            em.createNativeQuery("delete from entry_approval where id_role = '"+id_role+"' and id_monper = '"+id_monper+"' AND year = '"+tahun+"' AND type = 'entry_problem_identify'").executeUpdate();
           Query query = em.createNativeQuery("INSERT INTO entry_approval (id_role,id_monper,YEAR,TYPE,approval,approval_date,periode)\n" +
                                               "SELECT DISTINCT a.id_role,a.id_monper,a.year,'entry_problem_identify' AS TYPE,'1' AS approval, CURDATE() AS approval_date ,'0' as periode FROM entry_problem_identify a \n" +
                                               "LEFT JOIN ref_category b ON  a.id_cat = b.id_cat \n" +
                                               "WHERE a.id_monper = '"+id_monper+"' AND a.year = '"+tahun+"' AND a.id_role = '"+id_role+"'  ;");
           query.executeUpdate();
	}    
    @PostMapping(path = "admin/problem-identification/un-aply", consumes = "application/json", produces = "application/json")
	@ResponseBody
        @Transactional
	public void unaplyProblemidentification(@RequestBody Map<String, Object> payload,HttpSession session) {
        JSONObject jsonObunit = new JSONObject(payload);
        String id_monper            = jsonObunit.get("id_monper").toString();  
        String tahun                = jsonObunit.get("tahun").toString();
        String id_role              = jsonObunit.get("id_role").toString();
            Query query = em.createNativeQuery(" DELETE FROM entry_approval WHERE id_role='"+id_role+"' and id_monper='"+id_monper+"' AND YEAR='"+tahun+"' AND  TYPE='entry_problem_identify'");
            query.executeUpdate();
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
    
    @GetMapping("admin/list-problem/{id_monper}/{tahun}/{id_role}")
     public @ResponseBody Map<String, Object> govProgList(@PathVariable("id_monper") String id_monper,@PathVariable("tahun") String tahun,@PathVariable("id_role") String id_role) {
    	Optional<RanRad> monper = ranRadService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	String sql;
    	if(status.equals("completed")) {
    		sql = "SELECT   \n" +
    				"                         d.id_goals,d.id_old AS id_sdg_goals,d.nm_goals,d.nm_goals_eng  \n" +
    				"                        ,e.id_target,e.id_old AS id_sdg_target,e.nm_target,e.nm_target_eng  \n" +
    				"                        ,f.id_indicator,f.id_old AS id_sdg_indicator,f.nm_indicator,f.nm_indicator_eng \n" +
    				"                        ,b.id_cat,b.nm_cat,a.problem,a.follow_up,a.id,c.approval,a.id_monper,a.year,a.id_role,a.id_relation \n" +
    				"                        ,( SELECT GROUP_CONCAT(id_sdgs) FROM entry_problem_identify_map g WHERE g.id_relation_entry_problem_identify = a.id_relation )  AS id_sdgs"
    				                      + ",( SELECT GROUP_CONCAT(CONCAT_WS('###',b.id_goals,concat(b.id_goals,'.',c.id_target),concat(b.id_goals,'.',c.id_target,'.',d.id_indicator),CONCAT_WS('##*##',b.nm_goals,c.nm_target,d.nm_indicator))) AS sdgs FROM entry_problem_identify_map g\n" +
    				                            "LEFT JOIN history_sdg_goals b ON g.id_goals = b.id_old and b.id_monper = '"+id_monper+"' \n" +
    				                            "LEFT JOIN history_sdg_target c ON g.id_target = c.id_old and c.id_monper = '"+id_monper+"' \n" +
    				                            "LEFT JOIN history_sdg_indicator d ON g.id_indicator = d.id_old and d.id_monper = '"+id_monper+"'  \n" +
    				                            "WHERE g.id_relation_entry_problem_identify = a.id_relation )  AS id_sdgs2\n" +
    				"                         FROM entry_problem_identify a  \n" +
    				"                        LEFT JOIN ref_category b ON  a.id_cat = b.id_cat  \n" +
    				"                        LEFT JOIN entry_approval c ON  a.id_role = c.id_role AND a.id_monper = c.id_monper AND a.year = c.year AND c.type = 'entry_problem_identify' \n" +
    				"                        LEFT JOIN history_sdg_goals d ON a.id_goals = d.id_old and d.id_monper = '"+id_monper+"'  \n" +
    				"                        LEFT JOIN history_sdg_target e ON a.id_target = e.id_old and e.id_monper = '"+id_monper+"'  \n" +
    				"                        LEFT JOIN history_sdg_indicator f ON a.id_indicator = f.id_old and f.id_monper = '"+id_monper+"'  WHERE a.id_monper = '"+id_monper+"' and a.year = '"+tahun+"' and a.id_role = '"+id_role+"' ";
    	}else {
    		sql = "SELECT   \n" +
    				"                         d.id_goals,d.id AS id_sdg_goals,d.nm_goals,d.nm_goals_eng  \n" +
    				"                        ,e.id_target,e.id AS id_sdg_target,e.nm_target,e.nm_target_eng  \n" +
    				"                        ,f.id_indicator,f.id AS id_sdg_indicator,f.nm_indicator,f.nm_indicator_eng \n" +
    				"                        ,b.id_cat,b.nm_cat,a.problem,a.follow_up,a.id,c.approval,a.id_monper,a.year,a.id_role,a.id_relation \n" +
    				"                        ,( SELECT GROUP_CONCAT(id_sdgs) FROM entry_problem_identify_map g WHERE g.id_relation_entry_problem_identify = a.id_relation )  AS id_sdgs"
    				                      + ",( SELECT GROUP_CONCAT(CONCAT_WS('###',b.id_goals,concat(b.id_goals,'.',c.id_target),concat(b.id_goals,'.',c.id_target,'.',d.id_indicator),CONCAT_WS('##*##',b.nm_goals,c.nm_target,d.nm_indicator))) AS sdgs FROM entry_problem_identify_map g\n" +
    				                            "LEFT JOIN sdg_goals b ON g.id_goals = b.id \n" +
    				                            "LEFT JOIN sdg_target c ON g.id_target = c.id\n" +
    				                            "LEFT JOIN sdg_indicator d ON g.id_indicator = d.id \n" +
    				                            "WHERE g.id_relation_entry_problem_identify = a.id_relation )  AS id_sdgs2\n" +
    				"                         FROM entry_problem_identify a  \n" +
    				"                        LEFT JOIN ref_category b ON  a.id_cat = b.id_cat  \n" +
    				"                        LEFT JOIN entry_approval c ON  a.id_role = c.id_role AND a.id_monper = c.id_monper AND a.year = c.year AND c.type = 'entry_problem_identify' \n" +
    				"                        LEFT JOIN sdg_goals d ON a.id_goals = d.id \n" +
    				"                        LEFT JOIN sdg_target e ON a.id_target = e.id \n" +
    				"                        LEFT JOIN sdg_indicator f ON a.id_indicator = f.id WHERE a.id_monper = '"+id_monper+"' and a.year = '"+tahun+"' and a.id_role = '"+id_role+"' ";
    	}
    	        
        Query list = em.createNativeQuery(sql);
        
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list.getResultList());
        return hasil;
    }
     
     @GetMapping("admin/list-entry-problem/{id_goals}/{id_target}/{id_indicator}")
     public @ResponseBody Map<String, Object> entryProblemList(@PathVariable("id_goals") String id_goals,@PathVariable("id_target") String id_target,@PathVariable("id_indicator") String id_indicator) {
        String sql = "SELECT DISTINCT  a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up,c.approval,a.id_role,a.id_monper,a.year  FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat "
                     + " LEFT JOIN entry_approval c ON  a.id_role = c.id_role AND a.id_monper = c.id_monper AND a.year = c.year AND c.type = 'entry_problem_identify' "
                     + "WHERE a.id_goals =  '"+id_goals+"' and a.id_target =  '"+id_target+"' and a.id_indicator =  '"+id_indicator+"' ";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Problemlist> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new Problemlist((Integer)row[0],(String)row[1],(String)row[2], (String)row[3],(String)row[4],(String)row[5]));
        
        }
        
        hasil.put("content",result);
        hasil.put("count_data",rows.size());
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
    
    @DeleteMapping("admin/problem-identification/delete/{id}/{id_relation}")
    @ResponseBody    
    @Transactional
    public void deleteUnit(@PathVariable("id") Integer id,@PathVariable("id_relation") String id_relation) {
        em.createNativeQuery("delete from entry_problem_identify where id ='"+id+"'").executeUpdate();
        em.createNativeQuery("delete from entry_problem_identify_map where id_relation_entry_problem_identify ='"+id_relation+"'").executeUpdate();
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
        model.addAttribute("id_role", id_role);
        model.addAttribute("privilege", privilege);
        model.addAttribute("id_prov", id_prov);
        return "admin/dataentry/practice";
    }

    @GetMapping("admin/best-practice/list-govmap/{id_prov}/{id_monper}/{id}")
    public @ResponseBody Map<String, Object> getOptionMonperList(@PathVariable("id_prov") String id_prov,@PathVariable("id_monper") String id_monper,@PathVariable("id") String id) {
        String sql  = "select a.id, b.nm_goals, b.nm_goals_eng, c.nm_target, c.nm_target_eng, d.nm_indicator, d.nm_indicator_eng, "
        		+ "b.id_goals, c.id_target, d.id_indicator "
        		+ "from best_map as a "
        		+ "left join sdg_goals b on a.id_goals = b.id "
        		+ "left join sdg_target c on a.id_target = c.id "
        		+ "left join sdg_indicator d on a.id_indicator = d.id "
        		+ "where a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_best_practice = :id";
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
        		+ "from best_map as a "
        		+ "left join sdg_goals b on a.id_goals = b.id "
        		+ "left join sdg_target c on a.id_target = c.id "
        		+ "left join sdg_indicator d on a.id_indicator = d.id "
        		+ "where a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_best_practice = :id";
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
        		+ "a.challenges_learning, a.id_indicator, c.nm_program as nsa_prog, c.nm_program_eng as nsa_prog_eng, a.program "
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
    
    @GetMapping("admin/list-best-summary/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> listBestsummary(@PathVariable("id_monper") String id_monper,@PathVariable("year") String year) {
        String sql  = "select a.id, b.nm_program, b.nm_program_eng, a.location, DATE_FORMAT(a.time_activity, \"%M, %d %Y %H:%i\"), a.background, a.implementation_process, "
        		+ "a.challenges_learning, a.id_indicator, c.nm_program as nsa_prog, c.nm_program_eng as nsa_prog_eng, a.program "
        		+ "from best_practice as a "
        		+ "left join gov_program b on a.id_program = b.id "
        		+ "left join nsa_program c on a.id_program = c.id "
        		+ "where a.id_role = '999999' and a.id_monper = :id_monper and a.year = :year";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("id_role", id_role);
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
    
    @PostMapping(path = "admin/save-best/{sdg_indicator}/{id_monper}/{id_prov}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveBest(@RequestBody BestPractice best,
			@PathVariable("sdg_indicator") String sdg_indicator,
			@PathVariable("id_monper") Integer id_monper,
			@PathVariable("id_prov") String id_prov) {
    	bestService.saveBestPractice(best);
        if(!sdg_indicator.equals("0")) {
            bestMapService.deleteGovMapByGovInd(best.getId());
            String[] sdg = sdg_indicator.split(",");
            for(int i=0;i<sdg.length;i++) {
                String[] a = sdg[i].split("---");
                Integer id_goals = Integer.parseInt(a[0]);
                Integer id_target = Integer.parseInt(a[1]);
                Integer id_indicator = Integer.parseInt(a[2]);
                BestMap map = new BestMap();
                map.setId_goals(id_goals);
                if(id_target!=0) {
                        map.setId_target(id_target);
                }
                if(id_indicator!=0) {
                        map.setId_indicator(id_indicator);
                }
                map.setId_best_practice(best.getId());
                map.setId_monper(id_monper);
                map.setId_prov(id_prov);
                bestMapService.saveGovMap(map);
            }
    	}
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("v_id",best.getId());
        return hasil;
    }
    
    @RequestMapping(value = "admin/create-foto-best-pract",method = RequestMethod.POST)
//    @PostMapping(path = "admin/create-foto-best-pract"/*, consumes = "application/json", produces = "application/json"*/)
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public List<UploadFileResponse> saveFoto(@RequestParam("files") MultipartFile[] files, @RequestParam("idbest") String idbest, @RequestParam("idcek") String idcek, @RequestParam("isi_file") String isi_file)  {
       
        int idbes = Integer.valueOf(idbest);
        if(isi_file.equals("")){
                
        }else{
            bestFileService.deleteBestPracticeFile(idbes);
        }
            List<UploadFileResponse> list = Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, idbes))
                .collect(Collectors.toList());
            System.out.println("file ya = "+files);
        return list;
    }
    
    @PostMapping("/uploadFilePegawai")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("idbest") int idbest) {
        String fileName = "";
        String fileDownloadUri = "";
        try {
            
            BestPracticeFile best = new BestPracticeFile();
            int idbes = Integer.valueOf(idbest);
            Optional<BestPractice> listbest = bestService.findOne(idbest);
    //            int id    = listbest.get().getId();
            best.setId_best_practice(listbest.get().getId());
            if(!file.isEmpty()) {
    //			fileName = fileStorageService.storeFilePegawai(file, idpegawai.toString(),i);   
    //			fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
    //	                .path("/downloadFile/")
    //	                .path(fileName)
    //	                .toUriString();
                best.setFile(IOUtils.toByteArray(file.getInputStream()));
                bestFileService.saveBestPracticeFile(best);

            }
        
        } catch (Exception e) {
        }
        
        return new UploadFileResponse(fileName, fileDownloadUri,
            file.getContentType(), file.getSize());
    }
    
    @GetMapping("admin/get-all-foto/{id}")
    public @ResponseBody Map<String, Object> allfoto(@PathVariable("id") String id_best) {
        String sql  = "select * from best_practice_file where id_best_practice = :id_best_practice ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_best_practice", id_best);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
//    
//    @PostMapping(path = "admin/save-best/{sdg_indicator}/{id_monper}/{id_prov}", consumes = "application/json", produces = "application/json")
//    @ResponseBody
//    public void saveBest(@RequestBody BestPractice best,
//			@PathVariable("sdg_indicator") String sdg_indicator,
//			@PathVariable("id_monper") Integer id_monper,
//			@PathVariable("id_prov") String id_prov) {
//    	bestService.saveBestPractice(best);
//        if(!sdg_indicator.equals("0")) {
//            bestMapService.deleteGovMapByGovInd(best.getId());
//            String[] sdg = sdg_indicator.split(",");
//            for(int i=0;i<sdg.length;i++) {
//                String[] a = sdg[i].split("---");
//                Integer id_goals = Integer.parseInt(a[0]);
//                Integer id_target = Integer.parseInt(a[1]);
//                Integer id_indicator = Integer.parseInt(a[2]);
//                BestMap map = new BestMap();
//                map.setId_goals(id_goals);
//                if(id_target!=0) {
//                        map.setId_target(id_target);
//                }
//                if(id_indicator!=0) {
//                        map.setId_indicator(id_indicator);
//                }
//                map.setId_best_practice(best.getId());
//                map.setId_monper(id_monper);
//                map.setId_prov(id_prov);
//                bestMapService.saveGovMap(map);
//            }
//    	}
//    }
    
    @PostMapping(path = "admin/save-best-entry", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveBest(@RequestBody EntryBestPractice best, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	best.setCreated_by(id_role);
    	best.setDate_created(new Date());
    	enbestService.saveEntryBestPractice(best);
    }
        
//    @GetMapping(path = "admin/delete-best-practice/{id}/{id_prov}/{id_monper}")
//    @ResponseBody
//    @Transactional
//    @ResponseBody
    @DeleteMapping("admin/delete-best-practice/{id}/{id_prov}/{id_monper}")
    @ResponseBody
    @Transactional
    public void delete_best_practice(@PathVariable("id") Integer id, @PathVariable("id_prov") Integer id_prov, @PathVariable("id_monper") Integer id_monper) {
        System.out.println("delete ya ");
        em.createNativeQuery("delete from best_practice where id = '"+id+"' ").executeUpdate();
        bestFileService.deleteBestPracticeFile(id);
//        em.createNativeQuery("delete from best_map where id_best_practice = '"+id+"' and id_prov = '"+id_prov+"' and id_monper = '"+id_monper+"' ").executeUpdate();
//        em.createQuery(
//          "DELETE FROM Transaction e WHERE e IN (:transactions)").
//          setParameter("transactions", new ArrayList<Transaction>(
//          transactions)).executeUpdate();
        
    }
    
    @DeleteMapping("admin/delete-best-map/{id}/{id_prov}/{id_monper}")
    @ResponseBody
    @Transactional
    public void delete_best_map(@PathVariable("id") Integer id, @PathVariable("id_prov") String id_prov, @PathVariable("id_monper") String id_monper) {
        
        em.createNativeQuery("delete from best_map where id_best_practice = '"+id+"' and id_prov = '"+id_prov+"' and id_monper = '"+id_monper+"' ").executeUpdate();
//        em.createQuery(
//          "DELETE FROM Transaction e WHERE e IN (:transactions)").
//          setParameter("transactions", new ArrayList<Transaction>(
//          transactions)).executeUpdate();
        
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
