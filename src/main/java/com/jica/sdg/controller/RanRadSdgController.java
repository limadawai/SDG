package com.jica.sdg.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.model.SdgIndicator;
import com.jica.sdg.model.SdgTarget;
import com.jica.sdg.service.SdgGoalsService;
import com.jica.sdg.service.SdgIndicatorService;
import com.jica.sdg.service.SdgTargetService;

@Controller
public class RanRadSdgController {

	@Autowired
	SdgGoalsService sdgGoalsService;
	
	@Autowired
	SdgTargetService sdgTargetService;
	
	@Autowired
	SdgIndicatorService sdgIndicatorService;
	
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
	
	@GetMapping("admin/ran_rad/sdg/goals/{id}/target")
    public String target(Model model, @PathVariable("id") String id) {
		Optional<SdgGoals> list = sdgGoalsService.findOne(id);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("content", foundUpdateObject));
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
    
    @GetMapping("admin/ran_rad/sdg/goals/{id}/target/{id_target}/indicator")
    public String sdg(Model model, @PathVariable("id") String id, @PathVariable("id_target") String id_target) {
    	Optional<SdgGoals> list = sdgGoalsService.findOne(id);
    	Optional<SdgTarget> list1 = sdgTargetService.findOne(id_target);
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        list.ifPresent(foundUpdateObject -> model.addAttribute("goals", foundUpdateObject));
        list1.ifPresent(foundUpdate -> model.addAttribute("target", foundUpdate));
        return "admin/ran_rad/sdg/sdgs_indicator";
    }
    
    @GetMapping("admin/list-sdgIndicator/{id_goals}/{id_target}")
    public @ResponseBody Map<String, Object> sdgIndicatorList(@PathVariable("id_goals") String id_goals, @PathVariable("id_target") String id_target) {
        List<SdgIndicator> list = sdgIndicatorService.findAll(id_goals, id_target);
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
	
}
