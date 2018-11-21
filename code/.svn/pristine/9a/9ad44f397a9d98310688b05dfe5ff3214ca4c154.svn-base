package com.wis.mes.master.maintenance.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.core.entity.Removable;

/**
 * @author CFY
 * @company 上海西信信息科技有限公司
 * @date 2018年7月5日
 * @desc 钣金设备保养bean
 */
@Table(name = "TM_DEVICE_MAINTENANCE")
public class TmDeviceMaintenance extends AuditEntity implements Removable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/**设备名称*/
	private String deviceCode;
	/** 设备编号*/
	private String deviceName;
	/**保养项目*/
	private String maintenanceProject;
	/** 保养单位*/
	private String maintenanceUnit;
	/**保养值*/
	private Integer maintenanceValue;
	/**当前值(由TMP_MAINTENANCE_INFO_TEMP的触发器维护)*/
	private Integer currentValue;
	/**最新采集时间*/
	private Date lastCollectTime;
	/**保养代码*/
	private String maintenanceCode;
	/**新增备注*/
	private String maintenanceRemark;
	/**备注*/
	private String remark;
	private Integer removed;
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_DEVICE_MAINTENANCE_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "DEVICE_CODE")
	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	@Column(name = "DEVICE_NAME")
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Column(name = "MAINTENANCE_PROJECT")
	public String getMaintenanceProject() {
		return maintenanceProject;
	}

	public void setMaintenanceProject(String maintenanceProject) {
		this.maintenanceProject = maintenanceProject;
	}

	@Column(name = "MAINTENANCE_UNIT")
	public String getMaintenanceUnit() {
		return maintenanceUnit;
	}

	public void setMaintenanceUnit(String maintenanceUnit) {
		this.maintenanceUnit = maintenanceUnit;
	}

	@Column(name = "MAINTENANCE_VALUE")
	public Integer getMaintenanceValue() {
		return maintenanceValue;
	}

	public void setMaintenanceValue(Integer maintenanceValue) {
		this.maintenanceValue = maintenanceValue;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "MARK_FOR_DELETE")
	public Integer getRemoved() {
		return removed;
	}

	@Column(name = "CURRENT_VALUE")
	public Integer getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Integer currentValue) {
		this.currentValue = currentValue;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_COLLECT_TIIME", updatable = false)
	public Date getLastCollectTime() {
		return lastCollectTime;
	}

	public void setLastCollectTime(Date lastCollectTime) {
		this.lastCollectTime = lastCollectTime;
	}

	@Override
	public boolean isEntityRemoved() {
		return (null == removed || removed == 0) ? false : true;
	}

	@Override
	public void setRemoved(Integer removed) {
		this.removed = removed;
	}

	@Column(name = "MAINTENANCE_CODE")
	public String getMaintenanceCode() {
		return maintenanceCode;
	}

	public void setMaintenanceCode(String maintenanceCode) {
		this.maintenanceCode = maintenanceCode;
	}

	@Column(name = "MAINTENANCE_REMARK")
	public String getMaintenanceRemark() {
		return maintenanceRemark;
	}

	public void setMaintenanceRemark(String maintenanceRemark) {
		this.maintenanceRemark = maintenanceRemark;
	}
}
