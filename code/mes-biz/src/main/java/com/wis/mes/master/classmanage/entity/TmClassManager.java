package com.wis.mes.master.classmanage.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.plant.entity.TmPlant;

/**
 * 
 * @author zhuzw
 * @company 上海西信信息科技有限公司
 * @dete 2018-3-25
 * @desc 班次管理主表
 *
 */
@Table(name = "tm_class_manager")
public class TmClassManager extends AuditEntity{

	/**
	 * 
	 */  
	private static final long serialVersionUID = 1L;

	/** id 主健*/
	private Integer id;
	
	/** 所属产线id*/
	private Integer tmLineId;
	
	/** 产线*/
	private TmLine tmLine;
	/** 班次*/
	private String className;
	
	/** 所属公司id*/
	private Integer tmPlantId;
	
	/** 所属公司*/
	private TmPlant tmPlant;
	
	/** 班组*/
	private String classGroup;
	
	/** 启用*/
	private String enabled;
	
	
	/** 备注*/
	private String remark;
	
	public TmClassManager() {}
	
	public TmClassManager(String enabled) {
		this.enabled = enabled;
	}
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_CLASS_MANAGER_ID")
	@Column(name = "ID")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id=id;
		
	}

	@Column( name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column( name = "TM_LINE_ID")
	public TmLine getTmLine() {
		return tmLine;
	}

	public void setTmLine(TmLine tmLine) {
		this.tmLine = tmLine;
	}

	@Column( name = "CLASS_NAME")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name="TM_PLANT_ID")
	public Integer getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(Integer tmPlantId) {
		this.tmPlantId = tmPlantId;
	}

	@Column(name="TM_PLANT_ID")
	public TmPlant getTmPlant() {
		return tmPlant;
	}

	public void setTmPlant(TmPlant tmPlant) {
		this.tmPlant = tmPlant;
	}

	@Column(name="CLASS_GROUP")
	public String getClassGroup() {
		return classGroup;
	}

	public void setClassGroup(String classGroup) {
		this.classGroup = classGroup;
	}

	@Column(name="ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
