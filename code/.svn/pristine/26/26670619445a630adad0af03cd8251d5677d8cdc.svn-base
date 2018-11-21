package com.wis.mes.production.producttracing.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.basis.systemadmin.entity.Employee;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.uloc.entity.TmUloc;

/**
 * 岗位时间
 * 
 * @author zhuzw
 *
 */
@Table(name = "TB_PRODUCT_STATION")
public class TbProductStation extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 产品id */
	private Integer tbProductTracingId;
	/** 工位ID */
	private Integer tmUlocId;
	/** 作业员ID */
	private Integer staffUserId;
	/** 流入开始时间 */
	private String enterBeginTime;
	/** 流入结束时间 */
	private String enterFinishTime;
	/** 流出开始时间 */
	private String outBeginTime;
	/** 流出结束时间 */
	private String outFinishTime;
	/** 设备工作开始时间 */
	private String equipmentBeginTime;
	/** 设备工作结束时间 */
	private String equipmentFinishTime;
	private Long equipmentWorkDuration;
	/** 工位作业时间 */
	private Long stationWorkDuration;
	/** 员工作业时间 */
	private Long staffWorkDuration;
	/** 工位作业完成时间 */
	private String stationFinishTime;
	/** 前等工时间(秒) */
	private Long frontWaitDuration;
	/** 后等工时间（秒） */
	private Long backWaitDuration;
	/** 异常时间（秒） */
	private Long abnormalDuration;
	/** 异常故障码 */
	private String abnormalCode;
	/** 警告时间（秒） */
	private Long warningDuration;
	/** OK还是NG及其原因 */
	private String okNgResult;
	/** NG流入的入口号 */
	private String ngEntryNumber;
	/** NG入口流入时间 */
	private String ngEntryTime;
	/** NG流出的出口号 */
	private String ngExitNumber;
	/** NG出口流出时间 */
	private String ngExitTime;
	private String sn;
	private String exitNo;
	private Long processingValue;
	private TmUloc uloc;
	private Employee employee;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PRODUCT_STATION_ID")
	@Column(name = "ID")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TB_PRODUCT_TRACING_ID")
	public Integer getTbProductTracingId() {
		return tbProductTracingId;
	}

	public void setTbProductTracingId(Integer tbProductTracingId) {
		this.tbProductTracingId = tbProductTracingId;
	}

	@Column(name = "TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}

	@Column(name = "STAFF_USER_ID")
	public Integer getStaffUserId() {
		return staffUserId;
	}

	public void setStaffUserId(Integer staffUserId) {
		this.staffUserId = staffUserId;
	}

	@Column(name = "ENTER_BEGIN_TIME")
	public String getEnterBeginTime() {
		return enterBeginTime;
	}

	public void setEnterBeginTime(String enterBeginTime) {
		this.enterBeginTime = enterBeginTime;
	}

	@Column(name = "ENTER_FINISH_TIME")
	public String getEnterFinishTime() {
		return enterFinishTime;
	}

	public void setEnterFinishTime(String enterFinishTime) {
		this.enterFinishTime = enterFinishTime;
	}

	@Column(name = "OUT_BEGIN_TIME")
	public String getOutBeginTime() {
		return outBeginTime;
	}

	public void setOutBeginTime(String outBeginTime) {
		this.outBeginTime = outBeginTime;
	}

	@Column(name = "OUT_FINISH_TIME")
	public String getOutFinishTime() {
		return outFinishTime;
	}

	public void setOutFinishTime(String outFinishTime) {
		this.outFinishTime = outFinishTime;
	}

	@Column(name = "EQUIPMENT_BEGIN_TIME")
	public String getEquipmentBeginTime() {
		return equipmentBeginTime;
	}

	public void setEquipmentBeginTime(String equipmentBeginTime) {
		this.equipmentBeginTime = equipmentBeginTime;
	}

	@Column(name = "EQUIPMENT_FINISH_TIME")
	public String getEquipmentFinishTime() {
		return equipmentFinishTime;
	}

	public void setEquipmentFinishTime(String equipmentFinishTime) {
		this.equipmentFinishTime = equipmentFinishTime;
	}

	@Column(name = "EQUIPMENT_WORK_DURATION")
	public Long getEquipmentWorkDuration() {
		return equipmentWorkDuration;
	}

	public void setEquipmentWorkDuration(Long equipmentWorkDuration) {
		this.equipmentWorkDuration = equipmentWorkDuration;
	}

	@Column(name = "STATION_WORK_DURATION")
	public Long getStationWorkDuration() {
		return stationWorkDuration;
	}

	public void setStationWorkDuration(Long stationWorkDuration) {
		this.stationWorkDuration = stationWorkDuration;
	}

	@Column(name = "STAFF_WORK_DURATION")
	public Long getStaffWorkDuration() {
		return staffWorkDuration;
	}

	public void setStaffWorkDuration(Long staffWorkDuration) {
		this.staffWorkDuration = staffWorkDuration;
	}

	@Column(name = "STATION_FINISH_TIME")
	public String getStationFinishTime() {
		return stationFinishTime;
	}

	public void setStationFinishTime(String stationFinishTime) {
		this.stationFinishTime = stationFinishTime;
	}

	@Column(name = "FRONT_WAIT_DURATION")
	public Long getFrontWaitDuration() {
		return frontWaitDuration;
	}

	public void setFrontWaitDuration(Long frontWaitDuration) {
		this.frontWaitDuration = frontWaitDuration;
	}

	@Column(name = "BACK_WAIT_DURATION")
	public Long getBackWaitDuration() {
		return backWaitDuration;
	}

	public void setBackWaitDuration(Long backWaitDuration) {
		this.backWaitDuration = backWaitDuration;
	}

	@Column(name = "ABNORMAL_DURATION")
	public Long getAbnormalDuration() {
		return abnormalDuration;
	}

	public void setAbnormalDuration(Long abnormalDuration) {
		this.abnormalDuration = abnormalDuration;
	}

	@Column(name = "ABNORMAL_CODE")
	public String getAbnormalCode() {
		return abnormalCode;
	}

	public void setAbnormalCode(String abnormalCode) {
		this.abnormalCode = abnormalCode;
	}

	@Column(name = "WARNING_DURATION")
	public Long getWarningDuration() {
		return warningDuration;
	}

	public void setWarningDuration(Long warningDuration) {
		this.warningDuration = warningDuration;
	}

	@Column(name = "OK_NG_RESULT")
	public String getOkNgResult() {
		return okNgResult;
	}

	public void setOkNgResult(String okNgResult) {
		this.okNgResult = okNgResult;
	}

	@Column(name = "NG_ENTRY_NUMBER")
	public String getNgEntryNumber() {
		return ngEntryNumber;
	}

	public void setNgEntryNumber(String ngEntryNumber) {
		this.ngEntryNumber = ngEntryNumber;
	}

	@Column(name = "NG_ENTRY_TIME")
	public String getNgEntryTime() {
		return ngEntryTime;
	}

	public void setNgEntryTime(String ngEntryTime) {
		this.ngEntryTime = ngEntryTime;
	}

	@Column(name = "NG_EXIT_NUMBER")
	public String getNgExitNumber() {
		return ngExitNumber;
	}

	public void setNgExitNumber(String ngExitNumber) {
		this.ngExitNumber = ngExitNumber;
	}

	@Column(name = "NG_EXIT_TIME")
	public String getNgExitTime() {
		return ngExitTime;
	}

	public void setNgExitTime(String ngExitTime) {
		this.ngExitTime = ngExitTime;
	}

	@Column(name = "TM_ULOC_ID")
	public TmUloc getUloc() {
		return uloc;
	}

	public void setUloc(TmUloc uloc) {
		this.uloc = uloc;
	}

	@Column(name = "STAFF_USER_ID")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Column(name = "SN")
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "PROCESSING_VALUE")
	public Long getProcessingValue() {
		return processingValue;
	}

	public void setProcessingValue(Long processingValue) {
		this.processingValue = processingValue;
	}

	@Column(name = "EXIT_NO")
	public String getExitNo() {
		return exitNo;
	}

	public void setExitNo(String exitNo) {
		this.exitNo = exitNo;
	}

}
