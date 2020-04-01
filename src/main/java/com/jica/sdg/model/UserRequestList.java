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
    
    @Column(nullable = true, length = 15)
    private String level;
    
    @Column(nullable = true, length = 3)
    private String id_prov;
    
    @Column(nullable = true, length = 17)
    private String 	type;
    
    @Column(nullable = true, length = 25)
    private String institution;
    
    @Column(nullable = true, length = 75)
    private String name;
    
    @Column(nullable = true, length = 50)
    private String contact;
    
    @Column(nullable = true, length = 15)
    private String status;
    
    @Column(nullable = true)
    private String detail;

	public UserRequestList() {
	}

	public UserRequestList(Integer id, Date date, String level, String type, String req_type, String institution,
			String name, String contact, String status, String detail, String id_prov) {
		super();
		this.id = id;
		this.date = date;
		this.level = level;
		this.type = type;
		this.institution = institution;
		this.name = name;
		this.contact = contact;
		this.status = status;
		this.detail = detail;
		this.id_prov = id_prov;
	}

	public String getId_prov() {
		return id_prov;
	}

	public void setId_prov(String id_prov) {
		this.id_prov = id_prov;
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

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }


}
