package com.wis.mes.master.equipment.entity;

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
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

@Table(name = "tmp_equipment_status")
public class TmpEquipmentStatus extends AuditEntity {

	private static final long serialVersionUID = 6760236835936021049L;

	/**
	 * 主键
	 */
	private Integer id;

	private Long tmWorkTimeId;
	private TmWorktime tmWorktime;

	private Long tmLineId;
	private TmLine tmLine;

	private Long tmEquipmentId;
	private TmEquipment tmEquipment;

	private String runningState;

	private String statusNumber;

	private String statusContent;

	private String beginTime;

	private String finishTime;

	private String duration;

	private Long tmClassManagerId;
	private TmClassManager tmClassManager;
	
	private TmCodeRule codeRule;

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

	@Column(name = "TM_WORK_TIME_ID")
	public Long getTmWorkTimeId() {
		return tmWorkTimeId;
	}

	public void setTmWorkTimeId(Long tmWorkTimeId) {
		this.tmWorkTimeId = tmWorkTimeId;
	}

	@Column(name = "TM_LINE_ID")
	public Long getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Long tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "TM_EQUIPMENT_ID")
	public Long getTmEquipmentId() {
		return tmEquipmentId;
	}

	public void setTmEquipmentId(Long tmEquipmentId) {
		this.tmEquipmentId = tmEquipmentId;
	}

	@Column(name = "RUNNING_STATE")
	public String getRunningState() {
		return runningState;
	}

	public void setRunningState(String runningState) {
		this.runningState = runningState == null ? null : runningState.trim();
	}

	@Column(name = "STATUS_NUMBER")
	public String getStatusNumber() {
		return statusNumber;
	}

	public void setStatusNumber(String statusNumber) {
		this.statusNumber = statusNumber == null ? null : statusNumber.trim();
	}

	@Column(name = "STATUS_CONTENT")
	public String getStatusContent() {
		return statusContent;
	}

	public void setStatusContent(String statusContent) {
		this.statusContent = statusContent == null ? null : statusContent.trim();
	}

	@Column(name = "BEGIN_TIME")
	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime == null ? null : beginTime.trim();
	}

	@Column(name = "FINISH_TIME")
	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime == null ? null : finishTime.trim();
	}

	@Column(name = "DURATION")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration == null ? null : duration.trim();
	}

//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }

	@Column(name = "TM_CLASS_MANAGER_ID")
	public Long getTmClassManagerId() {
		return tmClassManagerId;
	}

	public void setTmClassManagerId(Long tmClassManagerId) {
		this.tmClassManagerId = tmClassManagerId;
	}

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_EQUIPMENT_STATUS")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_WORK_TIME_ID")
	public TmWorktime getTmWorktime() {
		return tmWorktime;
	}

	public void setTmWorktime(TmWorktime tmWorktime) {
		this.tmWorktime = tmWorktime;
	}

	@Column(name = "TM_LINE_ID")
	public TmLine getTmLine() {
		return tmLine;
	}

	public void setTmLine(TmLine tmLine) {
		this.tmLine = tmLine;
	}

	@Column(name = "TM_EQUIPMENT_ID")
	public TmEquipment getTmEquipment() {
		return tmEquipment;
	}

	public void setTmEquipment(TmEquipment tmEquipment) {
		this.tmEquipment = tmEquipment;
	}

	@Column(name = "TM_CLASS_MANAGER_ID")
	public TmClassManager getTmClassManager() {
		return tmClassManager;
	}

	public void setTmClassManager(TmClassManager tmClassManager) {
		this.tmClassManager = tmClassManager;
	}

	@Transient
	public TmCodeRule getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(TmCodeRule codeRule) {
		this.codeRule = codeRule;
	}

}