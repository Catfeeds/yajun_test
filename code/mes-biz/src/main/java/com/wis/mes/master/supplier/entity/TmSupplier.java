package com.wis.mes.master.supplier.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.core.entity.Removable;

@Table(name = "tm_supplier")
public class TmSupplier extends AuditEntity implements Removable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String no;
	private String name;
	private String addr;
	private String contact1;
	private String telNo1;
	private String faxNo1;
	private String mobileNo1;
	private String emaile1;
	private String contact2;
	private String telNo2;
	private String faxNo2;
	private String mobileNo2;
	private String emaile2;
	private String notes;
	private String enabled;
	private Integer removed;

	public TmSupplier() {
		super();
	}

	public TmSupplier(String enabled) {
		super();
		this.enabled = enabled;
	}

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_SUPPLIER_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Column(name = "ADDR")
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Column(name = "CONTACT1")
	public String getContact1() {
		return contact1;
	}

	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}

	@Column(name = "TEL_NO1")
	public String getTelNo1() {
		return telNo1;
	}

	public void setTelNo1(String telNo1) {
		this.telNo1 = telNo1;
	}

	@Column(name = "FAX_NO1")
	public String getFaxNo1() {
		return faxNo1;
	}

	public void setFaxNo1(String faxNo1) {
		this.faxNo1 = faxNo1;
	}

	@Column(name = "MOBILE_NO1")
	public String getMobileNo1() {
		return mobileNo1;
	}

	public void setMobileNo1(String mobileNo1) {
		this.mobileNo1 = mobileNo1;
	}

	@Column(name = "EMAIL1")
	public String getEmaile1() {
		return emaile1;
	}

	public void setEmaile1(String emaile1) {
		this.emaile1 = emaile1;
	}

	@Column(name = "CONTACT2")
	public String getContact2() {
		return contact2;
	}

	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}

	@Column(name = "TEL_NO2")
	public String getTelNo2() {
		return telNo2;
	}

	public void setTelNo2(String telNo2) {
		this.telNo2 = telNo2;
	}

	@Column(name = "FAX_NO2")
	public String getFaxNo2() {
		return faxNo2;
	}

	public void setFaxNo2(String faxNo2) {
		this.faxNo2 = faxNo2;
	}

	@Column(name = "MOBILE_NO2")
	public String getMobileNo2() {
		return mobileNo2;
	}

	public void setMobileNo2(String mobileNo2) {
		this.mobileNo2 = mobileNo2;
	}

	@Column(name = "EMAIL2")
	public String getEmaile2() {
		return emaile2;
	}

	public void setEmaile2(String emaile2) {
		this.emaile2 = emaile2;
	}

	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column(name = "MARK_FOR_DELETE")
	public Integer getRemoved() {
		return removed;
	}

	@Override
	public boolean isEntityRemoved() {
		return (null == removed || removed == 0) ? false : true;
	}

	@Override
	public void setRemoved(Integer removed) {
		this.removed = removed;
	}

}
