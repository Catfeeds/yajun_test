package com.wis.mes.dakin.report.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.dakin.report.vo.FinExportVo;

public interface FinExportService {
	public PageResult<FinExportVo> getFinExportInfo(QueryCriteria criteria);

	public Map<String, Object> exportExcelData(HttpServletResponse response, List<FinExportVo> list, String filePath,
			String[] header);
}
