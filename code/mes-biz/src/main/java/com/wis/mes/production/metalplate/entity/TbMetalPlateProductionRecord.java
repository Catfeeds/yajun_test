package com.wis.mes.production.metalplate.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.core.entity.Removable;

/**
 * @author CFY
 * @company 上海西信信息科技有限公司
 * @date 2018年7月9日
 * @desc 钣金生产管理，生产记录信息bean
 */
@Table(name = "TB_MP_PRODUCTION_RECORD")
public class TbMetalPlateProductionRecord extends AuditEntity implements Removable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 排产ID */
	private Integer schedulId;
	private TbMetalPlateSchedul schedul;
	/** 生产日期 */
	private String productionDate;
	/** 型号 */
	private String model;
	/** 批次 */
	private String batchNumber;
	/** 作业者 */
	private String workPeople;
	/** 班次 */
	private String workSchedule;
	/** 压机 */
	private String press;
	/** 模具 */
	private String moulds;
	/** 计划生产回数 */
	private Integer plannedCycles;
	/** 实际生产回数 */
	private Integer practicalCycles;
	/** 不良品数 */
	private Integer defectiveNumber;
	/** 库存消耗 */
	private Integer inventoryConsumption;
	/** 生产时间 */
	private String productionTime;
	/** 段取时间 */
	private String periodTakeTime;
	/** 放置时间 */
	private String placeTime;
	/** 报警回数 */
	private String alarmNumber;
	/** 作业开始时间 */
	private String startTime;
	/** 作业结束时间 */
	private String endTime;
	/** 状态 0初始化 1已录入不良 2已追加排产 */
	private String status;

	private Integer removed;

	/** 区域标识 */
	private String regionMark;
	/** 冲速 */
	private String velocity;
	/** 循环间隔时间 */
	private String cycleTime;
	/** 停止角度 */
	private String stopAngle;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_MP_PRODUCTION_RECORD_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "WORK_SCHEDULE")
	public String getWorkSchedule() {
		return workSchedule;
	}

	public void setWorkSchedule(String workSchedule) {
		this.workSchedule = workSchedule;
	}

	@Column(name = "MODEL")
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Column(name = "BATCH_NUMBER")
	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	@Column(name = "PRESS")
	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	@Column(name = "MOULDS")
	public String getMoulds() {
		return moulds;
	}

	public void setMoulds(String moulds) {
		this.moulds = moulds;
	}

	@Column(name = "PRODUCTION_DATE")
	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	@Column(name = "WORK_PEOPLE")
	public String getWorkPeople() {
		return workPeople;
	}

	public void setWorkPeople(String workPeople) {
		this.workPeople = workPeople;
	}

	@Column(name = "PLANNED_CYCLES")
	public Integer getPlannedCycles() {
		return plannedCycles;
	}

	public void setPlannedCycles(Integer plannedCycles) {
		this.plannedCycles = plannedCycles;
	}

	@Column(name = "PRACTICAL_CYCLES")
	public Integer getPracticalCycles() {
		return practicalCycles;
	}

	public void setPracticalCycles(Integer practicalCycles) {
		this.practicalCycles = practicalCycles;
	}

	@Column(name = "DEFECTIVE_NUMBER")
	public Integer getDefectiveNumber() {
		return defectiveNumber;
	}

	public void setDefectiveNumber(Integer defectiveNumber) {
		this.defectiveNumber = defectiveNumber;
	}

	@Column(name = "INVENTORY_CONSUMPTION")
	public Integer getInventoryConsumption() {
		return inventoryConsumption;
	}

	public void setInventoryConsumption(Integer inventoryConsumption) {
		this.inventoryConsumption = inventoryConsumption;
	}

	@Column(name = "PRODUCTION_TIME")
	public String getProductionTime() {
		return productionTime;
	}

	public void setProductionTime(String productionTime) {
		this.productionTime = productionTime;
	}

	@Column(name = "PERIOD_TAKE_TIME")
	public String getPeriodTakeTime() {
		return periodTakeTime;
	}

	public void setPeriodTakeTime(String periodTakeTime) {
		this.periodTakeTime = periodTakeTime;
	}

	@Column(name = "PLACE_TIME")
	public String getPlaceTime() {
		return placeTime;
	}

	public void setPlaceTime(String placeTime) {
		this.placeTime = placeTime;
	}

	@Column(name = "ALARM_NUMBER")
	public String getAlarmNumber() {
		return alarmNumber;
	}

	public void setAlarmNumber(String alarmNumber) {
		this.alarmNumber = alarmNumber;
	}

	@Column(name = "START_TIME")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "SCHEDUL_ID")
	public Integer getSchedulId() {
		return schedulId;
	}

	public void setSchedulId(Integer schedulId) {
		this.schedulId = schedulId;
	}

	@Column(name = "SCHEDUL_ID")
	public TbMetalPlateSchedul getSchedul() {
		return schedul;
	}

	public void setSchedul(TbMetalPlateSchedul schedul) {
		this.schedul = schedul;
	}

	@Column(name = "MARK_FOR_DELETE")
	public Integer getRemoved() {
		return removed;
	}

	@Override
	public boolean isEntityRemoved() {
		return (null == removed || removed == 0) ? false : true;
	}

	@Override
	public void setRemoved(Integer removed) {
		this.removed = removed;
	}

	@Column(name = "REGION_MARK")
	public String getRegionMark() {
		return regionMark;
	}

	public void setRegionMark(String regionMark) {
		this.regionMark = regionMark;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "VELOCITY")
	public String getVelocity() {
		return velocity;
	}

	public void setVelocity(String velocity) {
		this.velocity = velocity;
	}

	@Column(name = "CYCLE_TIME")
	public String getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(String cycleTime) {
		this.cycleTime = cycleTime;
	}

	@Column(name = "STOP_ANGLE")
	public String getStopAngle() {
		return stopAngle;
	}

	public void setStopAngle(String stopAngle) {
		this.stopAngle = stopAngle;
	}
}
