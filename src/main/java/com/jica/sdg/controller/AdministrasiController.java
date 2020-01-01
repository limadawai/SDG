package com.jica.sdg.controller;

import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.MenuService;
import com.jica.sdg.service.SubmenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdministrasiController {

    @Autowired
    IProvinsiService provinsiService;

    @Autowired
    IRoleService roleService;

    @Autowired
    MenuService menuService;

    @Autowired
    SubmenuService submenuService;

    //*********************** Manajemen Role & User ***********************
    @GetMapping("admin/manajemen/role")
    public String rolemanajemen(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/manajemen_role";
    }

    @GetMapping("admin/manajemen/list-role")
    public @ResponseBody Map<String, Object> roles() {
        List<Role> listRole = roleService.findAll();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content", listRole);
        return hasil;
    }

    @GetMapping("admin/manajemen/user")
    public String usermanajemen(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/manajemen_user";
    }

    @GetMapping("admin/manajemen/assignment")
    public String assignment(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/manajemen_assignment";
    }

    @GetMapping("admin/manajemen/request")
    public String requestlist(Model model, HttpSession session) {
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/role_manajemen/manajemen_request_list";
    }

    @GetMapping("admin/role/provinsi")
    public @ResponseBody List<Provinsi> provinsi() {
        List<Provinsi> list = provinsiService.findAllProvinsi();
        return list;
    }

    @GetMapping("admin/manajemen/menu")
    public @ResponseBody void menusubmenu() {
        List<Menu> listMenu = menuService.findAllMenu();
        System.out.println(listMenu.size());
    }

}
