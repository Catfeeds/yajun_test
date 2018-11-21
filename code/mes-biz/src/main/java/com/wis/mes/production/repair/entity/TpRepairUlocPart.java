package com.wis.mes.production.repair.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRepairUlocPart
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:36:45
 * 
 * @Description: 返修装配物料详情表
 */
@Table(name = "tp_repair_uloc_part")
public class TpRepairUlocPart extends AuditEntity {

	/** 主键ID */
	private Integer id;
	/** 返修过点表主键ID */
	private Integer tpRepairUlocId;
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

	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TP_REPAIR_ULOC_ID")
	public Integer getTpRepairUlocId() {
		return tpRepairUlocId;
	}

	public void setTpRepairUlocId(Integer tpRepairUlocId) {
		this.tpRepairUlocId = tpRepairUlocId;
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

}
