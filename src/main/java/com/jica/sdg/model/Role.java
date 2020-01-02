package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ref_role")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 4)
    private Integer id_role;
    
    @Column(name = "nm_role")
    private String nm_role;
    @Column(name = "desc_role")
    private String desc_role;
    @Column(name = "cat_role")
    private String cat_role;
    @Column(name = "status_role")
    private String status_role;
    @Column(name = "privilege")
    private String privilege;
    @Column(name = "menu")
    private String menu;
    @Column(name = "submenu")
    private String submenu;
    @Column(name = "id_prov")
    private String id_prov;

    public Role(Integer id_role, String nm_role, String desc_role, String cat_role, String status_role,
			String privilege, String menu, String submenu, String id_prov) {
		super();
		this.id_role = id_role;
		this.nm_role = nm_role;
		this.desc_role = desc_role;
		this.cat_role = cat_role;
		this.status_role = status_role;
		this.privilege = privilege;
		this.menu = menu;
		this.submenu = submenu;
		this.id_prov = id_prov;
	}

    public Role() {
	}
    
	public Integer getId_role() {
        return id_role;
    }

    public void setId_role(Integer id_role) {
        this.id_role = id_role;
    }

    public String getNm_role() {
        return nm_role;
    }

    public void setNm_role(String nm_role) {
        this.nm_role = nm_role;
    }

    public String getDesc_role() {
        return desc_role;
    }

    public void setDesc_role(String desc_role) {
        this.desc_role = desc_role;
    }

    public String getCat_role() {
        return cat_role;
    }

    public void setCat_role(String cat_role) {
        this.cat_role = cat_role;
    }

    public String getStatus_role() {
        return status_role;
    }

    public void setStatus_role(String status_role) {
        this.status_role = status_role;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getSubmenu() {
        return submenu;
    }

    public void setSubmenu(String submenu) {
        this.submenu = submenu;
    }

    public String getId_prov() {
        return id_prov;
    }

    public void setId_prov(String id_prov) {
        this.id_prov = id_prov;
    }

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
