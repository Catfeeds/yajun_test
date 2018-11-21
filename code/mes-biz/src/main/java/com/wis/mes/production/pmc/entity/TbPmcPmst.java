package com.wis.mes.production.pmc.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AbstractEntity;

@Table(name = "tb_pmc_pmst")
public class TbPmcPmst extends AbstractEntity {

	private Integer id;
	/**公告内容*/
	private String content;
	/**目标可动率*/
	private String targetMobility;
	/**目标冲数*/
	private String targetImpulse;
	/**屏保间隔时间*/
	private String spacingInterval;
	/**屏保保持时间*/
	private String retentionTime;
	/**类型*/
	private String modelType;
	/**时间*/
	private String timeFrame;
	/**空调能耗*/
	private String airConditionerEc;
	/**设备能耗*/
	private String equipmentEc;
	/**照明能耗*/
	private String lightingEc;
	/**有效时间开始*/
	private Date activeTimebegin;
	/**有效时间开始*/
	private Date activeTimefinish;
	
	/**可动率警界值*/
	private String pbTargetMobility;
	/**冲速警界值*/
	private String pbTargetImpulse;
	/**可动率极限值*/
	private String mobilityExtreme;
	/**冲速极限值*/
	private String impulseExtreme;
	
	private Integer earlyInspectionTime;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PMC_PMST_ID")
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

	public static TbPmcPmst createModelType(String modelType){
		TbPmcPmst pmcPmst = new TbPmcPmst();
		pmcPmst.setModelType(modelType);
		return pmcPmst;
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

	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTIVE_TIME_BEGIN", updatable = false)
	public Date getActiveTimebegin() {
		return activeTimebegin;
	}

	public void setActiveTimebegin(Date activeTimebegin) {
		this.activeTimebegin = activeTimebegin;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTIVE_TIME_FINISH", updatable = false)
	public Date getActiveTimefinish() {
		return activeTimefinish;
	}

	public void setActiveTimefinish(Date activeTimefinish) {
		this.activeTimefinish = activeTimefinish;
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
