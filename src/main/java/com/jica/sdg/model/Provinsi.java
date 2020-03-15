package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ref_province")
public class Provinsi implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id_prov;
    @Column(name = "nm_prov")
    private String nm_prov;
    
    @Column(name = "acronym")
    private String acronym;

    public Provinsi() {
    }

	public Provinsi(String id_prov, String nm_prov, String acronym) {
		super();
		this.id_prov = id_prov;
		this.nm_prov = nm_prov;
		this.acronym = acronym;
	}

	public String getId_prov() {
		return id_prov;
	}

	public void setId_prov(String id_prov) {
		this.id_prov = id_prov;
	}

	public String getNm_prov() {
		return nm_prov;
	}

	public void setNm_prov(String nm_prov) {
		this.nm_prov = nm_prov;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

        public static long getSerialVersionUID() {
            return serialVersionUID;
        }
        
    
}
