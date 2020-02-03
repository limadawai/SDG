package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "nsa_target")
public class NsaTarget implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = false, length = 11)
    private Integer id_nsa_indicator;
    
    @Column(nullable = false, length = 4)
    private Integer id_role;
    
    @Column(nullable = false, length = 4)
    private Integer year;
    
    @Column(nullable = false, length = 11)
    private Integer value;

	public NsaTarget() {
	}

	public NsaTarget(Integer id, Integer id_nsa_indicator, Integer id_role, Integer year, Integer value) {
		super();
		this.id = id;
		this.id_nsa_indicator = id_nsa_indicator;
		this.id_role = id_role;
		this.year = year;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_nsa_indicator() {
		return id_nsa_indicator;
	}

	public void setId_nsa_indicator(Integer id_nsa_indicator) {
		this.id_nsa_indicator = id_nsa_indicator;
	}

	public Integer getId_role() {
		return id_role;
	}

	public void setId_role(Integer id_role) {
		this.id_role = id_role;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
