package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "gov_map")
public class GovMap implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = true, length = 12)
    private String id_gov_indicator;
    
    @Column(nullable = true, length = 10)
    private String id_program;
    
    @Column(nullable = true, length = 6)
    private String id_activity;
    
    @Column(nullable = true, length = 12)
    private String id_indicator;
    
    @Column(nullable = true, length = 2)
    private String id_goals;
    
    @Column(nullable = true, length = 2)
    private String 	id_target;
    
	public GovMap() {
	}

	public GovMap(Integer id, String id_gov_indicator, String id_program, String id_activity, String id_indicator,
			String id_goals, String id_target) {
		super();
		this.id = id;
		this.id_gov_indicator = id_gov_indicator;
		this.id_program = id_program;
		this.id_activity = id_activity;
		this.id_indicator = id_indicator;
		this.id_goals = id_goals;
		this.id_target = id_target;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
