package com.wis.mes.master.part.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.plant.entity.TmPlant;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 工艺路径bean
 *
 */
@Table(name = "tm_part")
public class TmPart extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 工厂主键 */
	private Integer tmPlantId;
	/** 工厂bean */
	private TmPlant plant;
	/** 编码 */
	private String no;
	/** 中文描述 */
	private String nameCn;
	/** 英文描述 */
	private String nameEn;
	/** 中文简称 */
	private String nameCnS;
	/** 英文简称 */
	private String nameEnS;
	/** 物料类型 */
	private String type;
	/** 最大批次大小 */
	private Integer batchQty;
	/** 单位 */
	private String baseUnit;
	/** 启用 */
	private String enabled;
	/** 规格型号 */
	private String spectyp;// SPECTYP;
	/** 质保单位 */
	private String ukunit;// UKUNIT;
	/** 备注 */
	private String remarks;// REMARKS;
	/** 质保时间 */
	private Date uktime;// UKTIME;

	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PART_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	@Column(name = "TM_PLANT_ID")
	public Integer getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(final Integer tmPlantId) {
		this.tmPlantId = tmPlantId;
	}

	@Column(name = "TM_PLANT_ID")
	public TmPlant getPlant() {
		return plant;
	}

	public void setPlant(final TmPlant plant) {
		this.plant = plant;
	}

	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(final String no) {
		this.no = no;
	}

	@Column(name = "NAME_CN")
	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(final String nameCn) {
		this.nameCn = nameCn;
	}

	@Column(name = "NAME_EN")
	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(final String nameEn) {
		this.nameEn = nameEn;
	}

	@Column(name = "NAME_CN_S")
	public String getNameCnS() {
		return nameCnS;
	}

	public void setNameCnS(final String nameCnS) {
		this.nameCnS = nameCnS;
	}

	@Column(name = "NAME_EN_S")
	public String getNameEnS() {
		return nameEnS;
	}

	public void setNameEnS(final String nameEnS) {
		this.nameEnS = nameEnS;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@Column(name = "BATCH_QTY")
	public Integer getBatchQty() {
		return batchQty;
	}

	public void setBatchQty(final Integer batchQty) {
		this.batchQty = batchQty;
	}

	@Column(name = "BASE_UNIT")
	public String getBaseUnit() {
		return baseUnit;
	}

	public void setBaseUnit(final String baseUnit) {
		this.baseUnit = baseUnit;
	}

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(final String enabled) {
		this.enabled = enabled;
	}

	public TmPart() {
		super();
	}

	public TmPart(final String type, final String enabled) {
		super();
		this.type = type;
		this.enabled = enabled;
	}

	public TmPart(String enabled) {
		super();
		this.enabled = enabled;
	}

	@Column(name = "SPECTYP")
	public String getSpectyp() {
		return spectyp;
	}

	public void setSpectyp(String spectyp) {
		this.spectyp = spectyp;
	}

	@Column(name = "UKUNIT")
	public String getUkunit() {
		return ukunit;
	}

	public void setUkunit(String ukunit) {
		this.ukunit = ukunit;
	}
	
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Column(name = "UKTIME")
	public Date getUktime() {
		return uktime;
	}

	public void setUktime(Date uktime) {
		this.uktime = uktime;
	}
}
