package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "entry_problem_identify")
public class EntryProblemIdentify implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;

    @Column(name = "id_goals")
    private String id_goals;

    @Column(name = "id_target")
    private String id_target;

    @Column(name = "id_indicator")
    private String id_indicator;

    @Column(name = "id_cat")
    private String id_cat;

    @Column(name = "problem")
    private String problem;

    @Column(name = "follow_up")
    private String follow_up;

    @Column(name = "id_prov")
    private String id_prov;

    @Column(name = "id_role")
    private int id_role;

    @Column(name = "year")
    private int year;

    @Column(name = "year_entry")
    private int year_entry;

    @Column(name = "created_by")
    private int created_by;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_created;

    @Column(name = "summary")
    private String summary;

    @Column(name = "id_monper")
    private int id_monper;

    public EntryProblemIdentify() {
    }

    public EntryProblemIdentify(Integer id, String id_cat, String problem, String follow_up) {
        this.id = id;
        this.id_cat = id_cat;
        this.problem = problem;
        this.follow_up = follow_up;
    }
    
    
    
    public EntryProblemIdentify(String id_goals, String id_target, String id_indicator, String id_cat, String problem, String follow_up, String id_prov, int id_role, int year, int year_entry, int created_by, Date date_created, String summary, int id_monper) {
        this.id_goals = id_goals;
        this.id_target = id_target;
        this.id_indicator = id_indicator;
        this.id_cat = id_cat;
        this.problem = problem;
        this.follow_up = follow_up;
        this.id_prov = id_prov;
        this.id_role = id_role;
        this.year = year;
        this.year_entry = year_entry;
        this.created_by = created_by;
        this.date_created = date_created;
        this.summary = summary;
        this.id_monper = id_monper;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getId_goals() {
        return id_goals;
    }

    public void setId_goals(String id_goals) {
        this.id_goals = id_goals;
    }

    public String getId_target() {
        return id_target;
    }

    public void setId_target(String id_target) {
        this.id_target = id_target;
    }

    public String getId_indicator() {
        return id_indicator;
    }

    public void setId_indicator(String id_indicator) {
        this.id_indicator = id_indicator;
    }

    public String getId_cat() {
        return id_cat;
    }

    public void setId_cat(String id_cat) {
        this.id_cat = id_cat;
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

    public String getId_prov() {
        return id_prov;
    }

    public void setId_prov(String id_prov) {
        this.id_prov = id_prov;
    }

    public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear_entry() {
        return year_entry;
    }

    public void setYear_entry(int year_entry) {
        this.year_entry = year_entry;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getId_monper() {
        return id_monper;
    }

    public void setId_monper(int id_monper) {
        this.id_monper = id_monper;
    }
}
