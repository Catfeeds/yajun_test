package com.wis.mes.production.wip.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpWip
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午3:02:16
 * 
 * @Description:生产在制
 */
@Table(name = "tp_wip")
public class TpWip extends AuditEntity {

	/** 主键id */
	private Integer id;
	/** SN唯一码 */
	private String sn;
	/** 工序在工艺路线中的顺序 */
	private String routingSeq;
	/** 站点ID */
	private Integer tmUlocId;
	/** 工位编号 */
	private String ulocNo;
	/** 工位名称 */
	private String ulocName;
	/** 工单编号 */
	private String porderNo;
	/** 生产序列号 */
	private String aviNo;
	/** 数量 */
	private Integer qty;
	/** 物料id */
	private Integer tmPartId;
	/** 物料编号 */
	private String partNo;
	/** 物料名称 */
	private String partName;
	/** 扫描时间 */
	private Date scanDate;
	/** 下一站点(逗号间隔) */
	private String nextUloc;
	/** 备注 */
	private String note;
	/** 是否hold */
	private String isHold;
	/** 状态（返修状态，等待生产，生产中） */
	private String status;

	@Override
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "SN")
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "ROUTING_SEQ")
	public String getRoutingSeq() {
		return routingSeq;
	}

	public void setRoutingSeq(String routingSeq) {
		this.routingSeq = routingSeq;
	}

	@Column(name = "TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}

	@Column(name = "ULOC_NO")
	public String getUlocNo() {
		return ulocNo;
	}

	public void setUlocNo(String ulocNo) {
		this.ulocNo = ulocNo;
	}

	@Column(name = "ULOC_NAME")
	public String getUlocName() {
		return ulocName;
	}

	public void setUlocName(String ulocName) {
		this.ulocName = ulocName;
	}

	@Column(name = "PORDER_NO")
	public String getPorderNo() {
		return porderNo;
	}

	public void setPorderNo(String porderNo) {
		this.porderNo = porderNo;
	}

	@Column(name = "AVI_NO")
	public String getAviNo() {
		return aviNo;
	}

	public void setAviNo(String aviNo) {
		this.aviNo = aviNo;
	}

	@Column(name = "QTY")
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	@Column(name = "TM_PART_ID")
	public Integer getTmPartId() {
		return tmPartId;
	}

	public void setTmPartId(Integer tmPartId) {
		this.tmPartId = tmPartId;
	}

	@Column(name = "PART_NO")
	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	@Column(name = "PART_NAME")
	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "SCAN_DATE")
	public Date getScanDate() {
		return scanDate;
	}

	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}

	@Column(name = "NEXT_ULOC")
	public String getNextUloc() {
		return nextUloc;
	}

	public void setNextUloc(String nextUloc) {
		this.nextUloc = nextUloc;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "IS_HOLD")
	public String getIsHold() {
		return isHold;
	}

	public void setIsHold(String isHold) {
		this.isHold = isHold;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
