package com.jica.sdg.controller;

import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Provinsi;
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
public class NsaController {

    //*********************** NSA ***********************

    @Autowired
    IProvinsiService provinsiService;

    @GetMapping("admin/nsa/profile")
    public String nsa_profile(Model model) {
        model.addAttribute("title", "NSA Profile");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        return "admin/nsa/nsa_profile";
    }

    @GetMapping("admin/nsa/inst-profile")
    public String nsa_ins_profile(Model model) {
        model.addAttribute("title", "Institution Profile");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        return "admin/nsa/ins_profile";
    }

    @GetMapping("admin/nsa/nsa-collaboration")
    public String nsa_collaboration(Model model) {
        model.addAttribute("title", "NSA Collaboration");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        return "admin/nsa/nsa_collaboration";
    }

}
