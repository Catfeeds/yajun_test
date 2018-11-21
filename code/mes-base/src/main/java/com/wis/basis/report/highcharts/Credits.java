/* 
 * Copyright (c) 2017, WIS Express Inc. All rights reserved.
 */
package com.wis.basis.report.highcharts;

import java.util.Map;
import java.util.TreeMap;

/**
 * Highchart默认在图表的右下角放置版权信息的标签
 *
 */
public class Credits {

	protected Map<String, Object> content = new TreeMap<String, Object>();

	protected Credits(Boolean enabled) {
		content.put("enabled", enabled);
	}
}
