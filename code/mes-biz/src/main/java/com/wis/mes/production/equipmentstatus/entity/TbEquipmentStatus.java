package com.wis.mes.production.equipmentstatus.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.coderule.entity.TmCodeRule;
import com.wis.mes.master.equipment.entity.TmEquipment;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

@Table(name = "tb_equipment_status")
public class TbEquipmentStatus extends AuditEntity {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 班次ID **/
	private Integer tmWorktimeId;
	/** 产线ID **/
	private Integer tmLineId;
	/** 设备ID **/
	private Integer tmEquipmentId;
	/** 运行状态 **/
	private String runningState;
	/** 状态编号 **/
	private String statusNumber;
	/** 状态内容 **/
	private String statusContent;
	/** 开始时间 **/
	private String beginTime;
	/** 结束时间 **/
	private String finishTime;
	/** 时长(min/s) **/
	private String duration;
	/** 班组管理 */
	private Integer tmClassManagerId;
	/** 班组管理 */
	private TmClassManager classManager;

	private TmLine tmline;

	private TmWorktime worktime;

	private TmEquipment tmEquipment;

	private TmCodeRule codeRule;

	@Column(name = "ID")
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_EQUIPMENT_STATUS_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_WORK_TIME_ID")
	public Integer getTmWorktimeId() {
		return tmWorktimeId;
	}

	public void setTmWorktimeId(Integer tmWorktimeId) {
		this.tmWorktimeId = tmWorktimeId;
	}

	@Column(name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "TM_EQUIPMENT_ID")
	public Integer getTmEquipmentId() {
		return tmEquipmentId;
	}

	public void setTmEquipmentId(Integer tmEquipmentId) {
		this.tmEquipmentId = tmEquipmentId;
	}

	@Column(name = "RUNNING_STATE")
	public String getRunningState() {
		return runningState;
	}

	public void setRunningState(String runningState) {
		this.runningState = runningState;
	}

	@Column(name = "STATUS_NUMBER")
	public String getStatusNumber() {
		return statusNumber;
	}

	public void setStatusNumber(String statusNumber) {
		this.statusNumber = statusNumber;
	}

	@Column(name = "STATUS_CONTENT")
	public String getStatusContent() {
		return statusContent;
	}

	public void setStatusContent(String statusContent) {
		this.statusContent = statusContent;
	}

	@Column(name = "BEGIN_TIME")
	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	@Column(name = "FINISH_TIME")
	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "DURATION")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Column(name = "TM_LINE_ID")
	public TmLine getTmline() {
		return tmline;
	}

	public void setTmline(TmLine tmline) {
		this.tmline = tmline;
	}

	@Column(name = "TM_WORK_TIME_ID")
	public TmWorktime getWorktime() {
		return worktime;
	}

	public void setWorktime(TmWorktime worktime) {
		this.worktime = worktime;
	}

	@Column(name = "TM_EQUIPMENT_ID")
	public TmEquipment getTmEquipment() {
		return tmEquipment;
	}

	public void setTmEquipment(TmEquipment tmEquipment) {
		this.tmEquipment = tmEquipment;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", updatable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "TM_CLASS_MANAGER_ID")
	public Integer getTmClassManagerId() {
		return tmClassManagerId;
	}

	public void setTmClassManagerId(Integer tmClassManagerId) {
		this.tmClassManagerId = tmClassManagerId;
	}

	@Column(name = "TM_CLASS_MANAGER_ID")
	public TmClassManager getClassManager() {
		return classManager;
	}

	public void setClassManager(TmClassManager classManager) {
		this.classManager = classManager;
	}

	@Transient
	public TmCodeRule getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(TmCodeRule codeRule) {
		this.codeRule = codeRule;
	}

}
