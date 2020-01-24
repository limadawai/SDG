package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sdg_funding")
public class SdgFunding implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;

    @Column(nullable = false, length = 11)
    private Integer id_sdg_indicator;

    @Column(nullable = false, length = 11)
    private Integer id_monper;

    @Column(name = "baseline")
    private String baseline;
    
    @Column(name = "funding_source")
    private String funding_source;

    public SdgFunding() {
    }

    public SdgFunding(int id, Integer id_sdg_indicator, Integer id_monper, String baseline, String funding_source) {
            this.id = id;
            this.id_sdg_indicator = id_sdg_indicator;
            this.id_monper = id_monper;
            this.baseline = baseline;
            this.funding_source = funding_source;
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

    public Integer getId_monper() {
        return id_monper;
    }

    public void setId_monper(Integer id_monper) {
        this.id_monper = id_monper;
    }

    public String getBaseline() {
        return baseline;
    }

    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    public String getFunding_source() {
        return funding_source;
    }

    public void setFunding_source(String funding_source) {
        this.funding_source = funding_source;
    }

    
        
}
