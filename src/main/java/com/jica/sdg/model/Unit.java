package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ref_unit")
public class Unit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id_unit;
    
    @Column(nullable = false, length = 16)
    private String nm_unit;

	public Unit(Integer id_unit, String nm_unit) {
		super();
		this.id_unit = id_unit;
		this.nm_unit = nm_unit;
	}

	public Unit() {
	}

	public Integer getId_unit() {
		return id_unit;
	}

	public void setId_unit(Integer id_unit) {
		this.id_unit = id_unit;
	}

	public String getNm_unit() {
		return nm_unit;
	}

	public void setNm_unit(String nm_unit) {
		this.nm_unit = nm_unit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
