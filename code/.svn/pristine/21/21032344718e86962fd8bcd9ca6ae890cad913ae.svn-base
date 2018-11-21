/* 
 * Copyright (c) 2017, WIS Express Inc. All rights reserved.
 */
package com.wis.basis.report.highcharts;

import java.util.Map;
import java.util.TreeMap;

/**
 * 饼图数据
 */
public class PieData implements SeriesData {

	protected Map<String, Object> content = new TreeMap<String, Object>();

	protected PieData() {
	}

	public void setName(String name) {
		content.put("name", name);
	}

	public void setY(Number y) {
		content.put("y", y);
	}

	/**
	 * 设置是否显示切割偏移
	 * @param sliced
	 */
	public void setSliced(Boolean sliced) {
		content.put("sliced", sliced);
	}

	/**
	 * 设置是否选中
	 * @param selected
	 */
	public void setSelected(Boolean selected) {
		content.put("selected", selected);
	}

	@Override
	public Map<String, Object> getContent() {
		return content;
	}
}
