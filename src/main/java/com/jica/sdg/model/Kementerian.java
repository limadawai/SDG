package com.jica.sdg.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ref_kl")
public class Kementerian implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id_kl;
    @Column(name = "nm_kl")
    private String nm_kl;

    public Kementerian() {
    }

    public Kementerian(String id_kl, String nm_kl) {
        this.id_kl = id_kl;
        this.nm_kl = nm_kl;
    }

    public String getId_kl() {
        return id_kl;
    }

    public void setId_kl(String id_kl) {
        this.id_kl = id_kl;
    }

    public String getNm_kl() {
        return nm_kl;
    }

    public void setNm_kl(String nm_kl) {
        this.nm_kl = nm_kl;
    }
}
