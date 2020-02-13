package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_goals")
public class SdgGoals implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, length = 11)
    private int id;

    @Column(nullable = false, length = 3)
    private String id_goals;
    
    @Column(nullable = true)
    private String nm_goals;

	@Column(nullable = true)
	private String nm_goals_eng;

	public SdgGoals() {
	}

	public SdgGoals(int id, String id_goals, String nm_goals, String nm_goals_eng) {
		this.id = id;
		this.id_goals = id_goals;
		this.nm_goals = nm_goals;
		this.nm_goals_eng = nm_goals_eng;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getNm_goals_eng() {
		return nm_goals_eng;
	}

	public void setNm_goals_eng(String nm_goals_eng) {
		this.nm_goals_eng = nm_goals_eng;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
