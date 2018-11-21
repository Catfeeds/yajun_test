package com.wis.basis.report.highcharts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 图表的数据列，可以在每个数据列中指定其参数， 另外也可以通过数据列配置（ plotOptions） 来指定对
 * 某一种类型的数据列有效的配置。其优先级高于数据列配置中指定的配置。
 *
 */
public class Series {

	protected Map<String, Object> content = new TreeMap<String, Object>();
	private List<Object> dataList;

	protected Series(String name) {
		if (null != name) {
			content.put("name", name);
		}
	}

	/**
	 * 数据类型为Number或者SeriesData
	 * 
	 * @param data
	 * @see SeriesData
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addData(Object data) {
		if (null == dataList) {
			dataList = new ArrayList<Object>();
			if (data instanceof SeriesData) {
				this.content.put("data", new ArrayList<Map<String, Object>>());
			} else {
				this.content.put("data", dataList);
			}
		}

		this.dataList.add(data);
		if (data instanceof SeriesData) {
			((List) this.content.get("data")).add(((SeriesData) data).getContent());
		}
	}

	public PieData addPieData() {
		PieData data = new PieData();
		addData(data);
		return data;
	}

	public void addData(int index, Number data) {
		if (index < 0) {
			return;
		}
		if (null == dataList) {
			dataList = new ArrayList<Object>();
			this.content.put("data", dataList);
		}
		if (index > dataList.size()) {
			for (int i = dataList.size(); i < index; i++) {
				dataList.add(i, null);
			}
		}
		dataList.add(index, data);
	}

	public Object[] getData() {
		return dataList.toArray(new Object[dataList.size()]);
	}

	public void setYAxis(int index) {
		content.put("yAxis", index);
	}
}
