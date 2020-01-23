package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_ranrad_disaggre")
public class SdgDisaggre implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = false, length = 16)
    private String id_disaggre;
    
    @Column(nullable = false, length = 11)
    private Integer id_indicator;
    
    @Column(nullable = false, length = 25)
    private String nm_disaggre;
    
    @Column(nullable = false, length = 25)
    private String nm_disaggre_eng;

	public SdgDisaggre() {
	}

	public SdgDisaggre(Integer id, String id_disaggre, Integer id_indicator, String nm_disaggre,
			String nm_disaggre_eng) {
		super();
		this.id = id;
		this.id_disaggre = id_disaggre;
		this.id_indicator = id_indicator;
		this.nm_disaggre = nm_disaggre;
		this.nm_disaggre_eng = nm_disaggre_eng;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getId_disaggre() {
		return id_disaggre;
	}

	public void setId_disaggre(String id_disaggre) {
		this.id_disaggre = id_disaggre;
	}

	public Integer getId_indicator() {
		return id_indicator;
	}

	public void setId_indicator(Integer id_indicator) {
		this.id_indicator = id_indicator;
	}

	public String getNm_disaggre() {
		return nm_disaggre;
	}

	public void setNm_disaggre(String nm_disaggre) {
		this.nm_disaggre = nm_disaggre;
	}

	public String getNm_disaggre_eng() {
		return nm_disaggre_eng;
	}

	public void setNm_disaggre_eng(String nm_disaggre_eng) {
		this.nm_disaggre_eng = nm_disaggre_eng;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
