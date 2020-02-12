package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_target")
public class SdgTarget implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
	@Column(nullable = false, length = 11)
	private int id;

    @Column(nullable = false, length = 6)
    private String id_target;
    
    @Column(nullable = false, length = 2)
    private String id_goals;

	@Column(nullable = false)
	private String nm_target;

	@Column(nullable = false)
	private String nm_target_eng;

	public SdgTarget() {
	}

	public SdgTarget(int id, String id_target, String id_goals, String nm_target, String nm_target_eng) {
		this.id = id;
		this.id_target = id_target;
		this.id_goals = id_goals;
		this.nm_target = nm_target;
		this.nm_target_eng = nm_target_eng;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getNm_target_eng() {
		return nm_target_eng;
	}

	public void setNm_target_eng(String nm_target_eng) {
		this.nm_target_eng = nm_target_eng;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
}
