package com.wis.mes.dakin.report.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wis.core.service.BaseService;
import com.wis.mes.dakin.report.entity.FinMovableRate;

public interface FinMovableRateService extends BaseService<FinMovableRate> {
	public List<Map<String, String>> getFinMovableRateEveryDayInfo(String shiftNo, String createTimeStart, String createTimeEnd);

	public void exportExcelData(HttpServletResponse response, List<FinMovableRate> content, String path,
			String[] header);
}
