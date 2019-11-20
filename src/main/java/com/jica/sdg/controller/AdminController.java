package com.jica.sdg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("admin/dashboard")
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("userName", "Admin");
        model.addAttribute("title", "Dashboard SDG Bappenas");
        return "admin/dashboard";
    }

    @GetMapping("admin/nsa/profile")
    public String nsa_profile(Model model) {
        model.addAttribute("title", "NSA Profile");
        return "admin/nsa/nsa_profile";
    }

}
