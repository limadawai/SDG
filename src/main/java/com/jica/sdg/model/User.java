package com.jica.sdg.model;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="ref_user")
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id_user;
    @Column(name = "id_role")
    private Integer id_role;
    @Column(name = "username")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "enabled")
    private short enabled;
    @Column(name = "detail")
    private String detail;
    @Column(name = "email")
    private String email;
    
    public User(Integer id_user, Integer id_role, String userName, String password, String name, short enabled,
			String detail, String email) {
		super();
		this.id_user = id_user;
		this.id_role = id_role;
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.enabled = enabled;
		this.detail = detail;
		this.email = email;
	}

	public User() {
	}

	public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public Integer getId_role() {
        return id_role;
    }

    public void setId_role(Integer id_role) {
        this.id_role = id_role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getEnabled() {
        return enabled;
    }

    public void setEnabled(short enabled) {
        this.enabled = enabled;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}