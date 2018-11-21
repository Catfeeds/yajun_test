package com.wis.mes.master.parametermanage.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AbstractEntity;

@Table(name = "tm_parameter_manage")
public class TmParameterManage extends AbstractEntity {

	private Integer id;
	/** 产品型号 **/
	private String productModel;

	private String remarks;

	/** 背番号 **/
	private String backNumber;
	/** 机种名 **/
	private String machineOfName;
	
	private List<TmParameterManageDetail> parameterManageDetail;
	
	private String details;

	@Column(name = "ID")
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "seq_parameter_manage_id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "PRODUCT_MODEL")
	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Column(name = "BACK_NUMBER")
	public String getBackNumber() {
		return backNumber;
	}

	public void setBackNumber(String backNumber) {
		this.backNumber = backNumber;
	}

	@Column(name = "MACHINE_OF_NAME")
	public String getMachineOfName() {
		return machineOfName;
	}

	public void setMachineOfName(String machineOfName) {
		this.machineOfName = machineOfName;
	}

	@Transient
	public List<TmParameterManageDetail> getParameterManageDetail() {
		return parameterManageDetail;
	}

	public void setParameterManageDetail(
			List<TmParameterManageDetail> parameterManageDetail) {
		this.parameterManageDetail = parameterManageDetail;
	}
	
	@Transient
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
