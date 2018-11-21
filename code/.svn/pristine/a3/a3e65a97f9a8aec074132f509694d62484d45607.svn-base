package com.wis.mes.master.equipment.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.workshop.entity.TmWorkshop;
/**
 * 
* @author Jixueyuan   
* @date 2017年5月25日
* @Description: 设备bean
 */
@Table(name = "tm_equipment")
public class TmEquipment extends AuditEntity {
	
	private static final long serialVersionUID = 1L;
	/** 设备ID */
	private Integer id;
	/** 公司编号 */
	private Integer tmPlantId;
	/** 公司bean */
	private TmPlant plant;
	/** 车间编号 */
	private Integer tmWorkshopId;
	/** 车间bean */
	private TmWorkshop workshop;
	/** 设备编号 */
	private String no;
	/** 设备名称 */
	private String name;
	/** 设备型号 */
	private String description;
	/** 物理位置 */
	private String location;
	/** 类型 */
	private String type;
	/** 状态 */
	private String status;
	/** 生产状态 */
	private String productStatus;
	/** 投入工作时间 */
	private String startDate;
	/** 建议检修周期(天) */
	private String preventiveDate;
	/** 图片地址 */
	private String pictureAddr;
	/** 对应工位信息ID */
	private Integer tmUlocId;
	/** 工位bean */
	private TmUloc uloc;
	/** 备注 */
	private String note;
	/** 启用 */
	private String enabled;
	/** IP地址 */
	private String ipAddress;
	/** PLC型号 */
	private String plcTyp;
	/** PLC品牌 */
	private String plcBrand;
	/** 网络模块型号 */
	private String networkModelTyp;
	/** 设备供应商 */
	private String equipmentSupplier;
	/** 投入工作时间 */
	private String workingHours;
	/** 财产编号*/
	private String propertyNumber;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_EQUIPMENT_ID")
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
	public void setPlant(final TmPlant plant) {
		this.plant = plant;
	}
	@Column(name = "TM_WORKSHOP_ID")
	public TmWorkshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(final TmWorkshop workshop) {
		this.workshop = workshop;
	}
	@Column(name = "TM_WORKSHOP_ID")
	public Integer getTmWorkshopId() {
		return tmWorkshopId;
	}

	public void setTmWorkshopId(Integer tmWorkshopId) {
		this.tmWorkshopId = tmWorkshopId;
	}

	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "LOCATION")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "PRODUCT_STATUS")
	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	@Column(name = "START_DATE")
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Column(name = "PREVENTIVE_DATE")
	public String getPreventiveDate() {
		return preventiveDate;
	}

	public void setPreventiveDate(String preventiveDate) {
		this.preventiveDate = preventiveDate;
	}

	@Column(name = "PICTURE_ADDR")
	public String getPictureAddr() {
		return pictureAddr;
	}

	public void setPictureAddr(String pictureAddr) {
		this.pictureAddr = pictureAddr;
	}

	@Column(name = "TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}
	@Column(name = "TM_ULOC_ID")
	public TmUloc getUloc() {
		return uloc;
	}

	public void setUloc(TmUloc uloc) {
		this.uloc = uloc;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	@Column(name = "IP_ADDRESS")
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	@Column(name = "PLC_TYP")
	public String getPlcTyp() {
		return plcTyp;
	}

	public void setPlcTyp(String plcTyp) {
		this.plcTyp = plcTyp;
	}
	@Column(name = "PLC_BRAND")
	public String getPlcBrand() {
		return plcBrand;
	}

	public void setPlcBrand(String plcBrand) {
		this.plcBrand = plcBrand;
	}
	@Column(name = "NETWORK_MODEL_TYP")
	public String getNetworkModelTyp() {
		return networkModelTyp;
	}

	public void setNetworkModelTyp(String networkModelTyp) {
		this.networkModelTyp = networkModelTyp;
	}
	@Column(name = "EQUIPMENT_SUPPLIER")
	public String getEquipmentSupplier() {
		return equipmentSupplier;
	}

	public void setEquipmentSupplier(String equipmentSupplier) {
		this.equipmentSupplier = equipmentSupplier;
	}
	@Column(name = "WORKING_HOURS")
	public String getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
	}
	
	public static TmEquipment createEquimentNo(String no){ 
		TmEquipment bean = new TmEquipment(); 
		bean.setNo(no);
		return bean;
	}
	@Column(name = "PROPERTY_NUMBER")
	public String getPropertyNumber() {
		return propertyNumber;
	}

	public void setPropertyNumber(String propertyNumber) {
		this.propertyNumber = propertyNumber;
	}
}
