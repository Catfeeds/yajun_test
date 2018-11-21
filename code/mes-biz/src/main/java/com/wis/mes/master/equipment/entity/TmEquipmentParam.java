package com.wis.mes.master.equipment.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AuditEntity;

/**
 * 
 * @author Jixueyuan
 * @date 2017年5月25日
 * @Description: 可提供的参数bean
 */
@Table(name = "tm_equipment_param")
public class TmEquipmentParam extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 设备ID */
	private Integer tmEquipmentId;
	/** 设备bean */
	private TmEquipment equipment;
	/** 参数ID */
	private Integer tsParameterId;
	/** 类型 */
	private String type;
	/** 备注 */
	private String note;
	/** 参数bean */
	/*
	 * private TsParameter parameter;
	 */
	/** 参数解释 */
	private String parameterExplain;
	/** 监控岗位编号 */
	private String controlPostNo;
	/** 监控岗位名称 */
	private String controlPostName;
	/** 参数名称 */
	private String parameterName;
	/** 参数单位 */
	private String parameterUloc;
	/** 是否配置参数范围 */
	private String ifCnfScope;

	/**设备名称**/
	private String equipmentName;
	
	/**参考值*/
	private String referenceValue;
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_EQUIPMENT_PARAM_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_EQUIPMENT_ID")
	public Integer getTmEquipmentId() {
		return tmEquipmentId;
	}

	public void setTmEquipmentId(Integer tmEquipmentId) {
		this.tmEquipmentId = tmEquipmentId;
	}

	@Column(name = "TS_PARAMETER_ID")
	public Integer getTsParameterId() {
		return tsParameterId;
	}

	public void setTsParameterId(Integer tsParameterId) {
		this.tsParameterId = tsParameterId;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/*
	 * @Column(name = "TS_PARAMETER_ID") public TsParameter getParameter() {
	 * return parameter; }
	 * 
	 * public void setParameter(TsParameter parameter) { this.parameter =
	 * parameter; }
	 */

	@Column(name = "TM_EQUIPMENT_ID")
	public TmEquipment getEquipment() {
		return equipment;
	}

	public void setEquipment(TmEquipment equipment) {
		this.equipment = equipment;
	}

	@Column(name = "PARAMETER_EXPLAIN")
	public String getParameterExplain() {
		return parameterExplain;
	}

	public void setParameterExplain(String parameterExplain) {
		this.parameterExplain = parameterExplain;
	}

	@Column(name = "CONTROL_POST_NO")
	public String getControlPostNo() {
		return controlPostNo;
	}

	public void setControlPostNo(String controlPostNo) {
		this.controlPostNo = controlPostNo;
	}

	@Column(name = "CONTROL_POST_NAME")
	public String getControlPostName() {
		return controlPostName;
	}

	public void setControlPostName(String controlPostName) {
		this.controlPostName = controlPostName;
	}

	@Column(name = "PARAMETER_NAME")
	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Column(name = "PARAMETER_ULOC")
	public String getParameterUloc() {
		return parameterUloc;
	}

	public void setParameterUloc(String parameterUloc) {
		this.parameterUloc = parameterUloc;
	}

	@Column(name = "IF_CNF_SCOPE")
	public String getIfCnfScope() {
		return ifCnfScope;
	}

	public void setIfCnfScope(String ifCnfScope) {
		this.ifCnfScope = ifCnfScope;
	}
	
	@Column(name = "REFERENCE_VALUE")
	public String getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(String referenceValue) {
		this.referenceValue = referenceValue;
	}
	
	@Transient
	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	
	
	public static TmEquipmentParam createParameterName(String parameterName) {
		TmEquipmentParam bean = new TmEquipmentParam();
		bean.setParameterName(parameterName);
		return bean;
	}
	
}
