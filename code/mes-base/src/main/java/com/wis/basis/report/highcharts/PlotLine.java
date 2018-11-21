package com.wis.basis.report.highcharts;

import java.util.Map;
import java.util.TreeMap;

/**
 * 标示线是用来标记坐标轴上特殊值的一条直线，在绘图区内绘制一条自定义的线
 *
 */
public class PlotLine {
	protected Map<String, Object> content = new TreeMap<String, Object>();

	protected PlotLine() {
	}

	public void setValue(Double value) {
		content.put("value", value);
	}

	public void setWidth(Integer width) {
		content.put("width", width);
	}

	public void setColor(String color) {
		content.put("color", color);
	}
}
