package com.wis.basis.report.highcharts;

import java.util.Map;
import java.util.TreeMap;

/**
 * 针对所有图表类型有效
 */
public class SeriesOptions {
	protected Map<String, Object> content = new TreeMap<String, Object>();
	private DataLabels dataLabels;

	protected SeriesOptions() {
	}

	/**
	 * 配置数据标签，默认显示数据值
	 * @return
	 */
	public DataLabels createDataLabels() {
		if (dataLabels == null) {
			dataLabels = new DataLabels();
			content.put("dataLabels", dataLabels.content);
		}
		return dataLabels;
	}
}
