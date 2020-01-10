package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "nsa_map")
public class NsaMap implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = true, length = 3)
    private String id_prov;
    
    @Column(nullable = true, length = 11)
    private Integer id_monper;
    
    @Column(nullable = true, length = 2)
    private String id_goals;
    
    @Column(nullable = true, length = 2)
    private String id_target;
    
    @Column(nullable = true, length = 12)
    private String id_indicator;
    
    @Column(nullable = true, length = 12)
    private String id_nsa_indicator;
    
	public NsaMap() {
	}

	public NsaMap(Integer id, String id_prov, Integer id_monper, String id_goals, String id_target, String id_indicator, String id_nsa_indicator) {
		super();
		this.id = id;
		this.id_prov = id_prov;
		this.id_monper = id_monper;
		this.id_goals = id_goals;
		this.id_target = id_target;
		this.id_indicator = id_indicator;
		this.id_nsa_indicator = id_nsa_indicator;
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

    public String getId_nsa_indicator() {
        return id_nsa_indicator;
    }

    public void setId_nsa_indicator(String id_nsa_indicator) {
        this.id_nsa_indicator = id_nsa_indicator;
    }

        
}
