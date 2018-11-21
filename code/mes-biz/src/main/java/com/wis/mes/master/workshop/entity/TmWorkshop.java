package com.wis.mes.master.workshop.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.plant.entity.TmPlant;

/**
 * @author zhf
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2016年4月14日
 *
 * @desc 车间的bean
 */
@Table(name = "tm_workshop")
public class TmWorkshop extends AuditEntity {

	private static final long serialVersionUID = 1L;

	/** 车间 id */
	private Integer id;
	/** 车间 工厂id */
	private Integer tmPlantId;
	/** 车间 编号 */
	private String no;
	/** 车间 中文名称 */
	private String nameCn;
	/** 车间 英文名称 */
	private String nameEn;
	/** 车间 中文简称 */
	private String nameCnS;
	/** 车间 英文简称 */
	private String nameEnS;
	/** 车间 启用 */
	private String enabled;
	/** 工厂bean引用 */
	private TmPlant plant;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_WORKSHOP_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_PLANT_ID")
	public Integer getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(Integer tmPlantId) {
		this.tmPlantId = tmPlantId;
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

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "TM_PLANT_ID")
	public TmPlant getPlant() {
		return plant;
	}

	public void setPlant(TmPlant plant) {
		this.plant = plant;
	}

	public TmWorkshop() {
		super();
	}

	public TmWorkshop(String enabled) {
		super();
		this.enabled = enabled;
	}

}
