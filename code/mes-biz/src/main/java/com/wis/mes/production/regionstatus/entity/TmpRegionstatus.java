package com.wis.mes.production.regionstatus.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

@Table(name = "TMP_REGION_STATUS")
public class TmpRegionstatus extends AuditEntity {

	private static final long serialVersionUID = 6760236835936021049L;

	private Integer id;

	private Long tmWorkTimeId;
	private TmWorktime tmWorktime;

	private Long tmLineId;
	private TmLine tmLine;

	private String runningState;

	private String beginTime;

	private String finishTime;

	private String duration;

	private Long tmClassManagerId;
	
	private TmClassManager tmClassManager;
	
	private String regionNo;//区域
	
	private Integer tmPlantId;
	
	private TmPlant tmPlant;
	
	

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_REGION_STATUS_ID")
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


	@Column(name = "TM_CLASS_MANAGER_ID")
	public TmClassManager getTmClassManager() {
		return tmClassManager;
	}

	public void setTmClassManager(TmClassManager tmClassManager) {
		this.tmClassManager = tmClassManager;
	}

	@Column(name = "REGION_NO")
	public String getRegionNo() {
		return regionNo;
	}

	public void setRegionNo(String regionNo) {
		this.regionNo = regionNo;
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

	@Column(name = "RUNNING_STATE")
	public String getRunningState() {
		return runningState;
	}

	public void setRunningState(String runningState) {
		this.runningState = runningState;
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

	@Column(name = "TM_CLASS_MANAGER_ID")
	public Long getTmClassManagerId() {
		return tmClassManagerId;
	}

	public void setTmClassManagerId(Long tmClassManagerId) {
		this.tmClassManagerId = tmClassManagerId;
	}

	@Column(name = "TM_PLANT_ID")
	public Integer getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(Integer tmPlantId) {
		this.tmPlantId = tmPlantId;
	}

	@Column(name = "TM_PLANT_ID")
	public TmPlant getTmPlant() {
		return tmPlant;
	}

	public void setTmPlant(TmPlant tmPlant) {
		this.tmPlant = tmPlant;
	}
	
}