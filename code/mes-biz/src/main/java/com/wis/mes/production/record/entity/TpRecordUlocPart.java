package com.wis.mes.production.record.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRecordUlocPart
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:25:37
 * 
 * @Description: 产品档案装配物料详情表
 */
@Table(name = "tp_record_uloc_part")
public class TpRecordUlocPart extends AuditEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 产品档案工位信息ID */
	private Integer tpRecordUlocId;
	private Integer tpRecordId;
	/** 物料ID */
	private Integer tmPartId;
	/** 物料编号 */
	private String partNo;
	/** 物料名称 */
	private String partName;
	/** 数量 */
	private Integer qty;
	/** 条码 */
	private String barCode;
	/** 备注 */
	private String note;
	/** 是否被替换 */
	private String isReplace;
	/** 替换数量 */
	private Integer perlaceQty;
	
	private TpRecord tpRecord;
	
	private TpRecordUloc tpRecordUloc;
	
	private String  replaceBarCode;//替换条码

	@Override
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TP_RECORD_ULOC_ID")
	public Integer getTpRecordUlocId() {
		return tpRecordUlocId;
	}

	public void setTpRecordUlocId(Integer tpRecordUlocId) {
		this.tpRecordUlocId = tpRecordUlocId;
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

	@Column(name = "QTY")
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	@Column(name = "BAR_CODE")
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "IS_REPLACE")
	public String getIsReplace() {
		return isReplace;
	}

	public void setIsReplace(String isReplace) {
		this.isReplace = isReplace;
	}

	@Column(name = "TP_RECORD_ID")
	public Integer getTpRecordId() {
		return tpRecordId;
	}

	public void setTpRecordId(Integer tpRecordId) {
		this.tpRecordId = tpRecordId;
	}
	
	@Column(name = "PERLACE_QTY")
	public Integer getPerlaceQty() {
		return perlaceQty;
	}

	public void setPerlaceQty(Integer perlaceQty) {
		this.perlaceQty = perlaceQty;
	}

	@Column(name = "TP_RECORD_ID")
	public TpRecord getTpRecord() {
		return tpRecord;
	}

	public void setTpRecord(TpRecord tpRecord) {
		this.tpRecord = tpRecord;
	}

	@Column(name = "TP_RECORD_ULOC_ID")
	public TpRecordUloc getTpRecordUloc() {
		return tpRecordUloc;
	}

	public void setTpRecordUloc(TpRecordUloc tpRecordUloc) {
		this.tpRecordUloc = tpRecordUloc;
	}

	@Column(name = "REPLACE_BAR_CODE")
	public String getReplaceBarCode() {
		return replaceBarCode;
	}

	public void setReplaceBarCode(String replaceBarCode) {
		this.replaceBarCode = replaceBarCode;
	}
	
}
