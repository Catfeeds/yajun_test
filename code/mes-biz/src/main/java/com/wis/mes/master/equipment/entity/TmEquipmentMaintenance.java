package com.wis.mes.master.equipment.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
/**
 * 
* @author Jixueyuan   
* @date 2017年5月25日
* @Description: 保养bean
 */

@Table(name = "tm_equipment_maintenance")
public class TmEquipmentMaintenance extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 设备ID */
	private Integer tmEquipmentId;
	/** 设备bean */
	private TmEquipment equipment;
	/**时间保养周期类型*/
	private String type;
	/**时间保养周期值*/
	private Integer typeValue;
	/** 剩余保养时间*/
	private Integer remainderTime;
	/** 保养时间警戒值*/
	private Integer timeWarningValue;
	/** 目标保养次数*/
	private Integer count;
	/** 剩余保养次数*/
	private Integer remainderCount;
	/**保养次数警戒值*/
	private Integer countWarningValue;
	/** 上一次保养时间*/
	private Date lastMaintenanceTime;
	/** 下一次保养时间*/
	private Date nextMaintenanceTime;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_EQUIPMENT_MAINTENANCE_ID")
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
	@Column(name = "TM_EQUIPMENT_ID")
	public TmEquipment getEquipment() {
		return equipment;
	}
	
	public void setEquipment(TmEquipment equipment) {
		this.equipment = equipment;
	}
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "TYPEVALUE")
	public Integer getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(Integer typeValue) {
		this.typeValue = typeValue;
	}

	@Column(name = "REMAINDER_TIME")
	public Integer getRemainderTime() {
		return remainderTime;
	}

	public void setRemainderTime(Integer remainderTime) {
		this.remainderTime = remainderTime;
	}
	@Column(name = "TIME_WARNINGVALUE")
	public Integer getTimeWarningValue() {
		return timeWarningValue;
	}

	public void setTimeWarningValue(Integer timeWarningValue) {
		this.timeWarningValue = timeWarningValue;
	}
	@Column(name = "COUNT")
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	@Column(name = "REMAINDER_COUNT")
	public Integer getRemainderCount() {
		return remainderCount;
	}

	public void setRemainderCount(Integer remainderCount) {
		this.remainderCount = remainderCount;
	}
	@Column(name = "COUNT_WARNINGVALUE")
	public Integer getCountWarningValue() {
		return countWarningValue;
	}

	public void setCountWarningValue(Integer countWarningValue) {
		this.countWarningValue = countWarningValue;
	}
	@Column(name = "LASTMAINTENANCE_TIME")
	public Date getLastMaintenanceTime() {
		return lastMaintenanceTime;
	}

	public void setLastMaintenanceTime(Date lastMaintenanceTime) {
		this.lastMaintenanceTime = lastMaintenanceTime;
	}
	@Column(name = "NEXTMAINTENANCE_TIME")
	public Date getNextMaintenanceTime() {
		return nextMaintenanceTime;
	}

	public void setNextMaintenanceTime(Date nextMaintenanceTime) {
		this.nextMaintenanceTime = nextMaintenanceTime;
	}
	
	

}
