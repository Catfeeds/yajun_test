package com.wis.mes.production.record.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.entity.TmNcGroup;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRecordUlocNc
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:22:16
 * 
 * @Description: 产品档案不合格信息表
 */
@Table(name = "tp_record_uloc_nc")
public class TpRecordUlocNc extends AuditEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	private Integer tpRecordId;
	/** 产品档案表ID */
	private Integer tpRecordUlocId;
	/** 不合格组代码 */
	private Integer tmNcGroupId;
	/** 不合格组代码引用 */
	private TmNcGroup tmNcGroup;
	/** 不合格代码 */
	private Integer tmNcId;
	/** 不合格代码引用 */
	private TmNc tmNc;

	/** 不合格备注 */
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

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "TM_NC_GROUP_ID")
	public Integer getTmNcGroupId() {
		return tmNcGroupId;
	}

	public void setTmNcGroupId(Integer tmNcGroupId) {
		this.tmNcGroupId = tmNcGroupId;
	}

	@Column(name = "TM_NC_ID")
	public Integer getTmNcId() {
		return tmNcId;
	}

	public void setTmNcId(Integer tmNcId) {
		this.tmNcId = tmNcId;
	}

	@Column(name = "TP_RECORD_ID")
	public Integer getTpRecordId() {
		return tpRecordId;
	}

	public void setTpRecordId(Integer tpRecordId) {
		this.tpRecordId = tpRecordId;
	}
	
	@Column(name = "TM_NC_GROUP_ID")
	public TmNcGroup getTmNcGroup() {
		return tmNcGroup;
	}

	public void setTmNcGroup(TmNcGroup tmNcGroup) {
		this.tmNcGroup = tmNcGroup;
	}
	
	@Column(name = "TM_NC_ID")
	public TmNc getTmNc() {
		return tmNc;
	}

	public void setTmNc(TmNc tmNc) {
		this.tmNc = tmNc;
	}

}
