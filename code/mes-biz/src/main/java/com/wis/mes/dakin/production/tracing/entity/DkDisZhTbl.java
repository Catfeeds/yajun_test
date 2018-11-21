package com.wis.mes.dakin.production.tracing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AbstractEntity;
import com.wis.mes.master.part.entity.TmPart;

/**
 * 
 * @author liuzejun
 *
 * @desc 物料绑定数据
 * 
 */
@Entity
@Table(name = "DK_DIS_ZH_TBL")
public class DkDisZhTbl extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 背番号 */
	private String bfid;
	/** 机号 */
	private String jh;
	/** 物料背番号 */
	private String bbfid;
	/** 物料图号 */
	private String bjzName;
	/** 物料种类区分 */
	private String bzl;
	/** 物料照合对比时间 */
	private String tzh;
	private TmPart part;

	@Column(name = "SORT_ID")
	@Transient
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "JH")
	public String getJh() {
		return jh;
	}

	public void setJh(String jh) {
		this.jh = jh;
	}

	@Column(name = "BBFID")
	public String getBbfid() {
		return bbfid;
	}

	public void setBbfid(String bbfid) {
		this.bbfid = bbfid;
	}

	@Column(name = "BJZNAME")
	public String getBjzName() {
		return bjzName;
	}

	public void setBjzName(String bjzName) {
		this.bjzName = bjzName;
	}

	@Column(name = "BZL")
	public String getBzl() {
		return bzl;
	}

	public void setBzl(String bzl) {
		this.bzl = bzl;
	}

	@Column(name = "TZH")
	public String getTzh() {
		return tzh;
	}

	public void setTzh(String tzh) {
		this.tzh = tzh;
	}

	@Column(name = "BFID")
	public String getBfid() {
		return bfid;
	}

	public void setBfid(String bfid) {
		this.bfid = bfid;
	}

	@Column(name = "BZL")
	public TmPart getPart() {
		return part;
	}

	public void setPart(TmPart part) {
		this.part = part;
	}

}
