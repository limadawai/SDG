package com.jica.sdg.controller;

import com.jica.sdg.model.EntryProblemIdentify;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import com.jica.sdg.service.*;
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

    // ****************** Problem Identification & Follow Up ******************
    @GetMapping("admin/problem-identification")
    public String problem(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Problem Identification & Follow Up");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("listRole", roleService.findAll());
        model.addAttribute("listranrad", ranRadService.findAll());
        model.addAttribute("listgoals", goalsService.findAll());
        return "admin/dataentry/problem";
    }

    @GetMapping("admin/list-problem")
    public @ResponseBody
    Map<String, Object> problemList() {
        List<EntryProblemIdentify> list = identifyService.findGoals();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
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
