package com.wis.mes.production.plan.porder.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.uloc.entity.TmUloc;

/**
 * 生产序列对应的BOM
 * 
 * @author liuzejun
 *
 */
@Table(name = "to_porder_avi_bom")
public class ToPorderAviBom extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 生产序列ID */
	private Integer toPorderAviId;
	/** 生产序列 */
	private ToPorderAvi avi;
	/** 工位ID */
	private Integer tmUlocId;
	/** 工位 */
	private TmUloc uloc;
	/** 物料ID */
	private Integer tmPartId;
	/** 物料 */
	private TmPart part;
	/** 数量 */
	private Integer qty;
	/** 顺序 */
	private Integer seq;
	/** 是否追溯 */
	private String isTrac;
	/** 是否批次追溯 */
	private String isBatchTrac;
	/** 备注 */
	private String note;

	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PORDER_AVI_BOM_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
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

	@Column(name = "SEQ")
	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Column(name = "IS_TRAC")
	public String getIsTrac() {
		return isTrac;
	}

	public void setIsTrac(String isTrac) {
		this.isTrac = isTrac;
	}

	@Column(name = "IS_BATCH_TRAC")
	public String getIsBatchTrac() {
		return isBatchTrac;
	}

	public void setIsBatchTrac(String isBatchTrac) {
		this.isBatchTrac = isBatchTrac;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
