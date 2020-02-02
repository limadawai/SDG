package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "gov_target")
public class GovTarget implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = false, length = 11)
    private Integer id_gov_indicator;
    
    @Column(nullable = false, length = 4)
    private Integer id_role;
    
    @Column(nullable = false, length = 4)
    private Integer year;
    
    @Column(nullable = false, length = 11)
    private Integer value;

	public GovTarget() {
	}

	public GovTarget(Integer id, Integer id_gov_indicator, Integer id_role, Integer year, Integer value) {
		super();
		this.id = id;
		this.id_gov_indicator = id_gov_indicator;
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

	public Integer getId_gov_indicator() {
		return id_gov_indicator;
	}

	public void setId_gov_indicator(Integer id_gov_indicator) {
		this.id_gov_indicator = id_gov_indicator;
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
