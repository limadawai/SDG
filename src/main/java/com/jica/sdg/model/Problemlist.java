package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;


public class Problemlist  {
    private Integer id;
    private String id_cat;
    private String nm_cat;
    private String problem;
    private String follow_up;
    private String approval;
    
    public Problemlist(){
        
    }

    public Problemlist(Integer id, String id_cat, String nm_cat, String problem, String follow_up,String approval) {
        this.id = id;
        this.id_cat = id_cat;
        this.nm_cat = nm_cat;
        this.problem = problem;
        this.follow_up = follow_up;
        this.approval = approval;
    }
    
    public Problemlist(Integer id, String id_cat, String nm_cat, String problem, String follow_up) {
        this.id = id;
        this.id_cat = id_cat;
        this.nm_cat = nm_cat;
        this.problem = problem;
        this.follow_up = follow_up;
    }
    
    public Problemlist(Integer id, String id_cat, String nm_cat, String problem) {
        this.id = id;
        this.id_cat = id_cat;
        this.nm_cat = nm_cat;
        this.problem = problem;
    }
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getFollow_up() {
        return follow_up;
    }

    public void setFollow_up(String follow_up) {
        this.follow_up = follow_up;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }
    
    
    
}
