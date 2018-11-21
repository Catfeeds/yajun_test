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
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.path.entity.TmPath;

/**
 * 生产序列
 * 
 * @author liuzejun
 *
 */
@Table(name = "to_porder_avi")
public class ToPorderAvi extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 工单ID */
	private Integer toPorderId;
	/** 工单 */
	private ToPorder porder;
	/** 生产序列编号 */
	private String no;
	/** 工艺路径ID */
	private Integer tmPathId;
	/** 工艺路 */
	private TmPath path;
	/** BOM id */
	private Integer tmBomId;
	/** BOM */
	private TmBom bom;
	/** 产线 ID */
	private Integer tmLineId;
	/** 产线bean */
	private TmLine line;
	/** 生产开始时间 */
	private Date productStartTime;
	/** 生产结束时间 */
	private Date productEndTime;
	/** 数量 */
	private Integer qty;
	/** 上线数量 */
	private Integer onlineQty;
	/** 下线数量 */
	private Integer offlineQty;
	/** 报废数量 */
	private Integer scrapQty;
	/** 状态 */
	private String status;
	/** 是否HOLD */
	private String isHold;
	/** 备注 */
	private String note;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PORDER_AVI_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TO_PORDER_ID")
	public Integer getToPorderId() {
		return toPorderId;
	}

	public void setToPorderId(Integer toPorderId) {
		this.toPorderId = toPorderId;
	}

	@Column(name = "TO_PORDER_ID")
	public ToPorder getPorder() {
		return porder;
	}

	public void setPorder(ToPorder porder) {
		this.porder = porder;
	}

	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
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

	@Column(name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "TM_LINE_ID")
	public TmLine getLine() {
		return line;
	}

	public void setLine(TmLine line) {
		this.line = line;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PRODUCT_START_TIME")
	public Date getProductStartTime() {
		return productStartTime;
	}

	public void setProductStartTime(Date productStartTime) {
		this.productStartTime = productStartTime;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PRODUCT_END_TIME")
	public Date getProductEndTime() {
		return productEndTime;
	}

	public void setProductEndTime(Date productEndTime) {
		this.productEndTime = productEndTime;
	}

	@Column(name = "QTY")
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
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

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "IS_HOLD")
	public String getIsHold() {
		return isHold;
	}

	public void setIsHold(String isHold) {
		this.isHold = isHold;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
