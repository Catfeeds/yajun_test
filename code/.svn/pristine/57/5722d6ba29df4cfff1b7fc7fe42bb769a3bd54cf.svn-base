package com.wis.basis.report.highcharts;

import java.util.Map;
import java.util.TreeMap;

/**
 * 关于图表区和图形区的参数及一般图表通用参数。
 */
public class Chart {

	protected Map<String, Object> content = new TreeMap<String, Object>();

	private Options3d options3d;

	protected Chart() {
		this(PlotType.LINE);
	}

	protected Chart(PlotType type) {
		content.put("type", type.getType());
	}
	
	/**
	 * 启用3D
	 * @param alpha 内旋转角度
	 * @return 3D配置对象
	 */
	public Options3d enable3D(Integer alpha) {
		options3d = new Options3d(alpha);
		content.put("options3d", options3d.content);
		return options3d;
	}

	public static enum PlotType {
		LINE("line"), COLUMN("column"), PIE("pie");
		private String type;

		private PlotType(String type) {
			this.type = type;
		}

		public String getType() {
			return this.type;
		}
	}
	
	/**
	 * 设置图表高度，Null自动匹配高度
	 * @param height
	 */
	public void setHeight(Integer height){
		content.put("height", height);
	}
	
}
