package com.wis.basis.numRule.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * 
 * @author liuzejun
 * 
 * @company 上海西信信息科技有限公司
 * 
 * @dete 2017-04-18
 * 
 * @desc 序列生产规则主表
 */
@Table(name = "ts_num_rule")
public class TsNumRule extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 编号类型 */
	private String type;
	/** 前缀 */
	private String prefix;
	/** 中缀 */
	private String infix;
	/** 后缀 */
	private String suffix;
	/** 长度 */
	private Integer length;
	/** 当前值 */
	private String currentValue;
	/** 前缀是否重置 */
	private String prefixReset;
	/** 中缀是否重置 */
	private String infixReset;
	/** 后缀是否重置 */
	private String suffixReset;
	/** 增量值 */
	private Integer incrementValue;

	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "PREFIX")
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Column(name = "INFIX")
	public String getInfix() {
		return infix;
	}

	public void setInfix(String infix) {
		this.infix = infix;
	}

	@Column(name = "SUFFIX")
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "LENGTH")
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Column(name = "CURRENT_VALUE")
	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	@Column(name = "PREFIX_RESET")
	public String getPrefixReset() {
		return prefixReset;
	}

	public void setPrefixReset(String prefixReset) {
		this.prefixReset = prefixReset;
	}

	@Column(name = "INFIX_RESET")
	public String getInfixReset() {
		return infixReset;
	}

	public void setInfixReset(String infixReset) {
		this.infixReset = infixReset;
	}

	@Column(name = "SUFFIX_RESET")
	public String getSuffixReset() {
		return suffixReset;
	}

	public void setSuffixReset(String suffixReset) {
		this.suffixReset = suffixReset;
	}

	@Column(name = "INCREMENT_VALUE")
	public Integer getIncrementValue() {
		return incrementValue;
	}

	public void setIncrementValue(Integer incrementValue) {
		this.incrementValue = incrementValue;
	}
}
