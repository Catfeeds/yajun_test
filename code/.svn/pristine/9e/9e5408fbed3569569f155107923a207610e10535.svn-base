/* 
 * Copyright (c) 2017, WIS Express Inc. All rights reserved.
 */
package com.wis.basis.report.highcharts;

import java.util.Map;
import java.util.TreeMap;
/**
 * 针对饼图的数据列选项
 */
public class PieOptions {
	protected Map<String, Object> content = new TreeMap<String, Object>();
	private DataLabels dataLabels;

	protected PieOptions() {
		content.put("allowPointSelect", true);
		content.put("depth", 35);
		content.put("showInLegend", true);
	}

	/**
	 * 设置模块是否可被选中
	 * 
	 * @param allowPointSelect
	 */
	public void setAllowPointSelect(boolean allowPointSelect) {
		content.put("allowPointSelect", allowPointSelect);
	}

	public void setDepth(int depth) {
		content.put("depth", depth);
	}

	/**
	 * 设置是否在图例中显示
	 * 
	 * @param showInLegend
	 */
	public void setShowInLegend(boolean showInLegend) {
		content.put("showInLegend", showInLegend);
	}

	/**
	 * 配置数据标签，默认显示数据值
	 * @return
	 */
	public DataLabels createDataLabels() {
		dataLabels = new DataLabels();
		dataLabels.setFormat("{point.y}");
		content.put("dataLabels", dataLabels.content);
		return dataLabels;
	}
}
