package com.wis.mes.dakin.production.tracing.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.wis.core.entity.AbstractEntity;

@Entity
@Table(name = "VB_PRODUCT_OPERATION")
public class VbProductOperation extends AbstractEntity {

	private static final long serialVersionUID = 7957491783946638209L;
	private Integer id;
	/** 事部 **/
	private String plantNo;
	/** 产线 **/
	private String lineName;
	/** 产线编码 **/
	private String lineNo;
	/** 生产追溯创建日期 **/
	private Date createTime;
	/** 生产追溯下线日期 **/
	private Date updateTime;
	/** 班次 **/
	private String shiftno;
	/** 背番号 **/
	private String backNumber;
	/** 机种名 **/
	private String machineOfName;
	/** 机号 **/
	private String machineName;
	/** 工装板id **/
	private String tpId;
	/** 工位编号 **/
	private String ulocNo;
	/** 加工工序 **/
	private String ulocName;
	/** 作业员工号 **/
	private String employeeNo;
	/** 配套设备编号 **/
	private String equipmentNo;
	/** 配套设备名称 **/
	private String equipmentName;
	/** 流入开始时间 **/
	private String enterBeginTime;
	/** 流入结束时间 **/
	private String enterFinishTime;
	/** 设备工作开始时间 **/
	private String equipmentBeginTime;
	/** 设备工作结束时间 **/
	private String equipmentFinishTime;
	/** 工位作业完成时间 **/
	private String stationFinishTime;
	/** 流出开始时间 **/
	private String outBeginTime;
	/** 流出结束时间 **/
	private String outFinishTime;
	/** 工位作业时间 (秒) **/
	private Long stationWorkDuration;
	/** 设备作业时间 */
	private Long equipmentWorkDuration;
	/** 员工作业时间 (秒) **/
	private Long staffWorkDuration;
	/** 前等工时间 (秒) **/
	private Long fontWaitDuration;
	/** 后等工时间 (秒) **/
	private Long backWaitDuration;
	/** 异常时间 (秒) **/
	private Long abnormalDuration;
	/** 警告时间 (秒) **/
	private Long warningDuration;
	/** 异常故障码 */
	private String abnormalCode;
	/** OK还是NG及其原因 */
	private String okNgResult;
	/** 故障代码ID **/
	private String ncGroupNo;
	private String ncGroupName;
	/** 故障内容ID **/
	private String ncName;
	/** 信息来源 **/
	private String infoSources;
	/** NG出口 **/
	private String ngExitNumber;
	/** NG口流出时刻 **/
	private String ngExitTime;
	/** NG入口 **/
	private String ngEntryNumber;
	/** NG口流入时刻 **/
	private String ngEntryTime;
	private Date ulocCreateTime;
	
	private Integer unhealthyCount;//不良区分(次)

	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "plantNo")
	public String getPlantNo() {
		return plantNo;
	}

	public void setPlantNo(String plantNo) {
		this.plantNo = plantNo;
	}

	@Column(name = "lineName")
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	@Column(name = "lineNo")
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	@Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "updateTime")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	
	
	@Column(name = "shiftno")
	public String getShiftno() {
		return shiftno;
	}

	public void setShiftno(String shiftno) {
		this.shiftno = shiftno;
	}

	@Column(name = "backNumber")
	public String getBackNumber() {
		return backNumber;
	}

	public void setBackNumber(String backNumber) {
		this.backNumber = backNumber;
	}

	@Column(name = "machineOfName")
	public String getMachineOfName() {
		return machineOfName;
	}

	public void setMachineOfName(String machineOfName) {
		this.machineOfName = machineOfName;
	}

	@Column(name = "machineName")
	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	@Column(name = "tpId")
	public String getTpId() {
		return tpId;
	}

	public void setTpId(String tpId) {
		this.tpId = tpId;
	}

	@Column(name = "ulocNo")
	public String getUlocNo() {
		return ulocNo;
	}

	public void setUlocNo(String ulocNo) {
		this.ulocNo = ulocNo;
	}

	@Column(name = "ulocName")
	public String getUlocName() {
		return ulocName;
	}

	public void setUlocName(String ulocName) {
		this.ulocName = ulocName;
	}

	@Column(name = "employeeNo")
	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	@Column(name = "equipmentNo")
	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	@Column(name = "equipmentName")
	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	@Column(name = "enterBeginTime")
	public String getEnterBeginTime() {
		return enterBeginTime;
	}

	public void setEnterBeginTime(String enterBeginTime) {
		this.enterBeginTime = enterBeginTime;
	}

	@Column(name = "enterFinishTime")
	public String getEnterFinishTime() {
		return enterFinishTime;
	}

	public void setEnterFinishTime(String enterFinishTime) {
		this.enterFinishTime = enterFinishTime;
	}

	@Column(name = "equipmentBeginTime")
	public String getEquipmentBeginTime() {
		return equipmentBeginTime;
	}

	public void setEquipmentBeginTime(String equipmentBeginTime) {
		this.equipmentBeginTime = equipmentBeginTime;
	}

	@Column(name = "equipmentFinishTime")
	public String getEquipmentFinishTime() {
		return equipmentFinishTime;
	}

	public void setEquipmentFinishTime(String equipmentFinishTime) {
		this.equipmentFinishTime = equipmentFinishTime;
	}

	@Column(name = "stationFinishTime")
	public String getStationFinishTime() {
		return stationFinishTime;
	}

	public void setStationFinishTime(String stationFinishTime) {
		this.stationFinishTime = stationFinishTime;
	}

	@Column(name = "outBeginTime")
	public String getOutBeginTime() {
		return outBeginTime;
	}

	public void setOutBeginTime(String outBeginTime) {
		this.outBeginTime = outBeginTime;
	}

	@Column(name = "outFinishTime")
	public String getOutFinishTime() {
		return outFinishTime;
	}

	public void setOutFinishTime(String outFinishTime) {
		this.outFinishTime = outFinishTime;
	}

	@Column(name = "stationWorkDuration")
	public Long getStationWorkDuration() {
		if(null == stationWorkDuration)stationWorkDuration=0l;
		return stationWorkDuration;
	}

	public void setStationWorkDuration(Long stationWorkDuration) {
		this.stationWorkDuration = stationWorkDuration;
	}

	@Column(name = "equipmentWorkDuration")
	public Long getEquipmentWorkDuration() {
		if(null == equipmentWorkDuration)equipmentWorkDuration=0l;
		return equipmentWorkDuration;
	}

	public void setEquipmentWorkDuration(Long equipmentWorkDuration) {
		this.equipmentWorkDuration = equipmentWorkDuration;
	}

	@Column(name = "staffWorkDuration")
	public Long getStaffWorkDuration() {
		if(null == staffWorkDuration)staffWorkDuration=0l;
		return staffWorkDuration;
	}

	public void setStaffWorkDuration(Long staffWorkDuration) {
		this.staffWorkDuration = staffWorkDuration;
	}

	@Column(name = "fontWaitDuration")
	public Long getFontWaitDuration() {
		if(null == fontWaitDuration)fontWaitDuration=0l;
		return fontWaitDuration;
	}

	public void setFontWaitDuration(Long fontWaitDuration) {
		this.fontWaitDuration = fontWaitDuration;
	}

	@Column(name = "backWaitDuration")
	public Long getBackWaitDuration() {
		if(null == backWaitDuration)backWaitDuration=0l;
		return backWaitDuration;
	}

	public void setBackWaitDuration(Long backWaitDuration) {
		this.backWaitDuration = backWaitDuration;
	}

	@Column(name = "abnormalDuration")
	public Long getAbnormalDuration() {
		if(null == abnormalDuration)abnormalDuration=0l;
		return abnormalDuration;
	}

	public void setAbnormalDuration(Long abnormalDuration) {
		this.abnormalDuration = abnormalDuration;
	}

	@Column(name = "warningDuration")
	public Long getWarningDuration() {
		if(null == warningDuration)warningDuration=0l;
		return warningDuration;
	}

	public void setWarningDuration(Long warningDuration) {
		this.warningDuration = warningDuration;
	}

	@Column(name = "abnormalCode")
	public String getAbnormalCode() {
		return abnormalCode;
	}

	public void setAbnormalCode(String abnormalCode) {
		this.abnormalCode = abnormalCode;
	}

	@Column(name = "okNgResult")
	public String getOkNgResult() {
		return okNgResult;
	}

	public void setOkNgResult(String okNgResult) {
		this.okNgResult = okNgResult;
	}

	@Column(name = "ncGroupNo")
	public String getNcGroupNo() {
		return ncGroupNo;
	}

	public void setNcGroupNo(String ncGroupNo) {
		this.ncGroupNo = ncGroupNo;
	}

	@Column(name = "ncGroupName")
	public String getNcGroupName() {
		return ncGroupName;
	}

	public void setNcGroupName(String ncGroupName) {
		this.ncGroupName = ncGroupName;
	}

	@Column(name = "ncName")
	public String getNcName() {
		return ncName;
	}

	public void setNcName(String ncName) {
		this.ncName = ncName;
	}

	@Column(name = "infoSources")
	public String getInfoSources() {
		return infoSources;
	}

	public void setInfoSources(String infoSources) {
		this.infoSources = infoSources;
	}

	@Column(name = "ngExitNumber")
	public String getNgExitNumber() {
		return ngExitNumber;
	}

	public void setNgExitNumber(String ngExitNumber) {
		this.ngExitNumber = ngExitNumber;
	}

	@Column(name = "ngExitTime")
	public String getNgExitTime() {
		return ngExitTime;
	}

	public void setNgExitTime(String ngExitTime) {
		this.ngExitTime = ngExitTime;
	}

	@Column(name = "ngEntryNumber")
	public String getNgEntryNumber() {
		return ngEntryNumber;
	}

	public void setNgEntryNumber(String ngEntryNumber) {
		this.ngEntryNumber = ngEntryNumber;
	}

	@Column(name = "ngEntryTime")
	public String getNgEntryTime() {
		return ngEntryTime;
	}

	public void setNgEntryTime(String ngEntryTime) {
		this.ngEntryTime = ngEntryTime;
	}

	@Column(name = "ulocCreateTime")
	public Date getUlocCreateTime() {
		return ulocCreateTime;
	}

	public void setUlocCreateTime(Date ulocCreateTime) {
		this.ulocCreateTime = ulocCreateTime;
	}

	@Column(name="unhealthyCount")
	public Integer getUnhealthyCount() {
		return unhealthyCount;
	}

	public void setUnhealthyCount(Integer unhealthyCount) {
		this.unhealthyCount = unhealthyCount;
	}

}
