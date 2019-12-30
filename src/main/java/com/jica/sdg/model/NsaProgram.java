package com.jica.sdg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "nsa_program")
public class NsaProgram implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, length = 10)
    private String id_program;
    
    @Column(nullable = false, length = 150)
    private String nm_program;
    
    @Column(nullable = false, length = 4)
    private Integer id_role;
    
    @Column(nullable = false, length = 11)
    private Integer id_monper;
    
    @Column(nullable = true, length = 10)
    private String rel_prog_id;
    
    @Column(nullable = false, length = 6)
    private Integer created_by;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_created;

	public NsaProgram() {
	}

	public NsaProgram(String id_program, String nm_program, Integer id_role, Integer id_monper, String rel_prog_id,
			Integer created_by, Date date_created) {
		super();
		this.id_program = id_program;
		this.nm_program = nm_program;
		this.id_role = id_role;
		this.id_monper = id_monper;
		this.rel_prog_id = rel_prog_id;
		this.created_by = created_by;
		this.date_created = date_created;
	}

	public String getId_program() {
		return id_program;
	}

	public void setId_program(String id_program) {
		this.id_program = id_program;
	}

	public String getNm_program() {
		return nm_program;
	}

	public void setNm_program(String nm_program) {
		this.nm_program = nm_program;
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

	public String getRel_prog_id() {
		return rel_prog_id;
	}

	public void setRel_prog_id(String rel_prog_id) {
		this.rel_prog_id = rel_prog_id;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
