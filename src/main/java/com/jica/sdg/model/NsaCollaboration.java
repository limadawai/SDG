package com.jica.sdg.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "nsa_collaboration")
public class NsaCollaboration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    @Column(name = "sector")
    private String sector;
    @Column(name = "id_program")
    private String id_program;
    @Column(name = "id_activity")
    private String id_activity;
    @Column(name = "id_nsa_profile")
    private String id_nsa_profile;
    @Column(name = "ex_benefit")
    private String ex_benefit;
    @Column(name = "type_support")
    private String type_support;
    @Column(name = "id_philanthropy")
    private String id_philanthropy;

    public NsaCollaboration() {
    }

    public NsaCollaboration(Integer id, String sector, String id_program, String id_activity, String id_nsa_profile, String ex_benefit, String type_support, String id_philanthropy) {
        this.id             = id;
        this.sector         = sector;
        this.id_program     = id_program;
        this.id_activity    = id_activity;
        this.id_nsa_profile = id_nsa_profile;
        this.ex_benefit     = ex_benefit;
        this.type_support   = type_support;
        this.id_philanthropy = id_philanthropy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getId_program() {
        return id_program;
    }

    public void setId_program(String id_program) {
        this.id_program = id_program;
    }

    public String getId_activity() {
        return id_activity;
    }

    public void setId_activity(String id_activity) {
        this.id_activity = id_activity;
    }

    public String getId_nsa_profile() {
        return id_nsa_profile;
    }

    public void setId_nsa_profile(String id_nsa_profile) {
        this.id_nsa_profile = id_nsa_profile;
    }

    public String getEx_benefit() {
        return ex_benefit;
    }

    public void setEx_benefit(String ex_benefit) {
        this.ex_benefit = ex_benefit;
    }

    public String getType_support() {
        return type_support;
    }

    public void setType_support(String type_support) {
        this.type_support = type_support;
    }

    public String getId_philanthropy() {
        return id_philanthropy;
    }

    public void setId_philanthropy(String id_philanthropy) {
        this.id_philanthropy = id_philanthropy;
    }

    
    
}
