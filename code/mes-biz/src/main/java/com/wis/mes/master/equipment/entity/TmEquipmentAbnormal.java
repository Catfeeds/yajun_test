package com.wis.mes.master.equipment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

@Entity
@Table(name = "TM_EQUIPMENT_ABNORMAL")
public class TmEquipmentAbnormal extends AuditEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String equipmentNo;
	private String abnormalCode;
	private String abnormalName;
	private Integer tmEquipmentId;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_TM_EQUIPMENT_ABNORMAL_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "EQUIPMENT_NO")
	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	@Column(name = "ABNORMAL_CODE")
	public String getAbnormalCode() {
		return abnormalCode;
	}

	public void setAbnormalCode(String abnormalCode) {
		this.abnormalCode = abnormalCode;
	}

	@Column(name = "ABNORMAL_NAME")
	public String getAbnormalName() {
		return abnormalName;
	}

	public void setAbnormalName(String abnormalName) {
		this.abnormalName = abnormalName;
	}

	@Column(name = "TM_EQUIPMENT_ID")
	public Integer getTmEquipmentId() {
		return tmEquipmentId;
	}

	public void setTmEquipmentId(Integer tmEquipmentId) {
		this.tmEquipmentId = tmEquipmentId;
	}

}
