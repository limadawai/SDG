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
@Table(name = "entry_sdg")
public class EntrySdg implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(name = "id_sdg_indicator")
    private String id_sdg_indicator;
    
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
    
    @Column(name = "id_role")
    private Integer id_role;
    
    @Column(name = "created_by")
    private Integer created_by;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_created;
    
    @Column(name = "id_monper")
    private Integer id_monper;

    public EntrySdg() {
    }

    public EntrySdg(Integer id, String id_sdg_indicator, Integer achievement1, Integer achievement2, Integer achievement3, Integer achievement4, Integer new_value1, Integer new_value2, Integer new_value3, Integer new_value4, Integer year_entry, Integer id_role, Integer created_by, Date date_created, Integer id_monper) {
        this.id                 = id;
        this.id_sdg_indicator   = id_sdg_indicator;
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
        this.id_role            = id_role;
        this.created_by         = created_by;
        this.date_created       = date_created;
        this.id_monper          = id_monper;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getId_sdg_indicator() {
        return id_sdg_indicator;
    }

    public void setId_sdg_indicator(String id_sdg_indicator) {
        this.id_sdg_indicator = id_sdg_indicator;
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

    public Integer getId_role() {
        return id_role;
    }

    public void setId_role(Integer id_role) {
        this.id_role = id_role;
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
