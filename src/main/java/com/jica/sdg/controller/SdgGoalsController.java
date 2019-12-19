package com.jica.sdg.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.service.SdgGoalsService;

@Controller
public class SdgGoalsController {

	@Autowired
	SdgGoalsService sdgGoalsService;
	
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
	
}
