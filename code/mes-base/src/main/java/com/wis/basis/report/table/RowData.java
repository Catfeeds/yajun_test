package com.wis.basis.report.table;

import java.util.Map;

import com.google.common.collect.Maps;

public class RowData {
	private Map<String, Object> data;

	protected RowData() {
		data = Maps.newLinkedHashMap();
	}

	public void putData(String key, Object value) {
		this.data.put(key, value);
	}

	public Map<String, Object> getData() {
		return data;
	}

	public RowData(Map<String, Object> data) {
		this.data = data;
	}

}
