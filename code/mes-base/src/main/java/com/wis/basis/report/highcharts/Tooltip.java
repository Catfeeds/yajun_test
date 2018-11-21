package com.wis.basis.report.highcharts;

import java.util.Map;
import java.util.TreeMap;

/**
 * 提示信息
 */
public class Tooltip {
	protected Map<String, Object> content = new TreeMap<String, Object>();

	protected Tooltip() {
	}

	/**
	 * 设置提示信息前缀
	 * @param valuePrefix
	 */
	public void setValuePrefix(String valuePrefix) {
		content.put("valuePrefix", valuePrefix);
	}

	/**
	 * 设置提示信息后缀
	 * @param valueSuffix
	 */
	public void setValueSuffix(String valueSuffix) {
		content.put("valueSuffix", valueSuffix);
	}
}
