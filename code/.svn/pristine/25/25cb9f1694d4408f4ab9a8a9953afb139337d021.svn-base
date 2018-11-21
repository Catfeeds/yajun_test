package com.wis.mes.production.record.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRecordUlocParameter
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:23:56
 * 
 * @Description: 产品档案参数信息表
 */
@Table(name = "tp_record_uloc_parameter")
public class TpRecordUlocParameter extends AuditEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** ID */
	private Integer id;
	private Integer tpRecordId;
	/** 产品过点表主键 */
	private Integer tpRecordUlocId;
	/** 参数CODE */
	private String parameterCode;
	/** 参数值 */
	private String parameterValue;
	/** 参数类型 */
	private String parameterType;
	/** 是否被替换 */
	private String isReplace;
	/** 备注 */
	private String note;
	
	private TpRecord tpRecord;
	
	private TpRecordUloc tpRecordUloc;

	@Override
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TP_RECORD_ULOC_ID")
	public Integer getTpRecordUlocId() {
		return tpRecordUlocId;
	}

	public void setTpRecordUlocId(Integer tpRecordUlocId) {
		this.tpRecordUlocId = tpRecordUlocId;
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

	@Column(name = "TP_RECORD_ID")
	public Integer getTpRecordId() {
		return tpRecordId;
	}

	public void setTpRecordId(Integer tpRecordId) {
		this.tpRecordId = tpRecordId;
	}
	
	@Column(name = "TP_RECORD_ID")
	public TpRecord getTpRecord() {
		return tpRecord;
	}

	public void setTpRecord(TpRecord tpRecord) {
		this.tpRecord = tpRecord;
	}
	@Column(name = "TP_RECORD_ULOC_ID")
	public TpRecordUloc getTpRecordUloc() {
		return tpRecordUloc;
	}

	public void setTpRecordUloc(TpRecordUloc tpRecordUloc) {
		this.tpRecordUloc = tpRecordUloc;
	}

	@Column(name = "IS_REPLACE")
	public String getIsReplace() {
		return isReplace;
	}

	public void setIsReplace(String isReplace) {
		this.isReplace = isReplace;
	}

}
