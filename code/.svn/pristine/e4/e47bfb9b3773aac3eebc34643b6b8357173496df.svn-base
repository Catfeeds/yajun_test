package com.wis.mes.master.uloc.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * 工位异常码
 * 
 * @author jiuxian
 *
 */
@Table(name = "TM_ULOC_ABNORMAL")
public class TmUlocAbnormal extends AuditEntity {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String ulocNo;
	private String abnormalCode;
	private String abnormalName;
	private Integer tmUlocId;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_ULOC_ABNORMAL_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "ULOC_NO")
	public String getUlocNo() {
		return ulocNo;
	}

	public void setUlocNo(String ulocNo) {
		this.ulocNo = ulocNo;
	}

	@Column(name = "ABNORMAL_CODE")
	public String getAbnormalCode() {
		return abnormalCode;
	}

	public void setAbnormalCode(String abnormalCode) {
		this.abnormalCode = abnormalCode;
	}

	@Column(name = "ABNORMAL_NAME")
	public String getAbnormalName() {
		return abnormalName;
	}

	public void setAbnormalName(String abnormalName) {
		this.abnormalName = abnormalName;
	}

	@Column(name = "TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}

}
