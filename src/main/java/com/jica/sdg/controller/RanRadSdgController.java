package com.jica.sdg.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.jica.sdg.model.GovActivity;
import com.jica.sdg.model.GovIndicator;
import com.jica.sdg.model.GovMap;
import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.RanRad;
import com.jica.sdg.model.NsaActivity;
import com.jica.sdg.model.NsaIndicator;
import com.jica.sdg.model.NsaMap;
import com.jica.sdg.model.NsaProgram;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.model.SdgTarget;
import com.jica.sdg.service.IGovActivityService;
import com.jica.sdg.service.IGovIndicatorService;
import com.jica.sdg.service.IGovMapService;
import com.jica.sdg.service.IGovProgramService;
import com.jica.sdg.service.IMonPeriodService;
import com.jica.sdg.service.INsaActivityService;
import com.jica.sdg.service.INsaIndicatorService;
import com.jica.sdg.service.INsaMapService;
import com.jica.sdg.service.INsaProgramService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.ISdgDisaggreDetailService;
import com.jica.sdg.service.ISdgDisaggreService;
import com.jica.sdg.service.ISdgGoalsService;
import com.jica.sdg.service.ISdgIndicatorService;
import com.jica.sdg.service.ISdgTargetService;
import com.jica.sdg.service.IUnitService;
import java.util.Arrays;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

@Controller
public class RanRadSdgController {

	@Autowired
	ISdgGoalsService sdgGoalsService;
	
	@Autowired
	ISdgTargetService sdgTargetService;
	
	@Autowired
	ISdgIndicatorService sdgIndicatorService;
	
	@Autowired
	ISdgDisaggreService sdgDisaggreService;
	
	@Autowired
	ISdgDisaggreDetailService sdgDisaggreDetailService;
	
	@Autowired
	IMonPeriodService monPerService;
	
	@Autowired
	IGovProgramService govProgService;
	
	@Autowired
	IProvinsiService prov;
	
	@Autowired
	IMonPeriodService monPeriodService;
	
	@Autowired
	IRoleService roleService;
	
	@Autowired
	IGovActivityService govActivityService;
	
	@Autowired
	IGovIndicatorService govIndicatorService;
	
	@Autowired
	INsaProgramService nsaProgService;
	
	@Autowired
	INsaActivityService nsaActivityService;
	
	@Autowired
	INsaIndicatorService nsaIndicatorService;
	
	@Autowired
	IGovMapService govMapService;
	
	@Autowired
	IUnitService unitService;
	
	@Autowired
	INsaMapService nsaMapService;
        
        @Autowired
	private EntityManager em;
	
	//*********************** SDG ***********************
	@GetMapping("admin/list-sdgGoals")
    public @ResponseBody Map<String, Object> sdgGoalsList() {
        List<SdgGoals> list = sdgGoalsService.findAll();
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
	@PostMapping(path = "admin/save-sdgGoals", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveSdg(@RequestBody SdgGoals sdg) {
		sdgGoalsService.saveSdgGoals(sdg);
	}
	
	@GetMapping("admin/get-sdgGoals/{id}")
    public @ResponseBody Map<String, Object> getSdgGoals(@PathVariable("id") String id) {
        Optional<SdgGoals> list = sdgGoalsService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
	@DeleteMapping("admin/delete-sdgGoals/{id}")
	@ResponseBody
	public void deleteSdg(@PathVariable("id") String id) {
		sdgGoalsService.deleteSdgGoals(id);
	}
	
	@GetMapping("admin/ran_rad/sdg/goals/{id}/target")
    public String target(Model model, @PathVariable("id") String id, HttpSession session) {
		Optional<SdgGoals> list = sdgGoalsService.findOne(id);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("content", foundUpdateObject));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/ran_rad/sdg/target";
    }
	
	@GetMapping("admin/list-sdgTarget/{id}")
    public @ResponseBody Map<String, Object> sdgtargetList(@PathVariable("id") String id) {
        List<SdgTarget> list = sdgTargetService.findAll(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
	@PostMapping(path = "admin/save-sdgTarget", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveSdg(@RequestBody SdgTarget sdg) {
		sdgTargetService.saveSdgTarget(sdg);
	}
	
	@GetMapping("admin/get-sdgTarget/{id}")
    public @ResponseBody Map<String, Object> getSdgTarget(@PathVariable("id") String id) {
        Optional<SdgTarget> list = sdgTargetService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
	@DeleteMapping("admin/delete-sdgTarget/{id}")
	@ResponseBody
	public void deleteSdgtarget(@PathVariable("id") String id) {
		sdgTargetService.deleteSdgTarget(id);
	}
    
    @GetMapping("admin/ran_rad/sdg/goals/{id}/target/{id_target}/indicator")
    public String sdg(Model model, @PathVariable("id") String id, @PathVariable("id_target") String id_target, HttpSession session) {
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
    
    @GetMapping("admin/list-sdgIndicator/{id_goals}/{id_target}")
    public @ResponseBody Map<String, Object> sdgIndicatorList(@PathVariable("id_goals") String id_goals, @PathVariable("id_target") String id_target) {
//        String sql = "select a.id_indicator, a.id_goals, a.id_target, a.nm_indicator, b.nm_unit from sdg_indicator a Left Join ref_unit b on a.unit = b.id_unit where a.id_goals = :id_goals and a.id_target = :id_target";
//        Query query  = em.createNativeQuery(sql)
//                        .setParameter("id_goals",id_goals)
//                        .setParameter("id_target", id_target);
//        List list = query.getResultList();
        
        List list = sdgIndicatorService.findAllGrid(id_goals, id_target);
	Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-sdgIndicator", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveSdgIndicator(@RequestBody SdgIndicator sdg) {
    	sdgIndicatorService.saveSdgIndicator(sdg);
	}
    
    @GetMapping("admin/get-sdgIndicator/{id}")
    public @ResponseBody Map<String, Object> getSdgIndicator(@PathVariable("id") String id) {
        Optional<SdgIndicator> list = sdgIndicatorService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-sdgIndicator/{id}")
	@ResponseBody
	public void deleteSdgIndicator(@PathVariable("id") String id) {
    	sdgIndicatorService.deleteSdgIndicator(id);
	}
    
    @GetMapping("admin/ran_rad/sdg/goals/{id}/target/{id_target}/indicator/{id_indicator}/disaggre")
    public String disagre(Model model, @PathVariable("id") String id, @PathVariable("id_target") String id_target, @PathVariable("id_indicator") String id_indicator, HttpSession session) {
    	Optional<SdgGoals> list = sdgGoalsService.findOne(id);
    	Optional<SdgTarget> list1 = sdgTargetService.findOne(id_target);
    	Optional<SdgIndicator> list2 = sdgIndicatorService.findOne(id_indicator);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("goals", foundUpdateObject));
        list1.ifPresent(foundUpdate -> model.addAttribute("target", foundUpdate));
        list2.ifPresent(foundUpdate1 -> model.addAttribute("indicator", foundUpdate1));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/ran_rad/sdg/disaggre";
    }
    
    @GetMapping("admin/list-sdgDisaggre/{id_indicator}")
    public @ResponseBody Map<String, Object> sdgDisaggreList(@PathVariable("id_indicator") String id_indicator) {
        List<SdgDisaggre> list = sdgDisaggreService.findAll(id_indicator);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-sdgDisaggre", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveSdgDisaggre(@RequestBody SdgDisaggre sdg) {
    	sdgDisaggreService.saveSdgDisaggre(sdg);
	}
    
    @GetMapping("admin/get-sdgDisaggre/{id}")
    public @ResponseBody Map<String, Object> getSdgDisaggre(@PathVariable("id") String id) {
        Optional<SdgDisaggre> list = sdgDisaggreService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-sdgDisaggre/{id}")
	@ResponseBody
	public void deleteSdgDisaggre(@PathVariable("id") String id) {
    	sdgDisaggreService.deleteSdgDisaggre(id);
	}
    
    @GetMapping("admin/ran_rad/sdg/goals/{id}/target/{id_target}/indicator/{id_indicator}/disaggre/{id_disaggre}")
    public String disagreDetail(Model model, @PathVariable("id") String id, @PathVariable("id_target") String id_target, @PathVariable("id_indicator") String id_indicator,
                                @PathVariable("id_disaggre") String id_disaggre, HttpSession session) {
    	Optional<SdgGoals> list = sdgGoalsService.findOne(id);
    	Optional<SdgTarget> list1 = sdgTargetService.findOne(id_target);
    	Optional<SdgIndicator> list2 = sdgIndicatorService.findOne(id_indicator);
    	Optional<SdgDisaggre> list3 = sdgDisaggreService.findOne(id_disaggre);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("goals", foundUpdateObject));
        list1.ifPresent(foundUpdate -> model.addAttribute("target", foundUpdate));
        list2.ifPresent(foundUpdate1 -> model.addAttribute("indicator", foundUpdate1));
        list3.ifPresent(foundUpdate2 -> model.addAttribute("disaggre", foundUpdate2));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/ran_rad/sdg/disaggre-detail";
    }
    
    @GetMapping("admin/list-sdgDisaggreDetail/{id}")
    public @ResponseBody Map<String, Object> sdgDisaggreDetailList(@PathVariable("id") String id) {
        List<SdgDisaggreDetail> list = sdgDisaggreDetailService.findAll(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-sdgDisaggreDetail", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveSdgDisaggreDetail(@RequestBody SdgDisaggreDetail sdg) {
    	sdgDisaggreDetailService.saveSdgDisaggreDetail(sdg);
	}
    
    @GetMapping("admin/get-sdgDisaggreDetail/{id}")
    public @ResponseBody Map<String, Object> getSdgDisaggreDetail(@PathVariable("id") Integer id) {
        Optional<SdgDisaggreDetail> list = sdgDisaggreDetailService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-sdgDisaggreDetail/{id}")
	@ResponseBody
	public void deleteSdgDisaggre(@PathVariable("id") Integer id) {
    	sdgDisaggreDetailService.deleteSdgDisaggreDetail(id);
	}
    
  //*********************** GOV PROGRAM ***********************
    //@GetMapping("admin/list-govProg/{id_role}/{id_monper}")
    @GetMapping("admin/list-govProg/{id_monper}")
    //public @ResponseBody Map<String, Object> govProgList(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper) {
    public @ResponseBody Map<String, Object> govProgList(@PathVariable("id_monper") String id_monper) {
        List<GovProgram> list = govProgService.findAllBy(id_monper);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-govProg", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void savGovProg(@RequestBody GovProgram gov) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	govProgService.saveGovProgram(gov);
	}
    
    @GetMapping("admin/get-govProg/{id}")
    public @ResponseBody Map<String, Object> getGovProg(@PathVariable("id") String id) {
        Optional<GovProgram> list = govProgService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-govProg/{id}")
	@ResponseBody
	public void deleteGovProg(@PathVariable("id") String id) {
    	govProgService.deleteGovProgram(id);
	}
    
    @GetMapping("admin/ran_rad/gov/program/{id_program}/activity")
    public String gov_kegiatan(Model model, @PathVariable("id_program") String id_program, HttpSession session) {
    	Optional<GovProgram> list = govProgService.findOne(id_program);
    	//Integer id_role = list.get().getId_role();
    	
    	//Optional<Role> role = roleService.findOne(id_role);
    	Optional<Role> role = roleService.findOne((Integer) session.getAttribute("id_role"));
    	Optional<RanRad> ranrad = monPeriodService.findOne(list.get().getId_monper());
    	Optional<Provinsi> provin = prov.findOne(ranrad.get().getId_prov());
    	Optional<RanRad> monper = monPeriodService.findOne(list.get().getId_monper());
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        list.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        role.ifPresent(foundUpdateObject -> model.addAttribute("role", foundUpdateObject));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
//       System.out.println(session);
        return "admin/ran_rad/gov/activity";
    }
    
    @GetMapping("admin/list-govActivity/{id_program}")
    public @ResponseBody Map<String, Object> govActivityList(@PathVariable("id_program") String id_program) {
        List<GovActivity> list = govActivityService.findAll(id_program);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-govActivity", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveGovActivity(@RequestBody GovActivity gov) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	govActivityService.saveGovActivity(gov);
	}
    
    @GetMapping("admin/get-govActivity/{id}")
    public @ResponseBody Map<String, Object> getGovActivity(@PathVariable("id") String id) {
        Optional<GovActivity> list = govActivityService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-govActivity/{id}")
	@ResponseBody
	public void deleteGovActivity(@PathVariable("id") String id) {
    	govActivityService.deleteGovActivity(id);
	}
    
    @GetMapping("admin/ran_rad/gov/program/{id_program}/activity/{id_activity}/indicator")
    public String gov_indikator(Model model, @PathVariable("id_program") String id_program,
                                @PathVariable("id_activity") String id_activity, HttpSession session) {
    	Optional<GovProgram> list = govProgService.findOne(id_program);
    	Optional<GovActivity> list1 = govActivityService.findOne(id_activity);
    	//Integer id_role = list.get().getId_role();
    	Optional<Role> role = roleService.findOne((Integer) session.getAttribute("id_role"));
    	Optional<RanRad> ranrad = monPeriodService.findOne(list.get().getId_monper());
    	Optional<Provinsi> provin = prov.findOne(ranrad.get().getId_prov());
    	Optional<RanRad> monper = monPeriodService.findOne(list.get().getId_monper());
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        list.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        list1.ifPresent(foundUpdateObject1 -> model.addAttribute("govActivity", foundUpdateObject1));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("unit", unitService.findAll());
        provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        role.ifPresent(foundUpdateObject -> model.addAttribute("role", foundUpdateObject));
        model.addAttribute("sdgIndicator", sdgIndicatorService.findAll());
        return "admin/ran_rad/gov/indicator";
    }
    
    @GetMapping("admin/list-govIndicator/{id_program}/{id_activity}")
    public @ResponseBody Map<String, Object> govIndicatorList(@PathVariable("id_program") String id_program, @PathVariable("id_activity") String id_activity) {
        List list = govIndicatorService.findAllIndi(id_program, id_activity);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-govIndicator/{sdg_indicator}/{id_monper}/{id_prov}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveGovIndicator(@RequestBody GovIndicator gov,
			@PathVariable("sdg_indicator") String sdg_indicator,
			@PathVariable("id_monper") Integer id_monper,
			@PathVariable("id_prov") String id_prov) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	govIndicatorService.saveGovIndicator(gov);
    	
    	if(!sdg_indicator.equals("0")) {
    		String[] a = sdg_indicator.split("---");
    		String id_goals = a[0];
    		String id_target = a[1];
    		String id_indicator = a[2];
    		GovMap map = new GovMap();
    		map.setId_goals(id_goals);
    		map.setId_target(id_target);
    		map.setId_indicator(id_indicator);
    		map.setId_gov_indicator(gov.getId_gov_indicator());
    		map.setId_monper(id_monper);
    		map.setId_prov(id_prov);
    		govMapService.saveGovMap(map);
    	}
	}
    
    @GetMapping("admin/get-govIndicator/{id}")
    public @ResponseBody Map<String, Object> getgovIndicator(@PathVariable("id") String id) {
        Optional<GovIndicator> list = govIndicatorService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-govIndicator/{id}")
	@ResponseBody
	public void deleteGovIndicator(@PathVariable("id") String id) {
    	govIndicatorService.deleteGovIndicator(id);
	}
    
  //*********************** NON GOV PROGRAM ***********************
    @GetMapping("admin/list-nsaProg/{id_role}/{id_monper}")
    public @ResponseBody Map<String, Object> nsaProgList(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper) {
        List<NsaProgram> list = nsaProgService.findAllBy(id_role, id_monper);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-nsaProg", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void savNsaProg(@RequestBody NsaProgram gov) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	nsaProgService.saveNsaProgram(gov);
	}
    
    @GetMapping("admin/get-nsaProg/{id}")
    public @ResponseBody Map<String, Object> getNsaProg(@PathVariable("id") String id) {
        Optional<NsaProgram> list = nsaProgService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-nsaProg/{id}")
	@ResponseBody
	public void deleteNsaProg(@PathVariable("id") String id) {
    	nsaProgService.deleteNsaProgram(id);
	}
    
    @GetMapping("admin/ran_rad/non-gov/program/{id_program}/activity")
    public String nsa_kegiatan(Model model, @PathVariable("id_program") String id_program, HttpSession session) {
    	Optional<NsaProgram> list = nsaProgService.findOne(id_program);
    	Integer id_role = list.get().getId_role();
    	Optional<Role> role = roleService.findOne(id_role);
    	Optional<Provinsi> provin = prov.findOne(role.get().getId_prov());
    	Optional<RanRad> monper = monPeriodService.findOne(list.get().getId_monper());
    	provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        role.ifPresent(foundUpdateObject -> model.addAttribute("role", foundUpdateObject));
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        list.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/ran_rad/non-gov/activity";
    }
    
    @GetMapping("admin/list-nsaActivity/{id_program}")
    public @ResponseBody Map<String, Object> nsaActivityList(@PathVariable("id_program") String id_program) {
        List<NsaActivity> list = nsaActivityService.findAll(id_program);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-nsaActivity", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveNsaActivity(@RequestBody NsaActivity gov) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	nsaActivityService.saveNsaActivity(gov);
	}
    
    @GetMapping("admin/get-nsaActivity/{id}")
    public @ResponseBody Map<String, Object> getNsaActivity(@PathVariable("id") String id) {
        Optional<NsaActivity> list = nsaActivityService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-nsaActivity/{id}")
	@ResponseBody
	public void deleteNsaActivity(@PathVariable("id") String id) {
    	nsaActivityService.deleteNsaActivity(id);
	}
    
    @GetMapping("admin/ran_rad/non-gov/program/{id_program}/activity/{id_activity}/indicator")
    public String nsa_indikator(Model model, @PathVariable("id_program") String id_program,
                                @PathVariable("id_activity") String id_activity, HttpSession session) {
    	Optional<NsaProgram> list = nsaProgService.findOne(id_program);
    	Optional<NsaActivity> list1 = nsaActivityService.findOne(id_activity);
    	Integer id_role = list.get().getId_role();
    	Optional<Role> role = roleService.findOne(id_role);
    	Optional<Provinsi> provin = prov.findOne(role.get().getId_prov());
    	Optional<RanRad> monper = monPeriodService.findOne(list.get().getId_monper());
    	provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        role.ifPresent(foundUpdateObject -> model.addAttribute("role", foundUpdateObject));
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        list.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        list1.ifPresent(foundUpdateObject1 -> model.addAttribute("govActivity", foundUpdateObject1));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("unit", unitService.findAll());
        model.addAttribute("sdgIndicator", sdgIndicatorService.findAll());
        return "admin/ran_rad/non-gov/indicator";
    }
    
    @GetMapping("admin/list-nsaIndicator/{id_program}/{id_activity}")
    public @ResponseBody Map<String, Object> nsaIndicatorList(@PathVariable("id_program") String id_program, @PathVariable("id_activity") String id_activity) {
        List<NsaIndicator> list = nsaIndicatorService.findAllIndi(id_program, id_activity);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-nsaIndicator/{sdg_indicator}/{id_monper}/{id_prov}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveNsaIndicator(@RequestBody NsaIndicator gov,
			@PathVariable("sdg_indicator") String sdg_indicator,
			@PathVariable("id_monper") Integer id_monper,
			@PathVariable("id_prov") String id_prov) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	nsaIndicatorService.saveNsaIndicator(gov);
    	
    	if(!sdg_indicator.equals("0")) {
    		String[] a = sdg_indicator.split("---");
    		String id_goals = a[0];
    		String id_target = a[1];
    		String id_indicator = a[2];
    		NsaMap map = new NsaMap();
    		map.setId_goals(id_goals);
    		map.setId_target(id_target);
    		map.setId_indicator(id_indicator);
    		map.setId_nsa_indicator(gov.getId_nsa_indicator());
    		map.setId_monper(id_monper);
    		map.setId_prov(id_prov);
    		nsaMapService.saveNsaMap(map);
    	}
	}
    
    @GetMapping("admin/get-nsaIndicator/{id}")
    public @ResponseBody Map<String, Object> getNsaIndicator(@PathVariable("id") String id) {
        Optional<NsaIndicator> list = nsaIndicatorService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-nsaIndicator/{id}")
	@ResponseBody
	public void deleteNsaIndicator(@PathVariable("id") String id) {
    	nsaIndicatorService.deleteNsaIndicator(id);
	}
    
  //*********************** RAN / RAD ***********************
    
    @GetMapping("admin/list-monPer/{id_prov}")
    public @ResponseBody Map<String, Object> monPerList(@PathVariable("id_prov") String id_prov) {
        List<RanRad> list = monPerService.findAll(id_prov);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-monPer", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveMonPer(@RequestBody RanRad sdg) {
    	monPerService.saveMonPeriod(sdg);
	}
    
    @GetMapping("admin/get-monPer/{id}")
    public @ResponseBody Map<String, Object> getMonPer(@PathVariable("id") Integer id) {
        Optional<RanRad> list = monPerService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-monPer/{id}")
	@ResponseBody
	public void deleteMonPer(@PathVariable("id") Integer id) {
    	monPerService.deleteMonPeriod(id);
	}
    
  //*********************** MAPPING ***********************
    @GetMapping("admin/ran_rad/map/goals/{id_monper}")
    public String goals(Model model, HttpSession session, @PathVariable("id_monper") Integer id_monper) {
    	Optional<RanRad> monper = monPeriodService.findOne(id_monper);
    	Optional<Provinsi> provin = prov.findOne(monper.get().getId_prov());
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        exportExcell(id_monper);
                
        return "admin/ran_rad/map/goals";
    }
    
    public void exportExcell(Integer id_monper){
        String sql = "select * from ran_rad where id_monper = '"+id_monper+"'";
        Query result = em.createNativeQuery(sql);
         Map<String, Object> test = new HashMap<>();
         test.put("test",result.getResultList() );
         JSONObject jsonObjlapgdbh = new JSONObject(test);
         JSONArray c = jsonObjlapgdbh.getJSONArray("test");
         JSONArray d = c.getJSONArray(0);
         String id_prov = d.getString(11);
         System.out.println(id_prov);

        
    }
    
    @GetMapping("admin/ran_rad/map/goals/{id_monper}/{id}/target")
    public String targetMap(Model model, @PathVariable("id_monper") Integer id_monper, @PathVariable("id") String id, HttpSession session) {
		Optional<SdgGoals> list = sdgGoalsService.findOne(id);
		Optional<RanRad> monper = monPeriodService.findOne(id_monper);
    	Optional<Provinsi> provin = prov.findOne(monper.get().getId_prov());
    	provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("content", foundUpdateObject));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_monper", id_monper);
        return "admin/ran_rad/map/target";
    }
    
    @GetMapping("admin/ran_rad/map/goals/{id_monper}/{id}/target/{id_target}/indicator")
    public String sdgMap(Model model, @PathVariable("id_monper") Integer id_monper, @PathVariable("id") String id, @PathVariable("id_target") String id_target, HttpSession session) {
    	Optional<SdgGoals> list = sdgGoalsService.findOne(id);
    	Optional<SdgTarget> list1 = sdgTargetService.findOne(id_target);
    	Optional<RanRad> monper = monPeriodService.findOne(id_monper);
    	Optional<Provinsi> provin = prov.findOne(monper.get().getId_prov());
    	provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("goals", foundUpdateObject));
        list1.ifPresent(foundUpdate -> model.addAttribute("target", foundUpdate));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_monper", id_monper);
        return "admin/ran_rad/map/sdgs_indicator";
    }
    
    @GetMapping("admin/list-govIndicatorByRole")
    public @ResponseBody Map<String, Object> govIndByRole(HttpSession session) {
        List <GovIndicator> list = govIndicatorService.findAllByRole((Integer)session.getAttribute("id_role"));
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-govMap", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveGovMap(@RequestBody GovMap sdg) {
    	govMapService.deleteGovMapBySdgInd(sdg.getId_indicator());
    	govMapService.saveGovMap(sdg);
	}
    
    @GetMapping("admin/list-getIdGovMap/{id}")
    public @ResponseBody Map<String, Object> govIndByRole(HttpSession session, @PathVariable("id") String id) {
        List <GovMap> list = govMapService.findAllBySdgInd(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
}
