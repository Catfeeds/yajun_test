package com.wis.mes.master.path.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.entity.AuditEntity;

/**
 * 工艺路径站点的质检项
 * 
 * @author liuzejun
 */
@Table(name = "tm_path_uloc_sip")
public class TmPathUlocSip extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 工艺路径站点ID */
	private Integer tmPathUlocId;
	/** 参数ID */
	private Integer tsParameterId;
	/** 参数Bean */
	private TsParameter parameter;
	/** 备注 */
	private String note;
	/** 节点ID */
	private String rectSeq;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PATH_ULOC_SIP_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_PATH_ULOC_ID")
	public Integer getTmPathUlocId() {
		return tmPathUlocId;
	}

	public void setTmPathUlocId(Integer tmPathUlocId) {
		this.tmPathUlocId = tmPathUlocId;
	}

	@Column(name = "TS_PARAMETER_ID")
	public Integer getTsParameterId() {
		return tsParameterId;
	}

	public void setTsParameterId(Integer tsParameterId) {
		this.tsParameterId = tsParameterId;
	}

	@Column(name = "TS_PARAMETER_ID")
	public TsParameter getParameter() {
		return parameter;
	}

	public void setParameter(TsParameter parameter) {
		this.parameter = parameter;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Transient
	public String getRectSeq() {
		return rectSeq;
	}

	public void setRectSeq(String rectSeq) {
		this.rectSeq = rectSeq;
	}

}
