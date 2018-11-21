package com.wis.mes.master.equipment.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.part.entity.TmPart;
/**
 * 
* @author Jixueyuan   
* @date 2017年5月25日
* @Description: 可消耗的辅材bean
 */
@Table(name = "tm_equipment_materials")
public class TmEquipmentMaterials extends AuditEntity {
	
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 设备ID */
	private Integer tmEquipmentId;
	/** 设备bean */
	private TmEquipment equipment;
	/** 物料ID*/
	private Integer tmPartId;
	/** 物料bean*/
	private TmPart part;
	/** 备注*/
	private String note;
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_TM_EQUIPMENT_MATERIALS_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_EQUIPMENT_ID")
	public Integer getTmEquipmentId() {
		return tmEquipmentId;
	}

	public void setTmEquipmentId(Integer tmEquipmentId) {
		this.tmEquipmentId = tmEquipmentId;
	}

	@Column(name = "TM_PART_ID")
	public Integer getTmPartId() {
		return tmPartId;
	}

	public void setTmPartId(Integer tmPartId) {
		this.tmPartId = tmPartId;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	@Column(name = "TM_EQUIPMENT_ID")
	public TmEquipment getEquipment() {
		return equipment;
	}

	public void setEquipment(TmEquipment equipment) {
		this.equipment = equipment;
	}
	@Column(name = "TM_PART_ID")
	public TmPart getPart() {
		return part;
	}

	public void setPart(TmPart part) {
		this.part = part;
	}
	
	
}
