package com.jica.sdg.controller;

import com.jica.sdg.model.Insprofile;
import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Nsaprofile;
import com.jica.sdg.model.NsaCollaboration;
import com.jica.sdg.model.Nsadetail;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Submenu;
import com.jica.sdg.service.IMenuService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.ISubmenuService;
import com.jica.sdg.service.InsProfileService;
import com.jica.sdg.service.NsaDetailService;
import com.jica.sdg.service.NsaProfileService;
import com.jica.sdg.service.NsaCollaborationService;
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

@Controller
public class NsaController {

    //*********************** NSA ***********************

    @Autowired
    IProvinsiService provinsiService;
    
    @Autowired
    InsProfileService insProfilrService;
    
    @Autowired
    NsaProfileService nsaProfilrService;
    
    @Autowired
    NsaDetailService nsaDetailService;
    
    @Autowired
    NsaCollaborationService nsaCollaborationService;

    @GetMapping("admin/nsa/profile")
    public String nsa_profile(Model model) {
//        model.addAttribute("title", "NSA Profile");
        model.addAttribute("listNsaProfile", nsaProfilrService.findAll());
        return "admin/nsa/nsa_profile";
    }
    
    @GetMapping("admin/list-getid-nsa-profil/{id}")
    public @ResponseBody Map<String, Object> nsaProfilListid(@PathVariable("id") String id) {
        List<Nsaprofile> list = nsaProfilrService.findId(id);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-nsa-profil-detail/{id}")
    public @ResponseBody Map<String, Object> nsaProfilListiddetail(@PathVariable("id") String id) {
        List<Nsadetail> list = nsaDetailService.findId(id);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-nsa-profil")
    public @ResponseBody Map<String, Object> nsaProfilList() {
        List<Nsaprofile> list = nsaProfilrService.findAll();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-nsa-profil-detail")
    public @ResponseBody Map<String, Object> nsaProfilListDetail() {
        List<Nsadetail> list = nsaDetailService.findAll();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content1",list);
        return hasil;
    }

    @GetMapping("admin/nsa/inst-profile")
    public String nsa_ins_profile(Model model) {
//        model.addAttribute("title", "Institution Profile");
        model.addAttribute("listInsProfile", insProfilrService.findAll());
        return "admin/nsa/ins_profile";
    }
    
    @PostMapping(path = "admin/save-nsa-profil", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveNsaProfil(@RequestBody Nsaprofile nsaprofil) {
        nsaProfilrService.saveNsaProfil(nsaprofil);
    }
    
    @PostMapping(path = "admin/save-nsa-detail", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveNsaDetail(@RequestBody Nsadetail nsadetil) {
        nsaDetailService.saveNsaDetail(nsadetil);
    }
    
    @GetMapping("admin/get-id-nsa-detail/{id_nsa}")
    public @ResponseBody Map<String, Object> getNsaDetail(@PathVariable("id_nsa") String id_nsa) {
        List<Nsadetail> list = nsaDetailService.findId(id_nsa);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @DeleteMapping("admin/delete-nsa-profil/{id}")
    @ResponseBody
    public void deletensa(@PathVariable("id") String id) {
        nsaDetailService.deleteIdNsa(id);
        nsaProfilrService.deleteNsaProfil(id);
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
    public String nsa_collaboration(Model model) {
        model.addAttribute("title", "NSA Collaboration");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        return "admin/nsa/nsa_collaboration";
    }

}
