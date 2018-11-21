package com.wis.mes.production.untreated.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.entity.TmNcGroup;
import com.wis.mes.master.uloc.entity.TmUloc;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpUntreatedNc
 * 
 * @author liuzejun
 *
 * @date 2017年6月25日 下午3:29:59
 * 
 * @Description: 未出理缺陷表
 */
@Table(name = "tp_untreated_nc")
public class TpUntreatedNc extends AuditEntity {

	private Integer id;
	private String sn;
	private Integer tmUlocId;
	private String rountingSeq;
	private Integer tmNcGroupId;
	private Integer tmNcId;
	private String ncType;
	private String note;
	private TmUloc tmUloc;
	private TmNcGroup tmNcGroup;
	private TmNc   tmNc;
	
	@Override
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "SN")
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
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

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "NC_TYPE")
	public String getNcType() {
		return ncType;
	}

	public void setNcType(String ncType) {
		this.ncType = ncType;
	}

	@Column(name = "TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}
	
	@Column(name = "TM_ULOC_ID")
	public TmUloc getTmUloc() {
		return tmUloc;
	}

	public void setTmUloc(TmUloc tmUloc) {
		this.tmUloc = tmUloc;
	}

	@Column(name = "ROUNTING_SEQ")
	public String getRountingSeq() {
		return rountingSeq;
	}

	public void setRountingSeq(String rountingSeq) {
		this.rountingSeq = rountingSeq;
	}

}
