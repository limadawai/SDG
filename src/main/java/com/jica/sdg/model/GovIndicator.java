package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "gov_indicator")
public class GovIndicator implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = false, length = 12)
    private String id_gov_indicator;
    
    @Column(nullable = false, length = 10)
    private String id_program;
    
    @Column(nullable = false, length = 6)
    private String id_activity;
    
    @Column(nullable = true, length = 125)
    private String nm_indicator;
    
    @Column(nullable = true, length = 125)
    private String nm_indicator_eng;
    
    @Column(nullable = false, length = 10)
    private String unit;
    
    @Column(nullable = false, length = 6)
    private Integer created_by;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_created;
    
    @Column(nullable = true, length = 11)
    private Integer internal_code;

	public GovIndicator() {
	}

	public GovIndicator(Integer id, String id_gov_indicator, String id_program, String id_activity, String nm_indicator,
			String nm_indicator_eng, String unit, Integer created_by, Date date_created, Integer internal_code) {
		super();
		this.id = id;
		this.id_gov_indicator = id_gov_indicator;
		this.id_program = id_program;
		this.id_activity = id_activity;
		this.nm_indicator = nm_indicator;
		this.nm_indicator_eng = nm_indicator_eng;
		this.unit = unit;
		this.created_by = created_by;
		this.date_created = date_created;
		this.internal_code = internal_code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getId_gov_indicator() {
		return id_gov_indicator;
	}

	public void setId_gov_indicator(String id_gov_indicator) {
		this.id_gov_indicator = id_gov_indicator;
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

	public String getNm_indicator_eng() {
		return nm_indicator_eng;
	}

	public void setNm_indicator_eng(String nm_indicator_eng) {
		this.nm_indicator_eng = nm_indicator_eng;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public Integer getInternal_code() {
		return internal_code;
	}

	public void setInternal_code(Integer internal_code) {
		this.internal_code = internal_code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
