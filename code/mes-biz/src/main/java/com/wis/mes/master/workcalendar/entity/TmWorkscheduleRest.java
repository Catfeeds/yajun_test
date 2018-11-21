package com.wis.mes.master.workcalendar.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
/**
 * @author zhf
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2016年4月20日
 *
 * @desc 工作日历休息模板
 */
@Table(name = "tm_workschedule_rest")
public class TmWorkscheduleRest extends AuditEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 工作日历模板ID */
	private Integer tmWorkscheduleId;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/** 工作日历模板bean引用 */
	private TmWorkschedule tmWorkschedule;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_WORKSCHEDULE_REST_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_WORKSCHEDULE_ID")
	public Integer getTmWorkscheduleId() {
		return tmWorkscheduleId;
	}

	public void setTmWorkscheduleId(Integer tmWorkscheduleId) {
		this.tmWorkscheduleId = tmWorkscheduleId;
	}

	@Column(name = "START_TIME")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "TM_WORKSCHEDULE_ID")
	public TmWorkschedule getTmWorkschedule() {
		return tmWorkschedule;
	}

	public void setTmWorkschedule(TmWorkschedule tmWorkschedule) {
		this.tmWorkschedule = tmWorkschedule;
	}

}
