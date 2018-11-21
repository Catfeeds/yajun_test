package com.wis.mes.production.plan.porder.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wis.core.service.BaseService;
import com.wis.mes.production.plan.porder.entity.ToPorderAviBom;

public interface ToPorderAviBomService extends BaseService<ToPorderAviBom> {

	/**
	 * 保存生产序列对应的BOM信息
	 * 
	 * @param aviId
	 *            生产序列ID
	 * @param tmBomId
	 *            BOM id
	 */
	public void doSaveBom(Integer aviId, Integer tmBomId);

	/**
	 * 导出工艺路径下BOM信息
	 * 
	 * @param response
	 * @param content
	 * @param filePath
	 * @param header
	 * @return
	 */
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<ToPorderAviBom> content,
			String filePath, String[] header);
}
