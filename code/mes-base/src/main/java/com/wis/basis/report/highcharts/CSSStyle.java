package com.wis.basis.report.highcharts;

import java.util.Map;
import java.util.TreeMap;

/**
 * CSS样式
 */
public class CSSStyle {

	protected Map<String, Object> content = new TreeMap<String, Object>();

	protected CSSStyle() {

	}

	/**
	 * 设置CSS样式
	 * 
	 * @param key
	 * @param value
	 */
	public void setStype(String key, Object value) {
		content.put(key, value);
	}

}
