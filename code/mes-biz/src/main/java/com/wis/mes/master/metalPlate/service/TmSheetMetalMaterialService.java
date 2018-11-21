package com.wis.mes.master.metalPlate.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.service.BaseService;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;

public interface TmSheetMetalMaterialService extends BaseService<TmSheetMetalMaterial> {
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmSheetMetalMaterial> list, String filePath,
			String[] header);

	String doImportExcelData(Workbook workbook, HttpServletRequest req);

	Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmSheetMetalMaterial> list);

	List<TmSheetMetalMaterial> findByMachiningSurface(String model);
	public Integer getInventoryNumber(String model);
}
