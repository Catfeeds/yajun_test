package com.wis.mes.production.record.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.uloc.entity.TmUloc;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRecordUloc
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:21:08
 * 
 * @Description: 产品档案站点信息表
 */
@Table(name = "tp_record_uloc")
public class TpRecordUloc extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 产品档案表ID */
	private Integer tpRecordId;
	private TpRecord record;
	/** 工位ID */
	private Integer tmUlocId;
	private TmUloc uloc;
	/** 工位编号 */
	private String ulocNo;
	/** 工位名称 */
	private String ulocName;
	/** 下一工位 */
	private String nextUloc;
	/** 班次 */
	private String shiftNo;
	/** 操作类型 */
	private String operationType;
	/** 工序在工艺路线中的顺序 */
	private String routingSeq;
	/** 备注 */
	private String note;

	@Override
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TP_RECORD_ID")
	public Integer getTpRecordId() {
		return tpRecordId;
	}

	public void setTpRecordId(Integer tpRecordId) {
		this.tpRecordId = tpRecordId;
	}

	@Column(name = "TP_RECORD_ID")
	public TpRecord getRecord() {
		return record;
	}

	public void setRecord(TpRecord record) {
		this.record = record;
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

	@Column(name = "SHIFT_NO")
	public String getShiftNo() {
		return shiftNo;
	}

	public void setShiftNo(String shiftNo) {
		this.shiftNo = shiftNo;
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

	@Column(name = "NEXT_ULOC")
	public String getNextUloc() {
		return nextUloc;
	}

	public void setNextUloc(String nextUloc) {
		this.nextUloc = nextUloc;
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

}
