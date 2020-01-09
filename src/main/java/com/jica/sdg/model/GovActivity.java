package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "gov_activity")
public class GovActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, length = 6)
    private String id_activity;
    
    @Column(nullable = false, length = 10)
    private String id_program;
    
    @Column(nullable = false, length = 4)
    private Integer id_role;
    
    @Column(nullable = false, length = 150)
    private String nm_activity;
    
    @Column(nullable = false, length = 6)
    private Integer created_by;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_created;
    
    @Column(nullable = false, length = 50)
    private String internal_code;

	public GovActivity() {
	}

	public GovActivity(String id_activity, String id_program, Integer id_role, String nm_activity, Integer created_by,
			Date date_created, String internal_code) {
		super();
		this.id_activity = id_activity;
		this.id_program = id_program;
		this.id_role = id_role;
		this.nm_activity = nm_activity;
		this.created_by = created_by;
		this.date_created = date_created;
		this.internal_code = internal_code;
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

        public String getInternal_code() {
            return internal_code;
        }

        public void setInternal_code(String internal_code) {
            this.internal_code = internal_code;
        }


	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}
