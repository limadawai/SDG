package com.jica.sdg.controller;

import com.jica.sdg.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
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

    @GetMapping("admin/getroleprov")
    public @ResponseBody List<Object> getGovMapById(@RequestParam("id_prov") String id) {
        String sql = "select a.*, b.nm_goals, c.nm_indicator as nm_gov_indicator, d.nm_indicator, e.start_year, e.end_year, " +
                "f.nm_target from gov_map a left join " +
                "sdg_goals b on b.id_goals = a.id_goals left join " +
                "gov_indicator c on c.id_gov_indicator = a.id_gov_indicator left join " +
                "sdg_indicator d on d.id_indicator = a.id_indicator left join " +
                "ran_rad e on e.id_monper = a.id_monper left join " +
                "sdg_target f on f.id_target = a.id_target " +
                "where a.id_prov = :id_prov";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", id);
        List list = query.getResultList();
        return list;
    }

    // ****************** Report Grafik ******************
    @GetMapping("admin/report-graph")
    public String grafik(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());

        model.addAttribute("title", "Report Graphic");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/report/graph";
    }

}
