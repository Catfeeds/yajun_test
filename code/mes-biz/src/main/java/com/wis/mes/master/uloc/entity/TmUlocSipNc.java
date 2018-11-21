package com.wis.mes.master.uloc.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.entity.TmNcGroup;

/**
 *
 * @author 赵宪泉
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年5月25日
 *
 * @desc 表TmUlocSipNc
 *
 */
@Table(name = "TM_ULOC_SIP_NC")
public class TmUlocSipNc extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 质检点 */
	private Integer tmUlocSipId;
	private TmUlocSip tmUlocSip = new TmUlocSip();
	/** 不合格 */
	private Integer tmNcId;
	private TmNc nc = new TmNc();
	/** 不合格组id */
	private Integer TmNcGroupId;
	private TmNcGroup ncGroup = new TmNcGroup();
	/** 备注 */
	private String note;

	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_ULOC_SIP_NC_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_ULOC_SIP_ID")
	public Integer getTmUlocSipId() {
		return tmUlocSipId;
	}

	public void setTmUlocSipId(Integer tmUlocSipId) {
		this.tmUlocSipId = tmUlocSipId;
	}

	@Column(name = "TM_ULOC_SIP_ID")
	public TmUlocSip getTmUlocSip() {
		return tmUlocSip;
	}

	public void setTmUlocSip(TmUlocSip tmUlocSip) {
		this.tmUlocSip = tmUlocSip;
	}

	@Column(name = "TM_NC_ID")
	public Integer getTmNcId() {
		return tmNcId;
	}

	public void setTmNcId(Integer tmNcId) {
		this.tmNcId = tmNcId;
	}

	@Column(name = "TM_NC_ID")
	public TmNc getNc() {
		return nc;
	}

	public void setNc(TmNc nc) {
		this.nc = nc;
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
		return TmNcGroupId;
	}

	public void setTmNcGroupId(Integer tmNcGroupId) {
		TmNcGroupId = tmNcGroupId;
	}

	@Column(name = "TM_NC_GROUP_ID")
	public TmNcGroup getNcGroup() {
		return ncGroup;
	}

	public void setNcGroup(TmNcGroup ncGroup) {
		this.ncGroup = ncGroup;
	}

}
