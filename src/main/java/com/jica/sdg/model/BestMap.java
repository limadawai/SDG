package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "best_map")
public class BestMap implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = true, length = 3)
    private String id_prov;
    
    @Column(nullable = true, length = 11)
    private Integer id_monper;
    
    @Column(nullable = true, length = 11)
    private Integer id_goals;
    
    @Column(nullable = true, length = 11)
    private Integer id_target;
    
    @Column(nullable = true, length = 11)
    private Integer id_indicator;
    
    @Column(nullable = true, length = 11)
    private Integer id_best_practice;
    
	public BestMap() {
	}

	public BestMap(Integer id, String id_prov, Integer id_monper, Integer id_goals, Integer id_target,
			Integer id_indicator, Integer id_best_practice) {
		super();
		this.id = id;
		this.id_prov = id_prov;
		this.id_monper = id_monper;
		this.id_goals = id_goals;
		this.id_target = id_target;
		this.id_indicator = id_indicator;
		this.id_best_practice = id_best_practice;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getId_prov() {
		return id_prov;
	}

	public void setId_prov(String id_prov) {
		this.id_prov = id_prov;
	}

	public Integer getId_monper() {
		return id_monper;
	}

	public void setId_monper(Integer id_monper) {
		this.id_monper = id_monper;
	}

	public Integer getId_goals() {
		return id_goals;
	}

	public void setId_goals(Integer id_goals) {
		this.id_goals = id_goals;
	}

	public Integer getId_target() {
		return id_target;
	}

	public void setId_target(Integer id_target) {
		this.id_target = id_target;
	}

	public Integer getId_indicator() {
		return id_indicator;
	}

	public void setId_indicator(Integer id_indicator) {
		this.id_indicator = id_indicator;
	}

    public Integer getId_best_practice() {
        return id_best_practice;
    }

    public void setId_best_practice(Integer id_best_practice) {
        this.id_best_practice = id_best_practice;
    }

        

	public static long getSerialversionuid() {
		return serialVersionUID;
	}        
}
