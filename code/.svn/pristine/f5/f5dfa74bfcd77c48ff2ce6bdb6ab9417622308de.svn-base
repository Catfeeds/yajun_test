package com.wis.basis.systemadmin.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AuditEntity;
import com.wis.core.entity.Removable;


@Table(name = "TS_USER")
public class Employee extends AuditEntity implements Removable {

	public Integer id;
	public String account;
	public String status;
//	public String code;
	public String name;
	public Integer removed;
	//public String cdsId;
	@SequenceGenerator(name = "generator", sequenceName = "SEQ_USER_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "ACCOUNT")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

//	@Column(name = "USER_CODE")
//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}

//	@Column(name = "CDSID")
//	public String getCdsId() {
//		return cdsId;
//	}
//
//	
//	public void setCdsId(String cdsId) {
//		this.cdsId = cdsId;
//	}

	@Column(name = "USER_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "MARK_FOR_DELETE")
	public Integer getRemoved() {
		return removed;
	}

	public void setRemoved(Integer removed) {
		this.removed = removed;
	}

	@Transient
	public boolean isEntityRemoved() {
		return (removed == null || removed == 0) ? false : true;
	}

}
