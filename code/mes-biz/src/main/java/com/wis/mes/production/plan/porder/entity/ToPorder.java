package com.wis.mes.production.plan.porder.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.bom.entity.TmBom;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.path.entity.TmPath;
import com.wis.mes.master.plant.entity.TmPlant;

/**
 * 工单
 * 
 * @author liuzejun
 */
@Table(name = "to_porder")
public class ToPorder extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 工单编号 */
	private String no;
	/** 工厂ID */
	private Integer tmPlantId;
	/** 工厂 */
	private TmPlant plant;
	/** 物料ID */
	private Integer tmPartId;
	/** 物料 */
	private TmPart part;
	/** 数量 */
	private Integer qty;
	/** 工艺路径ID */
	private Integer tmPathId;
	/** 工艺路径 */
	private TmPath path;
	/** BOM id */
	private Integer tmBomId;
	/** BOM */
	private TmBom bom;
	/** 计划开始时间 */
	private Date planStartTime;
	/** 计划结束时间 */
	private Date planEndTime;
	/** 实际开始时间 */
	private Date factStartTime;
	/** 实际结束时间 */
	private Date factEndTime;
	/** 生产状态 */
	private String productStatus;
	/** 派工状态 */
	private String taskStatus;
	/** 入库状态 */
	private String instorageStatus;
	/** 上线数量 */
	private Integer onlineQty;
	/** 下线数量 */
	private Integer offlineQty;
	/** 报废数量 */
	private Integer scrapQty;
	/** 入库数量 */
	private Integer instorageQty;
	/** 优先级 */
	private String priority;
	/** 备注 */
	private String note;
	/** 关闭理由 */
	private String closeReason;
	/** 是否ERP下达 */
	private String isErp;
	/** 是否HOLD */
	private String isHold;

	private Integer taskQty;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PORDER_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Column(name = "TM_PLANT_ID")
	public Integer getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(Integer tmPlantId) {
		this.tmPlantId = tmPlantId;
	}

	@Column(name = "TM_PLANT_ID")
	public TmPlant getPlant() {
		return plant;
	}

	public void setPlant(TmPlant plant) {
		this.plant = plant;
	}

	@Column(name = "TM_PART_ID")
	public Integer getTmPartId() {
		return tmPartId;
	}

	public void setTmPartId(Integer tmPartId) {
		this.tmPartId = tmPartId;
	}

	@Column(name = "TM_PART_ID")
	public TmPart getPart() {
		return part;
	}

	public void setPart(TmPart part) {
		this.part = part;
	}

	@Column(name = "QTY")
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	@Column(name = "TM_PATH_ID")
	public Integer getTmPathId() {
		return tmPathId;
	}

	public void setTmPathId(Integer tmPathId) {
		this.tmPathId = tmPathId;
	}

	@Column(name = "TM_PATH_ID")
	public TmPath getPath() {
		return path;
	}

	public void setPath(TmPath path) {
		this.path = path;
	}

	@Column(name = "TM_BOM_ID")
	public Integer getTmBomId() {
		return tmBomId;
	}

	public void setTmBomId(Integer tmBomId) {
		this.tmBomId = tmBomId;
	}

	@Column(name = "TM_BOM_ID")
	public TmBom getBom() {
		return bom;
	}

	public void setBom(TmBom bom) {
		this.bom = bom;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "PLAN_START_TIME")
	public Date getPlanStartTime() {
		return planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "PLAN_END_TIME")
	public Date getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FACT_START_TIME")
	public Date getFactStartTime() {
		return factStartTime;
	}

	public void setFactStartTime(Date factStartTime) {
		this.factStartTime = factStartTime;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FACT_END_TIME")
	public Date getFactEndTime() {
		return factEndTime;
	}

	public void setFactEndTime(Date factEndTime) {
		this.factEndTime = factEndTime;
	}

	@Column(name = "PRODUCT_STATUS")
	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	@Column(name = "TASK_STATUS")
	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	@Column(name = "INSTORAGE_STATUS")
	public String getInstorageStatus() {
		return instorageStatus;
	}

	public void setInstorageStatus(String instorageStatus) {
		this.instorageStatus = instorageStatus;
	}

	@Column(name = "ONLINE_QTY")
	public Integer getOnlineQty() {
		return onlineQty;
	}

	public void setOnlineQty(Integer onlineQty) {
		this.onlineQty = onlineQty;
	}

	@Column(name = "OFFLINE_QTY")
	public Integer getOfflineQty() {
		return offlineQty;
	}

	public void setOfflineQty(Integer offlineQty) {
		this.offlineQty = offlineQty;
	}

	@Column(name = "SCRAP_QTY")
	public Integer getScrapQty() {
		return scrapQty;
	}

	public void setScrapQty(Integer scrapQty) {
		this.scrapQty = scrapQty;
	}

	@Column(name = "INSTORAGE_QTY")
	public Integer getInstorageQty() {
		return instorageQty;
	}

	public void setInstorageQty(Integer instorageQty) {
		this.instorageQty = instorageQty;
	}

	@Column(name = "PRIORITY")
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "CLOSE_REASON")
	public String getCloseReason() {
		return closeReason;
	}

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}

	@Column(name = "IS_ERP")
	public String getIsErp() {
		return isErp;
	}

	public void setIsErp(String isErp) {
		this.isErp = isErp;
	}

	@Column(name = "IS_HOLD")
	public String getIsHold() {
		return isHold;
	}

	public void setIsHold(String isHold) {
		this.isHold = isHold;
	}

	@Column(name = "TASK_QTY")
	public Integer getTaskQty() {
		return taskQty;
	}

	public void setTaskQty(Integer taskQty) {
		this.taskQty = taskQty;
	}

}
