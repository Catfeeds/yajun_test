package com.wis.basis.notification.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.core.framework.entity.Attachment;

@Table(name = "TS_MSG_CONTENT")
public class MsgContent extends AuditEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private String content;
	private String msgType;

	private Set<Attachment> attachments;
	
	@SequenceGenerator(name = "generator", sequenceName = "SEQ_MSG_CONTENT_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Column(name = "MSG_TYPE")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	
}
