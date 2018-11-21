package com.wis.mes.production.producttracing.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

@Table(name = "tb_product_tracing")
public class TbProductTracing extends AuditEntity {

	private static final long serialVersionUID = -914747046697462742L;
	private Integer id;
	private Integer tmPlantId;
	private TmPlant plant;
	/** 产线ID **/
	private Integer tmLineId;
	/** 产线对象 */
	private TmLine tmLine;
	/** 工作日历ID **/
	private Integer tmWorktimeId;
	/** 工作日历对象 */
	private TmWorktime tmWorktime;
	/** 背番号 **/
	private String backNumber;
	/** 机种名 **/
	private String machineOfName;
	/** 机号 **/
	private String machineName;
	/** 上线时间 **/
	private String beginTime;
	/** 下线时间 **/
	private String finishTime;

	/** 日期 */
	private Date productDate;
	/** SN */
	private String sn;

	private String tpId;

	private String lineNo;

	private Integer tmClassManagerId;

	private TmClassManager classManager;
	
	private Integer unhealthyCount;//不良区分(次)

	public TbProductTracing() {
	}

	public TbProductTracing(String sn) {
		this.sn = sn;
	}

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PRODUCT_TRACING_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Column(name = "MACHINE_OF_NAME")
	public String getMachineOfName() {
		return machineOfName;
	}

	public void setMachineOfName(String machineOfName) {
		this.machineOfName = machineOfName;
	}

	@Column(name = "MACHINE_NAME")
	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
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

	@Column(name = "TM_LINE_ID")
	public TmLine getTmLine() {
		return tmLine;
	}

	public void setTmLine(TmLine tmLine) {
		this.tmLine = tmLine;
	}

	@Column(name = "TM_WORKTIME_ID")
	public Integer getTmWorktimeId() {
		return tmWorktimeId;
	}

	public void setTmWorktimeId(Integer tmWorktimeId) {
		this.tmWorktimeId = tmWorktimeId;
	}

	@Column(name = "TM_WORKTIME_ID")
	public TmWorktime getTmWorktime() {
		return tmWorktime;
	}

	public void setTmWorktime(TmWorktime tmWorktime) {
		this.tmWorktime = tmWorktime;
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

	@Column(name = "SN")
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "TM_PLANT_ID")
	public Integer getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(Integer tmPlantId) {
		this.tmPlantId = tmPlantId;
	}

	@Column(name = "TM_PLANT_ID")
	public TmPlant getPlant() {
		return plant;
	}

	public void setPlant(TmPlant plant) {
		this.plant = plant;
	}

	@Column(name = "TP_ID")
	public String getTpId() {
		return tpId;
	}

	public void setTpId(String tpId) {
		this.tpId = tpId;
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

	@Transient
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	@Column(name="UNHEALTHY_COUNT")
	public Integer getUnhealthyCount() {
		return unhealthyCount;
	}

	public void setUnhealthyCount(Integer unhealthyCount) {
		this.unhealthyCount = unhealthyCount;
	}

}
