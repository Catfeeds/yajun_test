package com.wis.mes.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.LogConstant;
import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.entity.AbstractEntity;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.service.BaseService;
import com.wis.mes.file.entity.TsFile;
import com.wis.mes.file.service.TsFileService;
import com.wis.mes.push.WebSocketWithSession;

import net.sf.json.JSONObject;

public interface ExportPageResult<T extends AbstractEntity> {
	public Log logger = LogFactory.getLog(LogConstant.BIZ_LOG);
	public static int everyQueryRow = 500;
	public static int everyExportRow = 1000;

	default PageResult<T> getExportPageResult(BaseService<T> service, QueryCriteria criteria, int page) {
		QueryCriteria criteriaNew = new QueryCriteria();
		BeanUtils.copyProperties(criteria, criteriaNew, "queryCondition");
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.putAll(criteria.getQueryCondition());
		criteriaNew.setQueryCondition(temp);
		criteriaNew.setQueryRelationEntity(true);
		criteriaNew.setQueryPage(true);
		criteriaNew.setRows(everyQueryRow);
		criteriaNew.setPage(page);
		return service.findBy(criteriaNew);
	}

	void exportData(List<T> content, SXSSFWorkbook workbook, Sheet sheet, int rownum);

	default void doExport(HttpServletRequest request, HttpServletResponse response, BaseService<T> service, String type,
			String downLoadName, String[] title, QueryCriteria criteria) {
		TsFileService fileService = (TsFileService) WebContextHolder.getWebAppContext().getBean("tsFileService");
		GlobalConfigurationService globalConfigurationService = (GlobalConfigurationService) WebContextHolder
				.getWebAppContext().getBean("globalConfigurationService");
		String path = globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_ATTACHMENT_BASE_PATH);
		String fileName = downLoadName + DateUtils.getCurrentDate().getTime() + ".xlsx";
		path = path + "\\" + type;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = path + "\\" + fileName;
		FileOutputStream fileOut = null;
		SXSSFWorkbook workbook = new SXSSFWorkbook(everyExportRow);
		TsFile tsFile = fileService.doSaveTsFile(type, filePath, downLoadName,
				JSONObject.fromObject(criteria.getQueryCondition()).toString());
		try {
			file = new File(filePath);
			file.createNewFile();
			PageResult<T> exportPageResult = getExportPageResult(service, criteria, 1);
			int totalPage = exportPageResult.getTotalPage();
			List<T> content = exportPageResult.getContent();
			Sheet sheet = workbook.createSheet();
			LoadUtils.setSheetTitle(sheet, title);
			exportData(content, workbook, sheet, 1);
			int index = 1;
			for (int i = 2; i <= totalPage; i++) {
				int rownum = (index++) * everyQueryRow + 1;
				if (rownum >= 1000000) {
					index = 0;
					sheet = workbook.createSheet();
					rownum = 1;
					LoadUtils.setSheetTitle(sheet, title);
				}
				exportPageResult = getExportPageResult(service, criteria, i);
				exportData(exportPageResult.getContent(), workbook, sheet, rownum);
				logger.info("总页数：" + totalPage + "，第" + i + "页");
			}
			fileOut = new FileOutputStream(file);
			workbook.write(fileOut);
			tsFile.setIsSuccess(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
			fileService.doUpdate(tsFile);
			WebSocketWithSession webSocketWithSession = new WebSocketWithSession();
			webSocketWithSession.sendMsg(String.valueOf(WebContextHolder.getCurrentUser().getId()), "导出完成,请到导出界面下载!");
		} catch (Exception e) {
			e.printStackTrace();
			tsFile.setIsSuccess(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
			fileService.doUpdate(tsFile);
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			try {
				fileOut.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getFullStackTrace(e2));
			}
			workbook.dispose();
		}
	}

}
