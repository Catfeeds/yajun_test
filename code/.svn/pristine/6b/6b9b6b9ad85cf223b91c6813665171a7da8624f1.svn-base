package com.wis.mes.production.repair.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRepairUloc
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:34:09
 * 
 * @Description: 返修站点信息表
 */
@Table(name = "tp_repair_uloc")
public class TpRepairUloc extends AuditEntity {

	/** 主键 */
	private Integer id;
	/** 返修主表ID */
	private Integer tpRepairId;
	/** 工位编号 */
	private String ulocNo;
	/** 工位名称 */
	private String ulocName;
	/** 操作类型 */
	private String operationType;
	/** 工序在工艺路线中的顺序 */
	private String routingSeq;
	/** 备注 */
	private String note;

	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TP_REPAIR_ID")
	public Integer getTpRepairId() {
		return tpRepairId;
	}

	public void setTpRepairId(Integer tpRepairId) {
		this.tpRepairId = tpRepairId;
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

	@Column(name = "OPERATION_TYPE")
	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	@Column(name = "ROUTING_SEQ")
	public String getRoutingSeq() {
		return routingSeq;
	}

	public void setRoutingSeq(String routingSeq) {
		this.routingSeq = routingSeq;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
