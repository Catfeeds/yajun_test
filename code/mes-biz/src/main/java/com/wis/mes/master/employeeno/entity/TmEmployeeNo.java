package com.wis.mes.master.employeeno.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AbstractEntity;

@Entity
@Table(name = "TM_EMPLOYEE_NO")
public class TmEmployeeNo extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String no;
	private String name;
	private Integer seniority;
	private String sex;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_EMPLOYEE_NO_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	
	@Column(name = "NAME_CN")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SENIORITY")
	public Integer getSeniority() {
		return seniority;
	}

	public void setSeniority(Integer seniority) {
		this.seniority = seniority;
	}

	@Column(name = "SEX")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public static TmEmployeeNo createName(String name){
		TmEmployeeNo bean = new TmEmployeeNo();
		bean.setName(name);
		return bean;
	}
	
	public static TmEmployeeNo createNo(String no){
		TmEmployeeNo bean = new TmEmployeeNo();
		bean.setNo(no);
		return bean;
	}
}
