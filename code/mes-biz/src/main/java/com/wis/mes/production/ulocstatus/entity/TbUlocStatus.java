package com.wis.mes.production.ulocstatus.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.coderule.entity.TmCodeRule;
import com.wis.mes.master.employeeno.entity.TmEmployeeNo;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

@Table(name = "tb_uloc_status")
public class TbUlocStatus extends AuditEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 工作日历id **/
	private Integer tmWorktimeId;
	/** 班组管理 */
	private Integer tmClassManagerId;
	/** 班组管理 */
	private TmClassManager classManager;
	/** 工位ID **/
	private Integer tmUlocId;
	/** 作业员工号 **/
	private Integer employeeNumber;
	/** 工位状态 **/
	private String stationState;
	/** 开始时间 **/
	private String beginTime;
	/** 结束时间 **/
	private String finishTime;
	/** 时长 **/
	private String duration;
	/** 状态内容 **/
	private String statusContent;
	/** 状态代码 **/
	private String statusCode;
	/** 内容描述 **/
	private String contentDescription;
	/** 日期 */
	private Date productDate;
	/** 异常码 */
	private String abnormalSource;
	
	private Date createDate;
	private TmWorktime worktime;

	private TmUloc uloc;

	private TmEmployeeNo employeeNo;
	private TmCodeRule codeRule;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_ULOC_STATUS_ID")
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

	@Column(name = "TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}

	@Column(name = "EMPLOYEE_NUMBER")
	public Integer getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Integer employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	@Column(name = "STATION_STATE")
	public String getStationState() {
		return stationState;
	}

	public void setStationState(String stationState) {
		this.stationState = stationState;
	}

	@Column(name = "BEGIN_TIME")
	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	@Column(name = "FINISH_TIME")
	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "DURATION")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Column(name = "STATUS_CONTENT")
	public String getStatusContent() {
		return statusContent;
	}

	public void setStatusContent(String statusContent) {
		this.statusContent = statusContent;
	}

	@Column(name = "STATUS_CODE")
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	@Column(name = "CONTENT_DESCRIPTION")
	public String getContentDescription() {
		return contentDescription;
	}

	public void setContentDescription(String contentDescription) {
		this.contentDescription = contentDescription;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Column(name = "PRODUCT_DATE")
	public Date getProductDate() {
		return productDate;
	}

	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}

	@Column(name = "TM_WORK_TIME_ID")
	public TmWorktime getWorktime() {
		return worktime;
	}

	public void setWorktime(TmWorktime worktime) {
		this.worktime = worktime;
	}

	@Column(name = "TM_ULOC_ID")
	public TmUloc getUloc() {
		return uloc;
	}

	public void setUloc(TmUloc uloc) {
		this.uloc = uloc;
	}

	@Column(name = "EMPLOYEE_NUMBER")
	public TmEmployeeNo getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(TmEmployeeNo employeeNo) {
		this.employeeNo = employeeNo;
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

	@Column(name = "TM_CLASS_MANAGER_ID")
	public Integer getTmClassManagerId() {
		return tmClassManagerId;
	}

	public void setTmClassManagerId(Integer tmClassManagerId) {
		this.tmClassManagerId = tmClassManagerId;
	}

	@Column(name = "TM_CLASS_MANAGER_ID")
	public TmClassManager getClassManager() {
		return classManager;
	}

	public void setClassManager(TmClassManager classManager) {
		this.classManager = classManager;
	}

	@Column(name = "ABNORMAL_SOURCE")
	public String getAbnormalSource() {
		return abnormalSource;
	}

	public void setAbnormalSource(String abnormalSource) {
		this.abnormalSource = abnormalSource;
	}

	@Transient
	public TmCodeRule getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(TmCodeRule codeRule) {
		this.codeRule = codeRule;
	}

}
