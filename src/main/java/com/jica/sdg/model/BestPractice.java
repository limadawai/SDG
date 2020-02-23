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
@Table(name = "best_practice")
public class BestPractice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(name = "id_program")
    private Integer id_program;
    
    @Column(name = "id_activity")
    private Integer id_activity;
    
    @Column(name = "id_indicator")
    private Integer id_indicator;
    
    @Column(name = "id_role")
    private Integer id_role;
    
    @Column(name = "location")
    private String location;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date time_activity;
    
    @Column(name = "background")
    private String background;
    
    @Column(name = "implementation_process")
    private String implementation_process;
    
    @Column(name = "challenges_learning")
    private String challenges_learning;
    
    @Column(name = "id_monper")
    private Integer id_monper;
    
    @Column(name = "year")
    private Integer year;

    public BestPractice() {
    }

	public BestPractice(Integer id, Integer id_program, Integer id_activity, Integer id_indicator, Integer id_role,
			String location, Date time_activity, String background, String implementation_process,
			String challenges_learning, Integer id_monper, Integer year) {
		super();
		this.id = id;
		this.id_program = id_program;
		this.id_activity = id_activity;
		this.id_indicator = id_indicator;
		this.id_role = id_role;
		this.location = location;
		this.time_activity = time_activity;
		this.background = background;
		this.implementation_process = implementation_process;
		this.challenges_learning = challenges_learning;
		this.id_monper = id_monper;
		this.year = year;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_program() {
		return id_program;
	}

	public void setId_program(Integer id_program) {
		this.id_program = id_program;
	}

	public Integer getId_activity() {
		return id_activity;
	}

	public void setId_activity(Integer id_activity) {
		this.id_activity = id_activity;
	}

	public Integer getId_indicator() {
		return id_indicator;
	}

	public void setId_indicator(Integer id_indicator) {
		this.id_indicator = id_indicator;
	}

	public Integer getId_role() {
		return id_role;
	}

	public void setId_role(Integer id_role) {
		this.id_role = id_role;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getTime_activity() {
		return time_activity;
	}

	public void setTime_activity(Date time_activity) {
		this.time_activity = time_activity;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getImplementation_process() {
		return implementation_process;
	}

	public void setImplementation_process(String implementation_process) {
		this.implementation_process = implementation_process;
	}

	public String getChallenges_learning() {
		return challenges_learning;
	}

	public void setChallenges_learning(String challenges_learning) {
		this.challenges_learning = challenges_learning;
	}

	public Integer getId_monper() {
		return id_monper;
	}

	public void setId_monper(Integer id_monper) {
		this.id_monper = id_monper;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
}
