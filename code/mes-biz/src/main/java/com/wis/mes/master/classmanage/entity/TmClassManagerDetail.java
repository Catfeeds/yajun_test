package com.wis.mes.master.classmanage.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.employeeno.entity.TmEmployeeNo;
import com.wis.mes.master.uloc.entity.TmUloc;

/**
 * 
 * @author zhuzw
 * @company 上海西信信息科技有限公司
 * @dete 2018-3-25
 * @desc 班次管理详情表
 *
 */
@Table(name="TM_CLASS_MANAGER_DETAIL")
public class TmClassManagerDetail extends AuditEntity{
	
	/**id */
	private Integer id;
	
	/**工位 */
	private Integer tmUlocId;
	
	/** 工位对象*/
	private TmUloc tmUloc;
	
	/**用户 */
	private Integer tmEmployeeNoId;
	
	/** 用户对象*/
	private TmEmployeeNo tmEmployeeNo;
	
	/**备注 */
	private String remark;
	
	/** 班次id*/
	private Integer tmClassManagerId;
	
	/** 班次对象*/
	private TmClassManager tmClassManager;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_CLASS_MANAGER_DETAIL_ID")
	@Column(name = "ID")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id=id;
		
	}

	@Column(name="TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}

	@Column(name="TM_EMPLOYEE_NO_ID")
	public Integer getTmEmployeeNoId() {
		return tmEmployeeNoId;
	}

	public void setTmEmployeeNoId(Integer tmEmployeeNoId) {
		this.tmEmployeeNoId = tmEmployeeNoId;
	}

	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="TM_ULOC_ID")
	public TmUloc getTmUloc() {
		return tmUloc;
	}

	public void setTmUloc(TmUloc tmUloc) {
		this.tmUloc = tmUloc;
	}

	@Column(name="TM_EMPLOYEE_NO_ID")
	public TmEmployeeNo getTmEmployeeNo() {
		return tmEmployeeNo;
	}

	public void setTmEmployeeNo(TmEmployeeNo tmEmployeeNo) {
		this.tmEmployeeNo = tmEmployeeNo;
	}

	@Column(name="TM_CLASS_MANAGER_ID")
	public Integer getTmClassManagerId() {
		return tmClassManagerId;
	}

	public void setTmClassManagerId(Integer tmClassManagerId) {
		this.tmClassManagerId = tmClassManagerId;
	}

	@Column(name="TM_CLASS_MANAGER_ID")
	public TmClassManager getTmClassManager() {
		return tmClassManager;
	}

	public void setTmClassManager(TmClassManager tmClassManager) {
		this.tmClassManager = tmClassManager;
	}
	
	

}
