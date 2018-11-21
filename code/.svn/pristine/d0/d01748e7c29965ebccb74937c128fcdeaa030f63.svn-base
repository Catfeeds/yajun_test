package com.wis.mes.master.rolemaster.entity;

import com.wis.core.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name = "ts_role_master_set")
public class TsRoleMasterSet extends AuditEntity {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer userId;
	private Integer roleId;
	private String tmPlantId;
	private String tmWorkshopId;
	private String tmLineId;
	private String tmUlocId;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_ROLE_MASTER_SET_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "USER_ID")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "ROLE_ID")
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Column(name = "TM_PLANT_ID")
	public String getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(String tmPlantId) {
		this.tmPlantId = tmPlantId;
	}

	@Column(name = "TM_WORKSHOP_ID")
	public String getTmWorkshopId() {
		return tmWorkshopId;
	}

	public void setTmWorkshopId(String tmWorkshopId) {
		this.tmWorkshopId = tmWorkshopId;
	}

	@Column(name = "TM_LINE_ID")
	public String getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(String tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "TM_ULOC_ID")
	public String getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(String tmUlocId) {
		this.tmUlocId = tmUlocId;
	}
	
}
