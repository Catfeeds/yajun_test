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
 * @desc 工作日历-休息时间
 */
@Table(name = "tm_worktime_rest")
public class TmWorktimeRest extends AuditEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	 /** 工作日历ID */
	private Integer tmWorktimeId;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/** 工作日历bean引用 */
	private TmWorktime tmWorkTime;
	
	/** 备注*/
	private String remark;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_WORKTIME_REST_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_WORKTIME_ID")
	public Integer getTmWorktimeId() {
		return tmWorktimeId;
	}

	public void setTmWorktimeId(Integer tmWorktimeId) {
		this.tmWorktimeId = tmWorktimeId;
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

	@Column(name = "TM_WORKTIME_ID")
	public TmWorktime getTmWorkTime() {
		return tmWorkTime;
	}

	public void setTmWorkTime(TmWorktime tmWorkTime) {
		this.tmWorkTime = tmWorkTime;
	}

	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
