package com.wis.mes.production.qualitytracing.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.basis.systemadmin.entity.Employee;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.entity.TmNcGroup;
import com.wis.mes.master.uloc.entity.TmUloc;

@Table(name = "tb_quality_tracing")
public class TbQualityTracing extends AuditEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 背番号 **/
	private String backNumber;
	/** 机种名 **/
	private String machineOfName;
	/** 机种 **/
	private String machineName;
	/** 班次 **/
	private String shiftno;
	/** 故障组ID **/
	private Integer tmNcGroupId;
	/** 故障ID **/
	private Integer tmNcId;
	/** 故障处理方式 **/
	private String ncProcessMode;

	private TmNcGroup tmNcGroup;

	private TmNc tmNc;
	/** 适应NG入口 **/
	private String ngEntrance;

	/** 信息来源 **/
	private String infoSources;
	/** 录入时间 */
	private String enteringTime;
	/** 发现工位 */
	private String discoveryStation;
	private TmUloc discoveryUloc;
	/** SN */
	private String sn;

	private String status;

	/*NG实际入口**/
	private String ngin;
	/**
	 * NG出口，从那个NG出口流出的工位编号
	 **/
	private String ngExit;
	/** 班组 */
	private Integer tmClassManagerId;
	private TmClassManager classManager;

	private Employee employee;
	
	
	private Date submitTime; 
	
	private Integer tmWorkTimeId; 
	
	/**上线时间***/
	private Date highLinesTime;
	
	@Column(name = "ID")
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_QUALITY_TRACING_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Column(name = "SHIFTNO")
	public String getShiftno() {
		return shiftno;
	}

	public void setShiftno(String shiftno) {
		this.shiftno = shiftno;
	}

	@Column(name = "TM_NC_GROUP_ID")
	public Integer getTmNcGroupId() {
		return tmNcGroupId;
	}

	public void setTmNcGroupId(Integer tmNcGroupId) {
		this.tmNcGroupId = tmNcGroupId;
	}

	@Column(name = "TM_NC_ID")
	public Integer getTmNcId() {
		return tmNcId;
	}

	public void setTmNcId(Integer tmNcId) {
		this.tmNcId = tmNcId;
	}

	@Column(name = "NC_PROCESS_MODE")
	public String getNcProcessMode() {
		return ncProcessMode;
	}

	public void setNcProcessMode(String ncProcessMode) {
		this.ncProcessMode = ncProcessMode;
	}

	@Column(name = "TM_NC_GROUP_ID")
	public TmNcGroup getTmNcGroup() {
		return tmNcGroup;
	}

	public void setTmNcGroup(TmNcGroup tmNcGroup) {
		this.tmNcGroup = tmNcGroup;
	}

	@Column(name = "TM_NC_ID")
	public TmNc getTmNc() {
		return tmNc;
	}

	public void setTmNc(TmNc tmNc) {
		this.tmNc = tmNc;
	}

	@Column(name = "NG_ENTRANCE")
	public String getNgEntrance() {
		return ngEntrance;
	}

	public void setNgEntrance(String ngEntrance) {
		this.ngEntrance = ngEntrance;
	}

	@Column(name = "INFO_SOURCES")
	public String getInfoSources() {
		return infoSources;
	}

	public void setInfoSources(String infoSources) {
		this.infoSources = infoSources;
	}

	@Column(name = "ENTERING_TIME")
	public String getEnteringTime() {
		return enteringTime;
	}

	public void setEnteringTime(String enteringTime) {
		this.enteringTime = enteringTime;
	}

	@Column(name = "DISCOVERY_STATION")
	public String getDiscoveryStation() {
		return discoveryStation;
	}

	public void setDiscoveryStation(String discoveryStation) {
		this.discoveryStation = discoveryStation;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "SN")
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "UPDATE_USER")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Column(name = "NG_EXIT")
	public String getNgExit() {
		return ngExit;
	}

	public void setNgExit(String ngExit) {
		this.ngExit = ngExit;
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

	@Column(name = "DISCOVERY_STATION")
	public TmUloc getDiscoveryUloc() {
		return discoveryUloc;
	}

	public void setDiscoveryUloc(TmUloc discoveryUloc) {
		this.discoveryUloc = discoveryUloc;
	}

	@Column(name = "NGIN")
	public String getNgin() {
		return ngin;
	}

	public void setNgin(String ngin) {
		this.ngin = ngin;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SUBMIT_TIME", updatable = false)
	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	@Column(name="TM_WORK_TIME_ID")
	public Integer getTmWorkTimeId() {
		return tmWorkTimeId;
	}

	public void setTmWorkTimeId(Integer tmWorkTimeId) {
		this.tmWorkTimeId = tmWorkTimeId;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="HIGH_LINES_TIME")
	public Date getHighLinesTime() {
		return highLinesTime;
	}

	public void setHighLinesTime(Date highLinesTime) {
		this.highLinesTime = highLinesTime;
	}
}
