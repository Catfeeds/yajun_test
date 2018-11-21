package com.wis.mes.master.plant.service.impl;

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
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.plant.dao.TmPlantDao;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Service
public class TmPlantServiceImpl extends BaseServiceImpl<TmPlant> implements TmPlantService {

	@Autowired
	private ImportLogService importLogService;

	@Autowired
	public void setDao(TmPlantDao dao) {
		this.dao = dao;
	}

	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@Autowired
	private DictEntryService entryService;

	@Autowired
	private TmWorkshopService workshopService;

	/**
	 * 获取工厂列表
	 * 
	 * @return List<DictEntry>
	 * @param tmPlant
	 * @author zhf
	 * @Time 2017/4/14
	 */
	public List<DictEntry> getDictItem(TmPlant tmPlant) {
		List<TmPlant> tmPlants = (tmPlant == null ? findAll() : findByEg(tmPlant));
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmPlant e : tmPlants) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getNo());
//			dict.setName(e.getNo() + "-" + e.getNameCn());
			dictList.add(dict);
		}
		return dictList;
	}

	/**
	 * 导出工厂数据
	 * 
	 * @return Map<String,Object>
	 * @param response,list,filePath,header
	 * @author zhf
	 * @Time 2017/4/14
	 */
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmPlant> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> enabled = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabled.put(e.getCode(), e.getName());
		}
		for (int i = 0; i < list.size(); i++) {
			TmPlant tmPlant = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			String enabledValue = enabled.get(tmPlant.getEnabled());
			tmPlant.setEnabled(enabledValue);
			map.put(header[0], tmPlant.getNo());
			map.put(header[1], StringUtils.isBlank(tmPlant.getNameCn()) ? "" : tmPlant.getNameCn());
			map.put(header[2], StringUtils.isBlank(tmPlant.getNameEn()) ? "" : tmPlant.getNameEn());
			map.put(header[3], StringUtils.isBlank(tmPlant.getNameCnS()) ? "" : tmPlant.getNameCnS());
			map.put(header[4], StringUtils.isBlank(tmPlant.getNameEnS()) ? "" : tmPlant.getNameEnS());
			map.put(header[5], StringUtils.isBlank(tmPlant.getAddrCn1()) ? "" : tmPlant.getAddrCn1());
			map.put(header[6], StringUtils.isBlank(tmPlant.getAddrCn2()) ? "" : tmPlant.getAddrCn2());
			map.put(header[7], StringUtils.isBlank(tmPlant.getAddrEn1()) ? "" : tmPlant.getAddrEn1());
			map.put(header[8], StringUtils.isBlank(tmPlant.getAddrEn2()) ? "" : tmPlant.getAddrEn2());
			map.put(header[9], StringUtils.isBlank(tmPlant.getEnabled()) ? "" : tmPlant.getEnabled());
			exportDataList.add(map);
		}
		result = BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		return result;
	}

	/**
	 * 导出工厂及车间数据
	 * 
	 * @return Map<String,Object>
	 * @param response,list,filePath,header
	 * @author zhf
	 * @Time 2017/4/14
	 */
	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmPlant> list,
			String parentHeadStr, String childHeadStr, String filePath) {
		String[] parentHead = parentHeadStr.split(",");
		String[] childHead = childHeadStr.split(",");
		List<Map<String, Object>> parentExportList = new ArrayList<Map<String, Object>>();
		Map<Integer, List<Map<String, Object>>> childExportMap = new HashMap<Integer, List<Map<String, Object>>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> enabled = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabled.put(e.getCode(), e.getName());
		}
		for (int i = 0; i < list.size(); i++) {
			TmPlant tmPlant = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			String enabledValue = enabled.get(tmPlant.getEnabled());
			tmPlant.setEnabled(enabledValue);
			map.put(parentHead[0], tmPlant.getId());
			map.put(parentHead[1], tmPlant.getNameCn());
			map.put(parentHead[2], tmPlant.getNameEn());
			map.put(parentHead[3], tmPlant.getNameCnS());
			map.put(parentHead[4], tmPlant.getNameEnS());
			map.put(parentHead[5], tmPlant.getAddrCn1());
			map.put(parentHead[6], tmPlant.getAddrCn2());
			map.put(parentHead[7], tmPlant.getAddrEn1());
			map.put(parentHead[8], tmPlant.getAddrEn2());
			map.put(parentHead[9], tmPlant.getEnabled());
			TmWorkshop tmWorkshop = new TmWorkshop();
			tmWorkshop.setTmPlantId(tmPlant.getId());
			List<TmWorkshop> workshops = workshopService.findByEg(tmWorkshop);
			List<Map<String, Object>> childExportList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < workshops.size(); j++) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				TmWorkshop workshop = workshops.get(j);
				String workshopEnabledValue = enabled.get(workshop.getEnabled());
				workshop.setEnabled(workshopEnabledValue);
				childMap.put(childHead[0], workshop.getNo() + "-" + workshop.getNameCn());
				childMap.put(childHead[1], workshop.getNameEn());
				childMap.put(childHead[2], workshop.getNameCnS());
				childMap.put(childHead[3], workshop.getNameEnS());
				childMap.put(childHead[4], workshop.getEnabled());
				childExportList.add(childMap);
			}
			childExportMap.put(tmPlant.getId(), childExportList);
			parentExportList.add(map);
		}
		result = BaseExcelUtil.exportDataAll(response, parentExportList, parentHead, childExportMap, childHead,
				filePath);
		return result;
	}

	/**
	 * 导入excel数据
	 * 
	 * @param workbook
	 * @author zhf
	 * @Time 2017/4/18
	 */
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
		for (final TmPlant e : findAll()) {
			plantMap.put(e.getNo(), e);
		}

		// 需要插入的对象List容器
		final List<TmPlant> addList = new ArrayList<TmPlant>();
		// 需要更新的对象Map容器 (Excel的行数:对象)
		final Map<Integer, TmPlant> updateMap = new HashMap<Integer, TmPlant>();
		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();

		// 是否启用的字典Map
		Map<String, String> enabledDict = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabledDict.put(e.getName(), e.getCode());
		}
		final String DI = getMessage(req, "DI");
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		String value = null;
		Row row;
		int judgeSize = 10;// 数据表格的列数（字段数）
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmPlant entity = new TmPlant();
			// 工厂编号
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// 判断工厂编号是否为空
			if (StringUtils.isEmpty(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_PLANT_NO__REQUIRED"));
				continue;
			}
			// 判断工厂编号的长度是否超过36位
			if (value.trim().length() > 36) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_NO_UN_EXCEED_THIRTY_SIX"));
				continue;
			}
			// 判断工厂编号是否符合规则 编号只允许输入英文、数字、中划线-、下划线_和反斜杠/
			if (StringUtils.isNotBlank(value) && (!Pattern.compile("[\\w-\\s]+$").matcher(value).matches()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_NO_RULE"));
				continue;
			}
			entity.setNo(value);

			// 工厂中文名称
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断工厂中文名称是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_NAME_REQUIRED"));
				continue;
			}
			// 判断工厂名称是否超过150位
			if (value.trim().length() > 150) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_NAME_UN_EXCEED_HUNDRED_FIFTY"));
				continue;
			}
			entity.setNameCn(value);

			// 工厂英文名称
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断文本长度是否超过100位
				if (value.trim().length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_ENAME_UN_EXCEED_HUNDRED"));
					continue;
				}
				// 校验工厂英文名称是否为英文
				if ((!Pattern.compile("[\\w-\\s]+$").matcher(value).find()
						|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_EN_RULE"));
					continue;
				}
				entity.setNameEn(value);
			}

			// 工厂中文简称
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断工厂中文简称是否超过150位
				if (value.trim().length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_SNS_UN_EXCEED_HUNDRED_FIFTY"));
					continue;
				}
				entity.setNameCnS(value);
			}

			// 工厂英文简称
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断英文简称是否超过100位
				if (value.trim().length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_ENS_UN_EXCEED_HUNDRED"));
					continue;
				}
				// 校验工厂英文名称是否为英文
				if ((!Pattern.compile("[\\w-\\s]+$").matcher(value).find()
						|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_EN_RULE"));
					continue;
				}
				entity.setNameEnS(value);
			}

			// 工厂中文地址1
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断中文地址1长度是否超过150位
				if (value.trim().length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_SNS_UN_EXCEED_HUNDRED_FIFTY"));
					continue;
				}
				entity.setAddrCn1(value);
			}

			// 工厂中文地址2
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断中文地址2长度是否超过150位
				if (value.trim().length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_SNS_UN_EXCEED_HUNDRED_FIFTY"));
					continue;
				}
				entity.setAddrCn2(value);
			}

			// 工厂英文地址1
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断英文地址1长度是否超过100位
				if (value.trim().length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_ENS_UN_EXCEED_HUNDRED"));
					continue;
				}
				// 校验工厂英文地址1是否为英文
				if ((!Pattern.compile("[\\w-\\s]+$").matcher(value).find()
						|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_EN_RULE"));
					continue;
				}
				entity.setAddrEn1(value);
			}
			// 工厂英文地址2
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断英文地址2长度是否超过100位
				if (value.trim().length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_ENS_UN_EXCEED_HUNDRED"));
					continue;
				}
				// 校验工厂英文地址2是否为英文
				if ((!Pattern.compile("[\\w-\\s]+$").matcher(value).find()
						|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_EN_RULE"));
					continue;
				}
				entity.setAddrEn2(value);
			}

			// 启用
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断启用是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_ENABLED_REQUIRED"));
				continue;
			}
			// 判断启用是否合法
			if (enabledDict.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PLANT_IMPORT_ENABLED_VALUE"));
				continue;
			}
			entity.setEnabled(enabledDict.get(value));

			// =========新增还是更新，放入各自集合中===========
			if (plantMap.get(entity.getNo()) == null) {
				addList.add(entity);
			} else {
				updateMap.put(i + 1, entity);
			}

		}

		List<TmPlant> updateList = needUpdateEntitys(updateMap);
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
				totalInt, repeatLindexes, errorInfos,
				req, getMessage(req, "PLANT_MANAGEMENT"));
		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));

		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmPlant> list, int num, String insert, StringBuffer buffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmPlant>> parts = SpiltUtils.averageAssign(list, num);
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

	private List<TmPlant> needUpdateEntitys(Map<Integer, TmPlant> updatePlantMap) {
		List<TmPlant> updateList = new ArrayList<TmPlant>();
		for (Integer key : updatePlantMap.keySet()) {
			TmPlant insert = updatePlantMap.get(key);
			TmPlant plant = new TmPlant();
			plant.setNo(insert.getNo());
			TmPlant newData = findUniqueByEg(plant);
			newData.setNameCn(insert.getNameCn());
			newData.setNameEn(insert.getNameEn());
			newData.setNameCnS(insert.getNameCnS());
			newData.setNameEnS(insert.getNameEnS());
			newData.setEnabled(insert.getEnabled());
			newData.setAddrCn1(insert.getAddrCn1());
			newData.setAddrCn2(insert.getAddrCn2());
			newData.setAddrEn1(insert.getAddrEn1());
			newData.setAddrEn2(insert.getAddrEn2());
			updateList.add(newData);
		}
		return updateList;
	}

	/**
	 * @author zhf
	 *
	 * @date 2017/4/18
	 *
	 * @desc 工厂excel模板导出
	 */
	@Override
	public Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmPlant> list) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}
}
