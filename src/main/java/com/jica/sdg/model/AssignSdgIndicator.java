package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "assign_sdg_indicator")
public class AssignSdgIndicator implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = false, length = 3)
    private String id_goals;
    
    @Column(nullable = false, length = 6)
    private String id_target;
    
    @Column(nullable = false, length = 12)
    private String id_indicator;
    
    @Column(nullable = false, length = 4)
    private Integer id_role;
    
    @Column(nullable = false, length = 15)
    private String id_monper;
    
    @Column(nullable = false, length = 3)
    private String 	id_prov;

	public AssignSdgIndicator(Integer id, String id_goals, String id_target, String id_indicator, Integer id_role,
			String id_monper, String id_prov) {
		super();
		this.id = id;
		this.id_goals = id_goals;
		this.id_target = id_target;
		this.id_indicator = id_indicator;
		this.id_role = id_role;
		this.id_monper = id_monper;
		this.id_prov = id_prov;
	}

	public AssignSdgIndicator() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getId_indicator() {
		return id_indicator;
	}

	public void setId_indicator(String id_indicator) {
		this.id_indicator = id_indicator;
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

	public String getId_prov() {
		return id_prov;
	}

	public void setId_prov(String id_prov) {
		this.id_prov = id_prov;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}    
}
