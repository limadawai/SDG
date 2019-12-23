package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_target")
public class SdgTarget implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, length = 6)
    private String id_target;
    
    @Column(nullable = false, length = 2)
    private String id_goals;
    
    @Column(nullable = false, length = 150)
    private String nm_target;

	public SdgTarget() {
	}

	public SdgTarget(String id_target, String id_goals, String nm_target) {
		super();
		this.id_target = id_target;
		this.id_goals = id_goals;
		this.nm_target = nm_target;
	}

	public String getId_target() {
		return id_target;
	}

	public void setId_target(String id_target) {
		this.id_target = id_target;
	}

	public String getId_goals() {
		return id_goals;
	}

	public void setId_goals(String id_goals) {
		this.id_goals = id_goals;
	}

	public String getNm_target() {
		return nm_target;
	}

	public void setNm_target(String nm_target) {
		this.nm_target = nm_target;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
}
