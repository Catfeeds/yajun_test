package com.wis.basis.systemadmin.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AbstractEntity;

@Table(name = "TS_POSITION")
public class Position extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String code;
	private String name;
	private String description;
	private Integer orgnizationId;
	private Integer parentId;
	private Position parent;
	private Orgnization orgnization;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_POSITION_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "POSITION_CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "POSITION_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "POSITION_DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "ORGNIZATION_ID")
	public Integer getOrgnizationId() {
		return orgnizationId;
	}

	public void setOrgnizationId(Integer orgnizationId) {
		this.orgnizationId = orgnizationId;
	}
	@Column(name = "ORGNIZATION_ID")
	public Orgnization getOrgnization() {
		return orgnization;
	}

	public void setOrgnization(Orgnization orgnization) {
		this.orgnization = orgnization;
	}

	@Column(name = "PARENT_ID")
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	@Column(name = "PARENT_ID")
	public Position getParent() {
		return this.parent;
	}

	public void setParent(Position parent) {
		this.parent = parent;
	}
	
}
