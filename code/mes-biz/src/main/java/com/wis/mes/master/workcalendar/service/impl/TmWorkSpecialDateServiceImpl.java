package com.wis.mes.master.workcalendar.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.SpiltUtils;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.workcalendar.dao.TmWorkSpecialDateDao;
import com.wis.mes.master.workcalendar.entity.TmWorkSpecialDate;
import com.wis.mes.master.workcalendar.service.TmWorkSpecialDateService;

@Service
public class TmWorkSpecialDateServiceImpl extends BaseServiceImpl<TmWorkSpecialDate> implements TmWorkSpecialDateService {

	@Autowired
	public void setDao(TmWorkSpecialDateDao dao) {
		this.dao = dao;
	}
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private ImportLogService importLogService;
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_DEFAULT);
	
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmWorkSpecialDate> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> dateType = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.DATE_TYPE_CODE)) {
			dateType.put(e.getCode(), e.getName());
		}
		Map<String, String> enabled = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabled.put(e.getCode(), e.getName());
		}
		for (int i = 0; i < list.size(); i++) {
			TmWorkSpecialDate workSpecialDate = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("日期", workSpecialDate.getSpecialDate());
			map.put("类型", dateType.get(workSpecialDate.getType()));
			map.put("描述", workSpecialDate.getNote());
			map.put("启用", enabled.get(workSpecialDate.getEnabled()));

			exportDataList.add(map);
		}
		result = BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req) {
		
		//覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

		// 已经存在的特殊日历Map
		Map<Date, TmWorkSpecialDate> existSpecialDateMap = new HashMap<Date, TmWorkSpecialDate>();
		for (TmWorkSpecialDate specialDate : findAll()) {
			// 以日期为唯一标识
			existSpecialDateMap.put(specialDate.getSpecialDate(), specialDate);
		}

		// 类型map
		Map<String, String> dateTypMap = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.DATE_TYPE_CODE)) {
			dateTypMap.put(e.getName(), e.getCode());
		}
		// 是否启用map
		Map<String, String> enableMap = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enableMap.put(e.getName(), e.getCode());
		}
		// 需要新增的数据集合
		List<TmWorkSpecialDate> addList = new ArrayList<TmWorkSpecialDate>();
		// 需要修改的数据的Map
		Map<Integer, TmWorkSpecialDate> updateMap = new HashMap<Integer, TmWorkSpecialDate>();
		// 格式错误的信息
		List<String> errorInfos = new ArrayList<String>();
		// "第"
		final String DI = getMessage(req, "DI");
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		String value = null;
		Row row;
		String regex = "\\d{4}-\\d{2}-\\d{2}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = null;
		int totalInt = 0;
		int judgeSize = 18;// 数据表格的列数（字段数）
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmWorkSpecialDate entity = new TmWorkSpecialDate();


			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {// 第一格为空
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// ======= 日 期 校 验 =======
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SPECIAL_DATE_REQUIRED"));
				continue;
			}
			// 格式是否正确
			matcher = pattern.matcher(value);
			if (!matcher.matches()) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SPECIAL_DATE_INVALID"));
				continue;
			}
			// 是否过期
			Date ExcelDate = null;
			Date nowDate = null;
			try {
				ExcelDate = sDateFormat.parse(value);
				nowDate = sDateFormat.parse(sDateFormat.format(new Date()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			if (ExcelDate.before(nowDate)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SPECIAL_DATE_OVERDUE"));
				continue;
			}
			entity.setSpecialDate(ExcelDate);

			// ======= 类 型 校 验 =======
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SPECIAL_DATE_TYPE_REQUIRED"));
				continue;
			}
			if (!dateTypMap.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SPECIAL_DATE_TYPE_RULE"));
				continue;
			}
			entity.setType(dateTypMap.get(value));

			// ======= 描 述 校 验 =======
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 可以为空
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SPECIAL_DATE_NOTE_EXCEED_100"));
					continue;
				}
				entity.setNote(value);
			}

			// ======= 启 用 校 验 =======
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SPECIAL_DATE_ENABLED_REQUIRED"));
				continue;
			}
			if (null == enableMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SPECIAL_DATE_ENABLED_ERROR"));
				continue;
			}
			entity.setEnabled(enableMap.get(value));

			// 新增还是更新，放入各自集合中
			if (existSpecialDateMap.get(entity.getSpecialDate()) == null) {
				addList.add(entity);
			} else {
				updateMap.put(i + 1, entity);
			}
		}

		List<TmWorkSpecialDate> updateList = needUpdateEntitys(updateMap);
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImport(addList, ConstantUtils.IMPORT_BATCH_COUNT, ConstantUtils.OPERATION_INSERT, addCount);
			// 修改数据
			if ("true".equals(repeatOrUpdateFlag)) {
				batchImport(updateList, ConstantUtils.IMPORT_BATCH_COUNT, null, updateCount);
			}
		} catch (Exception e) {
			if ("true".equals(isRollBack)) {
				throw new BusinessException("IMPORT_DATA_VALID_ERROR_INFO",
						getMessage(req, "IMPORT_UNKNOWN_EXCEPTION"));
			} else {
				return getMessage(req, "IMPORT_UNKNOWN_EXCEPTION");
			}
		}

		// 导入日志错误信息，页面提醒
		Set<Integer> repeatLindexes = updateMap.keySet();
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "WORK_SPECIAL_DATE_MANAGEMENT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}
	
	private void batchImport(List<TmWorkSpecialDate> list, int num, String insert,StringBuffer countBuffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmWorkSpecialDate>> parts = SpiltUtils.averageAssign(list, num);
			try {
				for (int i = 0; i < parts.size(); i++) {
					if ("insert".equals(insert)) {
						doSaveBatch(parts.get(i));
						successCount += parts.get(i).size();
					} else {
						doUpdateBatch(parts.get(i));
						successCount +=  parts.get(i).size();
					}
				}
				countBuffer.append(successCount);
			} catch (Exception e) {
				countBuffer.append(successCount);
				throw new RuntimeException();
			}
		}else{
			countBuffer.append(successCount);
		}
	}
	
	private List<TmWorkSpecialDate> needUpdateEntitys(Map<Integer, TmWorkSpecialDate> updateMap) {
		List<TmWorkSpecialDate> updateList = new ArrayList<TmWorkSpecialDate>();
		for (Integer key : updateMap.keySet()) {
			TmWorkSpecialDate insert = updateMap.get(key);
			TmWorkSpecialDate workSpecialDate = new TmWorkSpecialDate();
			workSpecialDate.setSpecialDate(insert.getSpecialDate());
			TmWorkSpecialDate newData = findUniqueByEg(workSpecialDate);
			newData.setType(insert.getType());
			newData.setNote(insert.getNote());
			newData.setEnabled(insert.getEnabled());
			updateList.add(newData);
		}
		return updateList;
	}

	@Override
	public PageResult<TmWorkSpecialDate> getPageTmWorkSpecialDate(QueryCriteria queryCriteria) {
		return ((TmWorkSpecialDateDao)dao).getPageTmWorkSpecialDate(queryCriteria);
	}
}
