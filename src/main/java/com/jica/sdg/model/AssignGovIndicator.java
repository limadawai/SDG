package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "assign_gov_indicator")
public class AssignGovIndicator implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = false, length = 10)
    private String id_program;
    
    @Column(nullable = false, length = 6)
    private String id_activity;
    
    @Column(nullable = false, length = 12)
    private String id_gov_indicator;
    
    @Column(nullable = false, length = 4)
    private Integer id_role;
    
    @Column(nullable = false, length = 15)
    private String id_monper;
    

	public AssignGovIndicator() {
	}

	public AssignGovIndicator(Integer id, String id_program, String id_activity, String id_gov_indicator,
			Integer id_role, String id_monper) {
		super();
		this.id = id;
		this.id_program = id_program;
		this.id_activity = id_activity;
		this.id_gov_indicator = id_gov_indicator;
		this.id_role = id_role;
		this.id_monper = id_monper;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getId_gov_indicator() {
		return id_gov_indicator;
	}

	public void setId_gov_indicator(String id_gov_indicator) {
		this.id_gov_indicator = id_gov_indicator;
	}

	public Integer getId_role() {
		return id_role;
	}

	public void setId_role(Integer id_role) {
		this.id_role = id_role;
	}

	public String getId_monper() {
		return id_monper;
	}

	public void setId_monper(String id_monper) {
		this.id_monper = id_monper;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
