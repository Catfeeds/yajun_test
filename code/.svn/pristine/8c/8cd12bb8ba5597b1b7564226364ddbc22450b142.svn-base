package com.wis.mes.master.maintenance.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.service.BaseService;
import com.wis.mes.master.maintenance.entity.TmDeviceMaintenance;

public interface TmDeviceMaintenanceService extends BaseService<TmDeviceMaintenance> {
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmDeviceMaintenance> list, String filePath,
			String[] header);

	String doImportExcelData(Workbook workbook, HttpServletRequest req);

	Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmDeviceMaintenance> list);
	
	void maintenanceWarningNotice();

	/***
	 * @author yajun_ren
	 * @desc 报警设置
	 * @param flag
	 * @date 2018/11/16
	 * **/
	void alarmSetting(boolean flag);
	
}
