package com.jica.sdg.controller;

import com.jica.sdg.model.Provinsi;
import com.jica.sdg.service.IProvinsiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AdministrasiController {

    @Autowired
    IProvinsiService provinsiService;

    //*********************** Dashboard ***********************
    @GetMapping("admin/manajemen/role")
    public String rolemanajemen(Model model) {
        model.addAttribute("title", "Role Manajemen");
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        return "admin/role_manajemen/manajemen_role";
    }

    @GetMapping("admin/role/provinsi")
    public @ResponseBody
    List<Provinsi> provinsi() {
        List<Provinsi> list = provinsiService.findAllProvinsi();
        return list;
    }

}
