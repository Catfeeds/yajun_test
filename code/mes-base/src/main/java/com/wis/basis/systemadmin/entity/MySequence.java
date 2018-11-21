package com.wis.basis.systemadmin.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AbstractEntity;

@Table(name = "TS_MY_SEQUENCE")
public class MySequence extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 461504587097091141L;
	private Integer id;
	private String module;
	private String generateDate;
	private String code;
	private Integer sn;

	@SequenceGenerator(name = "generator", sequenceName = "SEQ_MY_SEQUENCE_ID")
	@Column(name="ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="MODULE")
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	/**yyyyMMdd*/
	@Column(name="MY_DATE")
	public String getGenerateDate() {
		return generateDate;
	}

	public void setGenerateDate(String generateDate) {
		this.generateDate = generateDate;
	}
	@Transient
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Column(name="MY_SN")
	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}
	
}
