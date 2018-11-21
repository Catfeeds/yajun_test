package com.wis.mes.production.regionPrdInfo.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

/***
 * @author yajun_ren
 * @dete 2018/11/15
 * @desc 区域生产信息
 * 
 * **/

@Table(name = "TMP_REGION_PRD_INFO")
public class TmpRegionPrdInfo extends AuditEntity {

	private static final long serialVersionUID = 6760236835936021049L;

	private Integer id;

	private Integer tmWorkTimeId;//工作日历ID
	private Integer tmLineId;//产线ID
	private Integer tmPlantId;//公司ID
	private Integer tmClassManagerId;//班组ID
	private String  regionNo;//区域编号
	private String  grainMovingTime;//稼动时间
	private String  processTime;//加工时间
	private String  downTime;//故障时间
	private String  noDownTime;//非故障时间
	private String  availabilityRate;//可动率
	private String  isRest;//是否是休息时间
	private TmWorktime workTime;//工作日历bean
	private TmLine line;//产线bean
	private TmPlant plant;//公司bean
	private TmClassManager tmClassManager;//班组ID
	private String machiningSurface;//加工図面 
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_REGION_PRD_INFO_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_WORK_TIME_ID")
	public Integer getTmWorkTimeId() {
		return tmWorkTimeId;
	}

	public void setTmWorkTimeId(Integer tmWorkTimeId) {
		this.tmWorkTimeId = tmWorkTimeId;
	}

	@Column(name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "TM_PLANT_ID")
	public Integer getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(Integer tmPlantId) {
		this.tmPlantId = tmPlantId;
	}
	
	@Column(name = "TM_CLASS_MANAGER_ID")
	public Integer getTmClassManagerId() {
		return tmClassManagerId;
	}

	public void setTmClassManagerId(Integer tmClassManagerId) {
		this.tmClassManagerId = tmClassManagerId;
	}

	@Column(name = "REGION_NO")
	public String getRegionNo() {
		return regionNo;
	}

	public void setRegionNo(String regionNo) {
		this.regionNo = regionNo;
	}

	@Column(name = "GRAIN_MOVING_TIME")
	public String getGrainMovingTime() {
		return grainMovingTime;
	}

	public void setGrainMovingTime(String grainMovingTime) {
		this.grainMovingTime = grainMovingTime;
	}

	@Column(name = "PROCESS_TIME")
	public String getProcessTime() {
		return processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

	@Column(name = "DOWN_TIME")
	public String getDownTime() {
		return downTime;
	}

	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}

	@Column(name = "NO_DOWN_TIME")
	public String getNoDownTime() {
		return noDownTime;
	}

	public void setNoDownTime(String noDownTime) {
		this.noDownTime = noDownTime;
	}

	@Column(name = "AVAILABILITY_RATE")
	public String getAvailabilityRate() {
		return availabilityRate;
	}

	public void setAvailabilityRate(String availabilityRate) {
		this.availabilityRate = availabilityRate;
	}

	@Column(name = "IS_REST")
	public String getIsRest() {
		return isRest;
	}

	public void setIsRest(String isRest) {
		this.isRest = isRest;
	}

	@Column(name = "TM_WORK_TIME_ID")
	public TmWorktime getWorkTime() {
		return workTime;
	}

	public void setWorkTime(TmWorktime workTime) {
		this.workTime = workTime;
	}
	
	@Column(name = "TM_LINE_ID")
	public TmLine getLine() {
		return line;
	}

	public void setLine(TmLine line) {
		this.line = line;
	}

	@Column(name = "TM_PLANT_ID")
	public TmPlant getPlant() {
		return plant;
	}

	public void setPlant(TmPlant plant) {
		this.plant = plant;
	}

	@Column(name = "TM_CLASS_MANAGER_ID")
	public TmClassManager getTmClassManager() {
		return tmClassManager;
	}

	public void setTmClassManager(TmClassManager tmClassManager) {
		this.tmClassManager = tmClassManager;
	}

	@Column(name = "MACHINING_SURFACE")
	public String getMachiningSurface() {
		return machiningSurface;
	}

	public void setMachiningSurface(String machiningSurface) {
		this.machiningSurface = machiningSurface;
	}
	
	
	
}