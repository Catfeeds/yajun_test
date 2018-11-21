package com.wis.mes.opc.group.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AbstractEntity;

@Table(name = "TKS_GROUP_ITEM")
public class KsGroupItem extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer groupId;
	private KsGroup group;
	private Integer orderNumber;
	private String itemCode;
	private String itemName;
	private String itemNote;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_TKS_GROUP_ITEM_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "GROUP_ID")
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Column(name = "ITEM_CODE")
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "ITEM_NAME")
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "ITEM_NOTE")
	public String getItemNote() {
		return itemNote;
	}

	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;
	}

	@Column(name = "GROUP_ID")
	public KsGroup getGroup() {
		return group;
	}

	public void setGroup(KsGroup group) {
		this.group = group;
	}

	@Column(name = "ORDER_NUMBER")
	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

}
