package com.jica.sdg.controller;

import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.Submenu;
import com.jica.sdg.service.IMenuService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.ISubmenuService;
import com.jica.sdg.service.ProvinsiService;
import com.jica.sdg.model.User;
import com.jica.sdg.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class AdminController {
	
	@Autowired
	ProvinsiService prov;
	
	@Autowired
	MonPeriodService monPeriodService;
	
	@Autowired
	RoleService roleService;

	@Autowired
    UserService userService;

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
    public String dashboard(Model model, Authentication auth, HttpServletRequest request, HttpSession session) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        String uname = auth.getName();
        List<User> userData = userService.findOne(uname);
        request.getSession().setAttribute("id_user", userData.get(0).getId_user());
        request.getSession().setAttribute("id_role", userData.get(0).getId_role());
        request.getSession().setAttribute("username", userData.get(0).getUserName());
        request.getSession().setAttribute("name", userData.get(0).getName());

        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/dashboard";
    }

    @PostMapping("admin/bahasa")
    public @ResponseBody void bahasa(@RequestParam("bhs") String bhs, HttpServletRequest request, HttpSession session) {
        request.getSession().setAttribute("bahasa", bhs);
    }

    @PostMapping("admin/english")
    public @ResponseBody void english(@RequestParam("bhs") String bhs, HttpServletRequest request, HttpSession session) {
        request.getSession().setAttribute("bahasa", bhs);
    }
    
    //*********************** RAN RAD ***********************
    @GetMapping("admin/ran_rad/sdg/goals")
    public String goals(Model model, HttpSession session) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/ran_rad/sdg/goals";
    }

    @GetMapping("admin/ran_rad/gov/program")
    public String gov_program(Model model, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
    	if(id_prov.equals("000")) {
    		model.addAttribute("prov", prov.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = prov.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("prov", foundUpdateObject1));
    	}
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        model.addAttribute("monPer", monPeriodService.findAll(id_prov));
        model.addAttribute("role", roleService.findByProvince(id_prov));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/ran_rad/gov/program";
    }

    @GetMapping("admin/ran_rad/non-gov/program")
    public String nongov_program(Model model, HttpSession session) {
    	Integer id_role = (Integer) session.getAttribute("id_role");
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov = list.get().getId_prov();
    	if(id_prov.equals("000")) {
    		model.addAttribute("prov", prov.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = prov.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("prov", foundUpdateObject1));
    	}
        model.addAttribute("title", "Define RAN/RAD/Government Program");
        model.addAttribute("monPer", monPeriodService.findAll(id_prov));
        model.addAttribute("role", roleService.findByProvince(id_prov));
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/ran_rad/non-gov/program";
    }

    @GetMapping("admin/ran_rad")
    public String ran_goals(Model model, HttpSession session) {
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");
        model.addAttribute("prov",prov.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/ran_rad/monper";
    }

}
