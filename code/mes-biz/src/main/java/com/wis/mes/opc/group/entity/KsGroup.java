package com.wis.mes.opc.group.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AbstractEntity;

@Table(name = "TKS_GROUP")
public class KsGroup extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String groupCode;
	private String groupName;
	private String groupNote;
	private String groupServer;
	private String groupChannel;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_TKS_GROUP_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "GROUP_CODE")
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	@Column(name = "GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "GROUP_NOTE")
	public String getGroupNote() {
		return groupNote;
	}

	public void setGroupNote(String groupNote) {
		this.groupNote = groupNote;
	}

	@Column(name = "GROUP_SERVER")
	public String getGroupServer() {
		return groupServer;
	}

	public void setGroupServer(String groupServer) {
		this.groupServer = groupServer;
	}

	@Column(name = "GROUP_CHANNEL")
	public String getGroupChannel() {
		return groupChannel;
	}

	public void setGroupChannel(String groupChannel) {
		this.groupChannel = groupChannel;
	}

}
