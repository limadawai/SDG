package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_goals")
public class SdgGoals implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, length = 3)
    private String id_goals;
    
    @Column(nullable = false, length = 150)
    private String nm_goals;

	public SdgGoals() {
	}

	public SdgGoals(String id_goals, String nm_goals, Integer periode) {
		this.id_goals = id_goals;
		this.nm_goals = nm_goals;
	}

	public String getId_goals() {
		return id_goals;
	}

	public void setId_goals(String id_goals) {
		this.id_goals = id_goals;
	}

	public String getNm_goals() {
		return nm_goals;
	}

	public void setNm_goals(String nm_goals) {
		this.nm_goals = nm_goals;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
