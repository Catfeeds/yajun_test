package com.wis.basis.systemadmin.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.BaseEntity;

@Table(name = "TS_ORGNIZATION")
public class Orgnization extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String dimension;
	private String name;
	private String code;
	private Orgnization parent;
	private String type;
	private String description;
	private Integer leaderId;
	private Integer parentId;
	private Employee leader;
	private String systemCode;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_ORGNIZATION_ID")
	@Column(name = "ID")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "PARENT_ID")
	public Orgnization getParent() {
		return this.parent;
	}

	public void setParent(Orgnization parent) {
		this.parent = parent;
	}

	@Column(name = "PARENT_ID")
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Column(name = "ORGNIZATION_CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "ORGNIZATION_NAME")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ORGNIZATION_DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "DIMENSION")
	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	@Column(name = "ORGNIZATION_TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "LEADER_ID")
	public Integer getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}

	@Column(name = "LEADER_ID")
	public Employee getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	@Column(name = "SYSTEM_CODE")
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

}
