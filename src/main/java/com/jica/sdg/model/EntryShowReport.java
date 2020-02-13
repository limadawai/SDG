package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "entry_show_report")
public class EntryShowReport  implements Serializable {

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "id_monper")
    private int id_monper;
    @Column(name = "year")
    private int year;
    @Column(name = "show_report")
    private String show_report;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date show_report_date;
    @Column(name = "type")
    private String type;
    @Column(name = "period")
    private String period;

    public EntryShowReport() {
    }

    public EntryShowReport(int id, int id_monper, int year, String show_report, Date show_report_date, String type, String period) {
        this.id = id;
        this.id_monper = id_monper;
        this.year = year;
        this.show_report = show_report;
        this.show_report_date = show_report_date;
        this.type = type;
        this.period = period;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_monper() {
        return id_monper;
    }

    public void setId_monper(int id_monper) {
        this.id_monper = id_monper;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getShow_report() {
        return show_report;
    }

    public void setShow_report(String show_report) {
        this.show_report = show_report;
    }

    public Date getShow_report_date() {
        return show_report_date;
    }

    public void setShow_report_date(Date show_report_date) {
        this.show_report_date = show_report_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
