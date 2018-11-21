package com.wis.mes.production.producttracing.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;

/**
 * 产品参数
 * @author zhuzw
 *
 */
@Table(name="TB_PRODUCT_PARAMER")
public class TbProductParamer  extends AuditEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3762190680579292652L;

	/**
	 * id
	 */
	private Integer id;
	
	/**
	 * 产品id
	 */
	private Integer tbProductTracingId;
	
	/**
	 * 参数id
	 */
	private Integer TmEquipmentParamid;
	
	/**
	 * 参数对象
	 */
	private TmEquipmentParam tmEquipmentParam;
	
	/**
	 * 参数值
	 */
	private Integer parameterValue;
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PRODUCT_PARAM_ID")
	@Column(name = "ID")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id=id;
		
	}

	@Column(name="TB_PRODUCT_TRACING_ID")
	public Integer getTbProductTracingId() {
		return tbProductTracingId;
	}

	public void setTbProductTracingId(Integer tbProductTracingId) {
		this.tbProductTracingId = tbProductTracingId;
	}

	@Column(name="TM_EQUIPMENT_PARAM_ID")
	public Integer getTmEquipmentParamid() {
		return TmEquipmentParamid;
	}

	public void setTmEquipmentParamid(Integer tmEquipmentParamid) {
		TmEquipmentParamid = tmEquipmentParamid;
	}

	@Column(name="TM_EQUIPMENT_PARAM_ID")
	public TmEquipmentParam getTmEquipmentParam() {
		return tmEquipmentParam;
	}

	public void setTmEquipmentParam(TmEquipmentParam tmEquipmentParam) {
		this.tmEquipmentParam = tmEquipmentParam;
	}

	@Column(name="PARAMETER_VALUE")
	public Integer getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Integer parameterValue) {
		this.parameterValue = parameterValue;
	}
	
	

}
