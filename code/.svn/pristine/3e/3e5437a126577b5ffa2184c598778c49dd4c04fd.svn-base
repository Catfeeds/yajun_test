package com.wis.mes.production.plan.porder.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.part.entity.TmPart;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderAviPathPart
 * 
 * @author liuzejun
 *
 * @date 2017年5月27日 下午1:37:05
 * 
 * @Description: 关键件实体
 */
@Table(name = "to_porder_avi_path_part")
public class ToPorderAviPathPart extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 生产序列工艺路径站点ID */
	private Integer toPorderAviPathId;
	/** 物料ID */
	private Integer tmPartId;
	/** 物料Bean */
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
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PORDER_PART_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TO_PORDER_AVI_PATH_ID")
	public Integer getToPorderAviPathId() {
		return toPorderAviPathId;
	}

	public void setToPorderAviPathId(Integer toPorderAviPathId) {
		this.toPorderAviPathId = toPorderAviPathId;
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
