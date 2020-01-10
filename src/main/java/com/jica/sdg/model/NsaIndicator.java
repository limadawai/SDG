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
    
    @Column(nullable = false, length = 6)
    private Integer created_by;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_created;
    
    @Column(nullable = true, length = 150)
    private String internal_code;

	public NsaIndicator() {
	}

	public NsaIndicator(String id_nsa_indicator, String id_program, String id_activity, String nm_indicator,
			String unit, Integer created_by, Date date_created, String internal_code) {
		super();
		this.id_nsa_indicator = id_nsa_indicator;
		this.id_program = id_program;
		this.id_activity = id_activity;
		this.nm_indicator = nm_indicator;
		this.unit = unit;
		this.created_by = created_by;
		this.date_created = date_created;
		this.internal_code = internal_code;
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

	public String getInternal_code() {
		return internal_code;
	}

	public void setInternal_code(String internal_code) {
		this.internal_code = internal_code;
	}
}
