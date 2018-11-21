package com.wis.mes.master.uloc.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.entity.TmNcGroup;
import com.wis.mes.master.nc.service.TmNcGroupService;
import com.wis.mes.master.nc.service.TmNcService;
import com.wis.mes.master.uloc.dao.TmUlocSipNcDao;
import com.wis.mes.master.uloc.entity.TmUlocSipNc;
import com.wis.mes.master.uloc.service.TmUlocSipNcService;

@Service
public class TmUlocSipNcServiceImpl extends MasterBaseServiceImpl<TmUlocSipNc> implements TmUlocSipNcService {
	@Autowired
	public void setDao(TmUlocSipNcDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TmNcGroupService ncGroupService;
	@Autowired
	private TmNcService ncService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private ImportLogService importLogService;

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmUlocSipNc> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<Integer, String> ncGroupMap = new HashMap<Integer, String>();
		for (TmNcGroup entity : ncGroupService.findAll()) {
			ncGroupMap.put(entity.getId(), entity.getNo() + "-" + entity.getName());
		}
		Map<Integer, String> ncMap = new HashMap<Integer, String>();
		for (TmNc entity : ncService.findAll()) {
			ncMap.put(entity.getId(), entity.getNo() + "-" + entity.getName());
		}
		// 将每列数据值与标题对应
		for (TmUlocSipNc bean : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("不合格组", ncGroupMap.get(bean.getTmNcGroupId()));
			map.put("不合格代码", ncMap.get(bean.getTmNcId()));
			map.put("备注", bean.getNote());
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}

	@Override
	public Workbook getWorkbookTemp(String downName, String templatePath) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req, String tmUlocSipId) {
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

		// 不合格組
		Map<String, TmNcGroup> ncGroupMap = new HashMap<String, TmNcGroup>();
		for (TmNcGroup entity : ncGroupService.findAll()) {
			ncGroupMap.put(entity.getNo() + "-" + entity.getName(), entity);
		}
		// 不合格代碼
		Map<String, TmNc> ncMap = new HashMap<String, TmNc>();
		for (TmNc entity : ncService.findAll()) {
			ncMap.put(entity.getNo() + "-" + entity.getName(), entity);
		}
		// 已存在数据
		Map<String, TmUlocSipNc> ulocSipNcMap = new HashMap<String, TmUlocSipNc>();
		for (TmUlocSipNc entity : findAll()) {
			ulocSipNcMap.put(entity.getTmNcGroupId() + "-" + entity.getTmNcId(), entity);
		}

		// 需要插入的对象List容器
		final List<TmUlocSipNc> addList = new ArrayList<TmUlocSipNc>();
		// 需要更新的工位质检項不合格Map
		final Map<Integer, TmUlocSipNc> updateUlocSipNcMap = new HashMap<Integer, TmUlocSipNc>();
		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();
		final String DI = getMessage(req, "DI");

		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		String value = null;
		Row row;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmUlocSipNc entity = new TmUlocSipNc();
			entity.setTmUlocSipId(Integer.valueOf(tmUlocSipId));
			// 获得不合格组
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_GROUP_NULL"));
				continue;
			}
			if (value.lastIndexOf("-") < 0) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_GROUP_FORMAT"));
				continue;
			}
			if (!ncGroupMap.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_GROUP_NOT_EXIST"));
				continue;
			}
			entity.setTmNcGroupId(ncGroupMap.get(value).getId());
			// 获得不合格代码
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_NULL"));
				continue;
			}
			if (value.lastIndexOf("-") < 0) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_FORMAT"));
				continue;
			}
			if (!ncMap.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_NOT_EXIST"));
				continue;
			}
			entity.setTmNcId(ncMap.get(value).getId());
			// 获得备注
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_NC_IMPORT_NOTE_LENGTH"));
					continue;
				}
			}
			entity.setNote(value);
			if (ulocSipNcMap.containsKey(entity.getTmNcGroupId() + "-" + entity.getTmNcId())) {
				updateUlocSipNcMap.put(i + 1, entity);
			} else {
				addList.add(entity);
			}
		}
		List<TmUlocSipNc> updateList = needUpdateEntitys(updateUlocSipNcMap);
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
		Set<Integer> repeatLindexes = updateUlocSipNcMap.keySet();

		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				repeatLindexes, errorInfos, req, getMessage(req, "ULOC_SIP_NC_LIST_TITLE"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));

		return (String) logsAndMsgTip.get("msgTip");
	}

	private List<TmUlocSipNc> needUpdateEntitys(Map<Integer, TmUlocSipNc> updateUlocSipNcMap) {
		List<TmUlocSipNc> updateList = new ArrayList<TmUlocSipNc>();
		for (Integer key : updateUlocSipNcMap.keySet()) {
			TmUlocSipNc insert = updateUlocSipNcMap.get(key);
			TmUlocSipNc ulocSipNc = new TmUlocSipNc();
			ulocSipNc.setTmNcGroupId(insert.getTmNcGroupId());
			ulocSipNc.setTmNcId(insert.getTmNcId());
			ulocSipNc.setTmUlocSipId(insert.getTmUlocSipId());
			TmUlocSipNc newData = findUniqueByEg(ulocSipNc);
			newData.setNote(insert.getNote());
			updateList.add(newData);
		}
		return updateList;
	}

	private void batchImport(List<TmUlocSipNc> list, int num, String insert, StringBuffer buffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmUlocSipNc>> ulocSipNcs = SpiltUtils.averageAssign(list, num);
			try {
				for (int i = 0; i < ulocSipNcs.size(); i++) {
					if ("insert".equals(insert)) {
						doSaveBatch(ulocSipNcs.get(i));
						successCount += ulocSipNcs.get(i).size();
					} else {
						doUpdateBatch(ulocSipNcs.get(i));
						successCount += ulocSipNcs.get(i).size();
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

}
