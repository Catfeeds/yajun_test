package com.wis.mes.master.bom.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.workshop.entity.TmWorkshop;

/**
 * 
 * @author liuzejun
 * 
 * @company 上海西信信息科技有限公司
 * 
 * @dete 2017-04-18
 * 
 * @desc BOM主表
 */
@Table(name = "tm_bom")
public class TmBom extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 工厂ID */
	private Integer tmPlantId;
	/** 工厂 */
	private TmPlant plant = new TmPlant();
	/** 产品物料ID */
	private Integer tmPartId;
	/** 产品物料 */
	private TmPart part = new TmPart();
	/** 车间ID */
	private Integer tmWorkshopId;
	/** 车间 */
	private TmWorkshop workShop = new TmWorkshop();
	/** 产线ID */
	private Integer tmLineId;
	/** 产线 */
	private TmLine line = new TmLine();
	/** 维护状态 */
	private String status;
	/** 版本号 */
	private String bomversion;
	/** 最大版本 */
	private String maxFlag;
	/** 是否启用 */
	private String enabled;
	
	/** 背番号*/
	private String backNumber;
	
	/** 机名*/
	private String machineName;
	
	/** 机种名*/
	private String machineOfName;
	
	/** 备注*/
	private String remark;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_BOM_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}
	

	public void setId(Integer id) {
		this.id = id;
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

	@Column(name = "TM_PART_ID")
	public Integer getTmPartId() {
		return tmPartId;
	}

	public void setTmPartId(Integer tmPartId) {
		this.tmPartId = tmPartId;
	}

	@Column(name = "TM_PART_ID")
	public TmPart getPart() {
		return part;
	}

	public void setPart(TmPart part) {
		this.part = part;
	}

	@Column(name = "TM_WORKSHOP_ID")
	public Integer getTmWorkshopId() {
		return tmWorkshopId;
	}

	public void setTmWorkshopId(Integer tmWorkshopId) {
		this.tmWorkshopId = tmWorkshopId;
	}

	@Column(name = "TM_WORKSHOP_ID")
	public TmWorkshop getWorkShop() {
		return workShop;
	}

	public void setWorkShop(TmWorkshop workShop) {
		this.workShop = workShop;
	}

	@Column(name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "TM_LINE_ID")
	public TmLine getLine() {
		return line;
	}

	public void setLine(TmLine line) {
		this.line = line;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "BOMVERSION")
	public String getBomversion() {
		return bomversion;
	}

	public void setBomversion(String bomversion) {
		this.bomversion = bomversion;
	}

	@Column(name = "MAX_FLAG")
	public String getMaxFlag() {
		return maxFlag;
	}

	public void setMaxFlag(String maxFlag) {
		this.maxFlag = maxFlag;
	}

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
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


	@Column(name = "MACHINE_OF_NAME")
	public String getMachineOfName() {
		return machineOfName;
	}


	public void setMachineOfName(String machineOfName) {
		this.machineOfName = machineOfName;
	}

    @Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	

}
