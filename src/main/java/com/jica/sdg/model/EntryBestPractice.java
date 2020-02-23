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
@Table(name = "entry_best_practice")
public class EntryBestPractice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(name = "id_best_practice")
    private Integer id_best_practice;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "org_name")
    private String org_name;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "activity_periode")
    private String activity_periode;
    
    @Column(name = "partner")
    private String partner;
    
    @Column(name = "beneficiaries")
    private String beneficiaries;
    
    @Column(name = "story")
    private String story;
    
    @Column(name = "add_info")
    private String add_info;

    @Column(name = "created_by")
    private Integer created_by;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_created;
    
    public EntryBestPractice() {
    }

	public EntryBestPractice(Integer id, Integer id_best_practice, String title, String org_name, String location,
			String activity_periode, String partner, String beneficiaries, String story, String add_info,
			Integer created_by, Date date_created) {
		super();
		this.id = id;
		this.id_best_practice = id_best_practice;
		this.title = title;
		this.org_name = org_name;
		this.location = location;
		this.activity_periode = activity_periode;
		this.partner = partner;
		this.beneficiaries = beneficiaries;
		this.story = story;
		this.add_info = add_info;
		this.created_by = created_by;
		this.date_created = date_created;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_best_practice() {
		return id_best_practice;
	}

	public void setId_best_practice(Integer id_best_practice) {
		this.id_best_practice = id_best_practice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getActivity_periode() {
		return activity_periode;
	}

	public void setActivity_periode(String activity_periode) {
		this.activity_periode = activity_periode;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(String beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public String getAdd_info() {
		return add_info;
	}

	public void setAdd_info(String add_info) {
		this.add_info = add_info;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Date getDate_created() {
		return date_created;
	}

	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}
}
