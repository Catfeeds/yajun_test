package com.wis.mes.master.path.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.part.entity.TmPart;

/**
 * 工艺路径站点 关键件Entity
 * 
 * @author jiuxian
 *
 */
@Table(name = "tm_path_uloc_part")
public class TmPathUlocPart extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 工艺路径站点ID */
	private Integer tmPathUlocId;
	/** 工艺路径站点Bean */
	private TmPathUloc pathUloc;
	/** 物料ID */
	private Integer tmPartId;
	/** 物料bean */
	private TmPart part;
	/** 数量 */
	private Integer qty;
	/** 顺序号 */
	private Integer seq;
	/** 是否追溯 */
	private String isTrac;
	/** 是否批次追溯 */
	private String isBatchTrac;
	/** 节点ID */
	private String rectSeq;
	/** 备注 */
	private String note;

	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PATH_ULOC_PART_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_PATH_ULOC_ID")
	public Integer getTmPathUlocId() {
		return tmPathUlocId;
	}

	public void setTmPathUlocId(Integer tmPathUlocId) {
		this.tmPathUlocId = tmPathUlocId;
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

	@Column(name = "TM_PATH_ULOC_ID")
	public TmPathUloc getPathUloc() {
		return pathUloc;
	}

	public void setPathUloc(TmPathUloc pathUloc) {
		this.pathUloc = pathUloc;
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

	@Transient
	public String getRectSeq() {
		return rectSeq;
	}

	public void setRectSeq(String rectSeq) {
		this.rectSeq = rectSeq;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
