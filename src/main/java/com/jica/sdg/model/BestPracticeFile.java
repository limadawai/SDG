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

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Lob;

@Entity
@Table(name = "best_practice_file")
public class BestPracticeFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(name = "id_best_practice")
    private Integer id_best_practice;
    
    @Lob
    @Column(nullable = true, columnDefinition="BLOB")
    private byte[] file;

    public BestPracticeFile() {
    }

    public BestPracticeFile(Integer id, Integer id_best_practice, byte[]file) {
        super();
        this.id = id;
        this.id_best_practice = id_best_practice;
        this.file = file;
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

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
    
    

}
