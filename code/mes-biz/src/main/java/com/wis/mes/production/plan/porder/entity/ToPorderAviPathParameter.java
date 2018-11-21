package com.wis.mes.production.plan.porder.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.entity.AuditEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderAviPathParameter
 * 
 * @author liuzejun
 *
 * @date 2017年5月27日 下午1:37:56
 * 
 * @Description: 参数bean
 */
@Table(name = "to_porder_avi_path_parameter")
public class ToPorderAviPathParameter extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 生产序列工艺路径站点ID */
	private Integer toPorderAviPathId;
	/** 参数ID */
	private Integer tsParameterId;
	/** 参数bean */
	private TsParameter parameter;
	/** 备注 */
	private String note;
	/** 启用 */
	private String enabled;
	/** 是否必填 */
	private String isRequired;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PORDER_PARAMETER_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TO_PORDER_AVI_PATH_ID")
	public Integer getToPorderAviPathId() {
		return toPorderAviPathId;
	}

	public void setToPorderAviPathId(Integer toPorderAviPathId) {
		this.toPorderAviPathId = toPorderAviPathId;
	}

	@Column(name = "TS_PARAMETER_ID")
	public Integer getTsParameterId() {
		return tsParameterId;
	}

	public void setTsParameterId(Integer tsParameterId) {
		this.tsParameterId = tsParameterId;
	}

	@Column(name = "TS_PARAMETER_ID")
	public TsParameter getParameter() {
		return parameter;
	}

	public void setParameter(TsParameter parameter) {
		this.parameter = parameter;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "IS_REQUIRED")
	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

}
