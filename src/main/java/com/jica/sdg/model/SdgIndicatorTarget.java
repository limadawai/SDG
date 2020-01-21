package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_indicator_target")
public class SdgIndicatorTarget implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;

    @Column(nullable = false, length = 11)
    private Integer id_sdg_indicator;

    @Column(nullable = false, length = 4)
    private Integer id_role;

    @Column(nullable = false, length = 4)
    private Integer year;

    @Column(nullable = false, length = 11)
    private Integer value;

    public SdgIndicatorTarget() {
    }

    public SdgIndicatorTarget(int id, Integer id_sdg_indicator, Integer id_role, Integer year, Integer value) {
            this.id = id;
            this.id_sdg_indicator = id_sdg_indicator;
            this.id_role = id_role;
            this.year = year;
            this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_sdg_indicator() {
        return id_sdg_indicator;
    }

    public void setId_sdg_indicator(Integer id_sdg_indicator) {
        this.id_sdg_indicator = id_sdg_indicator;
    }

    public Integer getId_role() {
        return id_role;
    }

    public void setId_role(Integer id_role) {
        this.id_role = id_role;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    
        
}
