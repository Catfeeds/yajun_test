package com.wis.mes.master.line.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.workshop.entity.TmWorkshop;

/**
 * @author zhf
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2016年4月14日
 *
 * @desc 产线的bean
 */
@Table(name = "tm_line")
public class TmLine extends AuditEntity {

	private static final long serialVersionUID = 1L;

	/** 产线 id */
	private Integer id;
	/** 产线 对应工厂 id */
	private Integer tmPlantId;
	/** 产线 对应车间 id */
	private Integer tmWorkshopId;
	/** 产线 编号 */
	private String no;
	/** 产线 中文名称 */
	private String nameCn;
	/** 产线 英文名称 */
	private String nameEn;
	/** 产线 启用 */
	private String enabled;
	/** 工厂 bean引用 */
	private TmPlant plant;
	/** 车间 bean引用 */
	private TmWorkshop workshop;
	
	/** 备注*/
	private String remark;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_LINE_ID")
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

	@Column(name = "TM_WORKSHOP_ID")
	public Integer getTmWorkshopId() {
		return tmWorkshopId;
	}

	public void setTmWorkshopId(Integer tmWorkshopId) {
		this.tmWorkshopId = tmWorkshopId;
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

	@Column(name = "TM_WORKSHOP_ID")
	public TmWorkshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(TmWorkshop workshop) {
		this.workshop = workshop;
	}

	
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public TmLine() {
		super();
	}

	public TmLine(String enabled) {
		super();
		this.enabled = enabled;
	}

	public static  TmLine createLineNo(String lineNo){
		TmLine  bean= new TmLine(); 
		bean.setNo(lineNo);
		return bean;
	}
	
}
