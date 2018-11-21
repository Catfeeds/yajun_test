package com.wis.basis.numRule.service;

import com.wis.basis.numRule.entity.NumRuleValue;
import com.wis.basis.numRule.entity.TsNumRule;
import com.wis.core.service.BaseService;

public interface TsNumRuleService extends BaseService<TsNumRule> {

	/**
	 * 根据 序列规则类型 查询对应的规则列表
	 * 
	 * @param type
	 * @return
	 */
	public TsNumRule findByType(String type);

	/**
	 * 根据类型，获取当前sequence
	 * 
	 * @param type
	 *            类型
	 * @param seqRuleNo
	 *            传参
	 * 
	 * @return
	 */
	public String getSerialNumber(String type, NumRuleValue seqRuleNo);

	/**
	 * 获取bean 并对数据校验
	 * 
	 * @param jsonBean
	 * @return
	 * @throws IllegalAccessException
	 */
	public TsNumRule checkAndGetBean(String jsonBean);
}
