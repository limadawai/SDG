package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ref_unit")
public class Unit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id_unit;
    
    @Column(nullable = false, length = 16)
    private String nm_unit;
    
    @Column(nullable = false, length = 11)
    private Integer id_role;
    
    @Column(nullable = true, length = 1)
    private Integer calculation;
    
	public Unit(Integer id_unit, String nm_unit,Integer id_role, Integer calculation) {
		super();
		this.id_unit = id_unit;
		this.nm_unit = nm_unit;
                this.id_role = id_role;
                this.calculation = calculation;
	}

	public Unit() {
	}

	public Integer getId_unit() {
		return id_unit;
	}

	public void setId_unit(Integer id_unit) {
		this.id_unit = id_unit;
	}

	public String getNm_unit() {
		return nm_unit;
	}

	public void setNm_unit(String nm_unit) {
		this.nm_unit = nm_unit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

        public Integer getId_role() {
            return id_role;
        }

        public void setId_role(Integer id_role) {
            this.id_role = id_role;
        }

		public Integer getCalculation() {
			return calculation;
		}

		public void setCalculation(Integer calculation) {
			this.calculation = calculation;
		}   
}
