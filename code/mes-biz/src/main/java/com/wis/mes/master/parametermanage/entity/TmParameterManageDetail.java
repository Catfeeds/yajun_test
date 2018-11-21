package com.wis.mes.master.parametermanage.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AbstractEntity;

@Table(name = "TM_PARAMETER_MANAGE_DETAIL")
public class TmParameterManageDetail extends AbstractEntity {

	private Integer id;
	/** 参数管理主表ID */
	private Integer tmParameterManageId;
	/** 参数ID **/
	private Integer parameterId;
	/** 参数范围 ***/
	private String parameterRange;
	/** 参数名称*/
	private String parameterName;
	/** 参数解释*/
	private String parameterExplain;
	/** 最大值*/
	private String maxVal;
	/** 最最小值*/
	private String minVal;

	@Column(name = "ID")
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "seq_parameter_detail_id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_PARAMETER_MANAGE_ID")
	public Integer getTmParameterManageId() {
		return tmParameterManageId;
	}

	public void setTmParameterManageId(Integer tmParameterManageId) {
		this.tmParameterManageId = tmParameterManageId;
	}

	@Column(name = "PARAMETER_ID")
	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	@Column(name = "PARAMETER_RANGE")
	public String getParameterRange() {
		return parameterRange;
	}

	public void setParameterRange(String parameterRange) {
		this.parameterRange = parameterRange;
	}

	@Transient
	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Transient
	public String getParameterExplain() {
		return parameterExplain;
	}

	public void setParameterExplain(String parameterExplain) {
		this.parameterExplain = parameterExplain;
	}
	
	@Column(name="MAX_VAL")
	public String getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(String maxVal) {
		this.maxVal = maxVal;
	}

	@Column(name="MIN_VAL")
	public String getMinVal() {
		return minVal;
	}

	public void setMinVal(String minVal) {
		this.minVal = minVal;
	}

	public static TmParameterManageDetail createParameterManageDetail(Integer parameterManageId,Integer parameterId,String parameterName,String parameterExplain) {
		TmParameterManageDetail bean = new TmParameterManageDetail();
		bean.setTmParameterManageId(parameterManageId);
		bean.setParameterId(parameterId);
		bean.setParameterName(parameterName);
		bean.setParameterExplain(parameterExplain);
		return bean;
	}
}
