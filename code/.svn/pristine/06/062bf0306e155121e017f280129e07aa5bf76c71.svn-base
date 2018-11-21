package com.wis.mes.master.workshop.service.impl;

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
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.base.service.impl.MasterBaseServiceImpl;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.workshop.dao.TmWorkshopDao;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Service
public class TmWorkshopServiceImpl extends MasterBaseServiceImpl<TmWorkshop> implements TmWorkshopService {

	@Autowired
	private ImportLogService importLogService;

	@Autowired
	public void setDao(TmWorkshopDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TmPlantService plantService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	/**
	 * 通过工厂id获取车间名称和id
	 * 
	 * @param Integer
	 *            plantId
	 * @return List<TmWorkshop>
	 * @author zhf
	 * @Time 2017/4/14
	 */
	@Override
	public List<TmWorkshop> findWShopNameOrIdById(final Integer plantId) {
		TmWorkshop tmWorkshop = new TmWorkshop();
		tmWorkshop.setTmPlantId(plantId);
		return findByEg(tmWorkshop);
	}

	/**
	 * 导出工厂数据
	 * 
	 * @return Map<String,Object>
	 * @param response,list,filePath,header
	 * @author zhf
	 * @Time 2017/4/14
	 */
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmWorkshop> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> enabled = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabled.put(e.getCode(), e.getName());
		}
		for (int i = 0; i < list.size(); i++) {
			TmWorkshop tmWorkshop = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			String enabledValue = enabled.get(tmWorkshop.getEnabled());
			tmWorkshop.setEnabled(enabledValue);
			map.put("工厂编号-名称", tmWorkshop.getPlant().getNo() + "-" + tmWorkshop.getPlant().getNameCn());
			map.put("车间编号", tmWorkshop.getNo());
			map.put("车间中文名称", StringUtils.isBlank(tmWorkshop.getNameCn()) ? "" : tmWorkshop.getNameCn());
			map.put("车间英文名称", StringUtils.isBlank(tmWorkshop.getNameEn()) ? "" : tmWorkshop.getNameEn());
			map.put("车间中文简称", StringUtils.isBlank(tmWorkshop.getNameCnS()) ? "" : tmWorkshop.getNameCnS());
			map.put("车间英文简称", StringUtils.isBlank(tmWorkshop.getNameEnS()) ? "" : tmWorkshop.getNameEnS());
			map.put("启用", tmWorkshop.getEnabled());
			exportDataList.add(map);
		}
		result = BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req) {
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

		// 工厂Map
		final Map<String, TmPlant> plantMap = new HashMap<String, TmPlant>();
		for (final TmPlant e : plantService.findAll()) {
			plantMap.put(e.getNo() + "-" + e.getNameCn(), e);
		}
		// 车间Map
		final Map<String, TmWorkshop> workShopMap = new HashMap<String, TmWorkshop>();
		for (final TmWorkshop e : findAll()) {
			workShopMap.put(e.getTmPlantId() + e.getNo(), e);
		}
		// "是否启用"Map
		final Map<String, String> enabledDict = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabledDict.put(e.getName(), e.getCode());
		}

		// 需要插入的对象List容器
		final List<TmWorkshop> addList = new ArrayList<TmWorkshop>();
		// 需要更新的对象Map容器 (Excel的行数:对象)
		final Map<Integer, TmWorkshop> updateMap = new HashMap<Integer, TmWorkshop>();
		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		String value = null;
		// "第"
		final String DI = getMessage(req, "DI");
		Row row = null;
		int judgeSize = 7;// 数据表格的列数（字段数）
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmWorkshop entity = new TmWorkshop();


			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// 工厂编号名称
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_PLANT_EXISTENT"));
				continue;
			}
			// 判断工厂编号是否存在
			if (null == plantMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_PLANT_NO_NAME_REQUIRED"));
				continue;
			}
			// 判断工厂是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_PLANT_UNENABLED"));
				continue;
			}
			entity.setTmPlantId(plantMap.get(value).getId());

			// 车间编号
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断车间编号是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_NO_REQUIRED"));
				continue;
			}
			// 判断车间编号的长度是否超过100位
			if (value.trim().length() > 100) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_NO_UN_EXCEED_THIRTY_SIX"));
				continue;
			}
			// 判断车间编号是否符合规则 编号只允许输入英文、数字、中划线-、下划线_和反斜杠/
			if ((!Pattern.compile("[\\w-\\s]+$").matcher(value).find()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_NO_RULE"));
				continue;
			}
			entity.setNo(value);

			// 车间中文名称
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断车间中文名称是否为空
			if (StringUtils.isNotBlank(value)) {
				// 判断文本长度是否超过150位
				if (value.trim().length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_NAME_UN_EXCEED_HUNDRED_FIFTY"));
					continue;
				}
				entity.setNameCn(value);
			}

			// 车间英文名称
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断文本长度是否超过100位
				if (value.trim().length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_ENAME_UN_EXCEED_HUNDRED"));
					continue;
				}
				// 校验车间英文名称是否为英文
				if ((!Pattern.compile("[\\w-\\s]+$").matcher(value).find()
						|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_EN_RULE"));
					continue;
				}
				entity.setNameEn(value);
			}

			// 车间中文简称
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断文本长度是否超过150位
				if (value.trim().length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_NAME_UN_EXCEED_HUNDRED_FIFTY"));
					continue;
				}
				entity.setNameCnS(value);
			}

			// 车间英文名称
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断文本长度是否超过100位
				if (value.trim().length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_ENAME_UN_EXCEED_HUNDRED"));
					continue;
				}
				// 校验车间英文名称是否为英文
				if ((!Pattern.compile("[\\w-\\s]+$").matcher(value).find()
						|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_EN_RULE"));
					continue;
				}
				entity.setNameEnS(value);
			}

			// 启用
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断启用是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_ENABLED_REQUIRED"));
				continue;
			}

			// 判断启用是否合法
			if (enabledDict.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORKSHOP_IMPORT_ENABLED_VALUE"));
				continue;
			}
			entity.setEnabled(enabledDict.get(value));

			// =========新增还是更新，放入各自集合中===========
			if (workShopMap.get(entity.getTmPlantId() + entity.getNo()) == null) {
				addList.add(entity);
			} else {
				updateMap.put(i + 1, entity);
			}

		}

		List<TmWorkshop> updateList = needUpdateEntitys(updateMap);
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
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "WORKSHOP_MANAGEMENT"));
		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));

		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmWorkshop> list, int num, String insert, StringBuffer buffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmWorkshop>> parts = SpiltUtils.averageAssign(list, num);
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

	private List<TmWorkshop> needUpdateEntitys(Map<Integer, TmWorkshop> updateWorkshopMap) {
		List<TmWorkshop> updateList = new ArrayList<TmWorkshop>();
		for (Integer key : updateWorkshopMap.keySet()) {
			TmWorkshop excel = updateWorkshopMap.get(key);
			TmWorkshop eg = new TmWorkshop();
			eg.setTmPlantId(excel.getTmPlantId());
			eg.setNo(excel.getNo());
			TmWorkshop newData = findUniqueByEg(eg);
			newData.setNameCn(excel.getNameCn());
			newData.setNameCnS(excel.getNameCnS());
			newData.setNameEn(excel.getNameEn());
			newData.setEnabled(excel.getEnabled());
			updateList.add(newData);
		}
		return updateList;
	}

	public List<DictEntry> getDictItem(TmWorkshop tmWorkshop) {
		List<TmWorkshop> workshops = (tmWorkshop == null ? findAll() : findByEg(tmWorkshop));
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmWorkshop e : workshops) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getNo() + "-" + e.getNameCn());
			dictList.add(dict);
		}
		return dictList;
	}

	/**
	 * @author zhf
	 *
	 * @date 2017/4/18
	 *
	 * @desc 车间excel模板导出
	 */
	@Override
	public Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmWorkshop> list) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}

}
