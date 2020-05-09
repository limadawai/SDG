package com.jica.sdg.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jica.sdg.model.Insprofile;
import com.jica.sdg.model.NsaCollaboration;
import com.jica.sdg.model.Nsadetail;
import com.jica.sdg.model.Nsaprofile;
import com.jica.sdg.model.Nsaprofile2;
import com.jica.sdg.model.PhilanthropyCollaboration;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.model.SdgGoals;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.InsProfileService;
import com.jica.sdg.service.NsaCollaborationService;
import com.jica.sdg.service.NsaDetailService;
import com.jica.sdg.service.NsaProfileService;
import com.jica.sdg.service.PhilanthropyService;

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
        
        String id_role = id.equals("all")?"":" and a.id_role = '"+id+"' ";
        
        String sql = "select a.*,b.id_prov from nsa_profile a "
                     + "left join ref_role b on b.id_role = a.id_role where 1=1 "+id_role+" ";
        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Nsaprofile2> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            Short num = new Short((short)1);
            result.add(
                        new Nsaprofile2(
                                              (Integer)row[0]
                                            , (Integer)row[1]
                                            , (String) row[2]
                                            , (String) row[3]
                                            , (String) row[4]
                                            , (String) row[5]
                                            , Integer.parseInt(row[6].toString())
                                            , (String) row[7]
                                            , (String) row[8]
                                        )
                        );
        }
        hasil.put("content",result);
        return hasil;
//return null;
        
//        List<Nsaprofile> list = nsaProfilrService.findId(id);
//        Map<String, Object> hasil = new HashMap<>();
//        hasil.put("content",list);
//        return hasil;
    }
    
    @GetMapping("admin/list-nsa-profil-detail/{id}")
    public @ResponseBody Map<String, Object> nsaProfilListiddetailjadi(@PathVariable("id") String id) {
        List<Nsadetail> list = nsaDetailService.findId(id);
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-nsa-profil-detail-jadi/{id}/{id_prov}")
    public @ResponseBody Map<String, Object> nsaProfilListiddetail(@PathVariable("id") String id,@PathVariable("id_prov") String id_prov) {
    	String role = "";
    	String prov = "";
    	if(!id.equals("00")) {role=" and a.id_role = '"+id+"' ";}
    	if(!id_prov.equals("all")) {prov=" and b.id_prov = '"+id_prov+"' ";}
    	String sql = "select a.id_nsa,a.nm_nsa,a.achieve_nsa,a.loc_nsa,a.beneficiaries,a.year_impl,a.major_part, "
    			+ "c.nsa_type,c.web_url,c.head_office,c.name_pic,c.pos_pic,c.email_pic,c.hp_pic,a.id_role, e.nm_prov "
    			+ "from nsa_profile a "
    			+ "left join nsa_detail c on a.id_nsa=c.id_nsa "
    			+ "left join ref_role d on a.id_role = d.id_role "
    			+ "left join ref_province e on d.id_prov = e.id_prov "
                + "left join ref_role b on b.id_role = a.id_role where 1=1 "+prov+" "+role;
    	Query query = em.createNativeQuery(sql);
    	List list = query.getResultList();
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
    
    @GetMapping("admin/listnsa/{id_prov}")
    public @ResponseBody Map<String, Object> listnsa(@PathVariable("id_prov") String idprov) {
    	String sql  = "select a.id_role as idrole, b.*, c.nm_org, c.nsa_type, c.web_url, c.head_office, "
    			+ "c.name_pic, c.pos_pic, c.email_pic, c.hp_pic from ref_role a left join "
    			+ "nsa_profile b on b.id_role = a.id_role left join "
    			+ "nsa_detail c on c.id_role = a.id_role "
    			+ "where a.cat_role = 'NSA' and a.id_prov = :id_prov  and b.nm_nsa is not null  and c.nm_org is not null "
    			+ "order by a.id_role asc";
	    Query query = em.createNativeQuery(sql);
	    query.setParameter("id_prov", idprov);
	    List list   = query.getResultList();
	    Map<String, Object> hasil = new HashMap<>();
	    hasil.put("content",list);
	    return hasil;
	}
    
    @GetMapping("admin/listnsabyid/{id_role}")
    public @ResponseBody Map<String, Object> listnsabyid(@PathVariable("id_role") int idrole) {
    	String sql  = "select a.*, b.nm_org, b.nsa_type, b.web_url, b.head_office, b.name_pic, b.pos_pic, b.email_pic, b.hp_pic "
    			+ "from nsa_profile a left join nsa_detail b on b.id_role = a.id_role "
    			+ "where a.id_role = :id_role and a.nm_nsa is not null and b.nm_org is not null";
	    Query query = em.createNativeQuery(sql);
	    query.setParameter("id_role", idrole);
	    List list   = query.getResultList();
	    Map<String, Object> hasil = new HashMap<>();
	    hasil.put("content",list);
	    return hasil;
	}
    
    @GetMapping("admin/list-get-option-role-nsa-profil/{id}")
    public @ResponseBody Map<String, Object> getOptionNsaProfilList(@PathVariable("id") String id) {
        String id_prov = id.equals("all")?"":" and a.id_prov = '"+id+"' ";
        
        String sql  = "select * from ref_role as a where cat_role = 'NSA' "+id_prov;
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getallnsa")
    public @ResponseBody List<Object> getallnsa() {
    	String sql  = "select a.id_role as idrl, b.* from ref_role a left join "
    			+ "nsa_profile b on b.id_role = a.id_role where a.cat_role = 'NSA' order by a.id_role asc";
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallinst/{id_prov}")
    public @ResponseBody List<Object> getallinst(@PathVariable("id_prov") String idprov) {
    	String sql  = "select a.id_role as idrl, b.* from ref_role a right join "
    			+ "nsa_inst b on b.id_role = a.id_role where a.cat_role = 'Institution' and id_prov = :id_prov order by a.id_role asc ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        List list   = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/list-get-option-role-ins-profil/{id}")
    public @ResponseBody Map<String, Object> getOptionInsProfilList(@PathVariable("id") String id) {
    	 String id_prov = id.equals("all")?"":" and a.id_prov = '"+id+"' ";
        String sql  = "select * from ref_role as a where cat_role = 'Institution' "+id_prov;
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-role-ins-profil_1/{id}")
    public @ResponseBody Map<String, Object> getOptionInsProfilList_1(@PathVariable("id") String id) {
        String sql  = "select a.id_inst, a.nm_inst from nsa_inst as a\n" +
                    "left join ref_role as b on a.id_role = b.id_role " ;
//                    "left join ref_role as b on a.id_role = b.id_role\n" +
//                    "where b.id_prov = :id ";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-role-ins-profil_role/{id}")
    public @ResponseBody Map<String, Object> getOptionInsProfilList_role(@PathVariable("id") String id) {
        
        String sql  = "select a.id_inst, a.nm_inst from nsa_inst as a\n" +
                    "where a.id_role = :id ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-role-all-profil/{id}")
    public @ResponseBody Map<String, Object> getOptionAllProfilList(@PathVariable("id") String id) {
        
        String sql  = "select * from ref_role as a where a.id_prov = :id and privilege!='SUPER'";
//        String sql  = "select * from ref_role as a where a.id_prov = :id and id_role!=1";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-role-sub/{id}")
    public @ResponseBody Map<String, Object> getOptionProfilList(@PathVariable("id") String id) {
        
        String sql  = "select * from ref_role as a where a.id_prov = :id and id_role!=1 and privilege != 'ADMIN'";
//        String sql  = "select * from ref_role as a where a.id_prov = :id and id_role!=1";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-role-all-profil-notojk/{id}")
    public @ResponseBody Map<String, Object> getOptionAllProfilListNotOjk(@PathVariable("id") String id) {
        
        String sql  = "select * from ref_role as a where a.id_prov = :id and privilege!='SUPER' and cat_role != 'gri_ojk' and cat_role != 'Institution' ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-get-option-role-gov-profil/{id}")
    public @ResponseBody Map<String, Object> getOptionGovProfilList(@PathVariable("id") String id) {
        
        String sql  = "select * from ref_role as a where a.id_prov = :id and cat_role = 'Government' and a.id_role!=1";
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
    
    @GetMapping("admin/get-id-nsa-detail1/{id_nsa}")
    public @ResponseBody Map<String, Object> getNsaDetail1(@PathVariable("id_nsa") String id_nsa) {
        List<Nsadetail> list = nsaDetailService.findIdNsa(id_nsa);
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
        String sql  = "select a.*, c.nm_prov from nsa_inst a "
                    + "left join ref_role b on a.id_role = b.id_role "
                    + "left join ref_province c on b.id_prov = c.id_prov "
                    + "where a.id_role = :id";
	    Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
	    List list   = query.getResultList();
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
    
    @GetMapping("admin/listins/{id_prov}")
	    public @ResponseBody Map<String, Object> listins(@PathVariable("id_prov") String idprov) {
    	String id_prov = idprov.equals("all")?"":" and a.id_prov = '"+idprov+"' ";
	    	String sql  = "select b.*, c.nm_prov from ref_role a left join "
	    			+ "ref_province c on a.id_prov = c.id_prov "
	    			+ "left join nsa_inst b on b.id_role = a.id_role where a.cat_role = 'Institution' "+id_prov
	    			+ "order by a.id_role asc";
	    Query query = em.createNativeQuery(sql);
	    List list   = query.getResultList();
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
    
    @DeleteMapping("admin/delete-philan-collaboration/{id}")
    @ResponseBody
    public void deletePhilan(@PathVariable("id") Integer id) {
        philanthropyService.deletePhilantropi(id);
    }

    @GetMapping("admin/nsa/nsa-collaboration")
    public String nsa_collaboration(Model model, HttpSession session) {
        model.addAttribute("title", "NSA Collaboration");
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleNsa());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	String cat_role     = list.get().getCat_role();
    	if(!cat_role.equals("NSA") || privilege.equals("SUPER") || privilege.equals("ADMIN")) {
    		model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
    		Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
    		list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        model.addAttribute("cat_role", cat_role);
        
        
        return "admin/nsa/nsa_collaboration";
    }
    
    @GetMapping("admin/list-getid-nsa-collaboration/{id}")
    public @ResponseBody Map<String, Object> listNsaCollaboration(@PathVariable("id") String id) {
        String sql  = "select b.sector, a.nm_program, b.location, b.beneficiaries, b.ex_benefit, b.type_support, c.nm_philanthropy, b.id as id_collaboration, b.id_philanthropy, a.id_program, c.type_support as type_support1, c.nm_pillar, c.loc_philanthropy, d.id_prov, a.nm_program_eng from nsa_program as a \n" +
                    "left join nsa_collaboration as b on a.id_program = b.id_program\n" +
                    "left join philanthropy_collaboration as c on b.id_philanthropy = c.id_philanthropy\n" +
                    "left join ref_role as d on a.id_role = d.id_role\n " +
//                    "left join nsa_inst as e on c.id_inst = e.id_inst\n " +
                    "where a.id_role = :id ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list-getid-nsa-collaboration-all/{id}/{id_prov}")
    public @ResponseBody Map<String, Object> listNsaCollaboration(@PathVariable("id") String id,@PathVariable("id_prov") String id_prov) {
    	String role = "";
    	String prov = "";
    	if(!id.equals("all")) {role=" and a.id_role = '"+id+"' ";}
    	if(!id_prov.equals("all")) {prov=" and d.id_prov = '"+id_prov+"' ";}
    	String sql  = "select b.sector, a.nm_program, b.location, b.beneficiaries, b.ex_benefit, b.type_support, c.nm_philanthropy, b.id as id_collaboration, b.id_philanthropy, a.id, c.type_support as type_support1, c.nm_pillar, c.loc_philanthropy, d.id_prov, a.nm_program_eng, e.nm_prov from nsa_program as a \n" +
                    "left join nsa_collaboration as b on a.id = b.id_program\n" +
                    "left join philanthropy_collaboration as c on b.id_philanthropy = c.id_philanthropy\n" +
                    "left join ref_role as d on a.id_role = d.id_role\n " +
                    "left join ref_province as e on d.id_prov = e.id_prov\n " +
                    "where 1=1 "+role+" "+prov;
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/jumlah_role_philan/{id}/{id_collaboration}")
    public @ResponseBody Map<String, Object> jumlah_role_philan(@PathVariable("id") String id, @PathVariable("id_collaboration") String id_collaboration) {
        String sql  = "select count(*) as tot from philanthropy_collaboration a\n" +
                    "left join nsa_inst b on a.id_inst = b.id_inst\n" +
                    "where b.id_role = :id and nm_philanthropy = :id_collaboration";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        query.setParameter("id_collaboration", id_collaboration);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/list_add_support_philan/{id}")
    public @ResponseBody Map<String, Object> list_add_support_philan(@PathVariable("id") String id_collaboration) {
        String sql  = "select a.id_philanthropy, a.type_support, a.nm_philanthropy, a.nm_pillar, a.loc_philanthropy,\n" +
                    "b.id_inst, b.nm_inst, b.id_role, c.sector, c.location\n" +
                    "from philanthropy_collaboration a\n" +
                    "left join nsa_collaboration c on a.nm_philanthropy = c.id\n" +
                    "left join nsa_inst b on a.id_inst = b.id_inst\n" +
                    "where a.nm_philanthropy = :id_collaboration ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_collaboration", id_collaboration);
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
    
    //========================== Export to Excell ========================
    @GetMapping("admin/nsa/download_profil/{id_prov}/{id_role}")
    @ResponseBody
    public void download_profil(HttpServletResponse response, @PathVariable("id_prov") String idprov, 
    		@PathVariable("id_role") int idrole) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=NSA_Profile-"+idrole+".xlsx");
        ByteArrayInputStream stream = exprofil(idprov, idrole);
        IOUtils.copy(stream, response.getOutputStream());
    }
    
    private ByteArrayInputStream exprofil(String idprov, int idrole) {
        
//        System.out.print(finalDetail.getString(0));
        
		try(Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Profile");
			
			Row row = sheet.createRow(3);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
	        headerCellStyle.setWrapText(true);
	        // Creating header
	        Cell cell = row.createCell(0);
	        cell.setCellValue("No.");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(1);
	        cell.setCellValue("Nama Organisasi");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(2);
	        cell.setCellValue("Institusi");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(3);
	        cell.setCellValue("Atribut");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(4);
	        cell.setCellValue("Situs");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(5);
	        cell.setCellValue("Lokasi");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(6);
	        cell.setCellValue("Detail Perwakilan");
	        cell.setCellStyle(headerCellStyle);
	        
	        Row row2 = sheet.createRow(0);
	        CellStyle headerCellStyle2 = workbook.createCellStyle();
	        headerCellStyle2.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	        headerCellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerCellStyle2.setAlignment(HorizontalAlignment.CENTER);
	        headerCellStyle2.setWrapText(true);
	        // Creating header
	        Cell cell2 = row2.createCell(0);
	        cell2.setCellValue("No.");
	        cell2.setCellStyle(headerCellStyle);
	        
	        cell2 = row2.createCell(1);
	        cell2.setCellValue("Pencapaian");
	        cell2.setCellStyle(headerCellStyle);
	
	        cell2 = row2.createCell(2);
	        cell2.setCellValue("Lokasi");
	        cell2.setCellStyle(headerCellStyle);
	
	        cell2 = row2.createCell(3);
	        cell2.setCellValue("Penerima Manfaat");
	        cell2.setCellStyle(headerCellStyle);
	        
	        cell2 = row2.createCell(4);
	        cell2.setCellValue("Tahun Implementasi");
	        cell2.setCellStyle(headerCellStyle);
	        
	        cell2 = row2.createCell(5);
	        cell2.setCellValue("Partner");
	        cell2.setCellStyle(headerCellStyle);
	        
	        //=================== Isi tabel atas =================
	        
	        String sql = "SELECT a.nm_nsa, a.loc_nsa, b.web_url, b.name_pic FROM nsa_profile a LEFT JOIN "
	          		+ "nsa_detail b ON b.id_nsa = a.id_nsa "
	          		+ "WHERE a.id_role = :id_role";
	        Query query = em.createNativeQuery(sql);
	        query.setParameter("id_role", idrole);
	        Map<String, Object> mapDetail = new HashMap<>();
	        mapDetail.put("mapDetail",query.getResultList());
	        JSONObject objDetail = new JSONObject(mapDetail);
	        JSONArray  arrayDetail = objDetail.getJSONArray("mapDetail");
	        JSONArray  finalDetail = arrayDetail.getJSONArray(0);
	        
	    	Row dataRow = sheet.createRow(4);
	    	dataRow.createCell(0).setCellValue("1.");
	    	dataRow.createCell(1).setCellValue(finalDetail.getString(0));
	    	dataRow.createCell(2).setCellValue("Civil Society Organization/Organisasi Kepemudaan/Komunitas");
	    	dataRow.createCell(3).setCellValue("");
	    	dataRow.createCell(4).setCellValue(finalDetail.getString(2));
	    	dataRow.createCell(5).setCellValue(finalDetail.getString(1));
	    	dataRow.createCell(6).setCellValue(finalDetail.getString(3));
	        
	    	//================ Data table ke 2 =================
	        String sql2 = "SELECT a.achieve_nsa, a.loc_nsa, a.beneficiaries, a.year_impl, a.major_part "
	        		+ "FROM nsa_profile a WHERE id_role = :id_role";
	        Query query2 = em.createNativeQuery(sql2);
	        query2.setParameter("id_role", idrole);
	        Map<String, Object> mapDetail2 = new HashMap<>();
	        mapDetail2.put("mapDetail2",query2.getResultList());
	        JSONObject objDetail2 = new JSONObject(mapDetail2);
	        JSONArray  arrayDetail2 = objDetail2.getJSONArray("mapDetail2");
	        JSONArray  finalDetail2 = arrayDetail2.getJSONArray(0);
	        
	        Row dataRow2 = sheet.createRow(1);
	    	dataRow2.createCell(0).setCellValue("1.");
	    	dataRow2.createCell(1).setCellValue(finalDetail2.getString(0));
	    	dataRow2.createCell(2).setCellValue(finalDetail2.getString(1));
	    	dataRow2.createCell(3).setCellValue(finalDetail2.getString(2));
	    	dataRow2.createCell(4).setCellValue(finalDetail2.getInt(3));
	    	dataRow2.createCell(5).setCellValue(finalDetail2.getString(4));
	
	        sheet.autoSizeColumn(0);
	        sheet.autoSizeColumn(1);
	        sheet.autoSizeColumn(2);
	        sheet.autoSizeColumn(3);
	        sheet.autoSizeColumn(4);
	        sheet.autoSizeColumn(5);
	        sheet.autoSizeColumn(6);
	        
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        workbook.write(outputStream);
	        return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
    }
    
    @GetMapping("admin/nsa/dowload_inst_all/{id_prov}")
    @ResponseBody
    public void dowload_inst_profil(HttpServletResponse response, @PathVariable("id_prov") String idprov) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=INS_Profile-All.xlsx");
        ByteArrayInputStream stream = insprofilall(idprov);
        IOUtils.copy(stream, response.getOutputStream());
    }
    
    private ByteArrayInputStream insprofilall(String idprov) {
    	try(Workbook workbook = new XSSFWorkbook()) {
    		Sheet sheet = workbook.createSheet("Profile All");
    		
    		Row row = sheet.createRow(0);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
	        headerCellStyle.setWrapText(true);
	        // Creating header
	        Cell cell = row.createCell(0);
	        cell.setCellValue("No.");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(1);
	        cell.setCellValue("Nama Institusi");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(2);
	        cell.setCellValue("Pencapaian");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(3);
	        cell.setCellValue("Lokasi");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(4);
	        cell.setCellValue("Penerima Manfaat");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(5);
	        cell.setCellValue("Tahun Implementasi");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(6);
	        cell.setCellValue("Partner");
	        cell.setCellStyle(headerCellStyle);
	        
	        String sql  = "select a.id_role as idrl, b.* from ref_role a left join "
	    			+ "nsa_inst b on b.id_role = a.id_role where a.cat_role = 'Institution' and a.id_prov = :id_prov order by a.id_role asc ";
	        Query query = em.createNativeQuery(sql);
	        query.setParameter("id_prov", idprov);
	        List list   = query.getResultList();
	        Map<String, Object> mapDetail = new HashMap<>();
	        mapDetail.put("mapDetail", list);
	        JSONObject objDetail = new JSONObject(mapDetail);
	        JSONArray  arrayDetail = objDetail.getJSONArray("mapDetail");
	        System.out.println(arrayDetail);
	        for (int i=0; i<arrayDetail.length(); i++) {
	        	JSONArray finalDetail = arrayDetail.getJSONArray(i);
	        	Row dataRow = sheet.createRow(i+1);
		    	dataRow.createCell(0).setCellValue((i+1)+".");
		    	dataRow.createCell(1).setCellValue(finalDetail.getString(3));
		    	dataRow.createCell(2).setCellValue(finalDetail.getString(4));
		    	dataRow.createCell(3).setCellValue(finalDetail.getString(5));
		    	dataRow.createCell(4).setCellValue(finalDetail.getString(6));
		    	dataRow.createCell(5).setCellValue(finalDetail.getInt(7));
		    	dataRow.createCell(6).setCellValue(finalDetail.getString(8));
	        }
	        
	        sheet.autoSizeColumn(0);
	        sheet.autoSizeColumn(1);
	        sheet.autoSizeColumn(2);
	        sheet.autoSizeColumn(3);
	        sheet.autoSizeColumn(4);
	        sheet.autoSizeColumn(5);
	        sheet.autoSizeColumn(6);
    		
    		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        workbook.write(outputStream);
	        return new ByteArrayInputStream(outputStream.toByteArray());
    	} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
    }
    
    @GetMapping("admin/nsa/dowload_inst_profil/{id_role}")
    @ResponseBody
    public void dowload_inst_profil(HttpServletResponse response, @PathVariable("id_role") int idrole) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=INS_Profile-"+idrole+".xlsx");
        ByteArrayInputStream stream = insprofil(idrole);
        IOUtils.copy(stream, response.getOutputStream());
    }

	private ByteArrayInputStream insprofil(int idrole) {
		try(Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Profile");
			
			Row rownama = sheet.createRow(0);
			sheet.addMergedRegion(CellRangeAddress.valueOf("A1:F1"));
	        
	        Row row = sheet.createRow(1);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
	        headerCellStyle.setWrapText(true);
	        // Creating header
	        Cell cell = row.createCell(0);
	        cell.setCellValue("No.");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(1);
	        cell.setCellValue("Pencapaian");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(2);
	        cell.setCellValue("Lokasi");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(3);
	        cell.setCellValue("Penerima Manfaat");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(4);
	        cell.setCellValue("Tahun Implementasi");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(5);
	        cell.setCellValue("Partner");
	        cell.setCellStyle(headerCellStyle);
	        
	        //=================== Isi tabel atas =================
	        
	        String sql = "SELECT * FROM nsa_inst WHERE id_role = :id_role";
	        Query query = em.createNativeQuery(sql);
	        query.setParameter("id_role", idrole);
	        Map<String, Object> mapDetail = new HashMap<>();
	        mapDetail.put("mapDetail",query.getResultList());
	        JSONObject objDetail = new JSONObject(mapDetail);
	        JSONArray  arrayDetail = objDetail.getJSONArray("mapDetail");
	        JSONArray  finalDetail = arrayDetail.getJSONArray(0);
	        
	        rownama.createCell(0).setCellValue(finalDetail.getString(2));
	        
	    	Row dataRow = sheet.createRow(2);
	    	dataRow.createCell(0).setCellValue("1.");
	    	dataRow.createCell(1).setCellValue(finalDetail.getString(3));
	    	dataRow.createCell(2).setCellValue(finalDetail.getString(4));
	    	dataRow.createCell(3).setCellValue(finalDetail.getString(5));
	    	dataRow.createCell(4).setCellValue(finalDetail.getInt(6));
	    	dataRow.createCell(5).setCellValue(finalDetail.getString(7));
	    	
	        sheet.autoSizeColumn(0);
	        sheet.autoSizeColumn(1);
	        sheet.autoSizeColumn(2);
	        sheet.autoSizeColumn(3);
	        sheet.autoSizeColumn(4);
	        sheet.autoSizeColumn(5);
	        
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        workbook.write(outputStream);
	        return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("admin/nsa/dowload_inst_colab/{id_role}")
    @ResponseBody
    public void dowload_inst_colab(HttpServletResponse response, @PathVariable("id_role") int idrole) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=INS_Colab-"+idrole+".xlsx");
        ByteArrayInputStream stream = inscolab(idrole);
        IOUtils.copy(stream, response.getOutputStream());
    }

	private ByteArrayInputStream inscolab(int idrole) {
		try(Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Profile");
	        
	        Row row = sheet.createRow(0);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
	        headerCellStyle.setWrapText(true);
	        // Creating header
	        Cell cell = row.createCell(0);
	        cell.setCellValue("No.");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(1);
	        cell.setCellValue("Area / Sektor");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(2);
	        cell.setCellValue("Nama Program");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(3);
	        cell.setCellValue("Lokasi Intervensi Program");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(4);
	        cell.setCellValue("Penerima Manfaat");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(5);
	        cell.setCellValue("Keuntungan Yang diharapkan");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(6);
	        cell.setCellValue("Tantangan/Jenis Dukungan Yang Diharapkan");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(7);
	        cell.setCellValue("Nama Institusi");
	        cell.setCellStyle(headerCellStyle);
	        
	        //=================== Isi tabel atas =================
	        
//	        String sql  = "select b.sector, a.nm_program, b.location, b.beneficiaries, b.ex_benefit, b.type_support, "
//	        		+ "c.nm_philanthropy from nsa_program as a " +
//                    "left join nsa_collaboration as b on a.id_program = b.id_program " +
//                    "left join philanthropy_collaboration as c on b.id_philanthropy = c.id_philanthropy " +
//                    "where a.id_role = :id_role ";
	        String sql = "SELECT a.nm_program, b.sector, b.location, b.beneficiaries, b.ex_benefit, b.type_support, "
	        		+ "c.nm_philanthropy from nsa_program as a LEFT JOIN "
	        		+ "nsa_collaboration b ON b.id_program = a.id_program LEFT JOIN "
	        		+ "philanthropy_collaboration c ON c.id_philanthropy = b.id_philanthropy "
	        		+ "WHERE a.id_role = :id_role";
	        Query query = em.createNativeQuery(sql);
	        query.setParameter("id_role", idrole);
	        Map<String, Object> mapDetail = new HashMap<>();
	        mapDetail.put("mapDetail",query.getResultList());
	        JSONObject objDetail = new JSONObject(mapDetail);
	        JSONArray  arrayDetail = objDetail.getJSONArray("mapDetail");
	        
	        for (int i=0; i<arrayDetail.length(); i++) {
	        	JSONArray finalDetail = arrayDetail.getJSONArray(i);
	        	Row dataRow = sheet.createRow(i+1);
		    	dataRow.createCell(0).setCellValue(i+1);
		    	dataRow.createCell(1).setCellValue(finalDetail.get(1).toString());
		    	dataRow.createCell(2).setCellValue(finalDetail.get(0).toString());
		    	dataRow.createCell(3).setCellValue(finalDetail.get(2).toString());
		    	dataRow.createCell(4).setCellValue(finalDetail.get(3).toString());
		    	dataRow.createCell(5).setCellValue(finalDetail.get(4).toString());
		    	dataRow.createCell(6).setCellValue(finalDetail.get(5).toString());
		    	dataRow.createCell(7).setCellValue(finalDetail.get(6).toString());
	        }
	    	
	        sheet.autoSizeColumn(0);
	        sheet.autoSizeColumn(1);
	        sheet.autoSizeColumn(2);
	        sheet.autoSizeColumn(3);
	        sheet.autoSizeColumn(4);
	        sheet.autoSizeColumn(5);
	        sheet.autoSizeColumn(6);
	        sheet.autoSizeColumn(7);
	        
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        workbook.write(outputStream);
	        return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("admin/get-nsaCollab/{id}")
    public @ResponseBody Map<String, Object> getSdgGoals(@PathVariable("id") String id) {
        List<NsaCollaboration> list = nsaCollaborationService.findByProgram(id);
		Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }

}
