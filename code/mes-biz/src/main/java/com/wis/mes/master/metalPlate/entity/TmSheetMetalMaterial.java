package com.wis.mes.master.metalPlate.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.core.entity.Removable;

/**
 * @author CFY
 * @company 上海西信信息科技有限公司
 * @date 2018年7月3日
 * @desc 钣金原材料bean
 */
@Table(name = "TM_SHEET_METAL_MATERIAL")
public class TmSheetMetalMaterial extends AuditEntity implements Removable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 机种大类 */
	private String modelType;
	/** 室内机/室外机 */
	private String placeType;
	/** 材料图号 */
	private String materialDrawingNo;
	/** 流水线用 */
	private String assemblyLine;
	/** 图号等级 */
	private String mlLevel;
	/** 涂装用 */
	private String coatingWith;
	/** 图号等级 */
	private String cwLevel;
	/** 加工図面 */
	private String machiningSurface;
	/** 图号等级 */
	private String msLevel;
	/** 程序号 */
	private String programNumber;
	/** 累进 */
	private String progression;
	/** 部品名 */
	private String component;
	/** 后工程 */
	private String afterProcess;
	/** 材质 */
	private String texture;
	/** 板厚 */
	private String plateThickness;
	/** 开料尺寸 */
	private String materialSize;
	/** 批次数 */
	private Integer batchNumber;
	/** 批次上限 */
	private Integer batchCap;
	/** 工程数 */
	private Integer stepNumber;
	/** 机台 */
	private String machineTags;
	/** 模具厂家 */
	private String manufacturers;
	/** 完成识别码 */
	private String identificationCode;
	/** 模具编号 */
	private String toolNumber;
	/** 备用模具 */
	private String resToolNumber;
	/** 备注 */
	private String remark;
	
	private Integer removed;

	/** 区域标识 */
	private String regionMark;
	/** 剩余库存数量 */
	private Integer inventoryNumber;
	/** 标准CT */
	private Double standardCt;
	/** PLC该程序号对应的编码 */
	private String plcNo;
	/**apc 当日计划需求数**/
	private Integer apcPlannedDemand; 
	/**机台名称**/
	private String  machineTagNames;
	

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_SHEET_METAL_MATERIAL_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "MODEL_TYPE")
	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	@Column(name = "PLACE_TYPE")
	public String getPlaceType() {
		return placeType;
	}

	public void setPlaceType(String placeType) {
		this.placeType = placeType;
	}

	@Column(name = "MATERIAL_DRAWING_NO")
	public String getMaterialDrawingNo() {
		return materialDrawingNo;
	}

	public void setMaterialDrawingNo(String materialDrawingNo) {
		this.materialDrawingNo = materialDrawingNo;
	}

	@Column(name = "ASSEMBLY_LINE")
	public String getAssemblyLine() {
		return assemblyLine;
	}

	public void setAssemblyLine(String assemblyLine) {
		this.assemblyLine = assemblyLine;
	}

	@Column(name = "ML_LEVEL")
	public String getMlLevel() {
		return mlLevel;
	}

	public void setMlLevel(String mlLevel) {
		this.mlLevel = mlLevel;
	}

	@Column(name = "COATING_WITH")
	public String getCoatingWith() {
		return coatingWith;
	}

	public void setCoatingWith(String coatingWith) {
		this.coatingWith = coatingWith;
	}

	@Column(name = "CW_LEVEL")
	public String getCwLevel() {
		return cwLevel;
	}

	public void setCwLevel(String cwLevel) {
		this.cwLevel = cwLevel;
	}

	@Column(name = "MACHINING_SURFACE")
	public String getMachiningSurface() {
		return machiningSurface;
	}

	public void setMachiningSurface(String machiningSurface) {
		this.machiningSurface = machiningSurface;
	}

	@Column(name = "MS_LEVEL")
	public String getMsLevel() {
		return msLevel;
	}

	public void setMsLevel(String msLevel) {
		this.msLevel = msLevel;
	}

	@Column(name = "PROGRAM_NUMBER")
	public String getProgramNumber() {
		return programNumber;
	}

	public void setProgramNumber(String programNumber) {
		this.programNumber = programNumber;
	}

	@Column(name = "PROGRESSION")
	public String getProgression() {
		return progression;
	}

	public void setProgression(String progression) {
		this.progression = progression;
	}

	@Column(name = "COMPONENT")
	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	@Column(name = "AFTER_PROCESS")
	public String getAfterProcess() {
		return afterProcess;
	}

	public void setAfterProcess(String afterProcess) {
		this.afterProcess = afterProcess;
	}

	@Column(name = "TEXTURE")
	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	@Column(name = "PLATE_THICKNESS")
	public String getPlateThickness() {
		return plateThickness;
	}

	public void setPlateThickness(String plateThickness) {
		this.plateThickness = plateThickness;
	}

	@Column(name = "MATERIAL_SIZE")
	public String getMaterialSize() {
		return materialSize;
	}

	public void setMaterialSize(String materialSize) {
		this.materialSize = materialSize;
	}

	@Column(name = "BATCH_NUMBER")
	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

	@Column(name = "BATCH_CAP")
	public Integer getBatchCap() {
		return batchCap;
	}

	public void setBatchCap(Integer batchCap) {
		this.batchCap = batchCap;
	}

	@Column(name = "STEP_NUMBER")
	public Integer getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}

	@Column(name = "MACHINE_TAGS")
	public String getMachineTags() {
		return machineTags;
	}

	public void setMachineTags(String machineTags) {
		this.machineTags = machineTags;
	}

	@Column(name = "MANUFACTURERS")
	public String getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(String manufacturers) {
		this.manufacturers = manufacturers;
	}

	@Column(name = "IDENTIFICATION_CODE")
	public String getIdentificationCode() {
		return identificationCode;
	}

	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
	}

	@Column(name = "TOOL_NUMBER")
	public String getToolNumber() {
		return toolNumber;
	}

	public void setToolNumber(String toolNumber) {
		this.toolNumber = toolNumber;
	}

	@Column(name = "RES_TOOL_NUMBER")
	public String getResToolNumber() {
		return resToolNumber;
	}

	public void setResToolNumber(String resToolNumber) {
		this.resToolNumber = resToolNumber;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "MARK_FOR_DELETE")
	public Integer getRemoved() {
		return removed;
	}

	@Override
	public boolean isEntityRemoved() {
		return (null == removed || removed == 0) ? false : true;
	}

	@Override
	public void setRemoved(Integer removed) {
		this.removed = removed;
	}

	@Column(name = "REGION_MARK")
	public String getRegionMark() {
		return regionMark;
	}

	public void setRegionMark(String regionMark) {
		this.regionMark = regionMark;
	}

	@Column(name = "INVENTORY_NUMBER")
	public Integer getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(Integer inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	@Column(name = "STANDARD_CT")
	public Double getStandardCt() {
		return standardCt;
	}

	public void setStandardCt(Double standardCt) {
		this.standardCt = standardCt;
	}

	@Column(name = "PLC_NO")
	public String getPlcNo() {
		return plcNo;
	}

	public void setPlcNo(String plcNo) {
		this.plcNo = plcNo;
	}

	@Column(name="APC_PLANNED_DEMAND")
	public Integer getApcPlannedDemand() {
		return apcPlannedDemand;
	}

	public void setApcPlannedDemand(Integer apcPlannedDemand) {
		this.apcPlannedDemand = apcPlannedDemand;
	}

	@Column(name="MACHINE_TAG_NAMES")
	public String getMachineTagNames() {
		return machineTagNames;
	}

	public void setMachineTagNames(String machineTagNames) {
		this.machineTagNames = machineTagNames;
	}

	
}
