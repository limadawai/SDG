package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "nsa_indicator")
public class NsaIndicator implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, length = 12)
    private String id_nsa_indicator;
    
    @Column(nullable = false, length = 10)
    private String id_program;
    
    @Column(nullable = false, length = 6)
    private String id_activity;
    
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
    
    @Column(nullable = false, length = 6)
    private Integer created_by;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_created;

	public NsaIndicator() {
	}

	public NsaIndicator(String id_nsa_indicator, String id_program, String id_activity, String nm_indicator,
			String unit, Integer target_year1, Integer target_year2, Integer target_year3, Integer target_year4,
			Integer target_year5, String baseline, String budsource, Integer created_by, Date date_created) {
		super();
		this.id_nsa_indicator = id_nsa_indicator;
		this.id_program = id_program;
		this.id_activity = id_activity;
		this.nm_indicator = nm_indicator;
		this.unit = unit;
		this.target_year1 = target_year1;
		this.target_year2 = target_year2;
		this.target_year3 = target_year3;
		this.target_year4 = target_year4;
		this.target_year5 = target_year5;
		this.baseline = baseline;
		this.budsource = budsource;
		this.created_by = created_by;
		this.date_created = date_created;
	}

	public String getId_nsa_indicator() {
		return id_nsa_indicator;
	}

	public void setId_nsa_indicator(String id_nsa_indicator) {
		this.id_nsa_indicator = id_nsa_indicator;
	}

	public String getId_program() {
		return id_program;
	}

	public void setId_program(String id_program) {
		this.id_program = id_program;
	}

	public String getId_activity() {
		return id_activity;
	}

	public void setId_activity(String id_activity) {
		this.id_activity = id_activity;
	}

	public String getNm_indicator() {
		return nm_indicator;
	}

	public void setNm_indicator(String nm_indicator) {
		this.nm_indicator = nm_indicator;
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

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Date getDate_created() {
		return date_created;
	}

	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
