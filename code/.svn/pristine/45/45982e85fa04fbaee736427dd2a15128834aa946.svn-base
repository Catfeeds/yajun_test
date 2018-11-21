package com.wis.mes.production.pmc.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * @author yajun_ren
 *
 */
@Table(name = "tb_pmc_pmst_log")
public class TbPmcPmstLog extends AuditEntity {

	private Integer id;
	private String content;
	private String targetMobility;
	private String targetImpulse;
	private String spacingInterval;
	private String retentionTime;
	private String modelType;
	/**时间*/
	private String timeFrame;
	/**空调能耗*/
	private String airConditionerEc;
	/**设备能耗*/
	private String equipmentEc;
	/**照明能耗*/
	private String lightingEc;
	/**可动率警界值*/
	private String pbTargetMobility;
	/**冲速警界值*/
	private String pbTargetImpulse;
	/**可动率极限值*/
	private String mobilityExtreme;
	/**冲速极限值*/
	private String impulseExtreme;
	
	private Integer earlyInspectionTime;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PMC_PMST_LOG_ID")
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "TARGET_MOBILITY")
	public String getTargetMobility() {
		return targetMobility;
	}

	public void setTargetMobility(String targetMobility) {
		this.targetMobility = targetMobility;
	}

	@Column(name = "TARGET_IMPULSE")
	public String getTargetImpulse() {
		return targetImpulse;
	}

	public void setTargetImpulse(String targetImpulse) {
		this.targetImpulse = targetImpulse;
	}

	@Column(name = "SPACING_INTERVAL")
	public String getSpacingInterval() {
		return spacingInterval;
	}

	public void setSpacingInterval(String spacingInterval) {
		this.spacingInterval = spacingInterval;
	}

	@Column(name = "RETENTION_TIME")
	public String getRetentionTime() {
		return retentionTime;
	}

	public void setRetentionTime(String retentionTime) {
		this.retentionTime = retentionTime;
	}

	@Column(name = "MODEL_TYPE")
	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	@Column(name = "TIME_FRAME")
	public String getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}
	@Column(name = "AIR_CONDITIONER_EC")
	public String getAirConditionerEc() {
		return airConditionerEc;
	}

	public void setAirConditionerEc(String airConditionerEc) {
		this.airConditionerEc = airConditionerEc;
	}
	@Column(name = "EQUIPMENT_EC")
	public String getEquipmentEc() {
		return equipmentEc;
	}

	public void setEquipmentEc(String equipmentEc) {
		this.equipmentEc = equipmentEc;
	}
	@Column(name = "LIGHTING_EC")
	public String getLightingEc() {
		return lightingEc;
	}

	public void setLightingEc(String lightingEc) {
		this.lightingEc = lightingEc;
	}
	
	@Column(name="PB_TARGET_MOBILITY")
	public String getPbTargetMobility() {
		return pbTargetMobility;
	}

	public void setPbTargetMobility(String pbTargetMobility) {
		this.pbTargetMobility = pbTargetMobility;
	}

	@Column(name="PB_TARGET_IMPULSE")
	public String getPbTargetImpulse() {
		return pbTargetImpulse;
	}

	public void setPbTargetImpulse(String pbTargetImpulse) {
		this.pbTargetImpulse = pbTargetImpulse;
	}

	@Column(name="EARLY_INSPECTION_TIME")
	public Integer getEarlyInspectionTime() {
		return earlyInspectionTime;
	}

	public void setEarlyInspectionTime(Integer earlyInspectionTime) {
		this.earlyInspectionTime = earlyInspectionTime;
	}
	@Column(name="MOBILITY_EXTREME")
	public String getMobilityExtreme() {
		return mobilityExtreme;
	}

	public void setMobilityExtreme(String mobilityExtreme) {
		this.mobilityExtreme = mobilityExtreme;
	}

	@Column(name="IMPULSE_EXTREME")
	public String getImpulseExtreme() {
		return impulseExtreme;
	}

	public void setImpulseExtreme(String impulseExtreme) {
		this.impulseExtreme = impulseExtreme;
	}
	
}
