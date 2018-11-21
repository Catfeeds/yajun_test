package com.wis.mes.master.uloc.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.equipment.entity.TmEquipment;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.workshop.entity.TmWorkshop;

@Table(name = "tm_uloc")
public class TmUloc extends AuditEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer tmPlantId;
	private Integer tmWorkshopId;
	private Integer tmLineId;
	private String no;
	private String name;
	private String enabled;
	private String isSip;
	private TmPlant tmPlant = new TmPlant();
	private TmWorkshop tmWorkshop = new TmWorkshop();
	private TmLine tmLine = new TmLine();
	private String extCode;
	private String note;
	/** 工位类型 */
	private String positionType;
	/** 是否出入口 */
	private String isEntrance;
	/** 设备 */
	private Integer tmEquipmentId;
	private TmEquipment equipment;
	/** RFID ip **/
	private String rfidIP;
	/** RFID 天线口 */
	private Integer rfidAnt;
	/**端口号*/
	private Integer rfidPort;
	
	private String serviceName;
	
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_ULOC_ID")
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

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "IS_SIP")
	public String getIsSip() {
		return isSip;
	}

	public void setIsSip(String isSip) {
		this.isSip = isSip;
	}

	@Column(name = "TM_PLANT_ID")
	public TmPlant getTmPlant() {
		return tmPlant;
	}

	public void setTmPlant(TmPlant tmPlant) {
		this.tmPlant = tmPlant;
	}

	@Column(name = "TM_WORKSHOP_ID")
	public TmWorkshop getTmWorkshop() {
		return tmWorkshop;
	}

	public void setTmWorkshop(TmWorkshop tmWorkshop) {
		this.tmWorkshop = tmWorkshop;
	}

	@Column(name = "TM_Line_ID")
	public TmLine getTmLine() {
		return tmLine;
	}

	public void setTmLine(TmLine tmLine) {
		this.tmLine = tmLine;
	}

	public TmUloc(String enabled) {
		super();
		this.enabled = enabled;
	}

	@Column(name = "EXT_CODE")
	public String getExtCode() {
		return extCode;
	}

	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "POSITION_TYPE")
	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	@Column(name = "IS_ENTRANCE")
	public String getIsEntrance() {
		return isEntrance;
	}

	public void setIsEntrance(String isEntrance) {
		this.isEntrance = isEntrance;
	}

	public TmUloc() {
		super();
	}

	public TmUloc(String enabled, String isEntrance) {
		this.enabled = enabled;
		this.isEntrance = isEntrance;
	}

	@Column(name = "TM_EQUIPMENT_ID")
	public Integer getTmEquipmentId() {
		return tmEquipmentId;
	}

	public void setTmEquipmentId(Integer tmEquipmentId) {
		this.tmEquipmentId = tmEquipmentId;
	}

	@Column(name = "TM_EQUIPMENT_ID")
	public TmEquipment getEquipment() {
		return equipment;
	}

	public void setEquipment(TmEquipment equipment) {
		this.equipment = equipment;
	}

	@Column(name="RFID_IP")
	public String getRfidIP() {
		return rfidIP;
	}

	public void setRfidIP(String rfidIP) {
		this.rfidIP = rfidIP;
	}

	@Column(name = "RFID_ANT")
	public Integer getRfidAnt() {
		return rfidAnt;
	}

	public void setRfidAnt(Integer rfidAnt) {
		this.rfidAnt = rfidAnt;
	}
	
	@Column(name="RFID_PORT")
	public Integer getRfidPort() {
		return rfidPort;
	}

	public void setRfidPort(Integer rfidPort) {
		this.rfidPort = rfidPort;
	}

	public static final TmUloc createUlocNo(String no){
		TmUloc tmUloc = new TmUloc();
		tmUloc.setNo(no);
		tmUloc.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return tmUloc;
	}

	@Column(name="SERVICE_NAME")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
}
