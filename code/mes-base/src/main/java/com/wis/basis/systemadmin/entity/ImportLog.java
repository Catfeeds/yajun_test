package com.wis.basis.systemadmin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

@Entity
@Table(name = "ts_import_log")
public class ImportLog extends AuditEntity {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String errorDesc;
	private String operateEntityName;
	private Integer operatorId;
	private String operatorName;

	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "seq_import_log_id")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "ERROR_DESC")
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	
	@Column(name = "OPERATOR_ID")
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	
	@Column(name = "OPERATOR_NAME")
	public String getOperatorName() {
		return operatorName;
	}
	
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	@Column(name = "OPERATE_ENTITY")
	public String getOperateEntityName() {
		return operateEntityName;
	}
	public void setOperateEntityName(String operateEntityName) {
		this.operateEntityName = operateEntityName;
	}
	
}
