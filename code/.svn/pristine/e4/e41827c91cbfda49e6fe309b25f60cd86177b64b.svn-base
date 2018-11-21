package com.wis.mes.master.nc.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AbstractEntity;

@Table(name = "TM_FAULT_GRADE")
public class TmFaultGrade extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String ngLevel;
	private String ngEntrance;
	private String remarks;
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_FAULT_GRADE_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "NG_LEVEL")
	public String getNgLevel() {
		return ngLevel;
	}

	public void setNgLevel(String ngLevel) {
		this.ngLevel = ngLevel;
	}

	@Column(name = "NG_ENTRANCE")
	public String getNgEntrance() {
		return ngEntrance;
	}

	public void setNgEntrance(String ngEntrance) {
		this.ngEntrance = ngEntrance;
	}
	@Column(name="REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
