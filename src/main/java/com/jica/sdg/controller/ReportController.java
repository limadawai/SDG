package com.jica.sdg.controller;

import com.jica.sdg.model.GovMap;
import com.jica.sdg.model.Role;
import com.jica.sdg.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    ProvinsiService provinsiService;
    @Autowired
    GovMapService govMapService;

    // ****************** Report Hasil Monitoring ******************
    @GetMapping("admin/report-monitoring")
    public String monitoring(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());

        model.addAttribute("title", "SDG Problem Identification & Follow Up");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/report/monitoring";
    }

    @GetMapping("admin/getroleprov")
    public @ResponseBody List<GovMap> getGovMapById(@Param("id_prov") String id) {
        List govmap = (List<GovMap>) govMapService.getAllByIdProv(id);
        return govmap;
    }

}
