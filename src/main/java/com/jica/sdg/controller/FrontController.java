package com.jica.sdg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";
    }
    
    @GetMapping("/")
    public String logini(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";
    }

	/*
	 * @GetMapping("/") public String beranda(Model model) {
	 * model.addAttribute("title", "Beranda"); return "beranda"; }
	 */
}
