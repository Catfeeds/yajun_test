package com.wis.mes.production.repair.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRepairUlocParameter
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:36:01
 * 
 * @Description: 返修参数信息表
 */
@Table(name = "tp_repair_uloc_parameter")
public class TpRepairUlocParameter extends AuditEntity {

	/** 主键ID */
	private Integer id;
	/** 返修过点表主键ID */
	private Integer tpRepairUlocId;
	/** 参数CODE */
	private String parameterCode;
	/** 参数值 */
	private String parameterValue;
	/** 参数类型 */
	private String parameterType;
	/** 备注 */
	private String note;

	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TP_REPAIR_ULOC_ID")
	public Integer getTpRepairUlocId() {
		return tpRepairUlocId;
	}

	public void setTpRepairUlocId(Integer tpRepairUlocId) {
		this.tpRepairUlocId = tpRepairUlocId;
	}

	@Column(name = "PARAMETER_CODE")
	public String getParameterCode() {
		return parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}

	@Column(name = "PARAMETER_VALUE")
	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Column(name = "PARAMETER_TYPE")
	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
