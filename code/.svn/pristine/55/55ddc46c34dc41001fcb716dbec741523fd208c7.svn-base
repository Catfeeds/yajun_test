package com.wis.mes.master.workcalendar.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.workshop.entity.TmWorkshop;
/**
 * @author zhf
 * @date 2016年4月20日
 * @desc 工作日历
 */
@Table(name = "tm_worktime")
public class TmWorktime extends AuditEntity {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	/** 工厂ID */
	private Integer tmPlantId;
	/** 车间ID */
	private Integer tmWorkshopId;
	/** 产线ID */
	private Integer tmLineId;
	/** 工作日期 */
	private Date workDate;
	/** 班次 */
	private String shiftno;
	/** 星期 */
	private String week;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/**old 计划上线数 
	 * zhuzw 2018-3-25
	 * 修改为 当日计划
	 * 
	 * */
	private Integer planOnlineQty;
	/** 计划下线数 */
	private Integer planOfflineQty;
	/** 参考JPH */
	private String jphQty;
	/** 启用 */
	private String enabled;
	/** 日历模板ID */
	private Integer tmWorkscheduleId;
	/** 工厂 bean引用 */
	private TmPlant plant = new TmPlant();
	/** 车间 bean引用 */
	private TmWorkshop workshop = new TmWorkshop();
	/** 产线bean引用 */
	private TmLine line = new TmLine();
	/** 日历模板bean引用 */
	private TmWorkschedule workSchedule;
	
	/** 班次*///to班组
	private Integer tmClassManagerId;
	
	/**班次*///to班组
	private TmClassManager tmClassManager;
	
	/** 备注*/
	private String remark;
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_WORKTIME_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_PLANT_ID")
	public Integer getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(Integer tmPlantId) {
		this.tmPlantId = tmPlantId;
	}

	@Column(name = "TM_WORKSHOP_ID")
	public Integer getTmWorkshopId() {
		return tmWorkshopId;
	}

	public void setTmWorkshopId(Integer tmWorkshopId) {
		this.tmWorkshopId = tmWorkshopId;
	}

	@Column(name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "WORK_DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	@Column(name = "SHIFTNO")
	public String getShiftno() {
		return shiftno;
	}

	public void setShiftno(String shiftno) {
		this.shiftno = shiftno;
	}

	@Column(name = "WEEK")
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}
	
//	@JsonFormat(pattern = "HH:mm")
//	@DateTimeFormat(pattern = "HH:mm")
//	@Temporal(TemporalType.TIME)
	@Column(name = "START_TIME")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

//	@JsonFormat(pattern = "HH:mm")
//	@DateTimeFormat(pattern = "HH:mm")
//	@Temporal(TemporalType.TIME)
	@Column(name = "END_TIME")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "PLAN_ONLINE_QTY")
	public Integer getPlanOnlineQty() {
		return planOnlineQty;
	}

	public void setPlanOnlineQty(Integer planOnlineQty) {
		this.planOnlineQty = planOnlineQty;
	}

	@Column(name = "PLAN_OFFLINE_QTY")
	public Integer getPlanOfflineQty() {
		return planOfflineQty;
	}

	public void setPlanOfflineQty(Integer planOfflineQty) {
		this.planOfflineQty = planOfflineQty;
	}

	@Column(name = "JPH_QTY")
	public String getJphQty() {
		return jphQty;
	}

	public void setJphQty(String jphQty) {
		this.jphQty = jphQty;
	}

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "TM_WORKSCHEDULE_ID")
	public Integer getTmWorkscheduleId() {
		return tmWorkscheduleId;
	}

	public void setTmWorkscheduleId(Integer tmWorkscheduleId) {
		this.tmWorkscheduleId = tmWorkscheduleId;
	}

	@Column(name = "TM_PLANT_ID")
	public TmPlant getPlant() {
		return plant;
	}

	public void setPlant(TmPlant plant) {
		this.plant = plant;
	}

	
	@Column(name = "TM_WORKSHOP_ID")
	public TmWorkshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(TmWorkshop workshop) {
		this.workshop = workshop;
	}
	
	@Column(name = "TM_LINE_ID")
	public TmLine getLine() {
		return line;
	}

	public void setLine(TmLine line) {
		this.line = line;
	}

	@Column(name = "TM_WORKSCHEDULE_ID")
	public TmWorkschedule getWorkSchedule() {
		return workSchedule;
	}

	public void setWorkSchedule(TmWorkschedule workSchedule) {
		this.workSchedule = workSchedule;
	}
	
	@Column(name="TM_CLASS_MANAGER_ID")
	public Integer getTmClassManagerId() {
		return tmClassManagerId;
	}

	public void setTmClassManagerId(Integer tmClassManagerId) {
		this.tmClassManagerId = tmClassManagerId;
	}

	@Column(name="TM_CLASS_MANAGER_ID")
	public TmClassManager getTmClassManager() {
		return tmClassManager;
	}

	public void setTmClassManager(TmClassManager tmClassManager) {
		this.tmClassManager = tmClassManager;
	}

	@Column(name ="REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
