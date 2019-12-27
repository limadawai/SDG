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
    
    @Column(nullable = false, length = 16)
    private String id_disaggre;
    
    @Column(nullable = false, length = 75)
    private String desc_disaggre;

	public SdgDisaggreDetail() {
	}

	public SdgDisaggreDetail(Integer id, String id_disaggre, String desc_disaggre) {
		super();
		this.id = id;
		this.id_disaggre = id_disaggre;
		this.desc_disaggre = desc_disaggre;
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

	public String getDesc_disaggre() {
		return desc_disaggre;
	}

	public void setDesc_disaggre(String desc_disaggre) {
		this.desc_disaggre = desc_disaggre;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
