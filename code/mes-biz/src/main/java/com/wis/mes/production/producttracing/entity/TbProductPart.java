package com.wis.mes.production.producttracing.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.part.entity.TmPart;
/**
 * 关键件
 * @author zhuzw
 *
 */
@Table(name="TB_PRODUCT_PART")
public class TbProductPart extends AuditEntity{

	/**
	 * id
	 */
	private Integer id;
	
	/**
	 * 物料id
	 */
	private Integer tmPartId;
	
	/**
	 * 物料对象
	 */
	private TmPart tmPart;
	
	/**
	 * 数量
	 */
	private Integer partNumber;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 产品id
	 */
	private Integer tbProductTracingId;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PRODUCT_PART_ID")
	@Column(name = "ID")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id=id;
	}

	@Column(name="TM_PART_ID")
	public Integer getTmPartId() {
		return tmPartId;
	}

	public void setTmPartId(Integer tmPartId) {
		this.tmPartId = tmPartId;
	}

	@Column(name="TM_PART_ID")
	public TmPart getTmPart() {
		return tmPart;
	}

	public void setTmPart(TmPart tmPart) {
		this.tmPart = tmPart;
	}

	@Column(name="PART_NUMBER")
	public Integer getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(Integer partNumber) {
		this.partNumber = partNumber;
	}

	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="TB_PRODUCT_TRACING_ID")
	public Integer getTbProductTracingId() {
		return tbProductTracingId;
	}

	public void setTbProductTracingId(Integer tbProductTracingId) {
		this.tbProductTracingId = tbProductTracingId;
	}
	
	

}
