package com.wis.mes.master.workcalendar.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.workshop.entity.TmWorkshop;

/**
 * @author zhf
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2016年4月19日
 *
 * @desc 工作日历模板
 */
@Table(name = "tm_workschedule")
public class TmWorkschedule extends AuditEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 模板编号 */
	private String no;
	/** 工厂ID */
	private Integer tmPlantId;
	/** 车间ID */
	private Integer tmWorkshopId;
	/** 产线ID */
	private Integer tmLineId;
	/** 班次 */
	private String shiftnoId;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/** 星期一 */
	private String mon;
	/** 星期二 */
	private String tue;
	/** 星期三 */
	private String wed;
	/** 星期四 */
	private String thu;
	/** 星期五 */
	private String fri;
	/** 星期六 */
	private String sat;
	/** 星期日 */
	private String sun;
	/** 计划上线数 */
	private Integer planOnlineQty;
	/** 计划下线数 */
	private Integer planOfflineQty;
	/** 参考JPH */
	private String jph;
	/** 启用 */
	private String enabled;
	/** 工厂 bean引用 */
	private TmPlant plant = new TmPlant();
	/** 车间 bean引用 */
	private TmWorkshop workshop = new TmWorkshop();
	/** 产线bean引用 */
	private TmLine line = new TmLine();
	private Integer tmClassManagerId;
	private TmClassManager classManager;
	
	public TmWorkschedule() {}

	public TmWorkschedule(String enabled) {
		this.enabled = enabled;
	}
	
	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_WORKSCHEDULE_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Column(name = "TM_PLANT_ID")
	public Integer getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(Integer tmPlantId) {
		this.tmPlantId = tmPlantId;
	}

	@Column(name = "TM_WORKSHOP_ID")
	public Integer getTmWorkshopId() {
		return tmWorkshopId;
	}

	public void setTmWorkshopId(Integer tmWorkshopId) {
		this.tmWorkshopId = tmWorkshopId;
	}

	@Column(name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "SHIFTNO_ID")
	public String getShiftnoId() {
		return shiftnoId;
	}

	public void setShiftnoId(String shiftnoId) {
		this.shiftnoId = shiftnoId;
	}

	@Column(name = "START_TIME")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "MON")
	public String getMon() {
		return mon;
	}

	public void setMon(String mon) {
		this.mon = mon;
	}

	@Column(name = "TUE")
	public String getTue() {
		return tue;
	}

	public void setTue(String tue) {
		this.tue = tue;
	}

	@Column(name = "WED")
	public String getWed() {
		return wed;
	}

	public void setWed(String wed) {
		this.wed = wed;
	}

	@Column(name = "THU")
	public String getThu() {
		return thu;
	}

	public void setThu(String thu) {
		this.thu = thu;
	}

	@Column(name = "FRI")
	public String getFri() {
		return fri;
	}

	public void setFri(String fri) {
		this.fri = fri;
	}

	@Column(name = "SAT")
	public String getSat() {
		return sat;
	}

	public void setSat(String sat) {
		this.sat = sat;
	}

	@Column(name = "SUN")
	public String getSun() {
		return sun;
	}

	public void setSun(String sun) {
		this.sun = sun;
	}

	@Column(name = "PLAN_ONLINE_QTY")
	public Integer getPlanOnlineQty() {
		return planOnlineQty;
	}

	public void setPlanOnlineQty(Integer planOnlineQty) {
		this.planOnlineQty = planOnlineQty;
	}

	@Column(name = "PLAN_OFFLINE_QTY")
	public Integer getPlanOfflineQty() {
		return planOfflineQty;
	}

	public void setPlanOfflineQty(Integer planOfflineQty) {
		this.planOfflineQty = planOfflineQty;
	}

	@Column(name = "JPH")
	public String getJph() {
		return jph;
	}

	public void setJph(String jph) {
		this.jph = jph;
	}

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "TM_PLANT_ID")
	public TmPlant getPlant() {
		return plant;
	}

	public void setPlant(TmPlant plant) {
		this.plant = plant;
	}

	@Column(name = "TM_WORKSHOP_ID")
	public TmWorkshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(TmWorkshop workshop) {
		this.workshop = workshop;
	}

	@Column(name = "TM_LINE_ID")
	public TmLine getLine() {
		return line;
	}

	public void setLine(TmLine line) {
		this.line = line;
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

}
