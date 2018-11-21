package com.wis.mes.master.workcalendar.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.classmanage.entity.TmClassManager;
/**
 * @author zhf
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2016年4月20日
 *
 * @desc 特殊日历
 */
@Table(name = "tm_work_special_date")
public class TmWorkSpecialDate extends AuditEntity {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 日期 */
	private Date specialDate;
	/** 类型（工作or休息） */
	private String type;
	/** 描述  */
	private String note;
	/** 启用 */
	private String enabled;
	
	/**班次id*/
	private Integer tmClassManagerId;
	
	/**班次对象*/
	private TmClassManager TmClassManager;
	
	/**开始时间*/
	private String startTime;
	
	/**结束时间*/
	private String endTime;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_WORK_SPECIAL_DATE_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "SPECIAL_DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getSpecialDate() {
		return specialDate;
	}

	public void setSpecialDate(Date specialDate) {
		this.specialDate = specialDate;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name ="TM_CLASS_MANAGER_ID")
	public Integer getTmClassManagerId() {
		return tmClassManagerId;
	}

	public void setTmClassManagerId(Integer tmClassManagerId) {
		this.tmClassManagerId = tmClassManagerId;
	}

	@Column(name ="TM_CLASS_MANAGER_ID")
	public TmClassManager getTmClassManager() {
		return TmClassManager;
	}

	public void setTmClassManager(TmClassManager tmClassManager) {
		TmClassManager = tmClassManager;
	}

	@Column(name="STARTTIME")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name="ENDTIME")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	

}
