package com.jica.sdg.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jica.sdg.model.BestMap;
import com.jica.sdg.model.GovActivity;
import com.jica.sdg.model.GovFunding;
import com.jica.sdg.model.GovIndicator;
import com.jica.sdg.model.GovMap;
import com.jica.sdg.model.GovProgram;
import com.jica.sdg.model.GovTarget;
import com.jica.sdg.model.NsaActivity;
import com.jica.sdg.model.NsaFunding;
import com.jica.sdg.model.NsaIndicator;
import com.jica.sdg.model.NsaMap;
import com.jica.sdg.model.NsaProgram;
import com.jica.sdg.model.NsaTarget;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.RanRad;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.model.SdgTarget;
import com.jica.sdg.model.Unit;
import com.jica.sdg.service.IBestMapService;
import com.jica.sdg.service.IGovActivityService;
import com.jica.sdg.service.IGovFundingService;
import com.jica.sdg.service.IGovIndicatorService;
import com.jica.sdg.service.IGovMapService;
import com.jica.sdg.service.IGovProgramService;
import com.jica.sdg.service.IGovTargetService;
import com.jica.sdg.service.IMonPeriodService;
import com.jica.sdg.service.INsaActivityService;
import com.jica.sdg.service.INsaFundingService;
import com.jica.sdg.service.INsaIndicatorService;
import com.jica.sdg.service.INsaMapService;
import com.jica.sdg.service.INsaProgramService;
import com.jica.sdg.service.INsaTargetService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.ISdgDisaggreDetailService;
import com.jica.sdg.service.ISdgDisaggreService;
import com.jica.sdg.service.ISdgGoalsService;
import com.jica.sdg.service.ISdgIndicatorService;
import com.jica.sdg.service.ISdgTargetService;
import com.jica.sdg.service.IUnitService;

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
	
	@Autowired
	IGovTargetService govTargetService;
	
	@Autowired
	IGovFundingService govFundingService;
	
	@Autowired
	INsaTargetService nsaTargetService;
	
	@Autowired
	INsaFundingService nsaFundingService;
	
	@Autowired
	IBestMapService bestMapService;
	
	//*********************** SDG ***********************
	@GetMapping("admin/ran_rad/sdg/increment_decrement")
    public String replace(Model model, HttpSession session) {
        return "admin/ran_rad/sdg/sdgs_indicator :: increment_decrement";
    }
	

    @GetMapping("admin/ran_rad/sdg/goals")
    public String goals(Model model, HttpSession session) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        String bhs = (String) session.getAttribute("bahasa");
        if (bhs == null) {bhs = "0";}
        model.addAttribute("lang", bhs);
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/ran_rad/sdg/goals";
    }

	@GetMapping("admin/list-sdgGoals")
    public @ResponseBody
    Map<String, Object> sdgGoalsList() {
        List<SdgGoals> list = sdgGoalsService.findAll();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", list);
        return hasil;
    }
	
	@PostMapping(path = "admin/save-sdgGoals", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveSdg(@RequestBody SdgGoals sdg) {
		sdgGoalsService.saveSdgGoals(sdg);
	}
	
	@GetMapping("admin/get-sdgGoals/{id}")
    public @ResponseBody Map<String, Object> getSdgGoals(@PathVariable("id") int id) {
        Optional<SdgGoals> list = sdgGoalsService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
	@DeleteMapping("admin/delete-sdgGoals/{id}")
	@ResponseBody
	public void deleteSdg(@PathVariable("id") int id) {
		sdgGoalsService.deleteSdgGoals(id);
	}
	
	@GetMapping("admin/ran_rad/sdg/goals/{id}/target")
    public String target(Model model, @PathVariable("id") int id, HttpSession session) {
		Optional<SdgGoals> list = sdgGoalsService.findOne(id);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("content", foundUpdateObject));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/ran_rad/sdg/target";
    }
	
	@GetMapping("admin/cek-id_goals/{id_goals}")
    public @ResponseBody Map<String, Object> cekGoals(@PathVariable("id_goals") String id_goals) {
        String sql = "select count(id_goals) from sdg_goals where id_goals=:id_goals";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_goals", id_goals);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
	@GetMapping("admin/list-sdgTarget/{id}")
//    public @ResponseBody Map<String, Object> sdgtargetList(@PathVariable("id") int id) {
    public @ResponseBody Map<String, Object> sdgtargetList(@PathVariable("id") Integer id) {
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
//    public @ResponseBody Map<String, Object> getSdgTarget(@PathVariable("id") int id) {
    public @ResponseBody Map<String, Object> getSdgTarget(@PathVariable("id") Integer id) {
        Optional<SdgTarget> list = sdgTargetService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
	@GetMapping("admin/cek-id_target/{id_target}/{id_goals}")
    public @ResponseBody Map<String, Object> cektarget(@PathVariable("id_target") String id_target,@PathVariable("id_goals") String id_goals) {
        String sql = "select count(id_target) from sdg_target where id_target=:id_target and id_goals=:id_goals";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_target", id_target);
        query.setParameter("id_goals", id_goals);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
	
	@DeleteMapping("admin/delete-sdgTarget/{id}")
	@ResponseBody
//	public void deleteSdgtarget(@PathVariable("id") int id) {
	public void deleteSdgtarget(@PathVariable("id") Integer id) {
		sdgTargetService.deleteSdgTarget(id);
	}
	
	@GetMapping("admin/count-sdgTarget/{id}")
  public @ResponseBody Map<String, Object> countSdgTarget(@PathVariable("id") Integer id) {
      Integer list = sdgTargetService.countTarget(id);
      Map<String, Object> hasil = new HashMap<>();
      hasil.put("content",list);
      return hasil;
  }
    
    @GetMapping("admin/ran_rad/sdg/goals/{id}/target/{id_target}/indicator")
//    public String sdg(Model model, @PathVariable("id") int id, @PathVariable("id_target") int id_target, HttpSession session) {
    public String sdg(Model model, @PathVariable("id") int id, @PathVariable("id_target") Integer id_target, HttpSession session) {
    	Optional<SdgGoals> list = sdgGoalsService.findOne(id);
    	Optional<SdgTarget> list1 = sdgTargetService.findOne(id_target);
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> listRole = roleService.findOne(id_role);
    	String privilege = listRole.get().getPrivilege();
    	String id_prov = listRole.get().getId_prov();
    	String sql;
    	if(privilege.equals("SUPER")) {
    		sql = "select * from ref_unit";
    	}else {
    		sql = "select a.* from ref_unit a left join ref_role b on a.id_role = b.id_role where b.id_prov = '"+id_prov+"' or a.id_role = 1";
    	}
    	Query listUnit = em.createNativeQuery(sql);
        List<Object[]> rows = listUnit.getResultList();
        List<Unit> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(
                        new Unit((Integer)row[0], (String) row[1], (Integer)row[2], (Integer)row[3])
            );
        }
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("goals", foundUpdateObject));
        list1.ifPresent(foundUpdate -> model.addAttribute("target", foundUpdate));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("unit", result);
        return "admin/ran_rad/sdg/sdgs_indicator";
    }
    
    @GetMapping("admin/list-sdgIndicator/{id_goals}/{id_target}")
    public @ResponseBody Map<String, Object> sdgIndicatorList(@PathVariable("id_goals") Integer id_goals, @PathVariable("id_target") Integer id_target) {
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
    
    @GetMapping("admin/cek-id_indicator/{id_indicator}/{id_target}")
    public @ResponseBody Map<String, Object> cekindicator(@PathVariable("id_indicator") String id_indicator,@PathVariable("id_target") String id_target) {
        String sql = "select count(id_indicator) from sdg_indicator where id_indicator=:id_indicator and id_target = :id_target";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_indicator", id_indicator);
        query.setParameter("id_target", id_target);
        List list   = query.getResultList();
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
    public @ResponseBody Map<String, Object> getSdgIndicator(@PathVariable("id") Integer id) {
        Optional<SdgIndicator> list = sdgIndicatorService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-sdgIndicator/{id}")
	@ResponseBody
	public void deleteSdgIndicator(@PathVariable("id") Integer id) {
    	sdgIndicatorService.deleteSdgIndicator(id);
	}
    
    @GetMapping("admin/count-sdgIndicator/{id_goals}/{id_target}")
    public @ResponseBody Map<String, Object> countsdgIndicator(@PathVariable("id_goals") Integer id_goals, @PathVariable("id_target") Integer id_target) {
    	Integer list = sdgIndicatorService.countIndicator(id_goals, id_target);
    	Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/ran_rad/sdg/goals/{id}/target/{id_target}/indicator/{id_indicator}/disaggre")
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
        return "admin/ran_rad/sdg/disaggre";
    }
    
    @GetMapping("admin/list-sdgDisaggre/{id_indicator}")
    public @ResponseBody Map<String, Object> sdgDisaggreList(@PathVariable("id_indicator") Integer id_indicator) {
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
    public @ResponseBody Map<String, Object> getSdgDisaggre(@PathVariable("id") Integer id) {
        Optional<SdgDisaggre> list = sdgDisaggreService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/cek-id_disaggre/{id_disaggre}")
    public @ResponseBody Map<String, Object> cekDis(@PathVariable("id_disaggre") String id_disaggre) {
        String sql = "select count(id_disaggre) from sdg_ranrad_disaggre where id_disaggre=:id_disaggre";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_disaggre", id_disaggre);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/count-sdgDisaggre/{id}")
    public @ResponseBody Map<String, Object> countsdgDisaggreDetail(@PathVariable("id") Integer id) {
        Integer list = sdgDisaggreService.countDisaggre(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-sdgDisaggre/{id}")
	@ResponseBody
	public void deleteSdgDisaggre1(@PathVariable("id") Integer id) {
    	sdgDisaggreService.deleteSdgDisaggre(id);
	}
    
    @GetMapping("admin/ran_rad/sdg/goals/{id}/target/{id_target}/indicator/{id_indicator}/disaggre/{id_disaggre}")
//    public String disagreDetail(Model model, @PathVariable("id") int id, @PathVariable("id_target") int id_target, @PathVariable("id_indicator") String id_indicator,
    public String disagreDetail(Model model, @PathVariable("id") int id, @PathVariable("id_target") Integer id_target, @PathVariable("id_indicator") Integer id_indicator,
                                @PathVariable("id_disaggre") Integer id_disaggre, HttpSession session) {
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
    public @ResponseBody Map<String, Object> sdgDisaggreDetailList(@PathVariable("id") Integer id) {
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
    
    @GetMapping("admin/ran_rad/preview-gov-program/{id_prov}/{id_monper}")
    public String gov_program(Model model, HttpSession session,@PathVariable("id_monper") String id_monper,@PathVariable("id_prov") String id_prov) {
    	Optional<Provinsi> list1 = prov.findOne(id_prov);
    	Optional<RanRad> monper = monPerService.findOne(Integer.parseInt(id_monper));
    	model.addAttribute("prov", list1.get().getNm_prov());
    	model.addAttribute("monper", monper.get().getStart_year().toString()+"-"+monper.get().getEnd_year().toString());
    	model.addAttribute("id_prov", id_prov);
    	model.addAttribute("id_monper", id_monper);
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        model.addAttribute("name", session.getAttribute("name"));
        String bhs = (String) session.getAttribute("bahasa");
        if (bhs == null) {bhs = "0";}
        model.addAttribute("lang", bhs);
        return "admin/ran_rad/gov/export";
    }
    
    @GetMapping("admin/ran_rad/preview-non_gov-program/{id_prov}/{id_monper}")
    public String nonGov_program(Model model, HttpSession session,@PathVariable("id_monper") String id_monper,@PathVariable("id_prov") String id_prov) {
    	Optional<Provinsi> list1 = prov.findOne(id_prov);
    	Optional<RanRad> monper = monPerService.findOne(Integer.parseInt(id_monper));
    	model.addAttribute("prov", list1.get().getNm_prov());
    	model.addAttribute("monper", monper.get().getStart_year().toString()+"-"+monper.get().getEnd_year().toString());
    	model.addAttribute("id_prov", id_prov);
    	model.addAttribute("id_monper", id_monper);
        model.addAttribute("title", "Define RAN/RAD/Non Government Program");
        model.addAttribute("name", session.getAttribute("name"));
        String bhs = (String) session.getAttribute("bahasa");
        if (bhs == null) {bhs = "0";}
        model.addAttribute("lang", bhs);
        return "admin/ran_rad/non-gov/export";
    }
    
    @PostMapping(path = "admin/save-govProg")
	@ResponseBody
	@Transactional
	public void savGovProg(HttpSession session,
			@RequestParam("id") Integer id,
			@RequestParam("id_program") String id_program,
			@RequestParam("nm_program") String nm_program,
			@RequestParam("nm_program_eng") String nm_program_eng,
			@RequestParam("id_monper") Integer id_monper,
			@RequestParam("rel_prog_id") String rel_prog_id,
			@RequestParam("internal_code") Integer internal_code,
			@RequestParam("rel_prov") String rel_prov
			/*@RequestParam("id_ministry") Integer id_ministry*/) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	GovProgram gov = new GovProgram();
    	gov.setId(id);
    	gov.setId_program(id_program);
    	gov.setNm_program(nm_program);
    	gov.setNm_program_eng(nm_program_eng);
    	gov.setId_monper(id_monper);
    	gov.setRel_prog_id(rel_prog_id);
    	gov.setInternal_code(internal_code);
    	gov.setCreated_by(id_role);
    	gov.setDate_created(new Date());
		/* gov.setId_ministry(id_ministry); */
    	govProgService.saveGovProgram(gov);
    	if(gov.getInternal_code()==null || gov.getInternal_code()==0) {
    		String sql = "select IFNULL(max(internal_code)+1,1) as no from gov_program where id_monper = :id_monper";
        	Query query = em.createNativeQuery(sql);
        	query.setParameter("id_monper", gov.getId_monper());
        	Integer no = ((BigInteger) query.getResultList().get(0)).intValue();
    		em.createNativeQuery("UPDATE gov_program set internal_code = '"+no+"' where id ='"+gov.getId()+"'").executeUpdate();
    	}
    	em.createNativeQuery("delete from assign_related_program where id_program = '"+gov.getId()+"' and id_monper = '"+id_monper+"'").executeUpdate();
    	if(!rel_prov.equals("")) {
    		String[] prov = rel_prov.split(",");
    		for(int i=0;i<prov.length;i++) {
    			em.createNativeQuery("INSERT IGNORE INTO assign_related_program(id_program,id_monper,id_prov)VALUES('"+gov.getId()+"','"+id_monper+"','"+prov[i]+"')").executeUpdate();
    		}
    	}
	}
    
    @GetMapping("admin/get-govProg/{id}")
    public @ResponseBody Map<String, Object> getGovProg(@PathVariable("id") Integer id) {
        Optional<GovProgram> list = govProgService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/get-relProv/{id}")
    public @ResponseBody Map<String, Object> getrelProv(@PathVariable("id") Integer id) {
    	String sql = "select DISTINCT id_prov from assign_related_program where id_program = '"+id+"' ";
		Query query = em.createNativeQuery(sql);
		List list = query.getResultList();
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/get-ministry/{id_prov}/{id_monper}")
    public @ResponseBody Map<String, Object> getministry(@PathVariable("id_prov") String id_prov,@PathVariable("id_monper") String id_monper) {
    	String sql = "select DISTINCT c.id_role, c.nm_role from assign_related_program a\r\n" + 
    			"left join gov_activity b on a.id_program = b.id_program\r\n" + 
    			"left join ref_role c on b.id_role = c.id_role\r\n" + 
    			"where a.id_prov = '"+id_prov+"' and a.id_monper = '"+id_monper+"'";
		Query query = em.createNativeQuery(sql);
		List list = query.getResultList();
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/get-relProg/{id_prov}/{id_monper}")
    public @ResponseBody Map<String, Object> getRel(@PathVariable("id_prov") String id_prov,@PathVariable("id_monper") String id_monper) {
    	Optional<RanRad> monper = monPerService.findOne(Integer.parseInt(id_monper));
    	String sql = "select DISTINCT d.id, d.id_program, d.nm_program, d.nm_program_eng, d.internal_code from assign_related_program a\r\n" + 
    			"left join gov_activity b on a.id_program = b.id_program\r\n" + 
    			"left join ref_role c on b.id_role = c.id_role\r\n" +
    			"left join gov_program d on a.id_program = d.id\r\n "+
    			"left join ran_rad e on a.id_monper = e.id_monper " +
    			"where a.id_prov = '"+id_prov+"' and (('"+monper.get().getStart_year()+"' between e.start_year and e.end_year) or ('"+monper.get().getEnd_year()+"' between e.start_year and e.end_year)) ";
		Query query = em.createNativeQuery(sql);
		List list = query.getResultList();
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-govProg/{id}")
	@ResponseBody
	public void deleteGovProg(@PathVariable("id") Integer id) {
    	govProgService.deleteGovProgram(id);
	}
    
    @GetMapping("admin/ran_rad/gov/program/{id_program}/activity")
    public String gov_kegiatan(Model model, @PathVariable("id_program") Integer id_program, HttpSession session) {
    	Optional<GovProgram> list = govProgService.findOne(id_program);
    	//Integer id_role = list.get().getId_role();
    	
    	//Optional<Role> role = roleService.findOne(id_role);
    	Optional<Role> role = roleService.findOne((Integer) session.getAttribute("id_role"));
    	String sql = "select * from ref_role where id_prov = :id_prov and cat_role='Government' and id_role!=1";
    	Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", role.get().getId_prov());
        List<Object[]> rows = query.getResultList();
        List<Role> result = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
        	result.add(
        				new Role((Integer)row[0]
                                , (String)row[1]
                                , (String) row[2]
                                , (String) row[3]
                                , (String) row[4]
                                , (String) row[5]
                                , (String) row[6]
                                , (String) row[7]
                                , (String) row[8])
        			);
        }
    	//List<Role> roleList = roleService.findRoleGov(role.get().getId_prov());
    	Optional<RanRad> ranrad = monPeriodService.findOne(list.get().getId_monper());
    	Optional<Provinsi> provin = prov.findOne(ranrad.get().getId_prov());
    	Optional<RanRad> monper = monPeriodService.findOne(list.get().getId_monper());
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        list.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        model.addAttribute("role", result);
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("privilege", role.get().getPrivilege());
        model.addAttribute("id_role", session.getAttribute("id_role"));
        return "admin/ran_rad/gov/activity";
    }
    
    @GetMapping("admin/list-govActivity/{id_program}/{id_role}")
    public @ResponseBody Map<String, Object> govActivityList(@PathVariable("id_program") Integer id_program, @PathVariable("id_role") Integer id_role) {
        List<GovActivity> list = govActivityService.findAll(id_program, id_role);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-govActivity/{id_program}")
    public @ResponseBody Map<String, Object> govActivityList(@PathVariable("id_program") Integer id_program, HttpSession session) {
    	Optional<Role> role = roleService.findOne((Integer) session.getAttribute("id_role"));
    	String id_role="";
    	if(role.get().getPrivilege().equals("USER")) {
    		id_role = " and a.id_role = '"+role.get().getId_role()+"'";
    	}
    	String sql = "select a.id, a.nm_activity, a.nm_activity_eng, a.internal_code,b.nm_role "
    			+ "from gov_activity a "
    			+ "left join ref_role b on a.id_role = b.id_role "
    			+ "where a.id_program=:id_program "+id_role+" order by CAST(a.internal_code AS UNSIGNED)";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_program", id_program);
        List list   = query.getResultList();
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/count-govActivity/{id_program}")
    public @ResponseBody Map<String, Object> countgovActivity(@PathVariable("id_program") Integer id_program) {
        Integer list = govActivityService.countGovActivity(id_program);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-govActivityByProv/{id_program}/{id_prov}")
    public @ResponseBody Map<String, Object> govActivityListByProv(@PathVariable("id_program") Integer id_program, @PathVariable("id_prov") String id_prov) {
        List<GovActivity> list = govActivityService.findGovActivityByIdAndProv(id_program, id_prov);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-govActivity/{id_monper}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	@Transactional
	public void saveGovActivity(@RequestBody GovActivity gov,@PathVariable("id_monper") Integer id_monper) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	govActivityService.saveGovActivity(gov);
    	if(gov.getInternal_code()==null || gov.getInternal_code()==0) {
    		String sql = "select IFNULL(max(a.internal_code)+1,1) as no "
    				+ "from gov_activity a "
    				+ "left join gov_program b on a.id_program = b.id "
    				+ "where b.id_monper = :id_monper and a.id_program =:id_program ";
        	Query query = em.createNativeQuery(sql);
        	query.setParameter("id_monper", id_monper);
        	query.setParameter("id_program", gov.getId_program());
        	Integer no = ((BigInteger) query.getResultList().get(0)).intValue();
    		em.createNativeQuery("UPDATE gov_activity set internal_code = '"+no+"' where id ='"+gov.getId()+"'").executeUpdate();
    	}
	}
    
    @GetMapping("admin/cek-govActivity/{id_program}/{id_activity}")
    public @ResponseBody Map<String, Object> cekgovActivity(@PathVariable("id_program") String id_program,@PathVariable("id_activity") String id_activity) {
        String sql = "select count(id_activity) from gov_activity where id_program=:id_program and id_activity=:id_activity";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_program", id_program);
        query.setParameter("id_activity", id_activity);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/get-govActivity/{id}")
    public @ResponseBody Map<String, Object> getGovActivity(@PathVariable("id") Integer id) {
        Optional<GovActivity> list = govActivityService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-govActivity/{id}")
	@ResponseBody
	public void deleteGovActivity(@PathVariable("id") Integer id) {
    	govActivityService.deleteGovActivity(id);
	}
    
    @GetMapping("admin/ran_rad/gov/program/{id_program}/activity/{id_activity}/indicator")
    public String gov_indikator(Model model, @PathVariable("id_program") Integer id_program,
                                @PathVariable("id_activity") Integer id_activity, HttpSession session) {
    	Optional<GovProgram> list = govProgService.findOne(id_program);
    	Optional<GovActivity> list1 = govActivityService.findOne(id_activity);
    	//Integer id_role = list.get().getId_role();
    	Optional<Role> role = roleService.findOne((Integer) session.getAttribute("id_role"));
    	Optional<RanRad> ranrad = monPeriodService.findOne(list.get().getId_monper());
    	Optional<Provinsi> provin = prov.findOne(ranrad.get().getId_prov());
    	Optional<RanRad> monper = monPeriodService.findOne(list.get().getId_monper());
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> listRole = roleService.findOne(id_role);
    	String privilege = listRole.get().getPrivilege();
    	String id_prov = listRole.get().getId_prov();
    	String sql;
    	if(privilege.equals("SUPER")) {
    		sql = "select * from ref_unit";
    	}else {
    		sql = "select a.* from ref_unit a left join ref_role b on a.id_role = b.id_role where b.id_prov = '"+id_prov+"' or a.id_role = 1";
    	}
    	Query listUnit = em.createNativeQuery(sql);
        List<Object[]> rows = listUnit.getResultList();
        List<Unit> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(
                        new Unit((Integer)row[0], (String) row[1], (Integer)row[2],(Integer)row[3])
            );
        }
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        list.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        list1.ifPresent(foundUpdateObject1 -> model.addAttribute("govActivity", foundUpdateObject1));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("unit", result);
        provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        role.ifPresent(foundUpdateObject -> model.addAttribute("role", foundUpdateObject));
        model.addAttribute("sdgIndicator", sdgIndicatorService.findAll());
        model.addAttribute("privilege", role.get().getPrivilege());
        return "admin/ran_rad/gov/indicator";
    }
    
    @GetMapping("admin/list-govIndicator/{id_program}/{id_activity}")
    public @ResponseBody Map<String, Object> govIndicatorList(@PathVariable("id_program") Integer id_program, @PathVariable("id_activity") Integer id_activity) {
        List list = govIndicatorService.findAllIndi(id_program, id_activity);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/cek-govIndicaor/{id_activity}/{id_gov_indicator}")
    public @ResponseBody Map<String, Object> cekgovIndicator(@PathVariable("id_activity") String id_activity,@PathVariable("id_gov_indicator") String id_gov_indicator) {
        String sql = "select count(id_gov_indicator) from gov_indicator where id_activity=:id_activity and id_gov_indicator=:id_gov_indicator";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_activity", id_activity);
        query.setParameter("id_gov_indicator", id_gov_indicator);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/count-govIndicator/{id_program}/{id_activity}")
    public @ResponseBody Map<String, Object> countgovIndicator(@PathVariable("id_program") Integer id_program, @PathVariable("id_activity") Integer id_activity) {
        Integer list = govIndicatorService.countIndicator(id_program, id_activity);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-govIndicator/{sdg_indicator}/{id_monper}/{id_prov}", consumes = "application/json", produces = "application/json")
    @Transactional
    public @ResponseBody Map<String, Object> saveGovIndicator(@RequestBody GovIndicator gov,
			@PathVariable("sdg_indicator") String sdg_indicator,
			@PathVariable("id_monper") Integer id_monper,
			@PathVariable("id_prov") String id_prov) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	govIndicatorService.saveGovIndicator(gov);
    	if(gov.getInternal_code()==null || gov.getInternal_code()==0) {
    		String sql = "select IFNULL(max(a.internal_code)+1,1) as no "
    				+ "from gov_indicator a "
    				+ "left join gov_program b on a.id_program = b.id "
    				+ "where b.id_monper = :id_monper and a.id_activity = :id_activity ";
        	Query query = em.createNativeQuery(sql);
        	query.setParameter("id_monper", id_monper);
        	query.setParameter("id_activity", gov.getId_activity());
        	Integer no = ((BigInteger) query.getResultList().get(0)).intValue();
    		em.createNativeQuery("UPDATE gov_indicator set internal_code = '"+no+"' where id ='"+gov.getId()+"'").executeUpdate();
    	}
    	if(!sdg_indicator.equals("0")) {
    		govMapService.deleteGovMapByGovInd(gov.getId());
    		String[] sdg = sdg_indicator.split(",");
    		for(int i=0;i<sdg.length;i++) {
    			String[] a = sdg[i].split("---");
        		Integer id_goals = Integer.parseInt(a[0]);
        		Integer id_target = Integer.parseInt(a[1]);
        		Integer id_indicator = Integer.parseInt(a[2]);
        		GovMap map = new GovMap();
        		map.setId_goals(id_goals);
        		if(id_target!=0) {
        			map.setId_target(id_target);
        		}
        		if(id_indicator!=0) {
        			map.setId_indicator(id_indicator);
        		}
        		map.setId_gov_indicator(gov.getId());
        		map.setId_monper(id_monper);
        		map.setId_prov(id_prov);
        		govMapService.saveGovMap(map);
    		}
    	}else {
    		govMapService.deleteGovMapByGovInd(gov.getId());
    	}
    	Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",gov.getId());
        return hasil;
	}
    
    @PostMapping(path = "admin/save-govTarget/{id_gov_indicator}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveGovTarget(@RequestBody Map<String, Object> payload,@PathVariable("id_gov_indicator") Integer id_gov_indicator) {
    	JSONObject jsonObject = new JSONObject(payload);
        JSONObject catatan = jsonObject.getJSONObject("target");
        JSONArray c = catatan.getJSONArray("target");
        govTargetService.deleteByGovInd(id_gov_indicator);
        for (int i = 0 ; i < c.length(); i++) {
        	JSONObject obj = c.getJSONObject(i);
        	String year = obj.getString("year");
        	String value = obj.getString("nilai");
        	if(!value.equals("")) {
        		GovTarget gov = new GovTarget();
        		gov.setId_gov_indicator(id_gov_indicator);
        		gov.setYear(Integer.parseInt(year));
        		gov.setValue(Integer.parseInt(value));
        		govTargetService.saveGovTarget(gov);
        	}
        }
    }
    
    @GetMapping("admin/get-govTarget/{id}/{year}")
    public @ResponseBody Map<String, Object> getgovTarget(@PathVariable("id") Integer id, @PathVariable("year") Integer year) {
        List<GovTarget> list = govTargetService.findByGovYear(id, year);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-govFunding", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveGovFunding(@RequestBody GovFunding gov) {
    	govFundingService.deleteByGovInd(gov.getId_gov_indicator(), gov.getId_monper());
    	if(!gov.getBaseline().equals("") || !gov.getFunding_source().equals("")) {
    		govFundingService.saveGovFunding(gov);
    	}
	}
    
    @GetMapping("admin/get-govFunding/{id_gov_indicator}/{id_monper}")
    public @ResponseBody Map<String, Object> getgovFunding(@PathVariable("id_gov_indicator") Integer id_gov_indicator, @PathVariable("id_monper") Integer id_monper) {
        List<GovFunding> list = govFundingService.findByGovMon(id_gov_indicator, id_monper);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/get-govIndicator/{id}")
    public @ResponseBody Map<String, Object> getgovIndicator(@PathVariable("id") Integer id) {
        Optional<GovIndicator> list = govIndicatorService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-govIndicator/{id}")
	@ResponseBody
	public void deleteGovIndicator(@PathVariable("id") Integer id) {
    	govIndicatorService.deleteGovIndicator(id);
	}
    
    @RequestMapping(path = "admin/export-govProgram/{id_monper}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFileGovProgram(@PathVariable("id_monper") String id_monper, HttpServletResponse response,HttpSession session) throws FileNotFoundException {
        
    	String a = System.getProperty("user.dir"); 
        String path = System.getProperty("user.home");
        String sql = "select * from ran_rad where id_monper = '"+id_monper+"'";
        String id_prov = getStringByColumn(sql,11);
        File f = new File (path+"/export_ranrad"+id_monper+"-"+id_prov+".xls");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(f));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+"export_ranrad1-000.xls");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
           return ResponseEntity.ok()
                .headers(headers)
                .contentLength(f.length())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
        }
    
  //*********************** NON GOV PROGRAM ***********************
    @GetMapping("admin/list-nsaProg/{id_role}/{id_monper}")
    public @ResponseBody Map<String, Object> nsaProgList(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper) {
        List<NsaProgram> list = nsaProgService.findAllBy(id_role, id_monper);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-nsaProg-assign/{id_monper}/{id_prov}")
    public @ResponseBody Map<String, Object> nsaProgListAssign(@PathVariable("id_monper") Integer id_monper, @PathVariable("id_prov") String id_prov) {
        List<NsaProgram> list = nsaProgService.findAllByMonperProv(id_monper, id_prov);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-nsaProg", consumes = "application/json", produces = "application/json")
	@ResponseBody
	@Transactional
	public Map<String, Object> savNsaProg(@RequestBody NsaProgram gov) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	nsaProgService.saveNsaProgram(gov);
    	if(gov.getInternal_code()==null || gov.getInternal_code()==0) {
    		String sql = "select IFNULL(max(internal_code)+1,1) as no from nsa_program where id_monper = :id_monper";
        	Query query = em.createNativeQuery(sql);
        	query.setParameter("id_monper", gov.getId_monper());
        	System.out.print(query.getResultList().get(0).toString()+" "+gov.getId_monper());
        	Integer no = ((BigInteger) query.getResultList().get(0)).intValue();
    		em.createNativeQuery("UPDATE nsa_program set internal_code = '"+no+"' where id ='"+gov.getId()+"'").executeUpdate();
    	}
    	Map<String, Object> hasil = new HashMap<>();
        hasil.put("id_program",gov.getId());
        return hasil;
	}
    
    @GetMapping("admin/get-nsaProg/{id}")
    public @ResponseBody Map<String, Object> getNsaProg(@PathVariable("id") Integer id) {
        Optional<NsaProgram> list = nsaProgService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-nsaProg/{id}")
	@ResponseBody
	public void deleteNsaProg(@PathVariable("id") Integer id) {
    	nsaProgService.deleteNsaProgram(id);
	}

    @GetMapping("admin/ran_rad/non-gov/program/{id_program}/activity")
    public String nongov_kegiatan(Model model, @PathVariable("id_program") Integer id_program, HttpSession session) {
    	Optional<NsaProgram> list = nsaProgService.findOne(id_program);
    	Optional<RanRad> ranrad = monPeriodService.findOne(list.get().getId_monper());
    	Optional<Provinsi> provin = prov.findOne(ranrad.get().getId_prov());
    	Optional<RanRad> monper = monPeriodService.findOne(list.get().getId_monper());
    	Optional<Role> role = roleService.findOne((Integer) session.getAttribute("id_role"));
    	provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
    	monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
    	model.addAttribute("role", roleService.findRoleNonGov(ranrad.get().getId_prov()));
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        list.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("privilege", role.get().getPrivilege());
        model.addAttribute("id_role", session.getAttribute("id_role"));
        return "admin/ran_rad/non-gov/activity";
    }
    
    @GetMapping("admin/list-nsaActivity/{id_program}/{id_role}")
    public @ResponseBody Map<String, Object> nsaActivityList(@PathVariable("id_program") Integer id_program,@PathVariable("id_role") Integer id_role) {
        List<NsaActivity> list = nsaActivityService.findAll(id_program,id_role);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-nsaProgram/{id_monper}")
    public @ResponseBody Map<String, Object> nsaProgListMon(@PathVariable("id_monper") String id_monper, HttpSession session) {
    	Optional<Role> role = roleService.findOne((Integer) session.getAttribute("id_role"));
    	String id_role="";
    	if(role.get().getPrivilege().equals("USER")) {
    		id_role = " and a.id_role = '"+role.get().getId_role()+"'";
    	}
    	String sql = "select a.id, a.nm_program, a.nm_program_eng, a.internal_code,b.nm_role "
    			+ "from nsa_program a "
    			+ "left join ref_role b on a.id_role = b.id_role "
    			+ "where a.id_monper=:id_monper "+id_role+" order by CAST(a.internal_code AS UNSIGNED)";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_monper", id_monper);
        List list   = query.getResultList();
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-nsaActivityExp/{id_program}")
    public @ResponseBody Map<String, Object> nsaActivityList(@PathVariable("id_program") Integer id_program, HttpSession session) {
    	Optional<Role> role = roleService.findOne((Integer) session.getAttribute("id_role"));
    	String id_role="";
    	if(role.get().getPrivilege().equals("USER")) {
    		id_role = " and a.id_role = '"+role.get().getId_role()+"'";
    	}
    	String sql = "select a.id, a.nm_activity, a.nm_activity_eng, a.internal_code,b.nm_role "
    			+ "from nsa_activity a "
    			+ "left join ref_role b on a.id_role = b.id_role "
    			+ "where a.id_program=:id_program "+id_role+" order by CAST(a.internal_code AS UNSIGNED)";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_program", id_program);
        List list   = query.getResultList();
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-nsaActivity/{id_program}")
    public @ResponseBody Map<String, Object> nsaActivityListByProg(@PathVariable("id_program") Integer id_program) {
        List<NsaActivity> list = nsaActivityService.findAll(id_program);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/count-nsaActivity/{id_program}")
    public @ResponseBody Map<String, Object> countnsaActivity(@PathVariable("id_program") Integer id_program) {
        Integer list = nsaActivityService.countNsaActivity(id_program);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-nsaActivity/{id_monper}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	@Transactional
	public void saveNsaActivity(@RequestBody NsaActivity gov,@PathVariable("id_monper") Integer id_monper) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	nsaActivityService.saveNsaActivity(gov);
    	if(gov.getInternal_code()==null || gov.getInternal_code()==0) {
    		String sql = "select IFNULL(max(a.internal_code)+1,1) as no "
    				+ "from nsa_activity a left join nsa_program b on a.id_program = b.id "
    				+ "where b.id_monper = :id_monper and a.id_program = :id_program ";
        	Query query = em.createNativeQuery(sql);
        	query.setParameter("id_monper", id_monper);
        	query.setParameter("id_program", gov.getId_program());
        	Integer no = ((BigInteger) query.getResultList().get(0)).intValue();
    		em.createNativeQuery("UPDATE nsa_activity set internal_code = '"+no+"' where id ='"+gov.getId()+"'").executeUpdate();
    	}
	}
    
    @GetMapping("admin/get-nsaActivity/{id}")
    public @ResponseBody Map<String, Object> getNsaActivity(@PathVariable("id") Integer id) {
        Optional<NsaActivity> list = nsaActivityService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-nsaActivity/{id}")
	@ResponseBody
	public void deleteNsaActivity(@PathVariable("id") Integer id) {
    	nsaActivityService.deleteNsaActivity(id);
	}
    
    @GetMapping("admin/ran_rad/non-gov/program/{id_program}/activity/{id_activity}/indicator")
    public String nsa_indikator(Model model, @PathVariable("id_program") Integer id_program,
                                @PathVariable("id_activity") Integer id_activity, HttpSession session) {
    	Optional<NsaProgram> list = nsaProgService.findOne(id_program);
    	Optional<NsaActivity> list1 = nsaActivityService.findOne(id_activity);
    	//Integer id_role = list.get().getId_role();
    	//Optional<Role> role = roleService.findOne(id_role);
    	Optional<Role> role = roleService.findOne((Integer) session.getAttribute("id_role"));
    	Optional<Role> roleDrop = roleService.findOne(list.get().getId_role());
    	Optional<Provinsi> provin = prov.findOne(roleDrop.get().getId_prov());
    	Optional<RanRad> monper = monPeriodService.findOne(list.get().getId_monper());
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> listRole = roleService.findOne(id_role);
    	String privilege = listRole.get().getPrivilege();
    	String id_prov = listRole.get().getId_prov();
    	String sql;
    	if(privilege.equals("SUPER")) {
    		sql = "select * from ref_unit";
    	}else {
    		sql = "select a.* from ref_unit a left join ref_role b on a.id_role = b.id_role where b.id_prov = '"+id_prov+"' or a.id_role = 1";
    	}
    	Query listUnit = em.createNativeQuery(sql);
        List<Object[]> rows = listUnit.getResultList();
        List<Unit> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(
                        new Unit((Integer)row[0], (String) row[1], (Integer)row[2],(Integer)row[3])
            );
        }
    	provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        roleDrop.ifPresent(foundUpdateObject -> model.addAttribute("role", foundUpdateObject));
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        list.ifPresent(foundUpdateObject -> model.addAttribute("govProg", foundUpdateObject));
        list1.ifPresent(foundUpdateObject1 -> model.addAttribute("govActivity", foundUpdateObject1));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("unit", result);
        model.addAttribute("sdgIndicator", sdgIndicatorService.findAll());
        model.addAttribute("privilege", role.get().getPrivilege());
        return "admin/ran_rad/non-gov/indicator";
    }
    
    @GetMapping("admin/list-nsaIndicator/{id_program}/{id_activity}")
    public @ResponseBody Map<String, Object> nsaIndicatorList(@PathVariable("id_program") Integer id_program, @PathVariable("id_activity") Integer id_activity) {
        List<NsaIndicator> list = nsaIndicatorService.findAllIndi(id_program, id_activity);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/count-nsaIndicator/{id_program}/{id_activity}")
    public @ResponseBody Map<String, Object> countnsaIndicator(@PathVariable("id_program") Integer id_program, @PathVariable("id_activity") Integer id_activity) {
        Integer list = nsaIndicatorService.countIndicator(id_program, id_activity);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-nsaIndicator/{sdg_indicator}/{id_monper}/{id_prov}", consumes = "application/json", produces = "application/json")
    @Transactional
    public @ResponseBody Map<String, Object> saveNsaIndicator(@RequestBody NsaIndicator gov,
			@PathVariable("sdg_indicator") String sdg_indicator,
			@PathVariable("id_monper") Integer id_monper,
			@PathVariable("id_prov") String id_prov) {
    	gov.setCreated_by(1);
    	gov.setDate_created(new Date());
    	nsaIndicatorService.saveNsaIndicator(gov);
    	if(gov.getInternal_code()==null || gov.getInternal_code()==0) {
    		String sql = "select IFNULL(max(a.internal_code)+1,1) as no "
    				+ "from nsa_indicator a "
    				+ "left join nsa_program b on a.id_program = b.id "
    				+ "where b.id_monper = :id_monper and a.id_activity = :id_activity ";
        	Query query = em.createNativeQuery(sql);
        	query.setParameter("id_monper", id_monper);
        	query.setParameter("id_activity", gov.getId_activity());
        	Integer no = ((BigInteger) query.getResultList().get(0)).intValue();
    		em.createNativeQuery("UPDATE nsa_indicator set internal_code = '"+no+"' where id ='"+gov.getId()+"'").executeUpdate();
    	}
    	if(!sdg_indicator.equals("0")) {
    		nsaMapService.deleteNsaMapByNsaInd(gov.getId());
    		String[] sdg = sdg_indicator.split(",");
    		for(int i=0;i<sdg.length;i++) {
    			String[] a = sdg[i].split("---");
    			Integer id_goals = Integer.parseInt(a[0]);
        		Integer id_target = Integer.parseInt(a[1]);
        		Integer id_indicator = Integer.parseInt(a[2]);
        		NsaMap map = new NsaMap();
        		map.setId_goals(id_goals);
        		if(id_target!=0) {
        			map.setId_target(id_target);
        		}
        		if(id_indicator!=0) {
        			map.setId_indicator(id_indicator);
        		}
        		map.setId_nsa_indicator(gov.getId());
        		map.setId_monper(id_monper);
        		map.setId_prov(id_prov);
        		nsaMapService.saveNsaMap(map);
    		}
    	}
    	Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",gov.getId());
        return hasil;
	}
    
    @GetMapping("admin/get-nsaIndicator/{id}")
    public @ResponseBody Map<String, Object> getNsaIndicator(@PathVariable("id") Integer id) {
        Optional<NsaIndicator> list = nsaIndicatorService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-nsaIndicator/{id}")
	@ResponseBody
	public void deleteNsaIndicator(@PathVariable("id") Integer id) {
    	nsaIndicatorService.deleteNsaIndicator(id);
	}
    
    @PostMapping(path = "admin/save-nsaTarget/{id_nsa_indicator}/{id_role}", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveNsaTarget(@RequestBody Map<String, Object> payload,@PathVariable("id_nsa_indicator") Integer id_nsa_indicator,@PathVariable("id_role") Integer id_role) {
    	JSONObject jsonObject = new JSONObject(payload);
        JSONObject catatan = jsonObject.getJSONObject("target");
        JSONArray c = catatan.getJSONArray("target");
        nsaTargetService.deleteByNsaInd(id_nsa_indicator);
        for (int i = 0 ; i < c.length(); i++) {
        	JSONObject obj = c.getJSONObject(i);
        	String year = obj.getString("year");
        	String value = obj.getString("nilai");
        	if(!value.equals("")) {
        		NsaTarget nsa = new NsaTarget();
        		nsa.setId_nsa_indicator(id_nsa_indicator);
        		nsa.setId_role(id_role);
        		nsa.setYear(Integer.parseInt(year));
        		nsa.setValue(Integer.parseInt(value));
        		nsaTargetService.saveNsaTarget(nsa);
        	}
        }
    }
    
    @GetMapping("admin/get-nsaTarget/{id}/{year}")
    public @ResponseBody Map<String, Object> getNsaTarget(@PathVariable("id") Integer id, @PathVariable("year") Integer year) {
        List<NsaTarget> list = nsaTargetService.findByNsaYear(id, year);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-nsaFunding", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void saveNsaFunding(@RequestBody NsaFunding gov) {
    	nsaFundingService.deleteByNsaInd(gov.getId_nsa_indicator(), gov.getId_monper());
    	if(!gov.getBaseline().equals("") || !gov.getFunding_source().equals("")) {
    		nsaFundingService.saveNsaFunding(gov);
    	}
	}
    
    @GetMapping("admin/get-nsaFunding/{id_nsa_indicator}/{id_monper}")
    public @ResponseBody Map<String, Object> getNsaFunding(@PathVariable("id_nsa_indicator") Integer id_nsa_indicator, @PathVariable("id_monper") Integer id_monper) {
        List<NsaFunding> list = nsaFundingService.findByNsaMon(id_nsa_indicator, id_monper);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
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
	@Transactional
	public void saveMonPer(@RequestBody RanRad sdg) {
    	monPerService.saveMonPeriod(sdg);
    	Integer id_monper = sdg.getId_monper();
    	if(sdg.getStatus().equals("completed")) {
    		//COPY GOALS
        	em.createNativeQuery("DELETE FROM history_sdg_goals where id_monper = '"+id_monper+"'").executeUpdate();
        	em.createNativeQuery("INSERT INTO history_sdg_goals(id_old,id_goals,id_monper,nm_goals,nm_goals_eng) "
        			+ " select id,id_goals,'"+id_monper+"' as id_monper,nm_goals,nm_goals_eng from sdg_goals").executeUpdate();
        	//COPY TARGET
        	em.createNativeQuery("DELETE FROM history_sdg_target where id_monper = '"+id_monper+"'").executeUpdate();
        	em.createNativeQuery("INSERT INTO history_sdg_target(id_old,id_target,id_goals,id_monper,nm_target,nm_target_eng) "
        			+ " select id,id_target,id_goals,'"+id_monper+"' as id_monper,nm_target,nm_target_eng from sdg_target").executeUpdate();
        	//COPY INDICATOR
        	em.createNativeQuery("DELETE FROM history_sdg_indicator where id_monper = '"+id_monper+"'").executeUpdate();
        	em.createNativeQuery("INSERT INTO history_sdg_indicator(id_old,id_indicator,id_target,id_goals,id_monper,nm_indicator,nm_indicator_eng,unit,increment_decrement) "
        			+ " select id,id_indicator,id_target,id_goals,'"+id_monper+"' as id_monper,nm_indicator,nm_indicator_eng,unit,increment_decrement from sdg_indicator").executeUpdate();
        	//COPY DISAGGREGATION
        	em.createNativeQuery("DELETE FROM history_sdg_ranrad_disaggre where id_monper = '"+id_monper+"'").executeUpdate();
        	em.createNativeQuery("INSERT INTO history_sdg_ranrad_disaggre(id_old,id_disaggre,id_indicator,id_monper,nm_disaggre,nm_disaggre_eng) "
        			+ " select id,id_disaggre,id_indicator,'"+id_monper+"' as id_monper,nm_disaggre,nm_disaggre_eng from sdg_ranrad_disaggre").executeUpdate();
        	//COPY DISAGGREGATION DETAIL
        	em.createNativeQuery("DELETE FROM history_sdg_ranrad_disaggre_detail where id_monper = '"+id_monper+"'").executeUpdate();
        	em.createNativeQuery("INSERT INTO history_sdg_ranrad_disaggre_detail(id_old,id_disaggre,id_monper,desc_disaggre,desc_disaggre_eng) "
        			+ " select id,id_disaggre,'"+id_monper+"' as id_monper,desc_disaggre,desc_disaggre_eng from sdg_ranrad_disaggre_detail").executeUpdate();
        }
    	
	}
    
    @GetMapping("admin/get-monPer/{id}")
    public @ResponseBody Map<String, Object> getMonPer(@PathVariable("id") Integer id) {
        Optional<RanRad> list = monPerService.findOne(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/copy-period")
    @Transactional
    @ResponseBody
    public void copyPeriod() {
    	//COPY PERIOD
    	String sql = "select id_monper,end_year from ran_rad where iscopy = '1'";
    	Query list = em.createNativeQuery(sql);
    	List<Object[]> rows = list.getResultList();
    	for (Object[] row : rows) {
    		Integer id_monper = (Integer)row[0];
    		em.createNativeQuery("INSERT IGNORE INTO ran_rad(start_year,end_year,sdg_indicator,gov_prog,nsa_prog,gov_prog_bud,nsa_prog_bud,ident_problem,best_pract,status,id_prov,mapping_type,copied_from,iscopy) "
        			+ " select (end_year+1) as start_year,(end_year+6) as end_year,sdg_indicator,gov_prog,nsa_prog,gov_prog_bud,nsa_prog_bud,ident_problem,best_pract,'created' as status,id_prov,mapping_type,id_monper,'1' from ran_rad where iscopy = '1' and id_monper = '"+id_monper+"' ").executeUpdate();
        	em.createNativeQuery("UPDATE ran_rad set iscopy = '0' where iscopy = '1' and id_monper = '"+id_monper+"'").executeUpdate();
    	}
    }
    
    @DeleteMapping("admin/delete-monPer/{id}")
	@ResponseBody
	public void deleteMonPer(@PathVariable("id") Integer id) {
    	monPerService.deleteMonPeriod(id);
	}
    
  //*********************** MAPPING ***********************
    @GetMapping("admin/ran_rad/map/goals/{id_monper}")
    public String goals(Model model, HttpSession session, @PathVariable("id_monper") Integer id_monper) throws IOException {
    	Optional<RanRad> monper = monPeriodService.findOne(id_monper);
    	Optional<Provinsi> provin = prov.findOne(monper.get().getId_prov());
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_monper",id_monper);
        provin.ifPresent(foundUpdateObject -> model.addAttribute("prov", foundUpdateObject));
        monper.ifPresent(foundUpdateObject -> model.addAttribute("monPer", foundUpdateObject));
        Integer id_role = (Integer) session.getAttribute("id_role");
        Optional<Role> list = roleService.findOne(id_role);
    	String privilege    = list.get().getPrivilege();
    	model.addAttribute("privilege", privilege);
    	model.addAttribute("id_role", id_role);
        exportExcell(id_monper,id_role);
        String a = System.getProperty("user.dir"); 
        return "admin/ran_rad/map/goals";
    }
    
    @GetMapping("admin/get-mapping/{id_prov}/{id_monper}")
    public @ResponseBody Map<String, Object> getMapping(HttpSession session,@PathVariable("id_prov") String id_prov,@PathVariable("id_monper") String id_monper) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> listRole = roleService.findOne(id_role);
    	String privilege = listRole.get().getPrivilege();
    	String role = "";
    	if(privilege.equals("USER")) {
    		role = " and f.id_role = '"+id_role+"' ";
    	}
        String sql  = "Select b.id as idgol, c.id as idtar, d.id as idindi,\r\n" + 
        		"b.id_goals, c.id_target, d.id_indicator, b.nm_goals, b.nm_goals_eng,\r\n" + 
        		"c.nm_target,c.nm_target_eng,d.nm_indicator as sdg_indicator,d.nm_indicator_eng as sdg_indicator_eng,\r\n" + 
        		"g.id_program, f.id_activity, e.id_gov_indicator, g.nm_program, g.nm_program_eng,\r\n" + 
        		"f.nm_activity, f.nm_activity_eng, e.nm_indicator, e.nm_indicator_eng, h.nm_role,i.funding_source, \r\n"
        		+ "(select group_concat(concat(value,'---',year)) from gov_target where id_gov_indicator = a.id_gov_indicator) as target " + 
        		"from gov_map a \r\n" + 
        		"left join sdg_goals b on a.id_goals = b.id\r\n" + 
        		"left join sdg_target c on a.id_target = c.id\r\n" + 
        		"left join sdg_indicator d on a.id_indicator = d.id\r\n" + 
        		"left join gov_indicator e on a.id_gov_indicator = e.id\r\n" + 
        		"left join gov_activity f on e.id_activity = f.id\r\n" + 
        		"left join gov_program g on f.id_program = g.id\r\n" + 
        		"left join ref_role h on f.id_role = h.id_role\r\n" +
        		"left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper=i.id_monper\r\n" +
        		"where a.id_prov = :id_prov and a.id_monper = :id_monper "+role + 
        		"union\r\n" + 
        		"Select b.id as idgol, c.id as idtar, d.id as idindi,\r\n" + 
        		"b.id_goals, c.id_target, d.id_indicator, b.nm_goals, b.nm_goals_eng,\r\n" + 
        		"c.nm_target,c.nm_target_eng,d.nm_indicator as sdg_indicator,d.nm_indicator_eng as sdg_indicator_eng,\r\n" + 
        		"g.id_program, f.id_activity, e.id_nsa_indicator, g.nm_program, g.nm_program_eng,\r\n" + 
        		"f.nm_activity, f.nm_activity_eng, e.nm_indicator, e.nm_indicator_eng, h.nm_role,i.funding_source,\r\n" 
        		+ "(select group_concat(concat(value,'---',year)) from nsa_target where id_nsa_indicator = a.id_nsa_indicator) as target " +
        		"from nsa_map a \r\n" + 
        		"left join sdg_goals b on a.id_goals = b.id\r\n" + 
        		"left join sdg_target c on a.id_target = c.id\r\n" + 
        		"left join sdg_indicator d on a.id_indicator = d.id\r\n" + 
        		"left join nsa_indicator e on a.id_nsa_indicator = e.id\r\n" + 
        		"left join nsa_activity f on e.id_activity = f.id\r\n" + 
        		"left join nsa_program g on f.id_program = g.id\r\n" + 
        		"left join ref_role h on f.id_role = h.id_role\r\n" + 
        		"left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper=i.id_monper\r\n" +
        		"where a.id_prov = :id_prov and a.id_monper = :id_monper "+role+" order by 1,2,3";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_monper", id_monper);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    public String getStringByColumn(String sql,Integer column){
      Query list = em.createNativeQuery(sql);
      Map<String, Object> map = new HashMap<>();
      map.put("map",list.getResultList());
      JSONObject objMonper = new JSONObject(map);
      JSONArray array = objMonper.getJSONArray("map").getJSONArray(0);
      String result = array.getString(column);
      return result;  
    }
    
    public void exportExcell(Integer id_monper,Integer id_role) throws FileNotFoundException, IOException{
        String sql = "select * from ran_rad where id_monper = '"+id_monper+"'";
        String id_prov = getStringByColumn(sql,11);
        
        String getCatRole = "select * from ref_role where id_role = '"+id_role+"'";
        String cat_role = getStringByColumn(getCatRole,3);
        
        
         Workbook wb = new HSSFWorkbook();         
         CreationHelper createHelper = wb.getCreationHelper();
         
         Font fontTitle = wb.createFont();
         fontTitle.setFontHeightInPoints((short)14);
         fontTitle.setBold(true);
         
         //Buat Font Untuk Cell
         Font fontheader = wb.createFont();
         fontheader.setFontHeightInPoints((short)12);
         fontheader.setBold(true);
         
         Font fontchild = wb.createFont();
         fontchild.setFontHeightInPoints((short)11);
         
         CellStyle cellStyleTitle = wb.createCellStyle();
         cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
         cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
         cellStyleTitle.setFont(fontTitle);
         
         //Buat Style Untuk Cell
         CellStyle cellStyleHeader = wb.createCellStyle();
         cellStyleHeader.setDataFormat(createHelper.createDataFormat().getFormat("dd-mm-yyyy h:mm"));         
         cellStyleHeader.setBorderLeft(BorderStyle.THIN);
         cellStyleHeader.setBorderTop(BorderStyle.THIN);
         cellStyleHeader.setBorderRight(BorderStyle.THIN);
         cellStyleHeader.setBorderBottom(BorderStyle.THICK);
         cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
         cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
         cellStyleHeader.setFont(fontheader);
         
         
         CellStyle cellStyleChild = wb.createCellStyle();
//         cellStyleHeader.setDataFormat(createHelper.createDataFormat().getFormat("dd-mm-yyyy h:mm"));         
         cellStyleChild.setBorderLeft(BorderStyle.THIN);
         cellStyleChild.setBorderTop(BorderStyle.THIN);
         cellStyleChild.setBorderRight(BorderStyle.THIN);
         cellStyleChild.setBorderBottom(BorderStyle.THIN);
         cellStyleChild.setAlignment(HorizontalAlignment.LEFT);
         cellStyleChild.setFont(fontchild);
         
         Sheet sheet1 = wb.createSheet("new sheet");
         
         
        Row rowTitle = sheet1.createRow(0);
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("MAPING RAN RAD");
        cellTitle.setCellStyle(cellStyleTitle);
        sheet1.addMergedRegion(new CellRangeAddress(0,0,0,6)); 
         
         
         //buat row nama sheet row ke x
         Row row = sheet1.createRow(3); 
         //Set Tinggi Row
         row.setHeightInPoints((float)65.25);
         
         
         /** 
          * Dimulai Buat Cell Header
          * 
          */
         Cell cell0 = row.createCell(0);
         cell0.setCellValue("No");
         cell0.setCellStyle(cellStyleHeader);
         
         
         Cell cell1 = row.createCell(1);
         cell1.setCellValue("Provinsi");
         cell1.setCellStyle(cellStyleHeader);
         
         Cell cell2 = row.createCell(2);
         cell2.setCellValue("Monper");
         cell2.setCellStyle(cellStyleHeader);
         
         Cell cell3 = row.createCell(3);
         cell3.setCellValue("Goals");
         cell3.setCellStyle(cellStyleHeader);
         
         Cell cell4 = row.createCell(4);
         cell4.setCellValue("Target");
         cell4.setCellStyle(cellStyleHeader);
         
         Cell cell5 = row.createCell(5);
         cell5.setCellValue("Indicator");
         cell5.setCellStyle(cellStyleHeader);
         
         Cell cell6 = row.createCell(6);
         cell6.setCellValue("Nsa Indicator");
         cell6.setCellStyle(cellStyleHeader);
          /** 
          * Diakhiri Buat Header
          * 
          */
        String table = "";
        if(cat_role.equals("Government")){
            table = "gov_map";
        }else{
             table = "nsa_map";
        }
        
        String sqlExcell = "SELECT b.nm_prov,CONCAT(c.start_year,' - ',c.end_year) AS monper ,d.nm_goals,e.nm_target,f.nm_indicator,g.nm_indicator as nsa_nm_indicator  FROM "+table+" a\n" +
                            "LEFT JOIN ref_province b ON b.id_prov = a.id_prov\n" +
                            "LEFT JOIN ran_rad c ON c.id_monper = a.id_monper\n" +
                            "LEFT JOIN sdg_goals d ON d.id_goals = a.id_goals\n" +
                            "LEFT JOIN sdg_target e ON e.id_target = a.id_target\n" +
                            "LEFT JOIN sdg_indicator f ON f.id_indicator = a.id_indicator\n" +
                            "LEFT JOIN nsa_indicator g ON g.id_nsa_indicator = a.id_indicator";
        Query list = em.createNativeQuery(sqlExcell);
        Map<String, Object> mapRanRad = new HashMap<>();
        mapRanRad.put("mapRanRad",list.getResultList());
        JSONObject objRanRad = new JSONObject(mapRanRad); 
        JSONArray  arrayRanRad = objRanRad.getJSONArray("mapRanRad");
        Integer begin = 4;
        Integer no =1;
        for(int i=0;i<arrayRanRad.length();i++){
            JSONArray  finalArrayRanRad = arrayRanRad.getJSONArray(i);
            Row rowChild = sheet1.createRow(begin);
            
            Cell cellChild0 = rowChild.createCell(0);
            cellChild0.setCellValue(no++);
            cellChild0.setCellStyle(cellStyleChild);
            
            String prov = finalArrayRanRad.getString(0);
            Cell cellChild1 = rowChild.createCell(1);
            cellChild1.setCellValue(prov);
            cellChild1.setCellStyle(cellStyleChild);
            
            String monper = finalArrayRanRad.getString(1);
            Cell cellChild2 = rowChild.createCell(2);
            cellChild2.setCellValue(monper);
            cellChild2.setCellStyle(cellStyleChild);
            
            String goals = finalArrayRanRad.get(2).toString();
            Cell cellChild3 = rowChild.createCell(3);
            cellChild3.setCellValue(goals);
            cellChild3.setCellStyle(cellStyleChild);
            
            String target = finalArrayRanRad.get(3).toString();
            Cell cellChild4 = rowChild.createCell(4);
            cellChild4.setCellValue(target);
            cellChild4.setCellStyle(cellStyleChild);
            
            String indicator = finalArrayRanRad.get(4).toString();
            Cell cellChild5 = rowChild.createCell(5);
            cellChild5.setCellValue(indicator);
            cellChild5.setCellStyle(cellStyleChild);
            
            String nsaindicator = finalArrayRanRad.get(5).toString();
            Cell cellChild6 = rowChild.createCell(6);
            cellChild6.setCellValue(nsaindicator);
            cellChild6.setCellStyle(cellStyleChild);
            
            begin++;
        }
         String path = System.getProperty("user.home");
        try (OutputStream fileOut = new FileOutputStream(path+"/export_ranrad"+id_monper+"-"+id_prov+".xls")) {
            //auto size sheet k-0 column ke x
            wb.getSheetAt(0).autoSizeColumn(0);
            wb.getSheetAt(0).autoSizeColumn(1);
            wb.getSheetAt(0).autoSizeColumn(2);
            wb.getSheetAt(0).autoSizeColumn(3);
            wb.getSheetAt(0).autoSizeColumn(4);
            wb.getSheetAt(0).autoSizeColumn(5);
            wb.getSheetAt(0).autoSizeColumn(6);
            
            wb.write(fileOut);
            wb.close();
        }
        
    }
    
    @RequestMapping(path = "/export-excell/{id_monper}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFile(@PathVariable("id_monper") String id_monper, HttpServletResponse response,HttpSession session) throws FileNotFoundException {
        String a = System.getProperty("user.dir"); 
        String path = System.getProperty("user.home");
        System.out.println(path);
        
        
        String sql = "select * from ran_rad where id_monper = '"+id_monper+"'";
        String id_prov = getStringByColumn(sql,11);
//        Integer id_role = (Integer) session.getAttribute("id_role");
//        String getCatRole = "select * from ref_role where id_role = '"+id_role+"'";
//        String cat_role = getStringByColumn(getCatRole,3);
        File f = new File (path+"/export_ranrad"+id_monper+"-"+id_prov+".xls");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(f));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+"export_ranrad1-000.xls");
//         headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+file);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
           return ResponseEntity.ok()
                .headers(headers)
                .contentLength(f.length())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
        }
    
    
    @GetMapping("admin/ran_rad/map/goals/{id_monper}/{id}/target")
    public String targetMap(Model model, @PathVariable("id_monper") Integer id_monper, @PathVariable("id") int id, HttpSession session) {
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
//    public String sdgMap(Model model, @PathVariable("id_monper") Integer id_monper, @PathVariable("id") int id, @PathVariable("id_target") int id_target, HttpSession session) {
    public String sdgMap(Model model, @PathVariable("id_monper") Integer id_monper, @PathVariable("id") int id, @PathVariable("id_target") Integer id_target, HttpSession session) {
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
    
    @GetMapping("admin/list-getGovMapByGovInd/{id}")
    public @ResponseBody Map<String, Object> getGovMapByGovInd(HttpSession session, @PathVariable("id") Integer id) {
        List <GovMap> list = govMapService.findAllByGovInd(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-getBestMapByGovInd/{id}")
    public @ResponseBody Map<String, Object> getBestMapByGovInd(HttpSession session, @PathVariable("id") Integer id) {
        List <BestMap> list = bestMapService.findAllByGovInd(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-getNsaMapByGovInd/{id}")
    public @ResponseBody Map<String, Object> getNsaMapByGovInd(HttpSession session, @PathVariable("id") Integer id) {
        List <NsaMap> list = nsaMapService.findAllByNsaInd(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/cek-period/{id_prov}/{start}/{end}")
    public @ResponseBody Map<String, Object> cekPeriod(@PathVariable("id_prov") String id_prov, @PathVariable("start") Integer start, @PathVariable("end") Integer end) {
        Integer list = monPeriodService.cekPeriode(id_prov, start, end);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    //======================= RAN/RAD Export SDG ========================
    @GetMapping("admin/ranrad/dowload_sdg")
    @ResponseBody
    public void dowload_sdg(HttpServletResponse response, @PathParam("id_goals") int idgoals) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=SDG-"+idgoals+".xlsx");
        ByteArrayInputStream stream = sdg(idgoals);
        IOUtils.copy(stream, response.getOutputStream());
    }

	private ByteArrayInputStream sdg(int idgoals) {
		try(Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("SDG Goals");
	        
	        Row row = sheet.createRow(0);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
	        headerCellStyle.setWrapText(true);
	        // Creating header
	        Cell cell = row.createCell(0);
	        cell.setCellValue("No.");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(1);
	        cell.setCellValue("Goals");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(2);
	        cell.setCellValue("Target");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(3);
	        cell.setCellValue("Indicator");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(4);
	        cell.setCellValue("Unit");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(5);
	        cell.setCellValue("Baseline");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(6);
	        cell.setCellValue("Target");
	        cell.setCellStyle(headerCellStyle);
	        
	        //=================== Isi tabel =================
	        String sql = "SELECT b.nm_goals, c.nm_indicator as sdgindi  ";
	        Query query = em.createNativeQuery(sql);
	        query.setParameter("id_goals", idgoals);
	        Map<String, Object> mapDetail = new HashMap<>();
	        mapDetail.put("mapDetail",query.getResultList());
	        JSONObject objDetail = new JSONObject(mapDetail);
	        JSONArray  arrayDetail = objDetail.getJSONArray("mapDetail");
	        
	        System.out.println(arrayDetail);
	        
//	        for (int i=0; i<arrayDetail.length(); i++) {
//	        	JSONArray finalDetail = arrayDetail.getJSONArray(i);
//	        	Row dataRow = sheet.createRow(i+1);
//		    	dataRow.createCell(0).setCellValue(i+1);
//		    	dataRow.createCell(1).setCellValue(finalDetail.get(1).toString());
//		    	dataRow.createCell(2).setCellValue(finalDetail.get(0).toString());
//		    	dataRow.createCell(3).setCellValue(finalDetail.get(2).toString());
//		    	dataRow.createCell(4).setCellValue(finalDetail.get(3).toString());
//		    	dataRow.createCell(5).setCellValue(finalDetail.get(4).toString());
//		    	dataRow.createCell(6).setCellValue(finalDetail.get(5).toString());
//	        }
	    	
	        workbook.getSheetAt(0).autoSizeColumn(0);
	        workbook.getSheetAt(0).autoSizeColumn(1);
	        workbook.getSheetAt(0).autoSizeColumn(2);
	        workbook.getSheetAt(0).autoSizeColumn(3);
	        workbook.getSheetAt(0).autoSizeColumn(4);
	        workbook.getSheetAt(0).autoSizeColumn(5);
	        workbook.getSheetAt(0).autoSizeColumn(6);
	        
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        workbook.write(outputStream);
	        return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	//======================= RAN/RAD Gov Program Export SDG ========================
	@GetMapping("admin/ranrad/dowload_gov_prog/{id_prov}/{id_monper}")
    @ResponseBody
    public void dowload_gov_prog(HttpServletResponse response, @PathVariable("id_prov") String idprov, @PathVariable("id_monper") int idmonper) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=SDG-"+"govprog"+".xlsx");
        ByteArrayInputStream stream = govprog(idprov, idmonper);
        IOUtils.copy(stream, response.getOutputStream());
    }
	
	private ByteArrayInputStream govprog(String idprov, int idmonper) {
		try(Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Goverment Program");
	        
	        Row row = sheet.createRow(0);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
	        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	        headerCellStyle.setBorderLeft(BorderStyle.THIN);
	        headerCellStyle.setBorderTop(BorderStyle.THIN);
	        headerCellStyle.setBorderRight(BorderStyle.THIN);
	        headerCellStyle.setBorderBottom(BorderStyle.THIN);
	        headerCellStyle.setWrapText(true);
	        
	        String qry = "SELECT start_year FROM ran_rad WHERE id_monper = :id_monper";
	        Query q = em.createNativeQuery(qry);
	        q.setParameter("id_monper", idmonper);
	        Map<String, Object> thnDetail = new HashMap<>();
	        thnDetail.put("tahun", q.getResultList());
	        JSONObject objTahun = new JSONObject(thnDetail);
	        JSONArray  thnArray = objTahun.getJSONArray("tahun");
	        
//	        System.out.println(thnArray.get(0));
	        
	        int thnawal = (int) thnArray.get(0);
	        
	        Cell cell = row.createCell(0);
	        cell.setCellValue("Program/Kegiatan/Output Kegiatan");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(2);
	        cell.setCellValue("Satuan");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(3);
	        cell.setCellValue("Baseline ("+thnawal+")");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(4);
	        cell.setCellValue("Target Tahunan");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(8);
	        cell.setCellValue("Indikatif Alokasi Anggaran 5 Tahun (Rp Juta)");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(9);
	        cell.setCellValue("Sumber Pendanaan");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(10);
	        cell.setCellValue("Instansi Pelaksana");
	        cell.setCellStyle(headerCellStyle);
	        
	        sheet.addMergedRegion(CellRangeAddress.valueOf("A1:B2"));
	        sheet.addMergedRegion(CellRangeAddress.valueOf("C1:C2"));
	        sheet.addMergedRegion(CellRangeAddress.valueOf("D1:D2"));
	        sheet.addMergedRegion(CellRangeAddress.valueOf("E1:H1"));
	        sheet.addMergedRegion(CellRangeAddress.valueOf("I1:I2"));
	        sheet.addMergedRegion(CellRangeAddress.valueOf("J1:J2"));
	        sheet.addMergedRegion(CellRangeAddress.valueOf("K1:K2"));
	        
	        Row row2 = sheet.createRow(1);
	        Cell cell2 = row2.createCell(4);
	        cell2.setCellValue(thnawal+1);
	        cell2.setCellStyle(headerCellStyle);
	        
	        cell2 = row2.createCell(5);
	        cell2.setCellValue(thnawal+2);
	        cell2.setCellStyle(headerCellStyle);
	        
	        cell2 = row2.createCell(6);
	        cell2.setCellValue(thnawal+3);
	        cell2.setCellStyle(headerCellStyle);
	        
	        cell2 = row2.createCell(7);
	        cell2.setCellValue(thnawal+4);
	        cell2.setCellStyle(headerCellStyle);
	        
	        //=================== Isi tabel =================
	        String sql = "SELECT DISTINCT a.id_goals, b.nm_goals FROM gov_map a LEFT JOIN "
	        		+ "sdg_goals b ON b.id = a.id_goals "
	        		+ "WHERE a.id_prov = :id_prov AND a.id_monper = :id_monper ";
	        Query query = em.createNativeQuery(sql);
	        query.setParameter("id_prov", idprov);
	        query.setParameter("id_monper", idmonper);
	        Map<String, Object> result = new HashMap<>();
	        result.put("mapDetail",query.getResultList());
	        JSONObject objDetail = new JSONObject(result);
	        JSONArray  arrayDetail = objDetail.getJSONArray("mapDetail");
	        
	        System.out.println(arrayDetail);
	        
	        for (int i=0; i<arrayDetail.length(); i++) {
	        	JSONArray data = arrayDetail.getJSONArray(i);
	        	Row dataGoals = sheet.createRow(i+2);
	        	Cell cellGoals = dataGoals.createCell(0);
	        	cellGoals.setCellValue("TUJUAN   : "+data.get(1));
	        	sheet.addMergedRegion(CellRangeAddress.valueOf("A"+(i+3)+":K"+(i+3)));
	        }
	        
	        workbook.getSheetAt(0).autoSizeColumn(0);
	        workbook.getSheetAt(0).autoSizeColumn(1);
	        workbook.getSheetAt(0).autoSizeColumn(2);
	        workbook.getSheetAt(0).autoSizeColumn(3);
	        workbook.getSheetAt(0).autoSizeColumn(4);
	        workbook.getSheetAt(0).autoSizeColumn(5);
	        workbook.getSheetAt(0).autoSizeColumn(6);
	        workbook.getSheetAt(0).autoSizeColumn(7);
	        workbook.getSheetAt(0).autoSizeColumn(8);
	        workbook.getSheetAt(0).autoSizeColumn(9);
	        workbook.getSheetAt(0).autoSizeColumn(10);
	        
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        workbook.write(outputStream);
	        return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
    
}
