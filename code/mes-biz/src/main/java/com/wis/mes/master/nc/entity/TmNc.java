package com.wis.mes.master.nc.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AuditEntity;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 不合格代码bean
 *
 */
@Table(name = "tm_nc")
public class TmNc extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 不合格代码组主键 */
	private Integer tmNcGroupId;
	/** 不合格代码组bean */
	private TmNcGroup ncGroup = new TmNcGroup();
	/** 不合格编码 */
	private String no;
	/** 不合格描述 */
	private String name;
	/** 对应外系统编码 */
	private String extCode;
	/*** 不合格类型 */
	private String type;
	/**备注**/
	private String remarks;
	
	/**故障等级id**/
	private Integer tmFaultGradeId;
	
	private TmFaultGrade faultGrade;
	
	private String ngLevel;
	
	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_NC_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	@Column(name = "TM_NC_GROUP_ID")
	public Integer getTmNcGroupId() {
		return tmNcGroupId;
	}

	public void setTmNcGroupId(final Integer tmNcGroupId) {
		this.tmNcGroupId = tmNcGroupId;
	}

	@Column(name = "TM_NC_GROUP_ID")
	public TmNcGroup getNcGroup() {
		return ncGroup;
	}

	public void setNcGroup(final TmNcGroup ncGroup) {
		this.ncGroup = ncGroup;
	}

	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(final String no) {
		this.no = no;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "EXT_CODE")
	public String getExtCode() {
		return extCode;
	}

	public void setExtCode(final String extCode) {
		this.extCode = extCode;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Column(name = "TM_FAULT_GRADE_ID")
	public Integer getTmFaultGradeId() {
		return tmFaultGradeId;
	}

	public void setTmFaultGradeId(Integer tmFaultGradeId) {
		this.tmFaultGradeId = tmFaultGradeId;
	}

	@Column(name = "TM_FAULT_GRADE_ID")
	public TmFaultGrade getFaultGrade() {
		return faultGrade;
	}

	public void setFaultGrade(TmFaultGrade faultGrade) {
		this.faultGrade = faultGrade;
	}

	@Transient
	public String getNgLevel() {
		return ngLevel;
	}

	public void setNgLevel(String ngLevel) {
		this.ngLevel = ngLevel;
	}
	
	
}
