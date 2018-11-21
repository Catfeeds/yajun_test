package com.wis.basis.report.highcharts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wis.basis.report.IChart;
import com.wis.basis.report.highcharts.Chart.PlotType;
import com.wis.basis.report.highcharts.Title.Align;

public class HighCharts implements IChart {
	private Map<String, Object> content = new TreeMap<String, Object>();

	private Chart chart;

	private Title title;

	private List<Series> seriesList;

	private Axis xAxis;

	private List<Axis> yAxises;

	private Tooltip tooltip;

	private String id;

	private Credits credits;

	private PlotOptions plotOptions;

	public HighCharts() {
		this("");
	}

	public HighCharts(String id) {
		this.id = id;
		credits = new Credits(false);
		content.put("credits", credits.content);
	}

	public String getId() {
		return id;
	}

	public Chart createChart() {
		return createChart(PlotType.LINE);
	}

	public Chart createChart(PlotType plotType) {
		chart = new Chart(plotType);
		content.put("chart", chart.content);
		return chart;
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

	/**
	 * 添加一个数据列
	 * @param name 数据列名称，为空表示不指定
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Series addSeries(String name) {
		if (null == seriesList) {
			seriesList = new ArrayList<Series>();
			content.put("series", new ArrayList<Map<String, Object>>());
		}
		Series series = new Series(name);
		seriesList.add(series);
		((List) content.get("series")).add(series.content);
		return series;
	}

	/**
	 * 添加X抽
	 * @return
	 */
	public Axis createxAxis() {
		this.xAxis = new Axis();
		content.put("xAxis", xAxis.content);
		return xAxis;
	}

	/**
	 * 添加一个Y轴
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Axis createyAxis() {
		if (null == yAxises) {
			yAxises = new ArrayList<Axis>();
			content.put("yAxis", new ArrayList<Map<String, Object>>());
		}
		Axis yAxis = new Axis();
		yAxises.add(yAxis);
		((List) content.get("yAxis")).add(yAxis.content);
		return yAxis;
	}

	/**
	 * 添加提示信息框
	 * @return
	 */
	public Tooltip createTooltip() {
		this.tooltip = new Tooltip();
		content.put("tooltip", tooltip.content);
		return tooltip;
	}

	/**
	 * 
	 * @param enabled
	 *            获取默认版权信息标签
	 */
	public Credits retrieveCredits() {
		return credits;
	}

	/**
	 * 创建全局饼图数据列配置，默认切片可以选中，高度35，显示图例
	 * 
	 * @return
	 */
	public PieOptions createPiePlotOptions() {
		createPlotOptions();
		return plotOptions.createPieOption();
	}

	private void createPlotOptions() {
		if (null == plotOptions) {
			plotOptions = new PlotOptions();
			content.put("plotOptions", plotOptions.content);
		}
	}

	/**
	 * 
	 * 创建全局图形选项
	 * 
	 * @return PlotOptions
	 */
	public SeriesOptions createSeriesOptions() {
		createPlotOptions();
		return plotOptions.createSeriesOption();
	}

	public Map<String, Object> getData() {
		return content;
	}
}
