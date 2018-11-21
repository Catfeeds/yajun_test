package com.wis.mes.opc.callback.vo;

public class ProductData {
	private String abnormalCode;// 异常故障码
	private String abnormalDuration;// 异常时间（秒）
	private String backWaitDuration;// 后等工时间（秒）
	private String deviceWorkBegin;// 设备工作开始时间(时)
	private String deviceWorkEnd;// 设备工作结束时间(分)
	private String deviceWorkDuration;// 设备工作时间
	private String enterBegin;// 流入开始时间（时）
	private String enterEnd;// 流入结束时间（时）
	private String exitBegin;// 流出开始时间（时）
	private String exitEnd;// 流出结束时间（时）
	private String frontWaitDuration;// 前等工时间（秒）
	private String okNg;// OK_NG_CODE
	private String staId;// 工位编号
	private String staffWorkDuration;// 员工作业时间（秒）
	private String stationDoneTime;// 工位完成时间（时）
	private String stationWorkDuration;// 工位完成时间（秒）
	private String warningDuration;// 工位作业时间（秒）
	private String processingValue;

	public String getAbnormalCode() {
		return abnormalCode;
	}

	public void setAbnormalCode(String abnormalCode) {
		this.abnormalCode = abnormalCode;
	}

	public String getAbnormalDuration() {
		return abnormalDuration;
	}

	public void setAbnormalDuration(String abnormalDuration) {
		this.abnormalDuration = abnormalDuration;
	}

	public String getBackWaitDuration() {
		return backWaitDuration;
	}

	public void setBackWaitDuration(String backWaitDuration) {
		this.backWaitDuration = backWaitDuration;
	}

	public String getDeviceWorkBegin() {
		return deviceWorkBegin;
	}

	public void setDeviceWorkBegin(String deviceWorkBegin) {
		this.deviceWorkBegin = deviceWorkBegin;
	}

	public String getDeviceWorkEnd() {
		return deviceWorkEnd;
	}

	public void setDeviceWorkEnd(String deviceWorkEnd) {
		this.deviceWorkEnd = deviceWorkEnd;
	}

	public String getEnterBegin() {
		return enterBegin;
	}

	public void setEnterBegin(String enterBegin) {
		this.enterBegin = enterBegin;
	}

	public String getEnterEnd() {
		return enterEnd;
	}

	public void setEnterEnd(String enterEnd) {
		this.enterEnd = enterEnd;
	}

	public String getExitBegin() {
		return exitBegin;
	}

	public void setExitBegin(String exitBegin) {
		this.exitBegin = exitBegin;
	}

	public String getExitEnd() {
		return exitEnd;
	}

	public void setExitEnd(String exitEnd) {
		this.exitEnd = exitEnd;
	}

	public String getFrontWaitDuration() {
		return frontWaitDuration;
	}

	public void setFrontWaitDuration(String frontWaitDuration) {
		this.frontWaitDuration = frontWaitDuration;
	}

	public String getOkNg() {
		return okNg;
	}

	public void setOkNg(String okNg) {
		this.okNg = okNg;
	}

	public String getStaId() {
		return staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public String getStaffWorkDuration() {
		return staffWorkDuration;
	}

	public void setStaffWorkDuration(String staffWorkDuration) {
		this.staffWorkDuration = staffWorkDuration;
	}

	public String getStationDoneTime() {
		return stationDoneTime;
	}

	public void setStationDoneTime(String stationDoneTime) {
		this.stationDoneTime = stationDoneTime;
	}

	public String getStationWorkDuration() {
		return stationWorkDuration;
	}

	public void setStationWorkDuration(String stationWorkDuration) {
		this.stationWorkDuration = stationWorkDuration;
	}

	public String getWarningDuration() {
		return warningDuration;
	}

	public void setWarningDuration(String warningDuration) {
		this.warningDuration = warningDuration;
	}

	public String getProcessingValue() {
		return processingValue;
	}

	public void setProcessingValue(String processingValue) {
		this.processingValue = processingValue;
	}

	public String getDeviceWorkDuration() {
		return deviceWorkDuration;
	}

	public void setDeviceWorkDuration(String deviceWorkDuration) {
		this.deviceWorkDuration = deviceWorkDuration;
	}

}