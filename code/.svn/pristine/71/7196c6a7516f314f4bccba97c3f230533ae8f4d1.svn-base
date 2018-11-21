package com.wis.mes.production.record.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRecordUlocQuality
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:26:48
 * 
 * @Description: 产品档案质检信息表
 */
@Table(name = "tp_record_uloc_quality")
public class TpRecordUlocQuality extends AuditEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	private Integer tpRecordId;
	/** 产品过点表主键 */
	private Integer tpRecordUlocId;
	/** 检查项目（参数code） */
	private String checkItem;
	/** 检查结果 */
	private String checkResult;
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

	@Column(name = "TP_RECORD_ULOC_ID")
	public Integer getTpRecordUlocId() {
		return tpRecordUlocId;
	}

	public void setTpRecordUlocId(Integer tpRecordUlocId) {
		this.tpRecordUlocId = tpRecordUlocId;
	}

	@Column(name = "CHECK_ITEM")
	public String getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}

	@Column(name = "CHECK_RESULT")
	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "TP_RECORD_ID")
	public Integer getTpRecordId() {
		return tpRecordId;
	}

	public void setTpRecordId(Integer tpRecordId) {
		this.tpRecordId = tpRecordId;
	}
}
