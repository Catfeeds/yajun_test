package com.wis.mes.master.uloc.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.uloc.entity.TmUloc;

/**
 *
 * @author 赵宪泉
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年5月25日
 *
 * @desc 工位参数表 bean
 *
 */
@Table(name = "tm_uloc_sip")
public class TmUlocSip extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键id */
	private Integer id;
	/** 工位id */
	private Integer tmUlocId;
	/** 工位实体bean */
	private TmUloc tmUloc;
	/** 参数id */
	private Integer tsParameterId;
	/** 参数实体bean */
	private TsParameter tsParameter = new TsParameter();
	/** 详细信息 */
	private String detail;
	/** 是否必检 */
	private String isExamine;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_ULOC_SIP_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}

	@Column(name = "TM_ULOC_ID")
	public TmUloc getTmUloc() {
		return tmUloc;
	}

	public void setTmUloc(TmUloc tmUloc) {
		this.tmUloc = tmUloc;
	}

	@Column(name = "TS_PARAMETER_ID")
	public Integer getTsParameterId() {
		return tsParameterId;
	}

	public void setTsParameterId(Integer tsParameterId) {
		this.tsParameterId = tsParameterId;
	}

	@Column(name = "TS_PARAMETER_ID")
	public TsParameter getTsParameter() {
		return tsParameter;
	}

	public void setTsParameter(TsParameter tsParameter) {
		this.tsParameter = tsParameter;
	}

	@Column(name = "DETAIL")
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Column(name = "IS_EXAMINE")
	public String getIsExamine() {
		return isExamine;
	}

	public void setIsExamine(String isExamine) {
		this.isExamine = isExamine;
	}
}
