package com.jica.sdg.model;

import javax.persistence.*;

@Entity
@Table(name = "ref_user")
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id_user;
    private String username;
    private String password;
    private String nama;
    private String level;
    private String id_inst;

    public UserDetail() {
    }

    public UserDetail(String username, String password, String nama, String level, String id_inst) {
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.level = level;
        this.id_inst = id_inst;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getId_inst() {
        return id_inst;
    }

    public void setId_inst(String id_inst) {
        this.id_inst = id_inst;
    }
}
