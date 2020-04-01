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
@Table(name = "entry_nsa_budget")
public class EntryNsaBudget implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(name = "id_nsa_activity")
    private Integer id_nsa_activity;
    
    @Column(name = "achievement1")
    private Integer achievement1;
    
    @Column(name = "achievement2")
    private Integer achievement2;
    
    @Column(name = "achievement3")
    private Integer achievement3;
    
    @Column(name = "achievement4")
    private Integer achievement4;
    
    @Column(name = "new_value1")
    private Integer new_value1;
    
    @Column(name = "new_value2")
    private Integer new_value2;
    
    @Column(name = "new_value3")
    private Integer new_value3;
    
    @Column(name = "new_value4")
    private Integer new_value4;
    
    @Column(name = "year_entry")
    private Integer year_entry;
    
    @Column(name = "created_by")
    private Integer created_by;
    
    @Column(name = "created_by2")
    private Integer created_by2;
    
    @Column(name = "created_by3")
    private Integer created_by3;
    
    @Column(name = "created_by4")
    private Integer created_by4;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_created;
    
    @Column(name = "date_created2")
    @Temporal(TemporalType.DATE)
    private Date date_created2;
    
    @Column(name = "date_created3")
    @Temporal(TemporalType.DATE)
    private Date date_created3;
    
    @Column(name = "date_created4")
    @Temporal(TemporalType.DATE)
    private Date date_created4;
    
    @Column(name = "id_monper")
    private Integer id_monper;

    public EntryNsaBudget() {
    }

    public EntryNsaBudget(Integer id, Integer id_nsa_activity, Integer achievement1, Integer achievement2, Integer achievement3, Integer achievement4, Integer new_value1, Integer new_value2, Integer new_value3, Integer new_value4, Integer year_entry, Integer id_role, Integer created_by, Integer created_by2, Integer created_by3, Integer created_by4, Date date_created, Date date_created2, Date date_created3, Date date_created4, Integer id_monper) {
        this.id                 = id;
        this.id_nsa_activity    = id_nsa_activity;
        this.achievement1       = achievement1;
        this.achievement2       = achievement2;
        this.achievement3       = achievement3;
        this.achievement4       = achievement4;
        this.new_value1         = new_value1;
        this.new_value2         = new_value2;
        this.new_value3         = new_value3;
        this.new_value4         = new_value4;
//        this.show_report_date   = show_report_date;
        this.year_entry         = year_entry;
        this.created_by         = created_by;
        this.created_by2        = created_by2;
        this.created_by3        = created_by3;
        this.created_by4        = created_by4;
        this.date_created       = date_created;
        this.date_created2      = date_created2;
        this.date_created3      = date_created3;
        this.date_created4      = date_created4;
        this.id_monper          = id_monper;
    }

    public Integer getCreated_by2() {
        return created_by2;
    }

    public void setCreated_by2(Integer created_by2) {
        this.created_by2 = created_by2;
    }

    public Integer getCreated_by3() {
        return created_by3;
    }

    public void setCreated_by3(Integer created_by3) {
        this.created_by3 = created_by3;
    }

    public Integer getCreated_by4() {
        return created_by4;
    }

    public void setCreated_by4(Integer created_by4) {
        this.created_by4 = created_by4;
    }

    public Date getDate_created2() {
        return date_created2;
    }

    public void setDate_created2(Date date_created2) {
        this.date_created2 = date_created2;
    }

    public Date getDate_created3() {
        return date_created3;
    }

    public void setDate_created3(Date date_created3) {
        this.date_created3 = date_created3;
    }

    public Date getDate_created4() {
        return date_created4;
    }

    public void setDate_created4(Date date_created4) {
        this.date_created4 = date_created4;
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_nsa_activity() {
        return id_nsa_activity;
    }

    public void setId_nsa_activity(Integer id_nsa_activity) {
        this.id_nsa_activity = id_nsa_activity;
    }

    public Integer getAchievement1() {
        return achievement1;
    }

    public void setAchievement1(Integer achievement1) {
        this.achievement1 = achievement1;
    }

    public Integer getAchievement2() {
        return achievement2;
    }

    public void setAchievement2(Integer achievement2) {
        this.achievement2 = achievement2;
    }

    public Integer getAchievement3() {
        return achievement3;
    }

    public void setAchievement3(Integer achievement3) {
        this.achievement3 = achievement3;
    }

    public Integer getAchievement4() {
        return achievement4;
    }

    public void setAchievement4(Integer achievement4) {
        this.achievement4 = achievement4;
    }

    public Integer getNew_value1() {
        return new_value1;
    }

    public void setNew_value1(Integer new_value1) {
        this.new_value1 = new_value1;
    }

    public Integer getNew_value2() {
        return new_value2;
    }

    public void setNew_value2(Integer new_value2) {
        this.new_value2 = new_value2;
    }

    public Integer getNew_value3() {
        return new_value3;
    }

    public void setNew_value3(Integer new_value3) {
        this.new_value3 = new_value3;
    }

    public Integer getNew_value4() {
        return new_value4;
    }

    public void setNew_value4(Integer new_value4) {
        this.new_value4 = new_value4;
    }

    public Integer getYear_entry() {
        return year_entry;
    }

    public void setYear_entry(Integer year_entry) {
        this.year_entry = year_entry;
    }

    public Integer getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Integer created_by) {
        this.created_by = created_by;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Integer getId_monper() {
        return id_monper;
    }

    public void setId_monper(Integer id_monper) {
        this.id_monper = id_monper;
    }

    
    
    
}
