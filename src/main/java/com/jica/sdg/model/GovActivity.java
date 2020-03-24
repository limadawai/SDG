package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "gov_activity")
public class GovActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = false, length = 6)
    private String id_activity;
    
    @Column(nullable = false, length = 10)
    private String id_program;
    
    @Column(nullable = false, length = 4)
    private Integer id_role;
    
    @Column(nullable = true, length = 150)
    private String nm_activity;
    
    @Column(nullable = true, length = 150)
    private String nm_activity_eng;
    
    @Column(nullable = false, length = 6)
    private Integer created_by;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_created;
    
    @Column(nullable = true, length = 11)
    private Integer internal_code;
    
    @Column(nullable = true, length = 11)
    private Integer budget_allocation;

	public GovActivity() {
	}

	public GovActivity(Integer id, String id_activity, String id_program, Integer id_role, String nm_activity,
			String nm_activity_eng, Integer created_by, Date date_created, Integer internal_code, Integer budget_allocation) {
		super();
		this.id = id;
		this.id_activity = id_activity;
		this.id_program = id_program;
		this.id_role = id_role;
		this.nm_activity = nm_activity;
		this.nm_activity_eng = nm_activity_eng;
		this.created_by = created_by;
		this.date_created = date_created;
		this.internal_code = internal_code;
		this.budget_allocation = budget_allocation;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getId_activity() {
		return id_activity;
	}

	public void setId_activity(String id_activity) {
		this.id_activity = id_activity;
	}

	public String getId_program() {
		return id_program;
	}

	public void setId_program(String id_program) {
		this.id_program = id_program;
	}

	public Integer getId_role() {
		return id_role;
	}

	public void setId_role(Integer id_role) {
		this.id_role = id_role;
	}

	public String getNm_activity() {
		return nm_activity;
	}

	public void setNm_activity(String nm_activity) {
		this.nm_activity = nm_activity;
	}

	public String getNm_activity_eng() {
		return nm_activity_eng;
	}

	public void setNm_activity_eng(String nm_activity_eng) {
		this.nm_activity_eng = nm_activity_eng;
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

	public Integer getBudget_allocation() {
		return budget_allocation;
	}

	public void setBudget_allocation(Integer budget_allocation) {
		this.budget_allocation = budget_allocation;
	}
}
