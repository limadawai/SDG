package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ref_role")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_role;
    @Column(name = "nm_role")
    private String nm_role;
    @Column(name = "desc_role")
    private String desc_role;
    @Column(name = "cat_role")
    private String cat_role;
    @Column(name = "status_role")
    private String status_role;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }

    public String getNm_role() {
        return nm_role;
    }

    public void setNm_role(String nm_role) {
        this.nm_role = nm_role;
    }

    public String getDesc_role() {
        return desc_role;
    }

    public void setDesc_role(String desc_role) {
        this.desc_role = desc_role;
    }

    public String getCat_role() {
        return cat_role;
    }

    public void setCat_role(String cat_role) {
        this.cat_role = cat_role;
    }

    public String getStatus_role() {
        return status_role;
    }

    public void setStatus_role(String status_role) {
        this.status_role = status_role;
    }
}
