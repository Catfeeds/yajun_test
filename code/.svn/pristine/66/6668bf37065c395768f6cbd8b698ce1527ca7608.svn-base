package com.wis.mes.master.workshop.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.framework.entity.DictEntry;
import com.wis.mes.master.base.service.MasterBaseService;
import com.wis.mes.master.workshop.entity.TmWorkshop;

public interface TmWorkshopService extends MasterBaseService<TmWorkshop> {
	List<TmWorkshop> findWShopNameOrIdById(final Integer plantId);

	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmWorkshop> list, String filePath,
			String[] header);

	String doImportExcelData(Workbook workbook, HttpServletRequest request);

	List<DictEntry> getDictItem(TmWorkshop tmWorkshop);

	Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmWorkshop> list);
}
