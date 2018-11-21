package com.wis.mes.rfid.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

@Table(name = "tb_rfid_log")
public class TbRfidLog extends AuditEntity {

	private Integer id;
	/** SN **/
	private String sn;
	/** 事部 **/
	private Integer tbPlantId;
	/** 流水线代码 **/
	private Integer tmLineId;
	/** 背番号 **/
	private String backNumber;
	/** 机号 **/
	private String machineName;
	/** 写入RFID设备IP地址 **/
	private String rfidIp;
	/** 写入结果 **/
	private String fruit;
	/** P_ID **/
	private String tpId;

	private TmPlant plant;

	private TmLine line;
	
	private String epcInfo;
	
	private  Integer tmWorktimeId;
	
	private TmWorktime tmWorktime;
	
	private String ulocNo;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PMC_PMST_ID")
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "SN")
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "TB_PLANT_ID")
	public Integer getTbPlantId() {
		return tbPlantId;
	}

	public void setTbPlantId(Integer tbPlantId) {
		this.tbPlantId = tbPlantId;
	}

	@Column(name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "BACK_NUMBER")
	public String getBackNumber() {
		return backNumber;
	}

	public void setBackNumber(String backNumber) {
		this.backNumber = backNumber;
	}

	@Column(name = "MACHINE_NAME")
	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	@Column(name = "RFID_IP")
	public String getRfidIp() {
		return rfidIp;
	}

	public void setRfidIp(String rfidIp) {
		this.rfidIp = rfidIp;
	}

	@Column(name = "FRUIT")
	public String getFruit() {
		return fruit;
	}

	public void setFruit(String fruit) {
		this.fruit = fruit;
	}

	@Column(name = "P_ID")
	public String getTpId() {
		return tpId;
	}

	public void setTpId(String tpId) {
		this.tpId = tpId;
	}

	@Column(name = "TB_PLANT_ID")
	public TmPlant getPlant() {
		return plant;
	}

	public void setPlant(TmPlant plant) {
		this.plant = plant;
	}

	@Column(name = "TM_LINE_ID")
	public TmLine getLine() {
		return line;
	}

	public void setLine(TmLine line) {
		this.line = line;
	}

	@Column(name="EPC_INFO")
	public String getEpcInfo() {
		return epcInfo;
	}

	public void setEpcInfo(String epcInfo) {
		this.epcInfo = epcInfo;
	}

	@Column(name="TM_WORKTIME_ID")
	public Integer getTmWorktimeId() {
		return tmWorktimeId;
	}

	public void setTmWorktimeId(Integer tmWorktimeId) {
		this.tmWorktimeId = tmWorktimeId;
	}

	@Column(name="TM_WORKTIME_ID")
	public TmWorktime getTmWorktime() {
		return tmWorktime;
	}

	public void setTmWorktime(TmWorktime tmWorktime) {
		this.tmWorktime = tmWorktime;
	}

	@Column(name="ULOC_NO")
	public String getUlocNo() {
		return ulocNo;
	}

	public void setUlocNo(String ulocNo) {
		this.ulocNo = ulocNo;
	}

}
