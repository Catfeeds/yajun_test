package com.wis.basis.numRule.entity;

public class NumRuleValue {

	/** 物料编码 */
	private String partNo;
	/** 产线编码 */
	private String lineNo;
	/** 工厂编码 */
	private String plantNo;
	/** 车间编码 */
	private String workShopNo;
	/** 工单编码 */
	private String porderNo;
	/** 生产序列编码 */
	private String aviNo;
	/** 不检查项 */
	private String unCheck;
	private String supplierNo;

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getPlantNo() {
		return plantNo;
	}

	public void setPlantNo(String plantNo) {
		this.plantNo = plantNo;
	}

	public String getWorkShopNo() {
		return workShopNo;
	}

	public void setWorkShopNo(String workShopNo) {
		this.workShopNo = workShopNo;
	}

	public String getPorderNo() {
		return porderNo;
	}

	public void setPorderNo(String porderNo) {
		this.porderNo = porderNo;
	}

	public NumRuleValue(String partNo, String lineNo, String plantNo, String workShopNo, String porderNo,
			String aviNo) {
		super();
		this.partNo = partNo;
		this.lineNo = lineNo;
		this.plantNo = plantNo;
		this.workShopNo = workShopNo;
		this.porderNo = porderNo;
		this.aviNo = aviNo;
	}

	public NumRuleValue(String partNo, String lineNo, String plantNo, String workShopNo, String porderNo, String aviNo,
			String unCheck, String supplierNo) {
		super();
		this.partNo = partNo;
		this.lineNo = lineNo;
		this.plantNo = plantNo;
		this.workShopNo = workShopNo;
		this.porderNo = porderNo;
		this.aviNo = aviNo;
		this.unCheck = unCheck;
		this.supplierNo = supplierNo;
	}

	public NumRuleValue() {
		super();
	}

	public String getAviNo() {
		return aviNo;
	}

	public void setAviNo(String aviNo) {
		this.aviNo = aviNo;
	}

	public String getUnCheck() {
		return unCheck;
	}

	public void setUnCheck(String unCheck) {
		this.unCheck = unCheck;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

}
