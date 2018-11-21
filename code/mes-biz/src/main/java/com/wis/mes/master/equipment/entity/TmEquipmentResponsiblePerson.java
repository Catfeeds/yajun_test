package com.wis.mes.master.equipment.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.basis.systemadmin.entity.User;
import com.wis.core.entity.AuditEntity;
/**
 * 
* @author Jixueyuan   
* @date 2017年5月25日
* @Description: 责任人bean
 */
@Table(name = "tm_equipment_responsible_person")
public class TmEquipmentResponsiblePerson extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 设备ID */
	private Integer tmEquipmentId;
	/** 设备bean */
	private TmEquipment equipment;
	/** 用户ID */
	private Integer tsUserId;
	/** 人员bean */
	private User user;
	/** 备注 */
	private String  note;
	/** 上次邮件通知时间 */
	private Date  lastEmailTime;
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_EQUIPMENT_PERSON_ID")
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

	@Column(name = "TS_USER_ID")
	public Integer getTsUserId() {
		return tsUserId;
	}

	public void setTsUserId(Integer tsUserId) {
		this.tsUserId = tsUserId;
	}
	@Column(name = "TM_EQUIPMENT_ID")
	public TmEquipment getEquipment() {
		return equipment;
	}

	public void setEquipment(TmEquipment equipment) {
		this.equipment = equipment;
	}
	@Column(name = "TS_USER_ID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	@Column(name = "LAST_EMAIL_TIME")
	public Date getLastEmailTime() {
		return lastEmailTime;
	}

	public void setLastEmailTime(Date lastEmailTime) {
		this.lastEmailTime = lastEmailTime;
	}
	

}
