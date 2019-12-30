package com.jica.sdg.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "nsa_profile")
public class Nsaprofile implements Serializable {

    @Id
    private String id_nsa;
    @Column(name = "nm_nsa")
    private String nm_nsa;
    @Column(name = "achieve_nsa")
    private String achieve_nsa;
    @Column(name = "loc_nsa")
    private String loc_nsa;
    @Column(name = "beneficiaries")
    private String beneficiaries;
    @Column(name = "year_impl")
    private int year_impl;
    @Column(name = "major_part")
    private String major_part;

    public Nsaprofile() {
    }

    public Nsaprofile(String id_nsa, String nm_nsa, String achieve_nsa, String loc_nsa, String beneficiaries, Integer year_impl, String major_part) {
        this.id_nsa         = id_nsa;
        this.nm_nsa         = nm_nsa;
        this.achieve_nsa    = achieve_nsa;
        this.loc_nsa        = loc_nsa;
        this.beneficiaries  = beneficiaries;
        this.year_impl      = year_impl;
        this.major_part     = major_part;
    }

    public String getId_nsa() {
        return id_nsa;
    }

    public void setId_nsa(String id_nsa) {
        this.id_nsa = id_nsa;
    }

    public String getNm_nsa() {
        return nm_nsa;
    }

    public void setNm_nsa(String nm_nsa) {
        this.nm_nsa = nm_nsa;
    }

    public String getAchieve_nsa() {
        return achieve_nsa;
    }

    public void setAchieve_nsa(String achieve_nsa) {
        this.achieve_nsa = achieve_nsa;
    }

    public String getLoc_nsa() {
        return loc_nsa;
    }

    public void setLoc_nsa(String loc_nsa) {
        this.loc_nsa = loc_nsa;
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
