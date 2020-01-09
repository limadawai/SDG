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

@Entity
@Table(name = "entry_approval")
public class EntryApproval implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(name = "id_form_type")
    private Integer id_form_type;
    
    @Column(name = "id_role")
    private Integer id_role;
    
    @Column(name = "id_monper")
    private Integer id_monper;
    
    @Column(name = "approval")
    private String approval;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date approval_date;
    
    @Column(name = "type")
    private String type;

    public EntryApproval() {
    }

    public EntryApproval(Integer id, Integer id_form_type, Integer id_role, Integer id_monper, String approval, Date approval_date, String type) {
        this.id             = id;
        this.id_form_type   = id_form_type;
        this.id_role        = id_role;
        this.id_monper      = id_monper;
        this.approval       = approval;
        this.approval_date  = approval_date;
        this.type           = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_form_type() {
        return id_form_type;
    }

    public void setId_form_type(Integer id_form_type) {
        this.id_form_type = id_form_type;
    }

    public Integer getId_role() {
        return id_role;
    }

    public void setId_role(Integer id_role) {
        this.id_role = id_role;
    }

    public Integer getId_monper() {
        return id_monper;
    }

    public void setId_monper(Integer id_monper) {
        this.id_monper = id_monper;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public Date getApproval_date() {
        return approval_date;
    }

    public void setApproval_date(Date approval_date) {
        this.approval_date = approval_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    
}
