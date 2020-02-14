package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "entry_gri_ojk")
public class EntryGriojk implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id;
    
    @Column(nullable = false, length = 255)
    private String company_name;
    
    @Column(nullable = false, length = 11)
    private Integer year;
    
    @Column(nullable = false, length = 255)
    private String file1;
    
    @Column(nullable = false, length = 255)
    private String file2;
    
    @Column(nullable = false, length = 11)
    private Integer approval;
    
    @Column(nullable = false, length = 255)
    private String description;

    public EntryGriojk(Integer id, String company_name, Integer year, String file1, String file2) {
        this.id = id;
        this.company_name = company_name;
        this.year = year;
        this.file1 = file1;
        this.file2 = file2;
    }

    public EntryGriojk(Integer id, String company_name, Integer year, String file1, String file2, Integer approval, String description) {
        this.id = id;
        this.company_name = company_name;
        this.year = year;
        this.file1 = file1;
        this.file2 = file2;
        this.approval = approval;
        this.description = description;
    }
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getFile1() {
        return file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    public String getFile2() {
        return file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    public Integer getApproval() {
        return approval;
    }

    public void setApproval(Integer approval) {
        this.approval = approval;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
