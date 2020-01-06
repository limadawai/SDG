package com.jica.sdg.controller;

import com.jica.sdg.model.EntrySdg;
import com.jica.sdg.model.EntrySdgIndicatorJoin;
import com.jica.sdg.service.IEntrySdgService;
import com.jica.sdg.service.IProvinsiService;
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
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
//import org.springframework.data.jpa.repository.Query;

@Controller
public class DataEntryController {

    //*********************** NSA ***********************

    @Autowired
    IProvinsiService provinsiService;
    
    @Autowired
    IEntrySdgService entrySdgService;
    
    @Autowired
    private EntityManager em;

    //entry SDG
    @GetMapping("admin/sdg-indicator-monitoring")
    public String entri_sdg(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/dataentry/entry_sdg";
    }
    
    @GetMapping("admin/list-entry-sdg")
    public @ResponseBody Map<String, Object> listEntrySdg() {
        String sql  = "select a.*, b.achievement1, b.approval  from sdg_indicator as a left join entry_sdg as b on a.id_indicator = b.id_sdg_indicator ";
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-entry-sdg", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveNsaProfil(@RequestBody EntrySdg entrySdg) {
        String id = entrySdg.getId_sdg_indicator();
        entrySdgService.deleteEntrySdg(id);
        entrySdgService.saveEntrySdg(entrySdg);
    }

    @GetMapping("admin/government-program-monitoring")
    public String govprogram(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/dataentry/govprogram";
    }

    @GetMapping("admin/government-kegiatan-monitoring")
    public String govkegiatan(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/dataentry/govactivity";
    }


}
