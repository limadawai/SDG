package com.jica.sdg.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "nsa_inst")
public class Insprofile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id_inst;
    @Column(name = "id_role")
    private Integer id_role;
    @Column(name = "nm_inst")
    private String nm_inst;
    @Column(name = "achievement")
    private String achievement;
    @Column(name = "loc_inst")
    private String loc_inst;
    @Column(name = "beneficiaries")
    private String beneficiaries;
    @Column(name = "year_impl")
    private int year_impl;
    @Column(name = "major_part")
    private String major_part;

    public Insprofile() {
    }

    public Insprofile(Integer id_inst, Integer id_role, String nm_inst, String achievement, String loc_inst, String beneficiaries, Integer year_impl, String major_part ) {
        this.id_inst        = id_inst;
        this.id_role        = id_role;
        this.nm_inst        = nm_inst;
        this.achievement    = achievement;
        this.loc_inst       = loc_inst;
        this.beneficiaries  = beneficiaries;
        this.year_impl      = year_impl;
        this.major_part     = major_part;
    }

    public Integer getId_role() {
        return id_role;
    }

    public void setId_role(Integer id_role) {
        this.id_role = id_role;
    }

    
    public Integer getId_inst() {
        return id_inst;
    }

    public void setId_inst(Integer id_inst) {
        this.id_inst = id_inst;
    }

    public String getNm_inst() {
        return nm_inst;
    }

    public void setNm_inst(String nm_inst) {
        this.nm_inst = nm_inst;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getLoc_inst() {
        return loc_inst;
    }

    public void setLoc_inst(String loc_inst) {
        this.loc_inst = loc_inst;
    }

    public String getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(String beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public int getYear_impl() {
        return year_impl;
    }

    public void setYear_impl(int year_impl) {
        this.year_impl = year_impl;
    }

    public String getMajor_part() {
        return major_part;
    }

    public void setMajor_part(String major_part) {
        this.major_part = major_part;
    }

    
}
