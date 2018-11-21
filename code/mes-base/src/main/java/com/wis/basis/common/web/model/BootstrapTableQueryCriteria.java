package com.wis.basis.common.web.model;

import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;

public class BootstrapTableQueryCriteria extends QueryCriteria {

	private String order = "desc";
	private Integer limit = 10;
	private Integer offset = -1;
	private String sort;

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
		setOrderDirection("desc".equalsIgnoreCase(order) ? OrderEnum.DESC : OrderEnum.ASC);
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
		setRows(limit);
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
		this.setOrderField(sort);
	}
}
