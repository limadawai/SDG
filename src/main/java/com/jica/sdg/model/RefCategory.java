package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ref_category")
public class RefCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id_cat;
    @Column(name = "nm_cat")
    private String nm_cat;
    
    public RefCategory(){
        
    }

    public RefCategory(String id_cat, String nm_cat) {
        this.id_cat = id_cat;
        this.nm_cat = nm_cat;
    }

    public String getId_cat() {
        return id_cat;
    }

    public void setId_cat(String id_cat) {
        this.id_cat = id_cat;
    }

    public String getNm_cat() {
        return nm_cat;
    }

    public void setNm_cat(String nm_cat) {
        this.nm_cat = nm_cat;
    }
    
   
   
    
}
