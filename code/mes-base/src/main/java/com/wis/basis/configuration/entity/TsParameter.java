package com.wis.basis.configuration.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 *
 * @author 赵宪泉
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年5月25日
 *
 * @desc 参数配置表 bean
 *
 */
@Table(name = "ts_parameter")
public class TsParameter extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** 主键id */
	private Integer id;
	/** 编码 */
	private String code;
	/** 名称 */
	private String name;
	/** 正则表达式 */
	private String regularExpression;
	/** 变量名 */
	private String variuableName;
	/** 默认值 */
	private String defaultValue;
	/** 备注 */
	private String note;
	/** 单位 */
	private String unit;
	/** 参数解释 */
	private String parameterExplain;
	/** 监控岗位编号 */
	private String controlPostNo;
	/** 监控岗位名称 */
	private String controlPostName;
	
	@Column(name = "ID")
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PARAMETER_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "REGULAR_EXPRESSION")
	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	@Column(name = "VARIUABLE_NAME")
	public String getVariuableName() {
		return variuableName;
	}

	public void setVariuableName(String variuableName) {
		this.variuableName = variuableName;
	}

	@Column(name = "DEFAULT_VALUE")
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "UNIT")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	@Column(name = "PARAMETER_EXPLAIN")
	public String getParameterExplain() {
		return parameterExplain;
	}

	public void setParameterExplain(String parameterExplain) {
		this.parameterExplain = parameterExplain;
	}
	@Column(name = "CONTROL_POST_NO")
	public String getControlPostNo() {
		return controlPostNo;
	}

	public void setControlPostNo(String controlPostNo) {
		this.controlPostNo = controlPostNo;
	}
	@Column(name = "CONTROL_POST_NAME")
	public String getControlPostName() {
		return controlPostName;
	}

	public void setControlPostName(String controlPostName) {
		this.controlPostName = controlPostName;
	}
	
}
