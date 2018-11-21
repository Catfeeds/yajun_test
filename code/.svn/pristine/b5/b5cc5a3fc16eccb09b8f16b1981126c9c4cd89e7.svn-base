package com.wis.mes.dakin.production.tracing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AbstractEntity;

/**
 * 
 * @author liuzejun
 * 
 * @desc 参数
 * 
 */
@Entity
@Table(name = "VB_PRODUCT_PARAMETERS")
public class VbProductParameters extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 背番号 */
	private String sebango;
	/** 机号 */
	private String kiban;
	/** 设备 */
	private String equipmentType;
	/** 设备号 */
	private String equipmentNo;
	/** 参数 */
	private String params;
	/** 参数值 */
	private String paramsValue;

	/** 设备ID */
	private String equipmentId;
	/** 参数描述 */
	private String paramExplain;
	/** 参数单位 */
	private String unit;
	/** 备注 */
	private String note;
	private String isConfig;
	
	@Override
	@Column(name = "ID")
	@javax.persistence.Transient
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
	}

	@Column(name = "SEBANGO")
	public String getSebango() {
		return sebango;
	}

	public void setSebango(String sebango) {
		this.sebango = sebango;
	}

	@Column(name = "KIBAN")
	public String getKiban() {
		return kiban;
	}

	public void setKiban(String kiban) {
		this.kiban = kiban;
	}

	@Column(name = "EQUIPMENTTYPE")
	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}

	@Column(name = "EQUIPMENTNO")
	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	@Column(name = "PARAMS")
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name = "PARAMSVALUE")
	public String getParamsValue() {
		return paramsValue;
	}

	public void setParamsValue(String paramsValue) {
		this.paramsValue = paramsValue;
	}

	@Transient
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Transient
	public String getParamExplain() {
		return paramExplain;
	}

	public void setParamExplain(String paramExplain) {
		this.paramExplain = paramExplain;
	}

	@Transient
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	@Transient
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Transient
	public String getIsConfig() {
		return isConfig;
	}

	public void setIsConfig(String isConfig) {
		this.isConfig = isConfig;
	}
}
