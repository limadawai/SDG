package com.jica.sdg.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "philanthropy_collaboration")
public class PhilanthropyCollaboration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id_philanthropy;
    @Column(name = "type_support")
    private String type_support;
    @Column(name = "nm_philanthropy")
    private String nm_philanthropy;
    @Column(name = "nm_pillar")
    private String nm_pillar;
    @Column(name = "loc_philanthropy")
    private String loc_philanthropy;
    @Column(name = "id_inst")
    private Integer id_inst;

    public PhilanthropyCollaboration() {
    }

    public PhilanthropyCollaboration(Integer id_philanthropy, Integer id_inst, String type_support, String nm_philanthropy, String nm_pillar, String loc_philanthropy) {
        this.id_philanthropy    = id_philanthropy;
        this.type_support       = type_support;
        this.nm_philanthropy    = nm_philanthropy;
        this.nm_pillar          = nm_pillar;
        this.loc_philanthropy   = loc_philanthropy;
        this.id_inst   = id_inst;
    }

    public Integer getId_inst() {
        return id_inst;
    }

    public void setId_inst(Integer id_inst) {
        this.id_inst = id_inst;
    }
    
    

    public Integer getId_philanthropy() {
        return id_philanthropy;
    }

    public void setId_philanthropy(Integer id_philanthropy) {
        this.id_philanthropy = id_philanthropy;
    }

    public String getType_support() {
        return type_support;
    }

    public void setType_support(String type_support) {
        this.type_support = type_support;
    }

    public String getNm_philanthropy() {
        return nm_philanthropy;
    }

    public void setNm_philanthropy(String nm_philanthropy) {
        this.nm_philanthropy = nm_philanthropy;
    }

    public String getNm_pillar() {
        return nm_pillar;
    }

    public void setNm_pillar(String nm_pillar) {
        this.nm_pillar = nm_pillar;
    }

    public String getLoc_philanthropy() {
        return loc_philanthropy;
    }

    public void setLoc_philanthropy(String loc_philanthropy) {
        this.loc_philanthropy = loc_philanthropy;
    }

    
}
