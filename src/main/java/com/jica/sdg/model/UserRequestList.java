package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_request_list")
public class UserRequestList implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @Column(nullable = false, length = 15)
    private String level;
    
    @Column(nullable = false, length = 17)
    private String 	type;
    
    @Column(nullable = false, length = 20)
    private String req_type;
    
    @Column(nullable = false, length = 25)
    private String institution;
    
    @Column(nullable = false, length = 75)
    private String nm_inst;
    
    @Column(nullable = false, length = 50)
    private String contact;
    
    @Column(nullable = false, length = 15)
    private String status;

	public UserRequestList() {
	}

	public UserRequestList(Integer id, Date date, String level, String type, String req_type, String institution,
			String nm_inst, String contact, String status) {
		super();
		this.id = id;
		this.date = date;
		this.level = level;
		this.type = type;
		this.req_type = req_type;
		this.institution = institution;
		this.nm_inst = nm_inst;
		this.contact = contact;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReq_type() {
		return req_type;
	}

	public void setReq_type(String req_type) {
		this.req_type = req_type;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getNm_inst() {
		return nm_inst;
	}

	public void setNm_inst(String nm_inst) {
		this.nm_inst = nm_inst;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
