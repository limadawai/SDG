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

//    @GetMapping("admin/getroleperiode")
//    public @ResponseBody List<Object> getRoleMonper(@RequestParam("id_prov") String id) {
//        String sql = "SELECT ref_role.id_role, ref_role.nm_role, ref_role.cat_role, " +
//                "ran_rad.id_monper, ran_rad.start_year, ran_rad.end_year " +
//                "FROM ref_role JOIN ran_rad ON ref_role.id_prov = ran_rad.id_prov WHERE ref_role.id_prov = :id_prov";
//        Query query = manager.createNativeQuery(sql);
//        query.setParameter("id_prov", id);
//        List list = query.getResultList();
//        return list;
//    }

    @GetMapping("admin/getsdg")
    public @ResponseBody List<Object> getGovMapById(@RequestParam("id_prov") String id, @RequestParam("id_role") int idrole, @RequestParam("id_monper") int idmonper) {
        String sql = "SELECT a.*, b.nm_goals, b.nm_goals_eng, c.nm_target, c.nm_target_eng, d.nm_indicator, d.nm_indicator_eng, " +
                "e.nm_role, e.cat_role, f.start_year, f.end_year " +
                "FROM assign_sdg_indicator a LEFT JOIN " +
                "sdg_goals b ON b.id = a.id_goals LEFT JOIN " +
                "sdg_target c ON c.id = a.id_target LEFT JOIN " +
                "sdg_indicator d ON d.id = a.id_indicator LEFT JOIN " +
                "ref_role e ON e.id_role = a.id_role LEFT JOIN " +
                "ran_rad f ON f.id_monper = a.id_monper " +
                "WHERE a.id_prov = :id_prov AND a.id_role = :id_role AND a.id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", id);
        query.setParameter("id_role", idrole);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/sdgfunding")
    public @ResponseBody List<Object> getAllSdgFunding(@RequestParam("id_indicator") int idindikator) {
        String sql = "SELECT a.*, b.unit, c.value FROM sdg_funding a LEFT JOIN " +
                "sdg_indicator b ON b.id = a.id_sdg_indicator LEFT JOIN " +
                "sdg_indicator_target c on c.year = (SELECT start_year from ran_rad where id_monper = a.id_monper) " +
                "WHERE a.id_sdg_indicator = :id_sdg_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_sdg_indicator", idindikator);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/getgovmap")
    public @ResponseBody List<Object> getGovMapByIdProv(@RequestParam("id_prov") String idprov, @RequestParam("id_monper") int idmonper) {
        String sql = "SELECT a.*, b.start_year, b.end_year, c.id as idgoals, c.nm_goals, c.nm_goals_eng, d.id AS idtarget, d.nm_target, d.nm_target_eng, " +
                "e.id AS idindi, e.nm_indicator, e.nm_indicator_eng, e.unit, " +
                "f.nm_indicator AS gov_nm_indicator, f.nm_indicator_eng AS gov_nm_indicator_eng, " +
                "f.unit AS gov_indicator_unit FROM gov_map a LEFT JOIN " +
                "ran_rad b ON b.id_monper = a.id_monper LEFT JOIN " +
                "sdg_goals c ON c.id = a.id_goals LEFT JOIN " +
                "sdg_target d ON d.id = a.id_target LEFT JOIN " +
                "sdg_indicator e ON e.id = a.id_indicator LEFT JOIN " +
                "gov_indicator f ON f.id = a.id_gov_indicator " +
                "WHERE a.id_prov = :id_prov AND a.id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/govfunding")
    public @ResponseBody List<Object> getAllGovFunding(@RequestParam("id_indicator") int idindikator) {
        String sql = "SELECT a.*, (SELECT nm_unit from ref_unit WHERE id_unit = b.unit), c.value, (d.achievement1+d.achievement2+d.achievement3+d.achievement4) AS total " +
                "FROM gov_funding a LEFT JOIN " +
                "gov_indicator b ON b.id = a.id_gov_indicator LEFT JOIN " +
                "gov_target c ON c.year = (SELECT start_year FROM ran_rad where id_monper = a.id_monper) LEFT JOIN " +
                "entry_gov_budget d ON d.year_entry = (SELECT start_year FROM ran_rad where id_monper = a.id_monper) " +
                "WHERE a.id_gov_indicator = :id_gov_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindikator);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/govindicator")
    public @ResponseBody List<Object> getTabBawahContent(@RequestParam("id_gov_indicator") int idindikator) {
        String sql = "SELECT a.id, a.nm_indicator, a.nm_indicator_eng, a.unit, a.internal_code AS kodeindi, " +
                "b.nm_program, b.nm_program_eng, b.internal_code AS kodeprog, " +
                "c.nm_activity, c.nm_activity_eng, c.internal_code AS kodeact, " +
                "d.value, e.nm_unit, f.funding_source, g.achievement1, g.achievement2, g.achievement3, g.achievement4 " +
                "FROM gov_indicator a LEFT JOIN " +
                "gov_program b ON b.id = a.id_program LEFT JOIN " +
                "gov_activity c ON c.id = a.id_program LEFT JOIN " +
                "gov_target d ON d.id_gov_indicator = a.id LEFT JOIN " +
                "ref_unit e ON e.id_unit = a.unit LEFT JOIN " +
                "gov_funding f ON f.id_gov_indicator = a.id LEFT JOIN " +
                "entry_gov_indicator g ON g.id_assign = a.id " +
                "WHERE a.id = :id_gov_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindikator);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/getnsamap")
    public @ResponseBody List<Object> getNsaMapByIdProv(@RequestParam("id_prov") String idprov, @RequestParam("id_monper") int idmonper) {
        String sql = "SELECT a.*, b.start_year, b.end_year, c.id as idgoals, c.nm_goals, c.nm_goals_eng, d.id AS idtarget, d.nm_target, d.nm_target_eng, " +
                "e.id AS idindi, e.nm_indicator, e.nm_indicator_eng, e.unit, " +
                "f.nm_indicator AS nsa_nm_indicator, f.nm_indicator_eng AS nsa_nm_indicator_eng, " +
                "f.unit AS nsa_indicator_unit FROM nsa_map a LEFT JOIN " +
                "ran_rad b ON b.id_monper = a.id_monper LEFT JOIN " +
                "sdg_goals c ON c.id = a.id_goals LEFT JOIN " +
                "sdg_target d ON d.id = a.id_target LEFT JOIN " +
                "sdg_indicator e ON e.id = a.id_indicator LEFT JOIN " +
                "nsa_indicator f ON f.id = a.id_nsa_indicator " +
                "WHERE a.id_prov = :id_prov AND a.id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/nsafunding")
    public @ResponseBody List<Object> getAllNsaFunding(@RequestParam("id_indicator") int idindikator) {
        String sql = "SELECT a.*, b.unit, c.value, (d.achievement1+d.achievement2+d.achievement3+d.achievement4) " +
                "FROM nsa_funding a LEFT JOIN " +
                "nsa_indicator b ON b.id = a.id_nsa_indicator LEFT JOIN " +
                "nsa_target c ON c.year = (SELECT start_year from ran_rad where id_monper = a.id_monper) LEFT JOIN " +
                "entry_nsa_budget d ON d.year_entry = (SELECT start_year FROM ran_rad where id_monper = a.id_monper) " +
                "WHERE a.id_nsa_indicator = :id_nsa_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_nsa_indicator", idindikator);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/nsaindicator")
    public @ResponseBody List<Object> getTabBawahContentNsa(@RequestParam("id_nsa_indicator") int idindikator) {
        String sql = "SELECT a.id, a.nm_indicator, a.nm_indicator_eng, a.unit, a.internal_code AS kodeindi, " +
                "b.nm_program, b.nm_program_eng, b.internal_code AS kodeprog, " +
                "c.nm_activity, c.nm_activity_eng, c.internal_code AS kodeact, " +
                "d.value, e.nm_unit, f.funding_source, g.achievement1, g.achievement2, g.achievement3, g.achievement4 " +
                "FROM nsa_indicator a LEFT JOIN " +
                "nsa_program b ON b.id = a.id_program LEFT JOIN " +
                "nsa_activity c ON c.id = a.id_program LEFT JOIN " +
                "nsa_target d ON d.id_nsa_indicator = a.id LEFT JOIN " +
                "ref_unit e ON e.id_unit = a.unit LEFT JOIN " +
                "nsa_funding f ON f.id_nsa_indicator = a.id LEFT JOIN " +
                "entry_nsa_indicator g ON g.id_assign = a.id " +
                "WHERE a.id = :id_nsa_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_nsa_indicator", idindikator);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/getentryreport")
    public @ResponseBody List<Object> getEntryReportData(@RequestParam("id") int idreport) {
        String sql = "SELECT * FROM entry_show_report WHERE id = :id";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id", idreport);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/getrolebyidprov")
    public @ResponseBody List<Object> getRoleByIdProv(@RequestParam("id_prov") String id) {
        List list = roleService.findByProvince(id);
        return list;
    }

    @GetMapping("admin/getranradbyidprov")
    public @ResponseBody List<Object> getranrad(@RequestParam("id_prov") String id) {
        List list = ranRadService.findAllByIdProv(id);
        return list;
    }

    @GetMapping("admin/gettarget")
    public @ResponseBody List<Object> getTargetByGoals(@RequestParam("id_goals") int id) {
        List list = targetService.findAll(id);
        return list;
    }

    @GetMapping("admin/getindicator")
    public @ResponseBody List<Object> getIndicatorByTarget(@RequestParam("id_goals") int idgoals, @RequestParam("id_target") int idtarget) {
        List list = indicatorService.findAll(idgoals, idtarget);
        return list;
    }

    @GetMapping("admin/getdatarole")
    public @ResponseBody List<Object> getDataByRole(@RequestParam("id_role") int id) {
        String sql = "SELECT cat_role FROM ref_role WHERE id_role = :id_role";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_role", id);
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
        String sql = "SELECT id_gov_indicator FROM gov_map WHERE id_indicator = :id_indicator ORDER BY id_gov_indicator ASC";
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
        String sql = "SELECT id_nsa_indicator FROM nsa_map WHERE id_indicator = :id_indicator ORDER BY id_nsa_indicator ASC";
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
    @GetMapping("admin/report-graph-detail/{id}/{flag}")
    public String grafikdetail(Model model, HttpSession session, @PathVariable("id") String id, @PathVariable("flag") String flag) {
        model.addAttribute("title", "Report Graphic Detail");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("idindikator", id);
        model.addAttribute("flag", flag);
        return "admin/report/graphdetail";
    }

    //********************* Header Table *********************
    @GetMapping("admin/cekgovmonper")
    public @ResponseBody List<Object> cekgovmonper(@RequestParam("id_gov_indicator") int id) {
        String sql = "SELECT gov_prog FROM ran_rad WHERE id_monper = (SELECT id_monper FROM gov_map WHERE id_gov_indicator = :id_gov_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/ceknsamonper")
    public @ResponseBody List<Object> ceknsamonper(@RequestParam("id_nsa_indicator") int id) {
        String sql = "SELECT nsa_prog FROM ran_rad WHERE id_monper = (SELECT id_monper FROM nsa_map WHERE id_nsa_indicator = :id_nsa_indicator)";
        Query query = manager.createNativeQuery(sql);
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

    @GetMapping("admin/reportgovgrap")
    public @ResponseBody List<Object> reportgovgrap(@RequestParam("id_gov_indicator") int id) {
        String sql = "SELECT a.*, b.nm_unit, c.value AS val1, d.value AS val2, e.value AS val3, f.value AS val4, g.value AS val5, " +
                "h.funding_source, (i.achievement1+i.achievement2+i.achievement3+i.achievement4) FROM gov_map a LEFT JOIN " +
                "ref_unit b ON b.id_unit = (SELECT unit FROM gov_indicator WHERE id = a.id_gov_indicator) LEFT JOIN " +
                "gov_target c ON c.id_gov_indicator = a.id_gov_indicator AND c.year = (SELECT start_year FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
                "gov_target d ON d.id_gov_indicator = a.id_gov_indicator AND d.year = (SELECT start_year+1 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
                "gov_target e ON e.id_gov_indicator = a.id_gov_indicator AND e.year = (SELECT start_year+2 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
                "gov_target f ON f.id_gov_indicator = a.id_gov_indicator AND f.year = (SELECT start_year+3 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
                "gov_target g ON g.id_gov_indicator = a.id_gov_indicator AND g.year = (SELECT start_year+4 FROM ran_rad WHERE id_monper = a.id_monper) LEFT JOIN " +
                "gov_funding h ON h.id_gov_indicator = a.id_gov_indicator LEFT JOIN " +
                "entry_gov_budget i ON i.id_monper = a.id_monper " +
                "WHERE a.id_gov_indicator = :id";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/sumrealyear")
    public @ResponseBody List<Object> sumrealyear(@RequestParam("id_gov_indicator") int id) {
        String sql ="SELECT year_entry, achievement1 FROM entry_gov_indicator " +
                "WHERE id_monper = (SELECT id_monper FROM gov_map WHERE id_gov_indicator = :id_gov_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/sumrealsemester")
    public @ResponseBody List<Object> sumrealsemester(@RequestParam("id_gov_indicator") int id) {
        String sql ="SELECT year_entry, achievement1, achievement2 FROM entry_gov_indicator " +
                "WHERE id_monper = (SELECT id_monper FROM gov_map WHERE id_gov_indicator = :id_gov_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/sumrealquarter")
    public @ResponseBody List<Object> sumrealquarter(@RequestParam("id_gov_indicator") int id) {
        String sql ="SELECT year_entry, achievement1, achievement2, achievement3, achievement4 FROM entry_gov_indicator " +
                "WHERE id_monper = (SELECT id_monper FROM gov_map WHERE id_gov_indicator = :id_gov_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/sumrealyearnsa")
    public @ResponseBody List<Object> sumrealyearnsa(@RequestParam("id_nsa_indicator") int id) {
        String sql ="SELECT year_entry, achievement1 FROM entry_nsa_indicator " +
                "WHERE id_monper = (SELECT id_monper FROM nsa_map WHERE id_nsa_indicator = :id_nsa_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_nsa_indicator", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/sumrealsemesternsa")
    public @ResponseBody List<Object> sumrealsemesternsa(@RequestParam("id_nsa_indicator") int id) {
        String sql ="SELECT year_entry, achievement1, achievement2 FROM entry_nsa_indicator " +
                "WHERE id_monper = (SELECT id_monper FROM nsa_map WHERE id_nsa_indicator = :id_nsa_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_nsa_indicator", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/sumrealquarternsa")
    public @ResponseBody List<Object> sumrealquarternsa(@RequestParam("id_nsa_indicator") int id) {
        String sql ="SELECT year_entry, achievement1, achievement2, achievement3, achievement4 FROM entry_nsa_indicator " +
                "WHERE id_monper = (SELECT id_monper FROM nsa_map WHERE id_nsa_indicator = :id_nsa_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_nsa_indicator", id);
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
