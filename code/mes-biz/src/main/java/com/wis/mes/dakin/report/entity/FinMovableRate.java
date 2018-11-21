package com.wis.mes.dakin.report.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AbstractEntity;

@Entity
@Table(name = "FIN_MOVABLE_RATE")
public class FinMovableRate extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer realRunRate;
	private Date createDate;
	private Date createTime;
	private Date createDateTime;
	private String shiftNo;
	private Integer targetValue;

	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "REAL_RUN_RATE")
	public Integer getRealRunRate() {
		return realRunRate;
	}

	public void setRealRunRate(Integer realRunRate) {
		this.realRunRate = realRunRate;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Column(name = "CREATE_TIME")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "SHIFT_NO")
	public String getShiftNo() {
		return shiftNo;
	}

	public void setShiftNo(String shiftNo) {
		this.shiftNo = shiftNo;
	}

	@Column(name = "TARGET_VALUE")
	public Integer getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Integer targetValue) {
		this.targetValue = targetValue;
	}

}
