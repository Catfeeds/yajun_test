package com.wis.basis.report.highcharts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wis.basis.report.highcharts.Title.Align;

public class Axis {

	protected Map<String, Object> content = new TreeMap<String, Object>();

	private Title title;
	private List<PlotLine> plotLines;
	private List<String> categories;

	protected Axis() {
	}

	/**
	 * 创建图表标题，参数为空表示不指定
	 * 
	 * @param text
	 *            标题文本
	 * @param align
	 *            对齐方式
	 * @param x
	 *            水平偏移量
	 * @return
	 */
	public Title createTitle(String text, Align align, Integer x) {
		title = new Title(text, align, x);
		content.put("title", title.content);
		return title;
	}

	public void addCategory(String category) {
		if (null == categories) {
			categories = new ArrayList<String>();
			content.put("categories", categories);
		}

		if (!categories.contains(category)) {
			categories.add(category);
		}
	}

	/**
	 * 对分类进行排序
	 */
	public void addCategoryFinished() {
		Collections.sort(categories);
	}

	public String[] getCategories() {
		return categories.toArray(new String[categories.size()]);
	}

	public int indexOfCategory(String category) {
		return categories.indexOf(category);
	}
	
	/**
	 * 设置坐标轴线宽
	 * @param width
	 */
	public void setLineWidth(Integer width) {
		content.put("lineWidth", width);
	}

	/**
	 * 设置网格线宽度
	 * @param width
	 */
	public void setGridLineWidth(Integer width) {
		content.put("gridLineWidth", width);
	}

	/**
	 * 添加标识线
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PlotLine addPlotLine() {
		if (null == plotLines) {
			plotLines = new ArrayList<PlotLine>();
			content.put("plotLines", new ArrayList<Map<String, Object>>());
		}

		PlotLine plotLine = new PlotLine();
		plotLines.add(plotLine);
		((List) content.get("plotLines")).add(plotLine.content);
		return plotLine;
	}
	
	/**
	 * 设置是否对面显示
	 * @param opposite
	 */
	public void setOpposite(boolean opposite) {
		content.put("opposite", opposite);
	}
	
}
