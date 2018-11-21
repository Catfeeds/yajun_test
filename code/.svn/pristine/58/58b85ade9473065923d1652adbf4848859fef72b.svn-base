package com.wis.basis.report.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.wis.basis.report.ITable;

public class Table implements ITable {
	private String name;
	private String id;
	private List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

	public Table() {
	}

	public Table(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public String getId() {
		if (StringUtils.isBlank(id)) {
			return UUID.randomUUID().toString();
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public RowData addRow() {
		RowData rowData = new RowData();
		rows.add(rowData.getData());
		return rowData;
	}
	
	public void addRow(RowData  row) {
		rows.add(row.getData());
	}

	@Override
	public List<Map<String, Object>> getContents() {
		return rows;
	}

}
