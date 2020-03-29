package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ran_rad")
public class RanRad implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 11)
    private Integer id_monper;
    
    @Column(nullable = false, length = 11)
    private Integer start_year;
    
    @Column(nullable = false, length = 11)
    private Integer end_year;
    
    @Column(nullable = false, length = 1)
    private String sdg_indicator;
    
    @Column(nullable = false, length = 8)
    private String gov_prog;
    
    @Column(nullable = false, length = 8)
    private String nsa_prog;
    
    @Column(nullable = false, length = 8)
    private String gov_prog_bud;
    
    @Column(nullable = false, length = 8)
    private String nsa_prog_bud;
    
    @Column(nullable = false, length = 8)
    private String ident_problem;
    
    @Column(nullable = false, length = 8)
    private String best_pract;
    
    @Column(nullable = false, length = 25)
    private String status;
    
    @Column(nullable = false, length = 3)
    private String id_prov;
    
    @Column(nullable = true, length = 1)
    private Integer iscopy;
    
    @Column(nullable = true, length = 11)
    private Integer copied_from;

	public RanRad() {
	}

	public RanRad(Integer id_monper, Integer start_year, Integer end_year, String sdg_indicator, String gov_prog,
			String nsa_prog, String gov_prog_bud, String nsa_prog_bud, String ident_problem, String best_pract,
			String status, String id_prov, Integer iscopy, Integer copied_from) {
		super();
		this.id_monper = id_monper;
		this.start_year = start_year;
		this.end_year = end_year;
		this.sdg_indicator = sdg_indicator;
		this.gov_prog = gov_prog;
		this.nsa_prog = nsa_prog;
		this.gov_prog_bud = gov_prog_bud;
		this.nsa_prog_bud = nsa_prog_bud;
		this.ident_problem = ident_problem;
		this.best_pract = best_pract;
		this.status = status;
		this.id_prov = id_prov;
		this.iscopy = iscopy;
		this.copied_from = copied_from;
	}

	public Integer getIscopy() {
		return iscopy;
	}

	public void setIscopy(Integer iscopy) {
		this.iscopy = iscopy;
	}

	public Integer getCopied_from() {
		return copied_from;
	}

	public void setCopied_from(Integer copied_from) {
		this.copied_from = copied_from;
	}

	public Integer getId_monper() {
		return id_monper;
	}

	public void setId_monper(Integer id_monper) {
		this.id_monper = id_monper;
	}

	public Integer getStart_year() {
		return start_year;
	}

	public void setStart_year(Integer start_year) {
		this.start_year = start_year;
	}

	public Integer getEnd_year() {
		return end_year;
	}

	public void setEnd_year(Integer end_year) {
		this.end_year = end_year;
	}

	public String getSdg_indicator() {
		return sdg_indicator;
	}

	public void setSdg_indicator(String sdg_indicator) {
		this.sdg_indicator = sdg_indicator;
	}

	public String getGov_prog() {
		return gov_prog;
	}

	public void setGov_prog(String gov_prog) {
		this.gov_prog = gov_prog;
	}

	public String getNsa_prog() {
		return nsa_prog;
	}

	public void setNsa_prog(String nsa_prog) {
		this.nsa_prog = nsa_prog;
	}

	public String getGov_prog_bud() {
		return gov_prog_bud;
	}

	public void setGov_prog_bud(String gov_prog_bud) {
		this.gov_prog_bud = gov_prog_bud;
	}

	public String getNsa_prog_bud() {
		return nsa_prog_bud;
	}

	public void setNsa_prog_bud(String nsa_prog_bud) {
		this.nsa_prog_bud = nsa_prog_bud;
	}

	public String getIdent_problem() {
		return ident_problem;
	}

	public void setIdent_problem(String ident_problem) {
		this.ident_problem = ident_problem;
	}

	public String getBest_pract() {
		return best_pract;
	}

	public void setBest_pract(String best_pract) {
		this.best_pract = best_pract;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId_prov() {
		return id_prov;
	}

	public void setId_prov(String id_prov) {
		this.id_prov = id_prov;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
