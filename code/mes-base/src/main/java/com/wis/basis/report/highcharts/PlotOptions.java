/* 
 * Copyright (c) 2017, WIS Express Inc. All rights reserved.
 */
package com.wis.basis.report.highcharts;

import java.util.Map;
import java.util.TreeMap;

/**
 * 数据列配置是针对所有数据列及某种数据列有效的通用配置。
 */
public class PlotOptions {

	protected Map<String, Object> content = new TreeMap<String, Object>();

	private PieOptions pieOption;
	private SeriesOptions seriesOption;

	protected PlotOptions() {
	}

	/**
	 * 创建饼图选项，默认切片可以选中，高度35，显示图例
	 * 
	 * @return
	 */
	public PieOptions createPieOption() {
		this.pieOption = new PieOptions();
		content.put("pie", pieOption.content);
		return pieOption;
	}

	/**
	 * 创建针对所有图标的选项
	 * @return
	 */
	public SeriesOptions createSeriesOption() {
		this.seriesOption = new SeriesOptions();
		content.put("series", seriesOption.content);
		return seriesOption;
	}

}
