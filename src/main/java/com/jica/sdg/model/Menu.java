package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "menu")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_menu;
    @Column(name = "menu_indo")
    private String menu_indo;
    @Column(name = "menu_eng")
    private String menu_eng;
    @Column(name = "submenu")
    private String submenu;
    @Column(name = "link")
    private String link;

    public Menu() {
    }

    public Menu(String menu_indo, String menu_eng, String submenu, String link) {
        this.menu_indo = menu_indo;
        this.menu_eng = menu_eng;
        this.submenu = submenu;
        this.link = link;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public String getMenu_indo() {
        return menu_indo;
    }

    public void setMenu_indo(String menu_indo) {
        this.menu_indo = menu_indo;
    }

    public String getMenu_eng() {
        return menu_eng;
    }

    public void setMenu_eng(String menu_eng) {
        this.menu_eng = menu_eng;
    }

    public String getSubmenu() {
        return submenu;
    }

    public void setSubmenu(String submenu) {
        this.submenu = submenu;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
