package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "submenu")
public class Submenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_submenu;
    @Column(name = "id_menu")
    private int id_menu;
    @Column(name = "submenu_indo")
    private String submenu_indo;
    @Column(name = "submenu_eng")
    private String submenu_eng;
    @Column(name = "submenu_link")
    private String submenu_link;

    public Submenu() {
    }

    public Submenu(int id_menu, String submenu_indo, String submenu_eng, String submenu_link) {
        this.id_menu = id_menu;
        this.submenu_indo = submenu_indo;
        this.submenu_eng = submenu_eng;
        this.submenu_link = submenu_link;
    }

    public int getId_submenu() {
        return id_submenu;
    }

    public void setId_submenu(int id_submenu) {
        this.id_submenu = id_submenu;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public String getSubmenu_indo() {
        return submenu_indo;
    }

    public void setSubmenu_indo(String submenu_indo) {
        this.submenu_indo = submenu_indo;
    }

    public String getSubmenu_eng() {
        return submenu_eng;
    }

    public void setSubmenu_eng(String submenu_eng) {
        this.submenu_eng = submenu_eng;
    }

    public String getSubmenu_link() {
        return submenu_link;
    }

    public void setSubmenu_link(String submenu_link) {
        this.submenu_link = submenu_link;
    }
}
