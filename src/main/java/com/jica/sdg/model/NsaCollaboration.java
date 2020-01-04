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
    @Column(name = "location")
    private String location;
    @Column(name = "beneficiaries")
    private String beneficiaries;
    @Column(name = "ex_benefit")
    private String ex_benefit;
    @Column(name = "type_support")
    private String type_support;
    @Column(name = "id_philanthropy")
    private Integer id_philanthropy;

    public NsaCollaboration() {
    }

    public NsaCollaboration(Integer id, String sector, String id_program, String location, String beneficiaries, String ex_benefit, String type_support, Integer id_philanthropy) {
        this.id             = id;
        this.sector         = sector;
        this.id_program     = id_program;
        this.location       = location;
        this.beneficiaries  = beneficiaries;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(String beneficiaries) {
        this.beneficiaries = beneficiaries;
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

    public Integer getId_philanthropy() {
        return id_philanthropy;
    }

    public void setId_philanthropy(Integer id_philanthropy) {
        this.id_philanthropy = id_philanthropy;
    }

    
    
}
