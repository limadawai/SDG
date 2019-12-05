package com.jica.sdg.controller;

import com.google.gson.Gson;
import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Submenu;
import com.jica.sdg.service.IMenuService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.ISubmenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AdminController {

    //*********************** Menu Dari DB ***********************
    @Autowired
    IMenuService menuService;
    @GetMapping("admin/menu")
    public @ResponseBody List<Menu> menuList() {
        List<Menu> list = menuService.findAllMenu();
        return list;
    }

    @Autowired
    ISubmenuService submenuService;
    @GetMapping("admin/submenu")
    public @ResponseBody List<Submenu> submenuList(@RequestParam int id) {
        List<Submenu> list = submenuService.findSubmenu(id);
        return list;
    }

    //*********************** Dashboard ***********************

    @GetMapping("admin/dashboard")
    public String dashboard(Model model, Authentication auth) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        String uname = auth.getName();
        model.addAttribute("userName", uname);
        model.addAttribute("title", "Dashboard SDG Bappenas");
        return "admin/dashboard";
    }

    //*********************** NSA ***********************

    @Autowired
    IProvinsiService provinsiService;

    @GetMapping("admin/nsa/profile")
    public String nsa_profile(Model model) {
        model.addAttribute("title", "NSA Profile");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        return "admin/nsa/nsa_profile";
    }

    //*********************** Role Management ***********************

    @GetMapping("admin/role/role_manajemen")
    public String role_manajemen(Model model) {
        model.addAttribute("title", "Role Manajemen");
        return "admin/role_manajemen/manajemen_role";
    }
    
    //*********************** RAN RAD ***********************
    @GetMapping("admin/ran_rad/sdg/goals")
    public String goals(Model model) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        return "admin/ran_rad/sdg/goals";
    }
    
    @GetMapping("admin/ran_rad/sdg/goals/target")
    public String target(Model model) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        return "admin/ran_rad/sdg/target";
    }
    
    @GetMapping("admin/ran_rad/sdg/goals/target/sdgs_indicator")
    public String sdg(Model model) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        return "admin/ran_rad/sdg/sdgs_indicator";
    }
    
    @GetMapping("admin/ran_rad/gov/program")
    public String gov_program(Model model) {
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        return "admin/ran_rad/gov/program";
    }
    
    @GetMapping("admin/ran_rad/gov/program/kegiatan")
    public String gov_kegiatan(Model model) {
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        return "admin/ran_rad/gov/kegiatan";
    }
    
    @GetMapping("admin/ran_rad/gov/program/kegiatan/indikator")
    public String gov_indikator(Model model) {
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        return "admin/ran_rad/gov/indikator";
    }
    
    @GetMapping("admin/ran_rad/non_gov/program")
    public String nongov_program(Model model) {
        model.addAttribute("title", "Define RAN/RAD/Non Government Program");
        return "admin/ran_rad/non_gov/program";
    }
    
    @GetMapping("admin/ran_rad/non_gov/program/kegiatan")
    public String nongov_kegiatan(Model model) {
        model.addAttribute("title", "Define RAN/RAD/Non Government Program");
        return "admin/ran_rad/non_gov/kegiatan";
    }
    
    @GetMapping("admin/ran_rad/non_gov/program/kegiatan/indikator")
    public String nongov_indikator(Model model) {
        model.addAttribute("title", "Define RAN/RAD/Non Government Program");
        return "admin/ran_rad/non_gov/indikator";
    }
    
    @GetMapping("admin/ran_rad/goals")
    public String ran_goals(Model model) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        return "admin/ran_rad/goals";
    }
    
    @GetMapping("admin/ran_rad/goals/target")
    public String ran_target(Model model) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        return "admin/ran_rad/target";
    }
    
    @GetMapping("admin/ran_rad/goals/target/sdgs_indicator")
    public String ran_sdg(Model model) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        return "admin/ran_rad/sdgs_indicator";
    }

}
