package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_ranrad_disaggre")
public class SdgDisaggre implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, length = 16)
    private String id_disaggre;
    
    @Column(nullable = false, length = 12)
    private String id_indicator;
    
    @Column(nullable = false, length = 25)
    private String nm_disaggre;

	public SdgDisaggre() {
	}

	public SdgDisaggre(String id_disaggre, String id_indicator, String nm_disaggre) {
		super();
		this.id_disaggre = id_disaggre;
		this.id_indicator = id_indicator;
		this.nm_disaggre = nm_disaggre;
	}

	public String getId_disaggre() {
		return id_disaggre;
	}

	public void setId_disaggre(String id_disaggre) {
		this.id_disaggre = id_disaggre;
	}

	public String getId_indicator() {
		return id_indicator;
	}

	public void setId_indicator(String id_indicator) {
		this.id_indicator = id_indicator;
	}

	public String getNm_disaggre() {
		return nm_disaggre;
	}

	public void setNm_disaggre(String nm_disaggre) {
		this.nm_disaggre = nm_disaggre;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
