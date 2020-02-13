package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_indicator")
public class SdgIndicator implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
	@Column(nullable = false, length = 11)
	private int id;

    @Column(nullable = false, length = 12)
    private String id_indicator;
    
    @Column(nullable = false, length = 2)
    private String id_goals;
    
    @Column(nullable = false, length = 6)
    private String id_target;
    
    @Column(nullable = false)
    private String nm_indicator;

    @Column(nullable = false)
	private String nm_indicator_eng;
    
    @Column(nullable = false, length = 10)
    private String unit;
    
    @Column(nullable = false, length = 10)
    private String increment_decrement;

	public SdgIndicator() {
	}

	public SdgIndicator(int id, String id_indicator, String id_goals, String id_target, String nm_indicator, String nm_indicator_eng, String unit, String increment_decrement) {
		this.id = id;
		this.id_indicator = id_indicator;
		this.id_goals = id_goals;
		this.id_target = id_target;
		this.nm_indicator = nm_indicator;
		this.nm_indicator_eng = nm_indicator_eng;
		this.unit = unit;
		this.increment_decrement = increment_decrement;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getId_indicator() {
		return id_indicator;
	}

	public void setId_indicator(String id_indicator) {
		this.id_indicator = id_indicator;
	}

	public String getId_goals() {
		return id_goals;
	}

	public void setId_goals(String id_goals) {
		this.id_goals = id_goals;
	}

	public String getId_target() {
		return id_target;
	}

	public void setId_target(String id_target) {
		this.id_target = id_target;
	}

	public String getNm_indicator() {
		return nm_indicator;
	}

	public void setNm_indicator(String nm_indicator) {
		this.nm_indicator = nm_indicator;
	}

	public String getNm_indicator_eng() {
		return nm_indicator_eng;
	}

	public void setNm_indicator_eng(String nm_indicator_eng) {
		this.nm_indicator_eng = nm_indicator_eng;
	}

	public String getIncrement_decrement() {
		return increment_decrement;
	}

	public void setIncrement_decrement(String increment_decrement) {
		this.increment_decrement = increment_decrement;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
