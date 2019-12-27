package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_indicator")
public class SdgIndicator implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, length = 12)
    private String id_indicator;
    
    @Column(nullable = false, length = 2)
    private String id_goals;
    
    @Column(nullable = false, length = 6)
    private String id_target;
    
    @Column(nullable = false, length = 125)
    private String nm_indicator;
    
    @Column(nullable = false, length = 10)
    private String unit;
    
    @Column(nullable = true, length = 11)
    private Integer target_year1;
    
    @Column(nullable = true, length = 11)
    private Integer target_year2;
    
    @Column(nullable = true, length = 11)
    private Integer target_year3;
    
    @Column(nullable = true, length = 11)
    private Integer target_year4;
    
    @Column(nullable = true, length = 11)
    private Integer target_year5;
    
    @Column(nullable = false, length = 100)
    private String baseline;
    
    @Column(nullable = false, length = 100)
    private String budsource;
    
    @Column(nullable = false, length = 10)
    private String increment_decrement;

	public SdgIndicator() {
	}

	public SdgIndicator(String id_indicator, String id_goals, String id_target, String nm_indicator, String unit,
			Integer target_year1, Integer target_year2, Integer target_year3, Integer target_year4,
			Integer target_year5, String baseline, String budsource, String increment_decrement) {
		super();
		this.id_indicator = id_indicator;
		this.id_goals = id_goals;
		this.id_target = id_target;
		this.nm_indicator = nm_indicator;
		this.unit = unit;
		this.target_year1 = target_year1;
		this.target_year2 = target_year2;
		this.target_year3 = target_year3;
		this.target_year4 = target_year4;
		this.target_year5 = target_year5;
		this.baseline = baseline;
		this.budsource = budsource;
		this.increment_decrement = increment_decrement;
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

	public Integer getTarget_year1() {
		return target_year1;
	}

	public void setTarget_year1(Integer target_year1) {
		this.target_year1 = target_year1;
	}

	public Integer getTarget_year2() {
		return target_year2;
	}

	public void setTarget_year2(Integer target_year2) {
		this.target_year2 = target_year2;
	}

	public Integer getTarget_year3() {
		return target_year3;
	}

	public void setTarget_year3(Integer target_year3) {
		this.target_year3 = target_year3;
	}

	public Integer getTarget_year4() {
		return target_year4;
	}

	public void setTarget_year4(Integer target_year4) {
		this.target_year4 = target_year4;
	}

	public Integer getTarget_year5() {
		return target_year5;
	}

	public void setTarget_year5(Integer target_year5) {
		this.target_year5 = target_year5;
	}

	public String getBaseline() {
		return baseline;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}
	
	public String getBudsource() {
		return budsource;
	}
	
	public void setBudsource(String budsource) {
		this.budsource = budsource;
	}
}
