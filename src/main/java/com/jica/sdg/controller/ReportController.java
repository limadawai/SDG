package com.jica.sdg.controller;

import com.jica.sdg.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReportController {

    @Autowired
    ProvinsiService provinsiService;
    @Autowired
    RoleService roleService;
    @Autowired
    RanRadService radService;
    @Autowired
    SdgGoalsService goalsService;
    @Autowired
    SdgTargetService targetService;
    @Autowired
    SdgIndicatorService indicatorService;
    @Autowired
    EntityManager manager;
    @Autowired
    RanRadService ranRadService;
    @Autowired
    SdgFundingService sdgFundingService;

    // ****************** Report Hasil Monitoring ******************
    @GetMapping("admin/report-monitoring")
    public String monitoring(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("listrole", roleService.findAll());
        model.addAttribute("listranrad", radService.findAll());
        model.addAttribute("listgoals", goalsService.findAll());

        model.addAttribute("title", "SDG Problem Identification & Follow Up");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/report/monitoring";
    }

    @GetMapping("admin/getrolebyidprov")
    public @ResponseBody List<Object> getRoleByIdProv(@RequestParam("id_prov") String id) {
        List list = roleService.findByProvince(id);
        return list;
    }

    @GetMapping("admin/getranradbyidprov")
    public @ResponseBody List<Object> getranrad(@RequestParam("id_prov") String id) {
        List list = ranRadService.findAll();
        return list;
    }
    
    @GetMapping("admin/getentryshowreport")
    public @ResponseBody List<Object> getentryshowreport(@RequestParam("id_monper") int idmonper) {
    	String sql = "SELECT * FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_sdg'";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getentrysdgyear")
    public @ResponseBody List<Object> getentrysdgyear(@RequestParam("id_role") int idrole, @RequestParam("id_monper") int idmonper, 
    		@RequestParam("year_entry") int year) {
    	String sql = "SELECT a.*, b.sdg_indicator, b.start_year, c.nm_goals, c.nm_goals_eng, "
    			+ "d.nm_target, d.nm_target_eng, e.nm_indicator, e.nm_indicator_eng, "
    			+ "f.funding_source, g.nm_unit, h.value AS val1, i.value AS val2, j.value AS val3, k.value AS val4, l.value AS val5, "
    			+ "m.achievement1 AS ach1, n.achievement1 AS ach2, o.achievement1 AS ach3, p.achievement1 as ach4 "
    			+ "FROM entry_sdg a LEFT JOIN "
    			+ "ran_rad b ON b.id_monper = :id_monper LEFT JOIN "
    			+ "sdg_goals c ON c.id = (SELECT id_goals FROM sdg_indicator WHERE id = a.id_sdg_indicator) LEFT JOIN "
    			+ "sdg_target d ON d.id = (SELECT id_target FROM sdg_indicator WHERE id = a.id_sdg_indicator) LEFT JOIN "
    			+ "sdg_indicator e ON e.id = a.id_sdg_indicator LEFT JOIN "
    			+ "sdg_funding f ON f.id_sdg_indicator = a.id_sdg_indicator LEFT JOIN "
    			+ "ref_unit g ON g.id_unit = (SELECT unit FROM sdg_indicator WHERE id = a.id_sdg_indicator) LEFT JOIN "
    			+ "sdg_indicator_target h ON h.id_sdg_indicator = a.id_sdg_indicator AND h.id_role = :id_role AND h.year = :year_entry+0 LEFT JOIN "
    			+ "sdg_indicator_target i ON i.id_sdg_indicator = a.id_sdg_indicator AND i.id_role = :id_role AND i.year = :year_entry+1 LEFT JOIN "
    			+ "sdg_indicator_target j ON j.id_sdg_indicator = a.id_sdg_indicator AND j.id_role = :id_role AND j.year = :year_entry+2 LEFT JOIN "
    			+ "sdg_indicator_target k ON k.id_sdg_indicator = a.id_sdg_indicator AND k.id_role = :id_role AND k.year = :year_entry+3 LEFT JOIN "
    			+ "sdg_indicator_target l ON l.id_sdg_indicator = a.id_sdg_indicator AND l.id_role = :id_role AND l.year = :year_entry+4 LEFT JOIN "
    			+ "entry_sdg m ON m.id_role = :id_role AND m.id_monper = :id_monper AND m.year_entry = :year_entry+1 LEFT JOIN "
    			+ "entry_sdg n ON n.id_role = :id_role AND n.id_monper = :id_monper AND n.year_entry = :year_entry+2 LEFT JOIN "
    			+ "entry_sdg o ON o.id_role = :id_role AND o.id_monper = :id_monper AND o.year_entry = :year_entry+3 LEFT JOIN "
    			+ "entry_sdg p ON p.id_role = :id_role AND p.id_monper = :id_monper AND p.year_entry = :year_entry+4 "
    			+ "WHERE a.id_role = :id_role AND a.id_monper = :id_monper AND a.year_entry = :year_entry";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_role", idrole);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year_entry", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovindicator")
    public @ResponseBody List<Object> getgovindicator(@RequestParam("id_prov") String idprov, 
    		@RequestParam("id_sdg_indicator") int idsdgindikator, 
    		@RequestParam("id_monper") int idmonper, 
    		@RequestParam("id_role") int idrole) {
    	String sql = "SELECT a.*, b.nm_program, b.nm_program_eng, c.nm_activity, c.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, e.nm_unit FROM gov_map a LEFT JOIN "
    			+ "gov_program b ON b.id = (SELECT id_program FROM gov_indicator WHERE id = a.id_gov_indicator) LEFT JOIN "
    			+ "gov_activity c ON c.id = (SELECT id_activity FROM gov_indicator WHERE id = a.id_gov_indicator AND id_role = :id_role) LEFT JOIN "
    			+ "gov_indicator d ON d.id = a.id_gov_indicator LEFT JOIN "
    			+ "ref_unit e ON e.id_unit = (SELECT unit FROM gov_indicator WHERE id = a.id_gov_indicator) "
    			+ "WHERE a.id_prov = :id_prov AND a.id_indicator = :id_indicator AND a.id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_indicator", idsdgindikator);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_role", idrole);
        List list = query.getResultList();
        return list;
    }
    

    // ****************************** Report Grafik ******************************

    @GetMapping("admin/report-graph")
    public String grafik(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());

        model.addAttribute("title", "Report Graphic");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/report/graph";
    }

    @GetMapping("admin/graphsdg")
    public @ResponseBody List<Object> graphSdg(@RequestParam("id_prov") String idprov) {
        String sql = "SELECT a.*, b.id AS idgoals, b.nm_goals, b.nm_goals_eng, c.id AS idtarget, c.nm_target, c.nm_target_eng, " +
                "d.id AS idindicator, d.nm_indicator, d.nm_indicator_eng FROM assign_sdg_indicator a LEFT JOIN " +
                "sdg_goals b ON b.id = a.id_goals LEFT JOIN " +
                "sdg_target c ON c.id = a.id_target LEFT JOIN " +
                "sdg_indicator d ON d.id = a.id_indicator WHERE a.id_prov = :id_prov";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/graphidgovindi")
    public @ResponseBody List<Object> idGovIndi(@RequestParam("id_indicator") int idindi) {
        String sql = "SELECT id_gov_indicator, id_monper FROM gov_map WHERE id_indicator = :id_indicator ORDER BY id_gov_indicator ASC";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", idindi);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/isigovmap")
    public @ResponseBody List<Object> isigovmap(@RequestParam("id") int id_gov_indicator) {
        String sql = "SELECT a.*, b.nm_program, b.nm_program_eng, b.internal_code as progcode, " +
                "c.nm_activity, c.nm_activity_eng, c.internal_code AS actcode FROM gov_indicator a LEFT JOIN " +
                "gov_program b ON b.id = a.id_program LEFT JOIN " +
                "gov_activity c ON c.id = a.id_activity WHERE a.id = :id_gov_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", id_gov_indicator);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/graphidnsaindi")
    public @ResponseBody List<Object> idNsaIndi(@RequestParam("id_indicator") int idindi) {
        String sql = "SELECT id_nsa_indicator, id_monper FROM nsa_map WHERE id_indicator = :id_indicator ORDER BY id_nsa_indicator ASC";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", idindi);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/isinsamap")
    public @ResponseBody List<Object> isinsamap(@RequestParam("id") int id_nsa_indicator) {
        String sql = "SELECT a.*, b.nm_program, b.nm_program_eng, b.internal_code as progcode, " +
                "c.nm_activity, c.nm_activity_eng, c.internal_code AS actcode FROM nsa_indicator a LEFT JOIN " +
                "nsa_program b ON b.id = a.id_program LEFT JOIN " +
                "nsa_activity c ON c.id = a.id_activity WHERE a.id = :id_nsa_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_nsa_indicator", id_nsa_indicator);
        List list = query.getResultList();
        return list;
    }
    
    

    //====================== Grafik Detail ======================
    
    @GetMapping("admin/report-graph-detail/{idsdg}/{idtar}/{idindi}/{id}/{idmonper}/{flag}")
    public String grafikdetail(Model model, HttpSession session, @PathVariable("idsdg") String idsdg, @PathVariable("idtar") String idtar,
    		@PathVariable("idindi") String idindi, @PathVariable("id") String id, @PathVariable("idmonper") String idmonper, @PathVariable("flag") String flag) {
        model.addAttribute("title", "Report Graphic Detail");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("idsdg", idsdg);
        model.addAttribute("idtar", idtar);
        model.addAttribute("idsdgindikator", idindi);
        model.addAttribute("idindikator", id);
        model.addAttribute("idmonper", idmonper);
        model.addAttribute("flag", flag);
        return "admin/report/graphdetail";
    }

    //********************* Header Table *********************
    @GetMapping("admin/cekgovmonper")
    public @ResponseBody List<Object> cekgovmonper(@RequestParam("id_goals") int idsdg, @RequestParam("id_target") int idtarget,
    		@RequestParam("id_indicator") int idsdgindi, @RequestParam("id_gov_indicator") int id) {
        String sql = "SELECT gov_prog FROM ran_rad WHERE id_monper IN (SELECT id_monper FROM gov_map "
        		+ "WHERE id_goals = :id_goals AND id_target = :id_target AND "
        		+ "id_indicator = :id_indicator AND id_gov_indicator = :id_gov_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idsdg);
        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", idsdgindi);
        query.setParameter("id_gov_indicator", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/ceknsamonper")
    public @ResponseBody List<Object> ceknsamonper(@RequestParam("id_sdg_goals") int idsdg, @RequestParam("id_sdg_target") int idtarget,
    		@RequestParam("id_sdg_indicator") int idsdgindi, @RequestParam("id_nsa_indicator") int id) {
    	String sql = "SELECT nsa_prog FROM ran_rad WHERE id_monper = (SELECT id_monper FROM nsa_map "
        		+ "WHERE id_goals = :id_goals AND id_target = :id_target AND "
        		+ "id_indicator = :id_indicator AND id_nsa_indicator = :id_nsa_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idsdg);
        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", idsdgindi);
        query.setParameter("id_nsa_indicator", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/getperiodeheader")
    public @ResponseBody List<Object> getperiodeheader(@RequestParam("id_gov_indicator") int id) {
        String sql = "SELECT a.id_monper, b.start_year, b.end_year FROM gov_map a LEFT JOIN " +
                "ran_rad b ON b.id_monper = a.id_monper WHERE a.id_gov_indicator = :id_gov_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", id);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getnsaperiodeheader")
    public @ResponseBody List<Object> getnsaperiodeheader(@RequestParam("id_nsa_indicator") int id) {
        String sql = "SELECT a.id_monper, b.start_year, b.end_year FROM nsa_map a LEFT JOIN " +
                "ran_rad b ON b.id_monper = a.id_monper WHERE a.id_nsa_indicator = :id_nsa_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_nsa_indicator", id);
        List list = query.getResultList();
        return list;
    }

//    @GetMapping("admin/reportgovgrap")
//    public @ResponseBody List<Object> reportgovgrap(@RequestParam("id_gov_indicator") int id) {
//        String sql = "SELECT a.*, b.nm_unit, c.value AS val1, d.value AS val2, e.value AS val3, f.value AS val4, g.value AS val5, " +
//                "h.funding_source, (i.achievement1+i.achievement2+i.achievement3+i.achievement4) FROM gov_map a LEFT JOIN " +
//                "ref_unit b ON b.id_unit = (SELECT unit FROM gov_indicator WHERE id = a.id_gov_indicator) LEFT JOIN " +
//                "gov_target c ON c.id_gov_indicator = a.id_gov_indicator AND c.year = (SELECT start_year FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
//                "gov_target d ON d.id_gov_indicator = a.id_gov_indicator AND d.year = (SELECT start_year+1 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
//                "gov_target e ON e.id_gov_indicator = a.id_gov_indicator AND e.year = (SELECT start_year+2 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
//                "gov_target f ON f.id_gov_indicator = a.id_gov_indicator AND f.year = (SELECT start_year+3 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
//                "gov_target g ON g.id_gov_indicator = a.id_gov_indicator AND g.year = (SELECT start_year+4 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
//                "gov_funding h ON h.id_gov_indicator = a.id_gov_indicator LEFT JOIN " +
//                "entry_gov_budget i ON i.id_monper = a.id_monper " +
//                "WHERE a.id_gov_indicator = :id";
//        Query query = manager.createNativeQuery(sql);
//        query.setParameter("id", id);
//        List list = query.getResultList();
//        return list;
//    }
    
    @GetMapping("admin/reportgovgrap")
    public @ResponseBody List<Object> reportgovgrap(@RequestParam("id_sdg_goals") int idsdg, @RequestParam("id_sdg_target") int idtarget,
    		@RequestParam("id_sdg_indicator") int idsdgindi, @RequestParam("id_gov_indicator") int id) {
        String sql = "SELECT a.*, b.nm_unit FROM gov_map a LEFT JOIN " +
                "ref_unit b ON b.id_unit = (SELECT unit FROM gov_indicator WHERE id = a.id_gov_indicator) "
                + "WHERE id_goals = :id_goals AND id_target = :id_target AND id_indicator = :id_indicator AND id_gov_indicator = :id";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idsdg);
        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", idsdgindi);
        query.setParameter("id", id);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/targetgov")
    public @ResponseBody List<Object> targetgov(@RequestParam("id_gov_indicator") int idindi, @RequestParam("year") String tahun) {
    	String sql = "SELECT value FROM gov_target WHERE id_gov_indicator = :id_gov_indicator AND year = :year";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_gov_indicator", idindi);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/targetnsa")
    public @ResponseBody List<Object> targetnsa(@RequestParam("id_nsa_indicator") int idindi, @RequestParam("year") String tahun) {
    	String sql = "SELECT value FROM nsa_target WHERE id_nsa_indicator = :id_nsa_indicator AND year = :year";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_nsa_indicator", idindi);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realgovyear")
    public @ResponseBody List<Object> realgov(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun) {
    	String sql = "SELECT achievement1 FROM entry_gov_indicator WHERE year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realgovsemester")
    public @ResponseBody List<Object> realgovsemester(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun) {
    	String sql = "SELECT achievement1, achievement2 FROM entry_gov_indicator WHERE year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realgovquarter")
    public @ResponseBody List<Object> realgovquarter(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun) {
    	String sql = "SELECT achievement1, achievement2, achievement3, achievement4 FROM entry_gov_indicator WHERE year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realnsayear")
    public @ResponseBody List<Object> realnsayear(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun) {
    	String sql = "SELECT achievement1 FROM entry_nsa_indicator WHERE year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realnsasemester")
    public @ResponseBody List<Object> realnsasemester(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun) {
    	String sql = "SELECT achievement1 FROM entry_nsa_indicator WHERE year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realnsaquarter")
    public @ResponseBody List<Object> realnsaquarter(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun) {
    	String sql = "SELECT achievement1 FROM entry_nsa_indicator WHERE year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/reportnsagrap")
    public @ResponseBody List<Object> reportnsagrap(@RequestParam("id_nsa_indicator") int id) {
        String sql = "SELECT a.*, b.nm_unit, c.value AS val1, d.value AS val2, e.value AS val3, f.value AS val4, g.value AS val5, " +
                "h.funding_source, (i.achievement1+i.achievement2+i.achievement3+i.achievement4) FROM nsa_map a LEFT JOIN " +
                "ref_unit b ON b.id_unit = (SELECT unit FROM nsa_indicator WHERE id = a.id_nsa_indicator) LEFT JOIN " +
                "nsa_target c ON c.id_nsa_indicator = a.id_nsa_indicator AND c.year = (SELECT start_year FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
                "nsa_target d ON d.id_nsa_indicator = a.id_nsa_indicator AND d.year = (SELECT start_year+1 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
                "nsa_target e ON e.id_nsa_indicator = a.id_nsa_indicator AND e.year = (SELECT start_year+2 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
                "nsa_target f ON f.id_nsa_indicator = a.id_nsa_indicator AND f.year = (SELECT start_year+3 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
                "nsa_target g ON g.id_nsa_indicator = a.id_nsa_indicator AND g.year = (SELECT start_year+4 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
                "nsa_funding h ON h.id_nsa_indicator = a.id_nsa_indicator LEFT JOIN " +
                "entry_nsa_budget i ON i.id_monper = a.id_monper " +
                "WHERE a.id_nsa_indicator = :id";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id", id);
        List list = query.getResultList();
        return list;
    }


}
