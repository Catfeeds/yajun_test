package com.wis.mes.master.plant.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.plant.entity.TmPlant;

public interface TmPlantService extends BaseService<TmPlant> {
	List<DictEntry> getDictItem(TmPlant tmPlant);

	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmPlant> list, String filePath,
			String[] header);
	Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmPlant> list,
			String parentHeadStr,String childHeadStr, String filePath);

	String doImportExcelData(Workbook workbook, HttpServletRequest req);

	Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmPlant> list);
}
