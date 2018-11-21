package com.wis.mes.master.nc.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.SpiltUtils;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.base.service.impl.MasterBaseServiceImpl;
import com.wis.mes.master.nc.dao.TmScrapDao;
import com.wis.mes.master.nc.entity.TmScrap;
import com.wis.mes.master.nc.service.TmScrapService;

@Service
public class TmScrapServiceImpl extends MasterBaseServiceImpl<TmScrap> implements TmScrapService {

	@Autowired
	public void setDao(final TmScrapDao dao) {
		this.dao = dao;
	}

	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private ImportLogService importLogService;

	@Override
	public Map<String, Object> exportExcelData(final HttpServletResponse response, final List<TmScrap> list,
			final String filePath, final String[] header) {
		final List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();

		// 将每列数据值与标题对应
		for (final TmScrap bean : list) {
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("报废编码", bean.getNo());
			map.put("报废描述", bean.getNote());
			map.put("对应外系统编码", bean.getExtCode());
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req) {
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

		// 获取报废代码Map
		final Map<String, TmScrap> scrapMap = new HashMap<String, TmScrap>();
		for (TmScrap scrap : findAll()) {
			scrapMap.put(scrap.getNo(), scrap);
		}

		// 需要插入的对象List容器
		final List<TmScrap> addList = new ArrayList<TmScrap>();
		// 需要更新的对象Map容器 (Excel的行数:对象)
		final Map<Integer, TmScrap> updateMap = new HashMap<Integer, TmScrap>();
		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();

		final String DI = getMessage(req, "DI");
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		String value = null;
		Row row;
		int judgeSize = 3;// 数据表格的列数（字段数）
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmScrap entity = new TmScrap();

			// 读取数据
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {// 第一格为空
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// 判断是否为为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "SCRAP_IMPORT_ERROR_ONE"));
				continue;
			}
			if (value.length() > 100) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "SCRAP_IMPORT_ERROR_SIX"));
				continue;
			}
			if ((!Pattern.compile("[\\w\\d-_\\\\\\/]+").matcher(value).find())
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find()) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "SCRAP_IMPORT_DATA_VALID_ERROR_NO"));
				continue;
			}
			entity.setNo(value);
			// 判断描述
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "SCRAP_IMPORT_ERROR_THREE"));
				continue;
			}
			if (value.length() > 150) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "SCRAP_IMPORT_ERROR_SEVEN"));
				continue;
			}
			entity.setNote(value);
			// 判断对外系统编码
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "SCRAP_IMPORT_ERROR_SEVEN_EXT_CODE"));
					continue;
				}
			}
			entity.setExtCode(value);
			// =========新增还是更新，放入各自集合中===========
			if (scrapMap.get(entity.getNo()) == null) {
				addList.add(entity);
			} else {
				updateMap.put(i + 1, entity);
			}
		}
		List<TmScrap> updateList = needUpdateEntitys(updateMap);
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
		Set<Integer> repeatLindexes = updateMap.keySet();
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "SCRAP_MANAGEMENT"));
		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));

		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmScrap> list, int num, String insert, StringBuffer buffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmScrap>> parts = SpiltUtils.averageAssign(list, num);
			try {
				for (int i = 0; i < parts.size(); i++) {
					if ("insert".equals(insert)) {
						doSaveBatch(parts.get(i));
						successCount += parts.get(i).size();
					} else {
						doUpdateBatch(parts.get(i));
						successCount += parts.get(i).size();
					}
				}
				buffer.append(successCount);
			} catch (Exception e) {
				buffer.append(successCount);
				throw new RuntimeException();
			}
		} else {
			buffer.append(successCount);
		}
	}

	private List<TmScrap> needUpdateEntitys(Map<Integer, TmScrap> updateMap) {
		List<TmScrap> updateList = new ArrayList<TmScrap>();
		for (Integer key : updateMap.keySet()) {
			TmScrap insert = updateMap.get(key);
			TmScrap scrap = new TmScrap();
			scrap.setNo(insert.getNo());
			TmScrap newData = findUniqueByEg(scrap);
			newData.setNote(insert.getNote());
			newData.setExtCode(insert.getExtCode());
			updateList.add(newData);
		}
		return updateList;
	}

	@Override
	public void checkUnique(final TmScrap operationBean, final String operationType, final List<String> errorList) {
		final TmScrap eg = new TmScrap();
		TmScrap bean = null;
		if (operationBean.getNo() != null) {
			// 校验编码是否已存在
			eg.setNo(operationBean.getNo());
			bean = findUniqueByEg(eg);
			if (bean != null) {
				// 新增操作校验
				if (ConstantUtils.STRING_ADD.equals(operationType)) {
					throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE,
							errorList.get(ConstantUtils.MATH_ZERO));
				} else if (ConstantUtils.STRING_UPDATE.equals(operationType)) {
					// 修改操作校验
					if (!bean.getId().equals(operationBean.getId())) {
						throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE,
								errorList.get(ConstantUtils.MATH_ZERO));
					}
				}
			}
		}
		if (operationBean.getNote() != null) {
			// 校验描述是否已存在
			eg.setNo(null);
			eg.setNote(operationBean.getNote());
			bean = findUniqueByEg(eg);
			if (bean != null) {
				// 新增操作校验
				if (ConstantUtils.STRING_ADD.equals(operationType)) {
					throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE,
							errorList.get(ConstantUtils.MATH_ZERO));
				} else if (ConstantUtils.STRING_UPDATE.equals(operationType)) {
					// 修改操作校验
					if (!bean.getId().equals(operationBean.getId())) {
						throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE,
								errorList.get(ConstantUtils.MATH_ZERO));
					}
				}
			}
		}
	}

	@Override
	public Workbook getWorkbookTemp(String downName, String templatePath, List<TmScrap> list) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}

}
