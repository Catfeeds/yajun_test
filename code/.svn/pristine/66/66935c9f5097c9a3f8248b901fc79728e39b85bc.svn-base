package com.wis.mes.production.plan.porder.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.uloc.entity.TmUloc;

/**
 * 生产序列对应的工艺路径
 * 
 * @author liuzejun
 *
 */
@Table(name = "to_porder_avi_path")
public class ToPorderAviPath extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 生产序列ID */
	private Integer toPorderAviId;
	/** 生产序列 */
	private ToPorderAvi avi;
	/** 顺序号 */
	private String seq;
	/** 工位ID */
	private Integer tmUlocId;
	/** 工位 */
	private TmUloc uloc;
	/** 是否上线点 */
	private String isOnline;
	/** 是否下线点 */
	private String isOffline;
	/** 上一节点集合 */
	private String parentId;
	/** 操作时间 */
	private Long operateTime;
	/** 间隔下线时间 */
	private Long intervalTime;
	/** 是否质检点 */
	private String isSip;
	/** 是否自动入库 */
	private String isAutoInstorage;
	/** 入库等待时间 */
	private Long instorageTime;
	/** 是否自动报工 */
	private String isReported;
	/** 对应外系统编码 */
	private String erpUlocCode;
	/** 备注 */
	private String note;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PORDER_AVI_PATH_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TO_PORDER_AVI_ID")
	public Integer getToPorderAviId() {
		return toPorderAviId;
	}

	public void setToPorderAviId(Integer toPorderAviId) {
		this.toPorderAviId = toPorderAviId;
	}

	@Column(name = "TO_PORDER_AVI_ID")
	public ToPorderAvi getAvi() {
		return avi;
	}

	public void setAvi(ToPorderAvi avi) {
		this.avi = avi;
	}

	@Column(name = "SEQ")
	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	@Column(name = "TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}

	@Column(name = "TM_ULOC_ID")
	public TmUloc getUloc() {
		return uloc;
	}

	public void setUloc(TmUloc uloc) {
		this.uloc = uloc;
	}

	@Column(name = "IS_ONLINE")
	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	@Column(name = "IS_OFFLINE")
	public String getIsOffline() {
		return isOffline;
	}

	public void setIsOffline(String isOffline) {
		this.isOffline = isOffline;
	}

	@Column(name = "PARENT_ID")
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "OPERATE_TIME")
	public Long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Long operateTime) {
		this.operateTime = operateTime;
	}

	@Column(name = "INTERVAL_TIME")
	public Long getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Long intervalTime) {
		this.intervalTime = intervalTime;
	}

	@Column(name = "IS_SIP")
	public String getIsSip() {
		return isSip;
	}

	public void setIsSip(String isSip) {
		this.isSip = isSip;
	}

	@Column(name = "IS_AUTO_INSTORAGE")
	public String getIsAutoInstorage() {
		return isAutoInstorage;
	}

	public void setIsAutoInstorage(String isAutoInstorage) {
		this.isAutoInstorage = isAutoInstorage;
	}

	@Column(name = "INSTORAGE_TIME")
	public Long getInstorageTime() {
		return instorageTime;
	}

	public void setInstorageTime(Long instorageTime) {
		this.instorageTime = instorageTime;
	}

	@Column(name = "IS_REPORTED")
	public String getIsReported() {
		return isReported;
	}

	public void setIsReported(String isReported) {
		this.isReported = isReported;
	}

	@Column(name = "ERP_ULOC_CODE")
	public String getErpUlocCode() {
		return erpUlocCode;
	}

	public void setErpUlocCode(String erpUlocCode) {
		this.erpUlocCode = erpUlocCode;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
