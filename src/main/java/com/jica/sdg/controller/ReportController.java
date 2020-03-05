package com.jica.sdg.controller;

import com.jica.sdg.model.EntryGriojk;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jica.sdg.model.Problemlist;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.service.ISdgGoalsService;
import com.jica.sdg.service.ISdgIndicatorService;
import com.jica.sdg.service.ISdgTargetService;
import com.jica.sdg.service.ModelCrud;
import com.jica.sdg.service.ProvinsiService;
import com.jica.sdg.service.RanRadService;
import com.jica.sdg.service.RoleService;
import com.jica.sdg.service.SdgFundingService;
import com.jica.sdg.service.SdgGoalsService;
import com.jica.sdg.service.SdgIndicatorService;
import com.jica.sdg.service.SdgTargetService;
import java.util.Optional;

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
    ISdgGoalsService sdgGoalsService;
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
    @Autowired
    ISdgTargetService sdgTargetService;
    @Autowired
    ISdgIndicatorService sdgIndicatorService;
    @Autowired
    ModelCrud modelCrud;
    @Autowired
    private EntityManager em;

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
    public @ResponseBody Map<String, Object> getentryshowreport(@RequestParam("id_monper") int idmonper) {
    	//String sql = "SELECT period FROM entry_show_report WHERE id_monper = :id_monper AND year = :year AND type = 'entry_sdg'";
    	String sql = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_sdg'";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        //query.setParameter("year", year);
        List listSdg = query.getResultList();
        
        //String sql1 = "SELECT period FROM entry_show_report WHERE id_monper = :id_monper AND year = :year AND type = 'entry_gov_indicator'";
        String sql1 = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_gov_indicator'";
        Query query1 = manager.createNativeQuery(sql1);
        query1.setParameter("id_monper", idmonper);
        //query1.setParameter("year", year);
        List listGovInd = query1.getResultList();
        
        //String sql2 = "SELECT period FROM entry_show_report WHERE id_monper = :id_monper AND year = :year AND type = 'entry_gov_budget'";
        String sql2 = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_gov_budget'";
        Query query2 = manager.createNativeQuery(sql2);
        query2.setParameter("id_monper", idmonper);
        //query2.setParameter("year", year);
        List listGovBud = query2.getResultList();
        
        //String sql3 = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND year = :year AND type = 'entry_nsa_budget'";
        String sql3 = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_nsa_budget'";
        Query query3 = manager.createNativeQuery(sql3);
        query3.setParameter("id_monper", idmonper);
        //query3.setParameter("year", year);
        List listNsaBud = query3.getResultList();
        
        //String sql4 = "SELECT period FROM entry_show_report WHERE id_monper = :id_monper AND year = :year AND type = 'entry_nsa_indicator'";
        String sql4 = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_nsa_indicator'";
        Query query4 = manager.createNativeQuery(sql4);
        query4.setParameter("id_monper", idmonper);
        //query4.setParameter("year", year);
        List listNsaInd = query4.getResultList();
        
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("sdg",listSdg);
        hasil.put("GovInd",listGovInd);
        hasil.put("GovBud",listGovBud);
        hasil.put("NsaInd",listNsaInd);
        hasil.put("NsaBud",listNsaBud);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-goals")
    public @ResponseBody Map<String, Object> getSdgGoals(@RequestParam("id_role") int id_role) {
    	String sql = "SELECT distinct a.id_goals as id, b.nm_goals, b.nm_goals_eng, b.id_goals FROM assign_sdg_indicator a "
    			+ " left join sdg_goals b on a.id_goals = b.id "
    			+ " WHERE a.id_role = :id_role";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("sdg",listSdg);
        return hasil;
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
    public @ResponseBody Map<String, Object> getgovindicator(@RequestParam("id_prov") String idprov, 
    		@RequestParam("id_sdg_indicator") int idsdgindikator, 
    		@RequestParam("id_monper") int idmonper, 
    		@RequestParam("id_role") int idrole,
    		@RequestParam("year") int year) {
    	String sql = "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4, a.id, j.nm_role, j.cat_role "
    			+ "from gov_map a "
    			+ "left join entry_gov_indicator b on a.id_gov_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join gov_indicator d on a.id_gov_indicator = d.id "
    			+ "left join entry_gov_budget c on d.id_activity = c.id_gov_activity and c.year_entry = :year "
    			+ "left join gov_program e on d.id_program = e.id "
    			+ "left join gov_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join gov_target h on a.id_gov_indicator = h.id_gov_indicator and year = :year "
    			+ "left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper "
    			+ "left join ref_role j on j.id_role = f.id_role "
    			+ "where a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_indicator = :id_indicator and f.id_role = :id_role ";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_indicator", idsdgindikator);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_role", idrole);
        query.setParameter("year", year);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getgovindicatorisi")
    public @ResponseBody Map<String, Object> getgovindicatorisi(@RequestParam("id") Integer id, @RequestParam("year") Integer year) {
    	String sql = "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4 "
    			+ "from gov_map a "
    			+ "left join entry_gov_indicator b on a.id_gov_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join gov_indicator d on a.id_gov_indicator = d.id "
    			+ "left join entry_gov_budget c on d.id_activity = c.id_gov_activity and c.year_entry = :year "
    			+ "left join gov_program e on d.id_program = e.id "
    			+ "left join gov_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join gov_target h on a.id_gov_indicator = h.id_gov_indicator and year = :year "
    			+ "left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper "
    			+ "where a.id = :id ";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id", id);
        query.setParameter("year", year);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getnsaindicator")
    public @ResponseBody Map<String, Object> getnsaindicator(@RequestParam("id_prov") String idprov, 
    		@RequestParam("id_sdg_indicator") int idsdgindikator, 
    		@RequestParam("id_monper") int idmonper, 
    		@RequestParam("id_role") int idrole,
    		@RequestParam("year") int year) {
    	String sql = "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4, a.id, j.nm_role, j.cat_role "
    			+ "from nsa_map a "
    			+ "left join entry_nsa_indicator b on a.id_nsa_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join nsa_indicator d on a.id_nsa_indicator = d.id "
    			+ "left join entry_nsa_budget c on d.id_activity = c.id_nsa_activity and c.year_entry = :year "
    			+ "left join nsa_program e on d.id_program = e.id "
    			+ "left join nsa_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join nsa_target h on a.id_nsa_indicator = h.id_nsa_indicator and year = :year "
    			+ "left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper "
    			+ "left join ref_role j on j.id_role = f.id_role "
    			+ "where a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_nsa_indicator = :id_indicator and f.id_role = :id_role ";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_indicator", idsdgindikator);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_role", idrole);
        query.setParameter("year", year);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getallindicatorreport")
    public @ResponseBody Map<String, Object> getallindicator(@RequestParam("id_prov") String idprov, 
    		@RequestParam("id_sdg_indicator") int idsdgindikator, 
    		@RequestParam("id_monper") int idmonper, 
    		@RequestParam("id_role") String idrole,
    		@RequestParam("year") int year) {
    	String sql = "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4, a.id, j.nm_role, j.cat_role "
    			+ "from gov_map a "
    			+ "left join entry_gov_indicator b on a.id_gov_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join gov_indicator d on a.id_gov_indicator = d.id "
    			+ "left join entry_gov_budget c on d.id_activity = c.id_gov_activity and c.year_entry = :year "
    			+ "left join gov_program e on d.id_program = e.id "
    			+ "left join gov_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join gov_target h on a.id_gov_indicator = h.id_gov_indicator and year = :year "
    			+ "left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper "
    			+ "left join ref_role j on j.id_role = f.id_role "
    			+ "where a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_indicator = :id_indicator ";
    	sql += " UNION ALL ";
    	sql += "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4, a.id, j.nm_role, j.cat_role "
    			+ "from nsa_map a "
    			+ "left join entry_nsa_indicator b on a.id_nsa_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join nsa_indicator d on a.id_nsa_indicator = d.id "
    			+ "left join entry_nsa_budget c on d.id_activity = c.id_nsa_activity and c.year_entry = :year "
    			+ "left join nsa_program e on d.id_program = e.id "
    			+ "left join nsa_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join nsa_target h on a.id_nsa_indicator = h.id_nsa_indicator and year = :year "
    			+ "left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper "
    			+ "left join ref_role j on j.id_role = f.id_role "
    			+ "where a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_nsa_indicator = :id_indicator ";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_indicator", idsdgindikator);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year", year);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getnsaindicatorisi")
    public @ResponseBody Map<String, Object> getnsaindicatorisi(@RequestParam("id") Integer id, @RequestParam("year") Integer year) {
    	String sql = "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4 "
    			+ "from nsa_map a "
    			+ "left join entry_nsa_indicator b on a.id_nsa_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join nsa_indicator d on a.id_nsa_indicator = d.id "
    			+ "left join entry_nsa_budget c on d.id_activity = c.id_nsa_activity and c.year_entry = :year "
    			+ "left join nsa_program e on d.id_program = e.id "
    			+ "left join nsa_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join nsa_target h on a.id_nsa_indicator = h.id_nsa_indicator and year = :year "
    			+ "left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper "
    			+ "where a.id = :id ";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id", id);
        query.setParameter("year", year);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
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
    
    @GetMapping("admin/getallgoals")
    public @ResponseBody List<Object> getallgoals() {
    	String sql = "SELECT id, nm_goals, nm_goals_eng FROM sdg_goals ORDER BY id ASC";
    	Query query = manager.createNativeQuery(sql);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getalltarget")
    public @ResponseBody List<Object> getalltarget(@RequestParam("id_goals") int id_goals) {
    	String sql = "SELECT id, nm_target, nm_target_eng FROM sdg_target WHERE id_goals = :id_goals ORDER BY id ASC";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", id_goals);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallindicator")
    public @ResponseBody List<Object> getallindicator(@RequestParam("id_goals") int id_goals, @RequestParam("id_target") int id_target) {
    	String sql = "SELECT id, nm_indicator, nm_indicator_eng FROM sdg_indicator WHERE id_goals = :id_goals AND "
    			+ "id_target = :id_target GROUP BY id, id_goals, id_target ORDER BY id ASC";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", id_goals);
        query.setParameter("id_target", id_target);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getalldisaggre")
    public @ResponseBody List<Object> getalldisaggre(@RequestParam("id_indicator") int idindi) {
    	String sql = "SELECT a.id, a.nm_disaggre, a.nm_disaggre_eng, b.desc_disaggre, b.desc_disaggre_eng "
    			+ "FROM sdg_ranrad_disaggre a LEFT JOIN sdg_ranrad_disaggre_detail b ON b.id_disaggre = a.id "
    			+ "WHERE a.id = :id_indicator ORDER BY a.id ASC";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", idindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallgovprogram")
    public @ResponseBody List<Object> getallgovprogram(@RequestParam("id_prov") String idprov) {
    	String sql = "SELECT a.id_gov_indicator, b.id, b.nm_program, b.nm_program_eng FROM gov_map a LEFT JOIN "
    			+ "gov_program b ON b.id = (SELECT id_program FROM gov_indicator WHERE id = a.id_gov_indicator) "
    			+ "WHERE a.id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallgovactivity")
    public @ResponseBody List<Object> getallgovactivity(@RequestParam("id_program") int idprogram) {
    	String sql = "SELECT id, nm_activity, nm_activity_eng FROM gov_activity WHERE id_program = :id_program";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_program", idprogram);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallgovindi")
    public @ResponseBody List<Object> getallgovindi(@RequestParam("id_program") int idprogram, @RequestParam("id_activity") int idactivity) {
    	String sql = "SELECT id, nm_indicator, nm_indicator_eng FROM gov_indicator WHERE id_program = :id_program AND "
    			+ "id_activity = :id_activity GROUP BY id, id_program, id_activity ORDER BY id ASC";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_program", idprogram);
        query.setParameter("id_activity", idactivity);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallnsaprogram")
    public @ResponseBody List<Object> getallnsaprogram(@RequestParam("id_prov") String idprov) {
    	String sql = "SELECT a.id_nsa_indicator, b.id, b.nm_program, b.nm_program_eng FROM nsa_map a LEFT JOIN "
    			+ "nsa_program b ON b.id = (SELECT id_program FROM nsa_indicator WHERE id = a.id_nsa_indicator) "
    			+ "WHERE a.id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallnsaactivity")
    public @ResponseBody List<Object> getallnsaactivity(@RequestParam("id_program") int idprogram) {
    	String sql = "SELECT id, nm_activity, nm_activity_eng FROM nsa_activity WHERE id = :id_program ORDER BY id ASC";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_program", idprogram);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallnsaindi")
    public @ResponseBody List<Object> getallnsaindi(@RequestParam("id_program") int idprogram, @RequestParam("id_activity") int idactivity) {
    	String sql = "SELECT id, nm_indicator, nm_indicator_eng FROM nsa_indicator WHERE id_program = :id_program AND "
    			+ "id_activity = :id_activity GROUP BY id, id_program, id_activity ORDER BY id ASC";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_program", idprogram);
        query.setParameter("id_activity", idactivity);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/cekshowreport")
    public @ResponseBody List<Object> cekshowreport(@RequestParam("id_monper") int idmonper, @RequestParam("year") int year) {
    	String sql = "SELECT EXISTS(SELECT * FROM entry_show_report WHERE id_monper = :id_monper AND year = :year)";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovtarget")
    public @ResponseBody List<Object> getgovtarget(@RequestParam("id_gov_indicator") int idgovindi, @RequestParam("year") int year) {
    	String sql = "SELECT value FROM gov_target WHERE id_gov_indicator = :id_gov_indicator AND year = :year";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idgovindi);
        query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getidassign")
    public @ResponseBody List<Object> getidassign(@RequestParam("id_monper") int idmonper, @RequestParam("id_prov") int idprov) {
    	String sql = "SELECT id FROM assign_sdg_indicator WHERE id_monper = :id_monper AND id_prov = :id_prov GROUP BY id";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovrealyear")
    public @ResponseBody List<Object> getgovrealyear(@RequestParam("id_monper") int idmonper, @RequestParam("year_entry") int year) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1) "
    			+ "FROM entry_gov_indicator WHERE id_monper = :id_monper AND year_entry = :year_entry";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year_entry", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovrealsemester")
    public @ResponseBody List<Object> getgovrealsemester(@RequestParam("id_monper") int idmonper, @RequestParam("year_entry") int year) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2) "
    			+ "FROM entry_gov_indicator WHERE id_monper = :id_monper AND year_entry = :year_entry";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year_entry", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovrealquarter")
    public @ResponseBody List<Object> getgovrealquarter(@RequestParam("id_monper") int idmonper, @RequestParam("year_entry") int year) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2), "
    			+ "SELECT COALESCE(NULLIF(new_value3,''),achievement3), COALESCE(NULLIF(new_value4,''),achievement4) "
    			+ "FROM entry_gov_indicator WHERE id_monper = :id_monper AND year_entry = :year_entry";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year_entry", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovunit")
    public @ResponseBody List<Object> getgovunit(@RequestParam("id_gov_indicator") int idindi) {
    	String sql = "SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM gov_indicator WHERE id = :id_gov_indicator)";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovfund")
    public @ResponseBody List<Object> getgovfund(@RequestParam("id_gov_indicator") int idindi, @RequestParam("id_monper") int idmonper) {
    	String sql = "SELECT funding_source, baseline FROM gov_funding WHERE id_gov_indicator = :id_gov_indicator AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovgoals")
    public @ResponseBody List<Object> getgovgoals(@RequestParam("id_prov") String idprov, @RequestParam("id_monper") int idmonper, 
    		@RequestParam("id_gov_indicator") int idindi) {
    	String sql = "SELECT a.id_gov_indicator, b.id as idgoals, b.nm_goals, b.nm_goals_eng, "
    			+ "c.id as idtarget, c.nm_target, c.nm_target_eng, d.id as idindi, d.nm_indicator, d.nm_indicator_eng "
    			+ "FROM gov_map a LEFT JOIN sdg_goals b ON b.id = a.id_goals LEFT JOIN "
    			+ "sdg_target c ON c.id = a.id_target LEFT JOIN sdg_indicator d ON d.id = a.id_indicator "
    			+ "WHERE id_prov = :id_prov AND id_monper = :id_monper AND id_gov_indicator = :id_gov_indicator";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_gov_indicator", idindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/cekgovranrad")
    public @ResponseBody List<Object> cekgovranrad(@RequestParam("id_monper") int idmonper, @RequestParam("id_prov") String idprov) {
    	String sql = "SELECT sdg_indicator FROM ran_rad WHERE id_monper = :id_monper AND id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getsdgtar")
    public @ResponseBody List<Object> getsdgtar(@RequestParam("id_gov_indicator") int idindi, @RequestParam("name") String name, 
    		@RequestParam("year") int year) {
    	String sql = "SELECT value FROM sdg_indicator_target WHERE id_sdg_indicator = (SELECT id_indicator FROM gov_map WHERE id_gov_indicator = :id_gov_indicator) AND "
    			+ "id_role = (SELECT id_role FROM ref_user WHERE name = :name) AND year = :year";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        query.setParameter("name", name);
        query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getsdgreal")
    public @ResponseBody List<Object> getsdgreal(@RequestParam("id_gov_indicator") int idindi, @RequestParam("name") String name, 
    		@RequestParam("year") int year, @RequestParam("id_monper") int idmonper) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1) FROM entry_sdg WHERE "
    			+ "id_sdg_indicator = (SELECT id_indicator FROM gov_map WHERE id_gov_indicator = :id_gov_indicator) AND "
    			+ "id_role = (SELECT id_role FROM ref_user WHERE name = :name) AND "
    			+ "year_entry = :year_entry AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        query.setParameter("name", name);
        query.setParameter("year_entry", year);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getsdgfund")
    public @ResponseBody List<Object> getsdgfund(@RequestParam("id_gov_indicator") int idindi, @RequestParam("id_monper") int idmonper) {
    	String sql = "SELECT funding_source, baseline FROM sdg_funding "
    			+ "WHERE id_sdg_indicator = (SELECT id_indicator FROM gov_map WHERE id_gov_indicator = :id_gov_indicator) "
    			+ "AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getsdgunit")
    public @ResponseBody List<Object> getsdgunit(@RequestParam("id_gov_indicator") int idindi) {
    	String sql = "SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = (SELECT id_indicator FROM gov_map WHERE id_gov_indicator = :id_gov_indicator))";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        List list = query.getResultList();
        return list;
    }
    
 // ****************************** End Of Report Grafik ******************************
    
    @GetMapping("admin/getrole")
    public @ResponseBody List<Object> getrole(@RequestParam("id_prov") String idprov) {
    	String sql = "SELECT id_role FROM ref_role WHERE id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getmonper")
    public @ResponseBody List<Object> getmonper(@RequestParam("id_prov") String idprov) {
    	String sql = "SELECT id_monper, start_year, end_year FROM ran_rad WHERE id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/graphsdg")
    public @ResponseBody List<Object> graphSdg(@RequestParam("id_prov") String idprov, @RequestParam("id_role") int idrole) {
        String sql = "SELECT a.*, b.id AS idgoals, b.nm_goals, b.nm_goals_eng, c.id AS idtarget, c.nm_target, c.nm_target_eng, " +
                "d.id AS idindicator, d.nm_indicator, d.nm_indicator_eng "
                + "FROM assign_sdg_indicator a LEFT JOIN " +
                "sdg_goals b ON b.id = a.id_goals LEFT JOIN " +
                "sdg_target c ON c.id = a.id_target LEFT JOIN " +
                "sdg_indicator d ON d.id = a.id_indicator WHERE a.id_prov = :id_prov AND id_role = :id_role";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_role", idrole);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/graphidgovindi")
    public @ResponseBody List<Object> idGovIndi(@RequestParam("id_indicator") int idindi, @RequestParam("id_monper") int idmonper) {
        String sql = "SELECT id_gov_indicator, id_monper FROM gov_map WHERE id_indicator = :id_indicator AND id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", idindi);
        query.setParameter("id_monper", idmonper);
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
    public @ResponseBody List<Object> idNsaIndi(@RequestParam("id_indicator") int idindi, @RequestParam("id_monper") int idmonper) {
        String sql = "SELECT id_nsa_indicator, id_monper FROM nsa_map WHERE id_indicator = :id_indicator AND id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", idindi);
        query.setParameter("id_monper", idmonper);
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
    
    @GetMapping("admin/report-graph-detail/{idsdg}/{idtar}/{idindi}/{idmonper}/{flag}/{valdaerah}")
    public String grafikdetail(Model model, HttpSession session, @PathVariable("idsdg") int idsdg, @PathVariable("idtar") int idtar,
    		@PathVariable("idindi") int idindi, @PathVariable("idmonper") int idmonper, 
    		@PathVariable("flag") int flag, @PathVariable("valdaerah") String valdaerah) {
        model.addAttribute("title", "Report Graphic Detail");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("idsdg", idsdg);
        model.addAttribute("idtar", idtar);
        model.addAttribute("idsdgindikator", idindi);
        model.addAttribute("idmonper", idmonper);
        model.addAttribute("flag", flag);
        model.addAttribute("valdaerah", valdaerah);
        return "admin/report/graphdetail";
    }
    
    @GetMapping("admin/getprov")
    public @ResponseBody List<Object> getprov(@RequestParam("valdaerah") String val) {
    	String sql = "SELECT nm_prov, acronym FROM ref_province WHERE id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", val);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgoals")
    public @ResponseBody List<Object> getgoals(@RequestParam("id_goals") int idgoals, @RequestParam("id_target") int idtarget, 
    		@RequestParam("id_indicator") int idindikator) {
    	String sql = "SELECT a.id AS idindicator, a.nm_indicator, a.nm_indicator_eng, b.id AS idgoals, b.nm_goals, b.nm_goals_eng, "
    			+ "c.id AS idtarget, c.nm_target, c.nm_target_eng, d.id AS iddisagre, d.nm_disaggre, d.nm_disaggre_eng, "
    			+ "e.desc_disaggre, e.desc_disaggre_eng FROM sdg_indicator a LEFT JOIN "
    			+ "sdg_goals b ON b.id = :id_goals LEFT JOIN "
    			+ "sdg_target c ON c.id = :id_target LEFT JOIN "
    			+ "sdg_ranrad_disaggre d ON d.id = :id_indicator LEFT JOIN "
    			+ "sdg_ranrad_disaggre_detail e ON e.id_disaggre = d.id WHERE a.id = :id_indicator";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idgoals);
        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", idindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getprogov")
    public @ResponseBody List<Object> getprogov(@RequestParam("id_program") int idgoals, @RequestParam("id_activity") int idtarget, 
    		@RequestParam("id_indicator") int idindikator) {
    	String sql = "SELECT a.id AS idindicator, a.nm_indicator, a.nm_indicator_eng, b.id AS idprog, b.nm_program, b.nm_program_eng, "
    			+ "c.id AS idact, c.nm_activity, c.nm_activity_eng, d.id AS iddisagre, d.nm_disaggre, d.nm_disaggre_eng, "
    			+ "e.desc_disaggre, e.desc_disaggre_eng FROM gov_indicator a LEFT JOIN "
    			+ "gov_program b ON b.id = :id_program LEFT JOIN "
    			+ "gov_activity c ON c.id = :id_activity LEFT JOIN "
    			+ "sdg_ranrad_disaggre d ON d.id = :id_indicator LEFT JOIN "
    			+ "sdg_ranrad_disaggre_detail e ON e.id_disaggre = d.id WHERE a.id = :id_indicator";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_program", idgoals);
        query.setParameter("id_activity", idtarget);
        query.setParameter("id_indicator", idindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getpronsa")
    public @ResponseBody List<Object> getpronsa(@RequestParam("id_program") int idgoals, @RequestParam("id_activity") int idtarget, 
    		@RequestParam("id_indicator") int idindikator) {
    	String sql = "SELECT a.id AS idindicator, a.nm_indicator, a.nm_indicator_eng, b.id AS idprog, b.nm_program, b.nm_program_eng, "
    			+ "c.id AS idact, c.nm_activity, c.nm_activity_eng, d.id AS iddisagre, d.nm_disaggre, d.nm_disaggre_eng, "
    			+ "e.desc_disaggre, e.desc_disaggre_eng FROM nsa_indicator a LEFT JOIN "
    			+ "nsa_program b ON b.id = :id_program LEFT JOIN "
    			+ "nsa_activity c ON c.id = :id_activity LEFT JOIN "
    			+ "sdg_ranrad_disaggre d ON d.id = :id_indicator LEFT JOIN "
    			+ "sdg_ranrad_disaggre_detail e ON e.id_disaggre = d.id WHERE a.id = :id_indicator";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_program", idgoals);
        query.setParameter("id_activity", idtarget);
        query.setParameter("id_indicator", idindikator);
        List list = query.getResultList();
        return list;
    }

    //********************* Header Table *********************
    @GetMapping("admin/getheaderyear")
    public @ResponseBody List<Object> getheaderyear(@RequestParam("id_monper") int idmonper, @RequestParam("id_prov") int idprov) {
    	String sql = "SELECT start_year FROM ran_rad WHERE id_monper = :id_monper AND id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/ceksdgmonper")
    public @ResponseBody List<Object> ceksdgmonper(@RequestParam("id_monper") int idmonper) {
        String sql = "SELECT sdg_indicator FROM ran_rad WHERE id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/cekgmonper")
    public @ResponseBody List<Object> cekgmonper(@RequestParam("id_monper") int idmonper) {
        String sql = "SELECT gov_prog FROM ran_rad WHERE id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/ceknmonper")
    public @ResponseBody List<Object> ceknmonper(@RequestParam("id_monper") int idmonper) {
        String sql = "SELECT gov_prog FROM ran_rad WHERE id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
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
    
  //****************************** Gov Grap *******************************
    
    @GetMapping("admin/reportgovgrap")
    public @ResponseBody List<Object> reportgovgrap(@RequestParam("id_sdg_goals") int idsdg, @RequestParam("id_sdg_target") int idtarget,
    		@RequestParam("id_sdg_indicator") int idsdgindi, @RequestParam("id_gov_indicator") int id) {
        String sql = "SELECT a.*, b.nm_unit FROM gov_map a LEFT JOIN "
                +"ref_unit b ON b.id_unit = (SELECT unit FROM gov_indicator WHERE id = a.id_gov_indicator) "
                + "WHERE id_goals = :id_goals AND id_target = :id_target AND id_indicator = :id_indicator AND id_gov_indicator = :id_gov_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idsdg);
        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", idsdgindi);
        query.setParameter("id_gov_indicator", id);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/targetgov")
    public @ResponseBody List<Object> targetgov(@RequestParam("id_gov_indicator") int idindi, @RequestParam("year") String tahun) {
    	String sql = "SELECT value FROM gov_target WHERE id_gov_indicator = :id_gov_indicator AND year = :year ORDER BY YEAR ASC";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_gov_indicator", idindi);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realgovyear")
    public @ResponseBody List<Object> realgov(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_gov_indicator") int idgovindi) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1) FROM entry_gov_indicator WHERE id_assign = :id_assign AND year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idgovindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realgovsemester")
    public @ResponseBody List<Object> realgovsemester(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_gov_indicator") int idgovindi) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2) "
    			+ "FROM entry_gov_indicator WHERE id_assign = :id_assign AND "
    			+ "year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idgovindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realgovquarter")
    public @ResponseBody List<Object> realgovquarter(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_gov_indicator") int idgovindi) {
    	String sql = "SELECT SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2), "
    			+ "COALESCE(NULLIF(new_value3,''),achievement3), COALESCE(NULLIF(new_value4,''),achievement4) "
    			+ "FROM entry_gov_indicator WHERE "
    			+ "id_assign = :id_assign AND year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idgovindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovfundsource")
    public @ResponseBody List<Object> getgovfundsource(@RequestParam("id_monper") int idmonper, @RequestParam("id_gov_indicator") int idindi){
    	String sql = "SELECT * FROM gov_funding WHERE id_gov_indicator = :id_gov_indicator AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_gov_indicator", idindi);
    	query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    //****************************** NSA Grap *******************************
    
    @GetMapping("admin/reportnsagrap")
    public @ResponseBody List<Object> reportnsagrap(@RequestParam("id_sdg_goals") int idsdg, @RequestParam("id_sdg_target") int idtarget,
    		@RequestParam("id_sdg_indicator") int idsdgindi, @RequestParam("id_nsa_indicator") int id) {
        String sql = "SELECT a.*, b.nm_unit FROM nsa_map a LEFT JOIN "
                +"ref_unit b ON b.id_unit = (SELECT unit FROM nsa_indicator WHERE id = a.id_nsa_indicator) "
                + "WHERE id_goals = :id_goals AND id_target = :id_target AND id_indicator = :id_indicator AND id_nsa_indicator = :id_nsa_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idsdg);
        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", idsdgindi);
        query.setParameter("id_nsa_indicator", id);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/targetnsa")
    public @ResponseBody List<Object> targetnsa(@RequestParam("id_nsa_indicator") int idindi, @RequestParam("year") String tahun) {
    	String sql = "SELECT value FROM nsa_target WHERE id_nsa_indicator = :id_nsa_indicator AND year = :year ORDER BY YEAR ASC";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_nsa_indicator", idindi);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realnsayear")
    public @ResponseBody List<Object> realnsayear(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_nsa_indicator") int idindikator) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1) FROM entry_nsa_indicator "
    			+ "WHERE id_assign = :id_assign AND year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realnsasemester")
    public @ResponseBody List<Object> realnsasemester(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_nsa_indicator") int idindikator) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2) "
    			+ "FROM entry_nsa_indicator WHERE id_assign = :id_assign AND year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realnsaquarter")
    public @ResponseBody List<Object> realnsaquarter(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_nsa_indicator") int idindikator) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2), "
    			+ "COALESCE(NULLIF(new_value3,''),achievement3), COALESCE(NULLIF(new_value4,''),achievement4) "
    			+ "FROM entry_nsa_indicator WHERE id_assign = :id_assign AND year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getnsafundsource")
    public @ResponseBody List<Object> getnsafundsource(@RequestParam("id_monper") int idmonper, @RequestParam("id_nsa_indicator") int idindi){
    	String sql = "SELECT * FROM nsa_funding WHERE id_nsa_indicator = :id_nsa_indicator AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_nsa_indicator", idindi);
    	query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
  //****************************** Isi Table ****************************** 
    @GetMapping("admin/getentrysdg")
    public @ResponseBody List<Object> getentrysdg(@RequestParam("id_sdg_indicator") int idsdgindikator, @RequestParam("id_role") int idrole, @RequestParam("year") int year) {
    	String sql = "SELECT value FROM sdg_indicator_target WHERE id_sdg_indicator = :id_sdg_indicator AND id_role = :id_role "
    			+ "AND year = :year";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_sdg_indicator", idsdgindikator);
    	query.setParameter("id_role", idrole);
    	query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getunit")
    public @ResponseBody List<Object> getunit(@RequestParam("id_sdg_indicator") int idsdgindikator) {
    	String sql = "SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = :id_sdg_indicator)";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_sdg_indicator", idsdgindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getsdgfunding")
    public @ResponseBody List<Object> getsdgfunding(@RequestParam("id_sdg_indicator") int idsdgindikator, @RequestParam("id_monper") int idmonper) {
    	String sql = "SELECT funding_source FROM sdg_funding WHERE id_sdg_indicator = :id_sdg_indicator AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_sdg_indicator", idsdgindikator);
    	query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getshowrep")
    public @ResponseBody List<Object> getrealisasi(@RequestParam("id_monper") int idmonper, @RequestParam("year") int year) {
    	String sql = "SELECT EXISTS(SELECT * FROM entry_show_report WHERE id_monper = :id_monper AND year = :year LIMIT 1)";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getrealyear")
    public @ResponseBody List<Object> getreal(@RequestParam("id_monper") int idmonper, @RequestParam("year") int year) {
    	String sql = "SELECT id_disaggre, id_disaggre_detail, COALESCE(NULLIF(new_value1,''),achievement1) FROM entry_sdg_detail "
    			+ "WHERE year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
   
   //*********************************************************************************************** 
    @GetMapping("admin/report-problem-identification")
    public String report_problem_identify(Model model,  HttpSession session) {    	
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");        
        model.addAttribute("refcategory",modelCrud.getRefCategory());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/report/problemidentify";
    }
    
    @GetMapping("admin/list-report-problem")
     public @ResponseBody Map<String, Object> ProblemList() {
        String sql = "SELECT a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat ";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Problemlist> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new Problemlist((Integer)row[0],(String)row[1],(String)row[2], (String)row[3],(String)row[4]));
        }
        hasil.put("content",result);
        return hasil;
    }
     
    @GetMapping("admin/list-report-problem/{id_cat}")
     public @ResponseBody Map<String, Object> ProblemList(@PathVariable("id_cat") String id_cat) {
        String sql = "SELECT a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat where a.id_cat = '"+id_cat+"'";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Problemlist> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new Problemlist((Integer)row[0],(String)row[1],(String)row[2], (String)row[3],(String)row[4]));
        }
        hasil.put("content",result);
        return hasil;
    }
     
    @GetMapping("admin/list-report-problem-goals/{id_goals}")
     public @ResponseBody Map<String, Object> ProblemListGoals(@PathVariable("id_goals") String id_goals) {
        String sql = "SELECT a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat where a.id_goals = '"+id_goals+"'";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Problemlist> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new Problemlist((Integer)row[0],(String)row[1],(String)row[2], (String)row[3],(String)row[4]));
        }
        hasil.put("content",result);
        return hasil;
    }
    @GetMapping("admin/list-report-problem-role/{id_role}")
     public @ResponseBody Map<String, Object> ProblemListRole(@PathVariable("id_role") String id_role) {
        String sql = "SELECT a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat where a.id_role = '"+id_role+"'";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Problemlist> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new Problemlist((Integer)row[0],(String)row[1],(String)row[2], (String)row[3],(String)row[4]));
        }
        hasil.put("content",result);
        return hasil;
    }
     
    @GetMapping("admin/report-problem/get-category")
     public @ResponseBody Map<String, Object> ProblemGetCategory() {
        String sql = "SELECT * FROM ref_category WHERE id_cat IN(SELECT DISTINCT id_cat FROM entry_problem_identify)";        
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/report-problem/get-goals")
     public @ResponseBody Map<String, Object> ProblemGetGoals() {
        String sql = "SELECT id,nm_goals FROM sdg_goals WHERE id IN(SELECT DISTINCT id_goals FROM entry_problem_identify)";        
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
     
    @GetMapping("admin/report-problem/get-role")
     public @ResponseBody Map<String, Object> ProblemGetRole() {
        String sql = "SELECT * FROM ref_role WHERE id_role IN(SELECT DISTINCT id_role FROM entry_problem_identify)";        
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }

     @GetMapping("admin/home-report/gri-ojk")
        public String listgriojk(Model model, HttpSession session) {
            Integer id_role = (Integer) session.getAttribute("id_role");
            Optional<Role> list = roleService.findOne(id_role);
            String id_prov = list.get().getId_prov();
            String privilege = list.get().getPrivilege();
            if(privilege.equals("SUPER")) {
                    model.addAttribute("listprov", provinsiService.findAllProvinsi());
            }else {
                    Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
                    list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
            }
            model.addAttribute("lang", session.getAttribute("bahasa"));
                    model.addAttribute("name", session.getAttribute("name"));
                    model.addAttribute("id_prov", id_prov);
                    model.addAttribute("privilege", privilege);
                    model.addAttribute("id_role", id_role);
                    
            
            String sql = "SELECT DISTINCT company_name FROM trx_excell";             
            Query list2 = em.createNativeQuery(sql);
            
            String sql2 = "SELECT DISTINCT year FROM trx_excell";             
            Query list3 = em.createNativeQuery(sql2);
            Map<String, Object> hasil = new HashMap<>();
            
            hasil.put("company",list2.getResultList());  
            hasil.put("tahun",list3.getResultList());  
            
            model.addAttribute("data", hasil);
            return "admin/report/gri_ojk";

        }
        
        @GetMapping("admin/get-report/gri-ojk/{year}/{company}")
        public @ResponseBody Map<String, Object> getReport(@PathVariable("year") Integer year,@PathVariable("company") String  company) {
            String sql = "SELECT a.kode,a.pjok,a.sdgs,a.sdgs_desc,a.gri,a.gri_desc,b.year,b.company_name,b.value,a.unit FROM excell a JOIN trx_excell b ON a.kode = b.kode \n" +
                          "where b.year = '"+year+"' and company_name ='"+company+"'";
            Query list = em.createNativeQuery(sql);
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list.getResultList());
            return hasil;
        }
        @GetMapping("admin/get-report/gri-ojk")
        public @ResponseBody Map<String, Object> getReport() {
            String sql = "SELECT a.kode,a.pjok,a.sdgs,a.sdgs_desc,a.gri,a.gri_desc,b.year,b.company_name,b.value,a.unit FROM excell a JOIN trx_excell b ON a.kode = b.kode ";
            Query list = em.createNativeQuery(sql);
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list.getResultList());
            return hasil;
        }
        
       @GetMapping("admin/get-last-year-company/gri-ojk/{company}")
        public @ResponseBody Map<String, Object> getLastYear(@PathVariable("company") String  company) {
            String sql = "SELECT DISTINCT YEAR FROM trx_excell WHERE company_name = '"+company+"' ORDER BY YEAR DESC LIMIT 1";
            Query list = em.createNativeQuery(sql);
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list.getResultList());
            return hasil;
        }
        
        
         @GetMapping("admin/get-all-row-company/gri-ojk/{query}")
        public @ResponseBody Map<String, Object> getAllRow(@PathVariable("query") String  query) {
            String sql = query;
            Query list = em.createNativeQuery(sql);
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list.getResultList());
            return hasil;
        }
       
        
}
