package com.wis.mes.production.metalplate.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.core.entity.Removable;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;

/**
 * @author CFY
 * @company 上海西信信息科技有限公司
 * @date 2018年7月9日
 * @desc 钣金生产管理，源列表信息bean
 */
@Table(name = "TB_MP_SOURCE_DATA")
public class TbMetalPlateSourceData extends AuditEntity implements Removable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 日期 */
	private String taskDate;
	/** 数据源 1.APC汇总 2.手动新增 3.系统追加 */
	private String dataType;
	/** 班次 */
	private String workSchedule;
	/** 型号 */
	private String model;
	/** APC汇总数量 */
	private Integer apcTotalNumber;
	/** 计划数量 */
	private Integer plannedProduction;
	/** 剩余库存数量 */
	private Integer inventoryNumber;
	/** 派工时段 */
	private String sendPeriodTime;
	/** 数据状态 0初始新增，1部分派工，2派工完成 */
	private Integer status;

	private Integer removed;

	private Integer materialId;
	private TmSheetMetalMaterial material;
	
	public TbMetalPlateSourceData(String taskDate, String dataType) {
		this.taskDate = taskDate;
		// this.workSchedule = workSchedule;
		this.dataType = dataType;
	}

	public TbMetalPlateSourceData() {

	}

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_MP_SOURCE_DATA_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TASK_DATE")
	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
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

	@Column(name = "APC_TOTAL_NUMBER")
	public Integer getApcTotalNumber() {
		return apcTotalNumber;
	}

	public void setApcTotalNumber(Integer apcTotalNumber) {
		this.apcTotalNumber = apcTotalNumber;
	}

	@Column(name = "INVENTORY_NUMBER")
	public Integer getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(Integer inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	@Column(name = "PLANNED_PRODUCTION")
	public Integer getPlannedProduction() {
		return plannedProduction;
	}

	public void setPlannedProduction(Integer plannedProduction) {
		this.plannedProduction = plannedProduction;
	}

	@Column(name = "SEND_PERIOD_TIME")
	public String getSendPeriodTime() {
		return sendPeriodTime;
	}

	public void setSendPeriodTime(String sendPeriodTime) {
		this.sendPeriodTime = sendPeriodTime;
	}

	@Column(name = "DATA_TYPE")
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	@Column(name = "MATERIAL_ID")
	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	@Column(name = "MATERIAL_ID")
	public TmSheetMetalMaterial getMaterial() {
		return material;
	}

	public void setMaterial(TmSheetMetalMaterial material) {
		this.material = material;
	}
	
	
}
