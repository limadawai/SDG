package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_ranrad_disaggre_detail")
public class SdgDisaggreDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = false, length = 11)
    private Integer id_disaggre;
    
    @Column(nullable = false, length = 75)
    private String desc_disaggre;
    
    @Column(nullable = false, length = 75)
    private String desc_disaggre_eng;

	public SdgDisaggreDetail() {
	}

	public SdgDisaggreDetail(Integer id, Integer id_disaggre, String desc_disaggre, String desc_disaggre_eng) {
		super();
		this.id = id;
		this.id_disaggre = id_disaggre;
		this.desc_disaggre = desc_disaggre;
		this.desc_disaggre_eng = desc_disaggre_eng;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_disaggre() {
		return id_disaggre;
	}

	public void setId_disaggre(Integer id_disaggre) {
		this.id_disaggre = id_disaggre;
	}

	public String getDesc_disaggre() {
		return desc_disaggre;
	}

	public void setDesc_disaggre(String desc_disaggre) {
		this.desc_disaggre = desc_disaggre;
	}

	public String getDesc_disaggre_eng() {
		return desc_disaggre_eng;
	}

	public void setDesc_disaggre_eng(String desc_disaggre_eng) {
		this.desc_disaggre_eng = desc_disaggre_eng;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
