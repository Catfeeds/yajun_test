package com.wis.mes.dakin.report.service;

import java.util.Map;

public interface TbNoFaultDetailsService {
	public Map<String, String> getFinNoFaultDetails(String createTimeStart, String createTimeEnd);
	public Map<String, String> noFaultedDetailsYearShow(String year, String createTimeStart, String createTimeEnd);
}
