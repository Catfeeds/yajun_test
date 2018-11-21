package com.wis.mes.production.metalplate.entity;

import javax.persistence.Transient;
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
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

/**
 * @author CFY
 * @company 上海西信信息科技有限公司
 * @date 2018年7月9日
 * @desc 钣金生产管理，排产列表信息bean
 */
@Table(name = "TB_MP_SCHEDUL")
public class TbMetalPlateSchedul extends AuditEntity implements Removable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 源列表ID */
	private Integer sourceDataId;
	private TbMetalPlateSourceData sourceData;
	/** 日期 */
	private String schedulDate;
	/** 班次 */
	private String workSchedule;
	/** 型号 */
	private String model;
	/** 批次 */
	private String batchNumber;
	/** 压机  冲床 */
	private String press;
	/** 模具 */
	private String moulds;
	/** 派工数 */
	private Integer dispatchNumber;
	/** 成品数 */
	private Integer finishNumber;
	/** 派工人员 */
	private String sendWorkers;
	/** 状态 0待产 1在产 2完成 3已下发 4已校验 
	 * 9左关联右的右数据,不做显示派发等操作，生产完成后直接进生产记录表 
	 */
	private Integer status;
	/** 派工时间 */
	private Date dispatchTime;
	/** 删除标记 */
	private Integer removed;

	/** 区域标识 */
	private String regionMark;
	/** 消耗APC汇总数 */
	private Integer consumeApcTotal;
	/** 关联ID */
	private Integer relationId;
	/** 排序 */
	private String sort;
	/** 生产顺位 */
	private String synPosition;
	/** 模具校验状态 */
	private String verify;

	/** 工作日历ID */
	private Integer tmWorktimeId;
	private TmWorktime tmWorkTime;
	/** 任务下发PLC时间 */
	private Integer productionTime;
	/** 任务开始生产时间 */
	private Date issuedTime;
	/** 实际生产耗时 */
	private Date startTime;

	private Integer materialId;
	private TmSheetMetalMaterial material;
	/**资产编号***/
	private String propertyNumbers;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_MP_SCHEDUL_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "SOURCE_DATA_ID")
	public Integer getSourceDataId() {
		return sourceDataId;
	}

	public void setSourceDataId(Integer sourceDataId) {
		this.sourceDataId = sourceDataId;
	}

	@Column(name = "SOURCE_DATA_ID")
	public TbMetalPlateSourceData getSourceData() {
		return sourceData;
	}

	public void setSourceData(TbMetalPlateSourceData sourceData) {
		this.sourceData = sourceData;
	}

	@Column(name = "SCHEDUL_DATE")
	public String getSchedulDate() {
		return schedulDate;
	}

	public void setSchedulDate(String schedulDate) {
		this.schedulDate = schedulDate;
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

	@Column(name = "DISPATCH_NUMBER")
	public Integer getDispatchNumber() {
		return dispatchNumber;
	}

	public void setDispatchNumber(Integer dispatchNumber) {
		this.dispatchNumber = dispatchNumber;
	}

	@Column(name = "FINISH_NUMBER")
	public Integer getFinishNumber() {
		return finishNumber;
	}

	public void setFinishNumber(Integer finishNumber) {
		this.finishNumber = finishNumber;
	}

	@Column(name = "SEND_WORKERS")
	public String getSendWorkers() {
		return sendWorkers;
	}

	public void setSendWorkers(String sendWorkers) {
		this.sendWorkers = sendWorkers;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DISPATCH_TIME")
	public Date getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
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

	@Column(name = "CONSUME_APC_TOTAL")
	public Integer getConsumeApcTotal() {
		return consumeApcTotal;
	}

	public void setConsumeApcTotal(Integer consumeApcTotal) {
		this.consumeApcTotal = consumeApcTotal;
	}

	@Column(name = "RELATION_ID")
	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}

	@Column(name = "SORT")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Column(name = "SYN_POSITION")
	public String getSynPosition() {
		return synPosition;
	}

	public void setSynPosition(String synPosition) {
		this.synPosition = synPosition;
	}

	@Column(name = "VERIFY")
	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	@Column(name = "WORK_TIME_ID")
	public Integer getTmWorktimeId() {
		return tmWorktimeId;
	}

	public void setTmWorktimeId(Integer tmWorktimeId) {
		this.tmWorktimeId = tmWorktimeId;
	}

	@Column(name = "WORK_TIME_ID")
	public TmWorktime getTmWorkTime() {
		return tmWorkTime;
	}

	public void setTmWorkTime(TmWorktime tmWorkTime) {
		this.tmWorkTime = tmWorkTime;
	}

	@Column(name = "PRODUCTION_TIME")
	public Integer getProductionTime() {
		return productionTime;
	}

	public void setProductionTime(Integer productionTime) {
		this.productionTime = productionTime;
	}

	@Column(name = "ISSUED_TIME")
	public Date getIssuedTime() {
		return issuedTime;
	}

	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
	}

	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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

	@Transient
	public String getPropertyNumbers() {
		return propertyNumbers;
	}

	public void setPropertyNumbers(String propertyNumbers) {
		this.propertyNumbers = propertyNumbers;
	}
	
}
