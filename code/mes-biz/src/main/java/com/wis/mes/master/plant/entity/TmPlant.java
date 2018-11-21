package com.wis.mes.master.plant.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * @author zhf
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2016年4月14日
 *
 * @desc 工厂的bean
 */
@Table(name = "tm_plant")
public class TmPlant extends AuditEntity {

	private static final long serialVersionUID = 1L;

	/** 工厂 id */
	private Integer id;
	/** 公司 编号 */
	private String no;
	/** 公司 中文名称 */
	private String nameCn;
	/** 工厂 英文名称 */
	private String nameEn;
	/** 工厂中文简称名称 */
	private String nameCnS;
	/** 工厂 英文简称 */
	private String nameEnS;
	/** 工厂 中文地址1 */
	private String addrCn1;
	/** 工厂 中文地址2 */
	private String addrCn2;
	/** 工厂 英文地址1 */
	private String addrEn1;
	/** 工厂 英文地址2 */
	private String addrEn2;
	/** 工厂 启用 */
	private String enabled;
	
	/** 公司地址*/
	private String companyAddress;
	
	/** 联系方式*/
	private String contact;
	
	/** 备注*/
	private String remark;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PLANT_ID")
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

	@Column(name = "NAME_CN")
	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	@Column(name = "NAME_EN")
	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	@Column(name = "NAME_CN_S")
	public String getNameCnS() {
		return nameCnS;
	}

	public void setNameCnS(String nameCnS) {
		this.nameCnS = nameCnS;
	}

	@Column(name = "NAME_EN_S")
	public String getNameEnS() {
		return nameEnS;
	}

	public void setNameEnS(String nameEnS) {
		this.nameEnS = nameEnS;
	}

	@Column(name = "ADDR_CN1")
	public String getAddrCn1() {
		return addrCn1;
	}

	public void setAddrCn1(String addrCn1) {
		this.addrCn1 = addrCn1;
	}

	@Column(name = "ADDR_CN2")
	public String getAddrCn2() {
		return addrCn2;
	}

	public void setAddrCn2(String addrCn2) {
		this.addrCn2 = addrCn2;
	}

	@Column(name = "ADDR_EN1")
	public String getAddrEn1() {
		return addrEn1;
	}

	public void setAddrEn1(String addrEn1) {
		this.addrEn1 = addrEn1;
	}

	@Column(name = "ADDR_EN2")
	public String getAddrEn2() {
		return addrEn2;
	}

	public void setAddrEn2(String addrEn2) {
		this.addrEn2 = addrEn2;
	}

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	
	@Column(name = "COMPANY_ADDRESS")
	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	@Column(name = "CONTACT")
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static TmPlant createPlantByNo(String no) {
		TmPlant plant = new TmPlant();
		plant.setNo(no);
		return plant;
	}

	public TmPlant(String enabled) {
		super();
		this.enabled = enabled;
	}

	public TmPlant() {
		super();
	}

}
