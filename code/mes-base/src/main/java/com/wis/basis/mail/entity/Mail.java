package com.wis.basis.mail.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.BaseEntity;

@Entity
@Table(name = "TS_EMAIL")
public class Mail extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String subject;
	private String to ;
	private String cc ;
 	private String text ;
	private String files;
	private String flag;
	
	@Id
	@SequenceGenerator(name="generator" , allocationSize=1 , sequenceName = "SEQ_EMAIL_ID")
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "SUBJECT" )
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Column(name = "MAIL_TO" )
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	@Column(name = "CC" )
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	
	@Column(name = "TEXT" )
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Column(name="FILES")
	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	@Column(name="FLAG")
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
