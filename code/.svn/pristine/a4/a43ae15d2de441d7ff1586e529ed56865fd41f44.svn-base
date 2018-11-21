package com.wis.mes.production.record.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AbstractEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRecordUlocScrap
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:27:54
 * 
 * @Description: 产品档案报废信息表
 */
@Table(name = "tp_record_uloc_scrap")
public class TpRecordUlocScrap extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	private Integer tpRecordId;
	/** 产品档案表ID */
	private Integer tpRecordUlocId;
	/** 报废编号 */
	private String scrapCode;
	/** 报废备注 */
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

	@Column(name = "SCRAP_CODE")
	public String getScrapCode() {
		return scrapCode;
	}

	public void setScrapCode(String scrapCode) {
		this.scrapCode = scrapCode;
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
