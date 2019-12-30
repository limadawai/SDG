package com.jica.sdg.controller;

import com.jica.sdg.model.Insprofile;
import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Submenu;
import com.jica.sdg.service.IMenuService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.ISubmenuService;
import com.jica.sdg.service.InsProfileService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@Controller
public class NsaController {

    //*********************** NSA ***********************

    @Autowired
    IProvinsiService provinsiService;
    
    @Autowired
    InsProfileService insProfilrService;

    @GetMapping("admin/nsa/profile")
    public String nsa_profile(Model model, HttpSession session) {
        model.addAttribute("title", "NSA Profile");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/nsa/nsa_profile";
    }

    @GetMapping("admin/nsa/inst-profile")
    public String nsa_ins_profile(Model model, HttpSession session) {
//        model.addAttribute("title", "Institution Profile");
        model.addAttribute("listInsProfile", insProfilrService.findAll());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/nsa/ins_profile";
    }
    
    @GetMapping("admin/list-getid-ins-profil/{id}")
    public @ResponseBody Map<String, Object> insProfilListid(@PathVariable("id") String id) {
        List<Insprofile> list = insProfilrService.findId(id);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-ins-profil")
    public @ResponseBody Map<String, Object> insProfilList() {
        List<Insprofile> list = insProfilrService.findAll();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-ins-profil", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveInsProfil(@RequestBody Insprofile insprofil) {
        insProfilrService.saveInsProfil(insprofil);
    }
    
    @DeleteMapping("admin/delete-ins-profil/{id}")
    @ResponseBody
    public void deleteSdg(@PathVariable("id") String id) {
        insProfilrService.deleteInsProfil(id);
    }

    @GetMapping("admin/nsa/nsa-collaboration")
    public String nsa_collaboration(Model model, HttpSession session) {
        model.addAttribute("title", "NSA Collaboration");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        return "admin/nsa/nsa_collaboration";
    }

}
