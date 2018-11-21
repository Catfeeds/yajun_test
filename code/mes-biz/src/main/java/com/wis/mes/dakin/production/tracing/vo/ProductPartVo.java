package com.wis.mes.dakin.production.tracing.vo;

import java.util.Date;

public class ProductPartVo {

	/** 物料背番号 */
	private String partBackNumber;
	/** 机号 */
	private String partMachineNumber;
	/** 物料图号 */
	private String partFigure;
	/** 物料种类区分 */
	private String partType;
	/** 物料照合对比时间 */
	private String partIrradiationTime;
	/** 背番号 */
	private String backNumber;
	/** 机号 */
	private String machineName;
	/** 工装板ID */
	private String pId;
	/** 机种号 */
	private String machineOfName;
	/** 事部编号 */
	private String plantNo;
	/** 事部名称 */
	private String plantName;
	/** 产线编号 */
	private String lineNo;
	/** 产线名称 */
	private String lineName;
	/** 班次 */
	private String shiftNo;
	/** 创建时间 */
	private Date createTime;
	/** 修改时间 */
	private Date updateTime;
	/** 开始时间 */
	private String beginTime;
	/** 结束时间 */
	private String finishTime;

	public String getPartBackNumber() {
		return partBackNumber;
	}

	public void setPartBackNumber(String partBackNumber) {
		this.partBackNumber = partBackNumber;
	}

	public String getPartMachineNumber() {
		return partMachineNumber;
	}

	public void setPartMachineNumber(String partMachineNumber) {
		this.partMachineNumber = partMachineNumber;
	}

	public String getPartFigure() {
		return partFigure;
	}

	public void setPartFigure(String partFigure) {
		this.partFigure = partFigure;
	}

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public String getPartIrradiationTime() {
		return partIrradiationTime;
	}

	public void setPartIrradiationTime(String partIrradiationTime) {
		this.partIrradiationTime = partIrradiationTime;
	}

	public String getBackNumber() {
		return backNumber;
	}

	public void setBackNumber(String backNumber) {
		this.backNumber = backNumber;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getMachineOfName() {
		return machineOfName;
	}

	public void setMachineOfName(String machineOfName) {
		this.machineOfName = machineOfName;
	}

	public String getPlantNo() {
		return plantNo;
	}

	public void setPlantNo(String plantNo) {
		this.plantNo = plantNo;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getShiftNo() {
		return shiftNo;
	}

	public void setShiftNo(String shiftNo) {
		this.shiftNo = shiftNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

}
