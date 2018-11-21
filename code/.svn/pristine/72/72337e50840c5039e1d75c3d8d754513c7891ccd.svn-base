package com.wis.mes.production.pmc.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

@Table(name = "tb_pmc")
public class TbPmc extends AuditEntity {

	private Integer id;
	/** 工作日历Id **/
	private Integer tmWorktimeId;
	/** 产线ID **/
	private Integer tmLineId;
	/** 当日计划 **/
	private String dayPlan;
	/** 现实计划 **/
	private String realityPlan;
	/** 现实完成 **/
	private String realityCompletion;
	/** 延误时间(min) **/
	private String delayTime;
	/** 日期 **/
	private Date productDate;

	private TmWorktime tmWorktime;
	
	private Date createTime;
	
	private Integer availabilityRate;
	
	private TmLine tmLine;
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PMC_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_WORK_TIME_ID")
	public Integer getTmWorktimeId() {
		return tmWorktimeId;
	}

	public void setTmWorktimeId(Integer tmWorktimeId) {
		this.tmWorktimeId = tmWorktimeId;
	}

	@Column(name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "DAY_PLAN")
	public String getDayPlan() {
		return dayPlan;
	}

	public void setDayPlan(String dayPlan) {
		this.dayPlan = dayPlan;
	}

	@Column(name = "REALITY_PLAN")
	public String getRealityPlan() {
		return realityPlan;
	}

	public void setRealityPlan(String realityPlan) {
		this.realityPlan = realityPlan;
	}

	@Column(name = "REALITY_COMPLETION")
	public String getRealityCompletion() {
		return realityCompletion;
	}

	public void setRealityCompletion(String realityCompletion) {
		this.realityCompletion = realityCompletion;
	}

	@Column(name = "DELAY_TIME")
	public String getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(String delayTime) {
		this.delayTime = delayTime;
	}

	@Column(name = "PRODUCT_DATE")
	public Date getProductDate() {
		return productDate;
	}

	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}

	@Column(name = "TM_WORK_TIME_ID")
	public TmWorktime getTmWorktime() {
		return tmWorktime;
	}

	public void setTmWorktime(TmWorktime tmWorktime) {
		this.tmWorktime = tmWorktime;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", updatable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "AVAILABILITY_RATE")
	public Integer getAvailabilityRate() {
		return availabilityRate;
	}

	public void setAvailabilityRate(Integer availabilityRate) {
		this.availabilityRate = availabilityRate;
	}

	@Column(name = "TM_LINE_ID")
	public TmLine getTmLine() {
		return tmLine;
	}

	public void setTmLine(TmLine tmLine) {
		this.tmLine = tmLine;
	}
}
