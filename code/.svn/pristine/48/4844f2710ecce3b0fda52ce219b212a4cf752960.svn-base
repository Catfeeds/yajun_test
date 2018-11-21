package com.wis.mes.master.bom.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.uloc.entity.TmUloc;

/**
 * @author liuzejun
 * 
 * @company 上海西信信息科技有限公司
 * 
 * @dete 2017-04-18
 * 
 * @desc BOM明细表
 */
@Table(name = "tm_bom_detail")
public class TmBomDetail extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** BOM ID */
	private Integer tmBomId;
	/** BOM */
	private TmBom bom = new TmBom();
	/** 工位ID */
	private Integer tmUlocId;
	/** 工位 */
	private TmUloc uloc = new TmUloc();
	/** 物料ID */
	private Integer tmPartId;
	/** 物料 */
	private TmPart part = new TmPart();
	/** 工位用量 */
	private Integer qty;
	/** 顺序号 */
	private Integer seq;
	/** 是物料追溯 */
	private String isTrac;
	/** 是批次追溯 */
	private String isBatchTrac;
	/** 备注 */
	private String note;

	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_BOM_DETAIL_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_BOM_ID")
	public Integer getTmBomId() {
		return tmBomId;
	}

	public void setTmBomId(Integer tmBomId) {
		this.tmBomId = tmBomId;
	}

	@Column(name = "TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}

	@Column(name = "TM_PART_ID")
	public Integer getTmPartId() {
		return tmPartId;
	}

	public void setTmPartId(Integer tmPartId) {
		this.tmPartId = tmPartId;
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

	@Column(name = "TM_BOM_ID")
	public TmBom getBom() {
		return bom;
	}

	public void setBom(TmBom bom) {
		this.bom = bom;
	}

	@Column(name = "TM_PART_ID")
	public TmPart getPart() {
		return part;
	}

	public void setPart(TmPart part) {
		this.part = part;
	}

	@Column(name = "TM_ULOC_ID")
	public TmUloc getUloc() {
		return uloc;
	}

	public void setUloc(TmUloc uloc) {
		this.uloc = uloc;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
