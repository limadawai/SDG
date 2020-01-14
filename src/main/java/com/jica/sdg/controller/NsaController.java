package com.jica.sdg.controller;

import com.jica.sdg.model.Insprofile;
import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Nsaprofile;
import com.jica.sdg.model.NsaCollaboration;
import com.jica.sdg.model.PhilanthropyCollaboration;
import com.jica.sdg.model.Nsadetail;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.Submenu;
import com.jica.sdg.service.IMenuService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.ISubmenuService;
import com.jica.sdg.service.InsProfileService;
import com.jica.sdg.service.NsaDetailService;
import com.jica.sdg.service.NsaProfileService;
import com.jica.sdg.service.NsaCollaborationService;
import com.jica.sdg.service.PhilanthropyService;
import static java.util.Collections.list;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.servlet.http.HttpSession;

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
    
    @Autowired
    PhilanthropyService philanthropyService;
    
    @Autowired
    IRoleService roleService;
    
    @Autowired
    private EntityManager em;

//    @GetMapping("admin/nsa/profile")
//    public String nsa_profile(Model model, HttpSession session) {
//        model.addAttribute("title", "NSA Profile");
//        model.addAttribute("listprov", provinsiService.findAllProvinsi());
//        model.addAttribute("lang", session.getAttribute("bahasa"));
////        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleNsa());
//        return "admin/nsa/nsa_profile";
//    }

    @GetMapping("admin/nsa/profile")
    public String nsa_profile(Model model, HttpSession session) {
//        model.addAttribute("title", "NSA Profile");
    	Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleNsa());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
    		model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
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
    
    @GetMapping("admin/list-get-option-role-nsa-profil/{id}")
    public @ResponseBody Map<String, Object> getOptionNsaProfilList(@PathVariable("id") String id) {
        
        String sql  = "select * from ref_role as a where a.id_prov = :id and cat_role = 'NSA' ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List list   = query.getResultList();
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
    
    @PostMapping(path = "admin/save-nsa-profil", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveNsaProfil(@RequestBody Nsaprofile nsaprofil) {
        nsaProfilrService.saveNsaProfil(nsaprofil);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("v_id_nsa",nsaprofil.getId_nsa());
        return hasil;
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
    public void deletensa(@PathVariable("id") Integer id) {
        nsaDetailService.deleteIdNsa(id);
        nsaProfilrService.deleteNsaProfil(id);
    }
    
    
    

    @GetMapping("admin/nsa/inst-profile")
    public String nsa_ins_profile(Model model, HttpSession session) {
//        model.addAttribute("title", "Institution Profile");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("listInsProfile", insProfilrService.findRoleInstitusi());
        
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("id_role", session.getAttribute("id_role"));
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
    		model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        
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
    public void deleteSdg(@PathVariable("id") Integer id) {
        insProfilrService.deleteInsProfil(id);
    }

    
    
    
    
    @GetMapping("admin/nsa/nsa-collaboration")
    public String nsa_collaboration(Model model, HttpSession session) {
        model.addAttribute("title", "NSA Collaboration");
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleNsa());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/nsa/nsa_collaboration";
    }
    
    @GetMapping("admin/list-getid-nsa-collaboration/{id}")
    public @ResponseBody Map<String, Object> listNsaCollaboration(@PathVariable("id") String id) {
        String sql  = "select b.sector, a.nm_program, b.location, b.beneficiaries, b.ex_benefit, b.type_support, c.nm_philanthropy, b.id as id_collaboration, b.id_philanthropy, a.id_program, c.type_support as type_support1, c.nm_pillar, c.loc_philanthropy from nsa_program as a \n" +
                    "left join nsa_collaboration as b on a.id_program = b.id_program\n" +
                    "left join philanthropy_collaboration as c on b.id_philanthropy = c.id_philanthropy\n" +
                    "where a.id_role = :id ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @PostMapping(path = "admin/save-nsa-collaboration", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveNsaCollaboration(@RequestBody NsaCollaboration nsaCollaboration) {
        nsaCollaborationService.saveNsaCollaboration(nsaCollaboration);
    }
    
    @PostMapping(path = "admin/save-nsa-philanthropy", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveNsaPhilanthropy(@RequestBody PhilanthropyCollaboration philanthropyCollaboration) {
        philanthropyService.savePhilanthropyCollaboration(philanthropyCollaboration);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("v_id_phy",philanthropyCollaboration.getId_philanthropy());
        return hasil;
    }
    
    @PostMapping(path = "admin/save-nsa-collaboration-phy", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveNsaCollaborationPhy(@RequestBody NsaCollaboration nsaCollaboration) {
        int id_philanthropy = nsaCollaboration.getId_philanthropy();
        int id              = nsaCollaboration.getId();
        nsaCollaborationService.updateIdPhilanthropy(id_philanthropy, id);
    }

}
