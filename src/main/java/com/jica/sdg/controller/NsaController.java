package com.jica.sdg.controller;

import com.jica.sdg.model.Insprofile;
import com.jica.sdg.model.Menu;
import com.jica.sdg.model.Nsaprofile;
import com.jica.sdg.model.NsaCollaboration;
import com.jica.sdg.model.PhilanthropyCollaboration;
import com.jica.sdg.model.Nsadetail;
import com.jica.sdg.model.Nsaprofile2;
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
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.list;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
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
import javax.servlet.http.HttpServletResponse;
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
        String sql = "select a.*,b.id_prov from nsa_profile a "
                     + "left join ref_role b on b.id_role = a.id_role where a.id_role = '"+id+"'";
        
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
    
    @GetMapping("admin/list-get-option-role-ins-profil/{id}")
    public @ResponseBody Map<String, Object> getOptionInsProfilList(@PathVariable("id") String id) {
        
        String sql  = "select * from ref_role as a where a.id_prov = :id and cat_role = 'Institution' ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
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
        
        String sql  = "select * from ref_role as a where a.id_prov = :id and id_role!=1";
//        String sql  = "select * from ref_role as a where a.id_prov = :id and id_role!=1";
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
        String sql  = "select b.sector, a.nm_program, b.location, b.beneficiaries, b.ex_benefit, b.type_support, c.nm_philanthropy, b.id as id_collaboration, b.id_philanthropy, a.id_program, c.type_support as type_support1, c.nm_pillar, c.loc_philanthropy, d.id_prov, a.nm_program_eng, e.id_inst, e.nm_inst, e.id_role from nsa_program as a \n" +
                    "left join nsa_collaboration as b on a.id_program = b.id_program\n" +
                    "left join philanthropy_collaboration as c on b.id_philanthropy = c.id_philanthropy\n" +
                    "left join ref_role as d on a.id_role = d.id_role\n " +
                    "left join nsa_inst as e on c.id_inst = e.id_inst\n " +
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
    
    //========================== Export to Excell ========================
    @GetMapping("admin/nsa/download_profil/{id_prov}/{id_role}")
    @ResponseBody
    public void download_profil(HttpServletResponse response, @PathVariable("id_prov") String idprov, @PathVariable("id_role") int idrole) throws IOException {
    	response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Profile"+idprov+"-"+idrole+".xlsx");
        
        ByteArrayInputStream stream = exprofil(idprov, idrole);
        IOUtils.copy(stream, response.getOutputStream());
        //System.out.println(dataprof);
    }
    
    private ByteArrayInputStream exprofil(String idprov, int idrole) throws IOException {
    		Workbook workbook = new XSSFWorkbook();
    		Sheet sheet = workbook.createSheet("Profile");
			
			Row row = sheet.createRow(0);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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
	        
	        //=================== Isi tabel atas =================

	        String sql = "SELECT a.nm_nsa, a.loc_nsa, b.web_url, b.name_pic FROM nsa_profile a LEFT JOIN "
	        		+ "nsa_detail b ON b.id_nsa = a.id "
	        		+ "WHERE a.id_role = :id_role";
	        Query query = em.createNativeQuery(sql);
	        query.setParameter("id_role", idrole);
	        Map<String, Object> mapDetail = new HashMap<>();
	        mapDetail.put("mapDetail",query.getResultList());
	        JSONObject objDetail = new JSONObject(mapDetail); 
	        JSONArray  arrayDetail = objDetail.getJSONArray("mapDetail");
	        JSONArray  finalDetail = arrayDetail.getJSONArray(0);
	        
        	Row dataRow = sheet.createRow(1);
        	dataRow.createCell(0).setCellValue("1.");
        	dataRow.createCell(1).setCellValue(finalDetail.getString(0));
        	dataRow.createCell(2).setCellValue("Civil Society Organization/Organisasi Kepemudaan/Komunitas");
        	dataRow.createCell(3).setCellValue("");
        	dataRow.createCell(4).setCellValue(finalDetail.getString(2));
        	dataRow.createCell(5).setCellValue(finalDetail.getString(1));
        	dataRow.createCell(6).setCellValue(finalDetail.getString(3));
        	
        	//============= Spasi =============
//        	Row spasi = sheet.createRow(1);
//        	Cell cellspasi = spasi.createCell(0);cell.setCellValue("");
//        	cellspasi = spasi.createCell(1);cellspasi.setCellValue("");
//        	cellspasi = spasi.createCell(2);cellspasi.setCellValue("");
//        	cellspasi = spasi.createCell(3);cellspasi.setCellValue("");
//        	cellspasi = spasi.createCell(4);cellspasi.setCellValue("");
//        	cellspasi = spasi.createCell(5);cellspasi.setCellValue("");
//        	cellspasi = spasi.createCell(6);cellspasi.setCellValue("");
        	
        	//================= Tabel Pencapaian =================
//        	Row pencapaian = sheet.createRow(2);
//        	CellStyle headerCellStyle2 = workbook.createCellStyle();
//	        headerCellStyle2.setFillForegroundColor(IndexedColors.AQUA.getIndex());
//	        headerCellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//	        
//	        Cell celltab2 = pencapaian.createCell(0);
//	        celltab2.setCellValue("No.");
//	        celltab2.setCellStyle(headerCellStyle);
//	        
//	        celltab2 = pencapaian.createCell(1);
//	        celltab2.setCellValue("Pencapaian");
//	        celltab2.setCellStyle(headerCellStyle);
//	
//	        celltab2 = pencapaian.createCell(2);
//	        celltab2.setCellValue("Lokasi");
//	        celltab2.setCellStyle(headerCellStyle);
//	
//	        celltab2 = pencapaian.createCell(3);
//	        celltab2.setCellValue("Penerima Manfaat");
//	        celltab2.setCellStyle(headerCellStyle);
//	        
//	        celltab2 = pencapaian.createCell(4);
//	        celltab2.setCellValue("Tahun Implementasi");
//	        celltab2.setCellStyle(headerCellStyle);
//	        
//	        celltab2 = pencapaian.createCell(5);
//	        celltab2.setCellValue("Partner");
//	        celltab2.setCellStyle(headerCellStyle);
//	        
//	        Row dataRow2 = sheet.createRow(2);
//	        dataRow2.createCell(0).setCellValue(data[0]+".");
//	        dataRow2.createCell(1).setCellValue(data[7]);
//	        dataRow2.createCell(2).setCellValue(data[8]);
//	        dataRow2.createCell(3).setCellValue(data[9]);
//	        dataRow2.createCell(4).setCellValue(data[10]);
//	        dataRow2.createCell(5).setCellValue(data[11]);
	
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
    }

}
