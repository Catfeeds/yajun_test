package com.wis.mes.master.bom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.mes.master.base.service.MasterBaseService;
import com.wis.mes.master.bom.entity.TmBomDetail;

/**
 * @author liuzejun
 * 
 * @company 上海西信信息科技有限公司
 * 
 * @dete 2017-04-18
 * 
 * @desc BOM明细 Service
 */
public interface TmBomDetailService extends MasterBaseService<TmBomDetail> {

	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmBomDetail> content, String string,
			String[] header);

	public List<TmBomDetail> getTmBomDetails(Integer tmBomId);

	List<TmBomDetail> findByForginKey(Integer tmBomId);

	public String doImportExcelData(Workbook book, HttpServletRequest request, String tmBomId);
}
