package com.wis.mes.rfid.vo;

import org.apache.commons.lang3.StringUtils;

public class SnInfoObjVo {
	/** 事部**/
	private String plantNo;
	/** 背番号 **/
	private String backNumber;
	/** 机号 **/
	private String machineName;
	/**机种名**/
	private String machineOfName;
	/**SN*/
	private String newSn;
	
	private String originalSn;
	/**流水线**/
	private String lineNo;
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
	
	public String getNewSn() {
		return newSn;
	}
	
	public void setNewSn(String newSn) {
		this.newSn = newSn;
	}
	
	public String getPlantNo() {
		return plantNo;
	}
	
	public void setPlantNo(String plantNo) {
		this.plantNo = plantNo;
	}
	
	public String getOriginalSn() {
		return originalSn;
	}
	public void setOriginalSn(String originalSn) {
		this.originalSn = originalSn;
	}
	
	public String getMachineOfName() {
		return machineOfName;
	}
	public void setMachineOfName(String machineOfName) {
		this.machineOfName = machineOfName;
	}
	
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public static SnInfoObjVo splitSn(String sn){
		SnInfoObjVo obj = new SnInfoObjVo();
		obj.setOriginalSn(sn);
		obj.setNewSn(sn);
		if(StringUtils.isNotBlank(sn) && sn.length()==22){
			obj.setPlantNo(sn.substring(0,2));
			obj.setLineNo(sn.substring(2,5));
			obj.setBackNumber(sn.substring(5,sn.length()-13));
			obj.setMachineName(sn.substring(9,sn.length()-6));
		}
		return obj;
	}
}
