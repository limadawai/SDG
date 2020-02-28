package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class TahunMap implements Serializable {
    @Id
    private Integer tahun;

    public TahunMap(Integer tahun) {
        this.tahun = tahun;
    }

    public Integer getTahun() {
        return tahun;
    }

    public void setTahun(Integer tahun) {
        this.tahun = tahun;
    }

    
}
