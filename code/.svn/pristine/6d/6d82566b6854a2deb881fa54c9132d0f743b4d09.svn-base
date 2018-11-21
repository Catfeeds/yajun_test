package com.wis.basis.notification.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AbstractEntity;

@Table(name = "TS_NOTIFICATION")
public class Notification extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer senderId;
	private Integer receiverId;
	private Integer readFlag;
	private String msgType;
	private Integer msgId;

	private MsgContent msgContent;

	@SequenceGenerator(name = "generator", sequenceName = "SEQ_NOTIFICATION_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "SENDER_ID")
	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	@Column(name = "RECEIVER_ID")
	public Integer getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

	@Column(name = "READ_FLAG")
	public Integer getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(Integer readFlag) {
		this.readFlag = readFlag;
	}

	@Column(name = "MSG_TYPE")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	@Column(name = "MSG_ID")
	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	@Column(name = "MSG_ID")
	public MsgContent getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(MsgContent msgContent) {
		this.msgContent = msgContent;
	}

}
