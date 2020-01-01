package com.jica.sdg.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "sdg_indicator")
public class EntrySdgIndicatorJoin implements Serializable {
//    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_indicator")
    private String id_indicator;
    @Column(name = "id_goals")
    private String id_goals;
    @Column(name = "id_target")
    private String id_target;
    @Column(name = "nm_indicator")
    private String nm_indicator;
    @Column(name = "unit")
    private String unit;
    
    @Column(name = "target_year1")
    private Integer target_year1;
    @Column(name = "target_year2")
    private Integer target_year2;
    @Column(name = "target_year3")
    private Integer target_year3;
    @Column(name = "target_year4")
    private Integer target_year4;
    @Column(name = "target_year5")
    private Integer target_year5;
    
    @Column(name = "baseline")
    private String baseline;
    @Column(name = "budsource")
    private String budsource;
    @Column(name = "increment_decrement")
    private String increment_decrement;
    @Column(name = "achievement1")
    private String achievement1;
    @Column(name = "approval")
    private String approval;
    
	public EntrySdgIndicatorJoin() {
	}

	public EntrySdgIndicatorJoin(String id_indicator, String id_goals, String id_target, String nm_indicator, String unit,
			Integer target_year1, Integer target_year2, Integer target_year3, Integer target_year4,
			Integer target_year5, String baseline, String budsource, String increment_decrement, String achievement1, String approval) {
//		super();
		this.id_indicator = id_indicator;
		this.id_goals = id_goals;
		this.id_target = id_target;
		this.nm_indicator = nm_indicator;
		this.unit = unit;
		this.target_year1 = target_year1;
		this.target_year2 = target_year2;
		this.target_year3 = target_year3;
		this.target_year4 = target_year4;
		this.target_year5 = target_year5;
		this.baseline = baseline;
		this.budsource = budsource;
		this.increment_decrement = increment_decrement;
		this.achievement1 = achievement1;
		this.approval = approval;
	}

	public String getId_indicator() {
		return id_indicator;
	}

	public void setId_indicator(String id_indicator) {
		this.id_indicator = id_indicator;
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

	public String getNm_indicator() {
		return nm_indicator;
	}

	public void setNm_indicator(String nm_indicator) {
		this.nm_indicator = nm_indicator;
	}

	public String getIncrement_decrement() {
		return increment_decrement;
	}

	public void setIncrement_decrement(String increment_decrement) {
		this.increment_decrement = increment_decrement;
	}

//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getTarget_year1() {
		return target_year1;
	}

	public void setTarget_year1(Integer target_year1) {
		this.target_year1 = target_year1;
	}

	public Integer getTarget_year2() {
		return target_year2;
	}

	public void setTarget_year2(Integer target_year2) {
		this.target_year2 = target_year2;
	}

	public Integer getTarget_year3() {
		return target_year3;
	}

	public void setTarget_year3(Integer target_year3) {
		this.target_year3 = target_year3;
	}

	public Integer getTarget_year4() {
		return target_year4;
	}

	public void setTarget_year4(Integer target_year4) {
		this.target_year4 = target_year4;
	}

	public Integer getTarget_year5() {
		return target_year5;
	}

	public void setTarget_year5(Integer target_year5) {
		this.target_year5 = target_year5;
	}

	public String getBaseline() {
		return baseline;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}
	
	public String getBudsource() {
		return budsource;
	}
	
	public void setBudsource(String budsource) {
		this.budsource = budsource;
	}

    public String getAchievement1() {
        return achievement1;
    }

    public void setAchievement1(String achievement1) {
        this.achievement1 = achievement1;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }
        
}
