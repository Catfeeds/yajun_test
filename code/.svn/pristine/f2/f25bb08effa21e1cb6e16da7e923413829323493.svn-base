package com.wis.mes.master.labelmanage.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AbstractEntity;
import com.wis.mes.master.line.entity.TmLine;

@Table(name = "TM_LABEL_MANAGE")
public class TbLabelManage extends AbstractEntity {

	private Integer id;
	/**标签ID**/
	private String epcId;
	/**托盘编号*/
	private String salverNo;
	/**品牌**/
	private String brand; 
	/**批次**/
	private String batch;
	/**产线ID**/
	private String tmLineId;
	
	private TmLine tmLine;
	
	private  String remarks;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_LABEL_MANAGE_ID")
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "EPC_ID")
	public String getEpcId() {
		return epcId;
	}

	public void setEpcId(String epcId) {
		this.epcId = epcId;
	}

	@Column(name = "SALVER_NO")
	public String getSalverNo() {
		return salverNo;
	}

	public void setSalverNo(String salverNo) {
		this.salverNo = salverNo;
	}

	@Column(name = "BRAND")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Column(name = "BATCH")
	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	@Column(name = "TM_LINE_ID")
	public String getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(String tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "TM_LINE_ID")
	public TmLine getTmLine() {
		return tmLine;
	}

	public void setTmLine(TmLine tmLine) {
		this.tmLine = tmLine;
	}
	
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
