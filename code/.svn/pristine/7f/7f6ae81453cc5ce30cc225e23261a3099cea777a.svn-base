package com.wis.mes.file.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.basis.systemadmin.entity.Employee;
import com.wis.core.entity.AuditEntity;

@Entity
@Table(name = "TS_FILE")
public class TsFile extends AuditEntity {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String type;
	private String filePath;
	private String fileName;
	private String queryCondition;
	private Employee employee;
	private String isSuccess;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_TS_FILE_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "FILE_PATH")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "QUERY_CONDITION")
	public String getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(String queryCondition) {
		this.queryCondition = queryCondition;
	}

	@Column(name = "CREATE_USER")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Column(name = "IS_SUCCESS")
	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

}
