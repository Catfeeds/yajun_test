package com.wis.mes.master.supplier.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.part.entity.TmPart;

@Table(name = "tm_supplier_part")
public class TmSupplierPart extends AuditEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	// 零件ID
	private Integer tmPartId;
	// 物料引用
	private TmPart part;
	// 供应商ID
	private Integer tmSupplierId;
	// 供应商引用
	private TmSupplier supplier;
	// 启用
	private String enabled;

	public TmSupplierPart() {
		super();
	}

	public TmSupplierPart(String enabled) {
		super();
		this.enabled = enabled;
	}

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_SUPPLIER_PART_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "TM_PART_ID")
	public Integer getTmPartId() {
		return tmPartId;
	}

	public void setTmPartId(Integer tmPartId) {
		this.tmPartId = tmPartId;
	}

	@Column(name = "TM_PART_ID")
	public TmPart getPart() {
		return part;
	}

	public void setPart(TmPart part) {
		this.part = part;
	}

	@Column(name = "TM_SUPPLIER_ID")
	public Integer getTmSupplierId() {
		return tmSupplierId;
	}

	public void setTmSupplierId(Integer tmSupplierId) {
		this.tmSupplierId = tmSupplierId;
	}

	@Column(name = "TM_SUPPLIER_ID")
	public TmSupplier getSupplier() {
		return supplier;
	}

	public void setSupplier(TmSupplier supplier) {
		this.supplier = supplier;
	}

}
