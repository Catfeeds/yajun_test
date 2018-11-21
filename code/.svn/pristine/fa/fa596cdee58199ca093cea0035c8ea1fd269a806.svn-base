package com.wis.mes.master.uloc.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.SpiltUtils;
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.basis.configuration.service.TsParameterService;
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.uloc.dao.TmUlocDao;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.entity.TmUlocSip;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.uloc.service.TmUlocSipService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;
import com.wis.mes.util.StringUtil;

@Service
public class TmUlocServiceImpl extends BaseServiceImpl<TmUloc> implements TmUlocService {

	@Autowired
	public void setDao(TmUlocDao dao) {
		this.dao = dao;
	}

	@Autowired
	private ImportLogService importLogService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private TmWorkshopService workshopService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TmUlocSipService ulocSipService;
	@Autowired
	private TsParameterService parameterService;
	@Autowired
	private RedisTemplate<String, TmUloc> redisTemplate;

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmUloc> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, DictEntry> enabled = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_ENABLED);
		for (int i = 0; i < list.size(); i++) {
			TmUloc tmUloc = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("工厂", getString(tmUloc.getTmPlant().getNo()) + "-" + getString(tmUloc.getTmPlant().getNameCn()));
			map.put("车间",
					getString(tmUloc.getTmWorkshop().getNo()) + "-" + getString(tmUloc.getTmWorkshop().getNameCn()));
			map.put("产线", getString(tmUloc.getTmLine().getNo()) + "-" + getString(tmUloc.getTmLine().getNameCn()));
			map.put("工位编号", getString(tmUloc.getNo()));
			map.put("工位名称", getString(tmUloc.getName()));
			map.put("对外系统编码", getString(tmUloc.getExtCode()));
			map.put("备注", getString(tmUloc.getNote()));
			map.put("是否质检点", yesOrNo.containsKey(tmUloc.getIsSip()) ? yesOrNo.get(tmUloc.getIsSip()).getName() : "");
			map.put("启用", enabled.containsKey(tmUloc.getEnabled()) ? enabled.get(tmUloc.getEnabled()).getName() : "");
			exportDataList.add(map);
		}
		result = BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		return result;
	}

	private String getString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 导入 更新时间：2017/05/18 ryy
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
		for (final TmPlant e : plantService.findAll()) {
			plantMap.put(e.getNo() + "-" + e.getNameCn(), e);
		}
		// 车间Map
		final Map<String, TmWorkshop> workShopMap = new HashMap<String, TmWorkshop>();
		for (final TmWorkshop e : workshopService.findAll()) {
			workShopMap.put(e.getTmPlantId() + e.getNo() + "-" + e.getNameCn(), e);
		}
		// 产线Map
		final Map<String, TmLine> lineMap = new HashMap<String, TmLine>();
		for (final TmLine e : lineService.findAll()) {
			lineMap.put(e.getTmPlantId() + e.getTmWorkshopId() + e.getNo() + "-" + e.getNameCn(), e);
		}

		// 工位map
		final Map<String, TmUloc> ulocMap = new HashMap<String, TmUloc>();
		for (final TmUloc e : findAll()) {
			ulocMap.put(e.getTmPlantId() + e.getTmWorkshopId() + e.getTmLineId() + e.getNo(), e);
		}

		// 是否质检点的数据字典Map
		Map<String, String> yesOrNo = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO)) {
			yesOrNo.put(e.getName(), e.getCode());
		}
		// 是否启用的数据字典Map
		final Map<String, String> enabledDict = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabledDict.put(e.getName(), e.getCode());
		}

		// 需要插入的工位
		final List<TmUloc> addUlocList = new ArrayList<TmUloc>();
		// 需要修改的工位Map
		final Map<Integer, TmUloc> updateUlocMap = new HashMap<Integer, TmUloc>();
		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();

		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;
		Row row;
		int judgeSize = 9;// 数据表格的列数（字段数）
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmUloc entity = new TmUloc();

			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {// 第一格为空
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// ================================== 工 厂 校 验
			// 判断工厂
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_PLANT_REQUIRED"));
				continue;
			}
			// 判断 工厂格式
			if (value.lastIndexOf("-") < 0) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_PLANT_PATTERN_ERROR"));
				continue;
			}
			// 判断 工厂编号和名称是否存在
			if (null == plantMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_PLANT_NO_NOT_EXIST"));
				continue;
			}
			// 判断 工厂是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_PLANT_NOT_ENABLED"));
				continue;
			}
			entity.setTmPlantId(plantMap.get(value).getId());

			// ================================== 车 间 校 验
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断 车间是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_WORKSHOP_REQUIRED"));
				continue;
			}
			// 判断 车间输入的格式
			if (value.lastIndexOf("-") < 0) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_WORKSHOP_PATTERN_ERROR"));
				continue;
			}
			// 判断 车间是否存在
			if (null == workShopMap.get(entity.getTmPlantId() + value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_WORKSHOP_NOT_EXIST"));
				continue;
			}
			// 判断 该车间是否启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF
					.equals(workShopMap.get(entity.getTmPlantId() + value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_WORKSHOP_NOT_ENABLED"));
				continue;
			}
			entity.setTmWorkshopId(workShopMap.get(entity.getTmPlantId() + value).getId());

			// ================================== 产 线 校 验
			index++;
			value = LoadUtils.getCell(row, index);

			// 判断 产线是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_LINE_REQUIRED"));
				continue;
			}
			// 判断 产线格式
			if (value.lastIndexOf("-") < 0) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_LINE_PATTERN_ERROR"));
				continue;
			}
			// 工厂车间下的产线是否存在
			if (null == lineMap.get(entity.getTmPlantId() + entity.getTmWorkshopId() + value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_LINE_NOT_EXIST"));
				continue;
			}
			// 判断产线是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF
					.equals(lineMap.get(entity.getTmPlantId() + entity.getTmWorkshopId() + value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_LINE_NOT_ENABLED"));
				continue;
			}

			entity.setTmLineId(lineMap.get(entity.getTmPlantId() + entity.getTmWorkshopId() + value).getId());

			// ================================== 工 位 校 验
			// 工位编号
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 内容是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NO_REQUIRED"));
				continue;
			}
			// 判断工位编号的长度是否超过100位
			if (value.length() > 100) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NO_EXCEED_100"));
				continue;
			}
			// 判断工位编号是否符合规则 编号只允许输入英文、数字、中划线-、下划线_和反斜杠/ [\\w\\d-_\\\\\\/]+
			if (StringUtils.isNotBlank(value) && (!Pattern.compile("[\\w-\\s]+$").matcher(value).matches()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NO_RULE"));
				continue;
			}
			entity.setNo(value);

			// 工位名称
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 如果工位中文名称不为空
			if (StringUtils.isNotBlank(value)) {
				// 判断文本长度是否超过150位
				if (value.length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NAME_EXCEED_150"));
					continue;
				}
			}
			entity.setName(value);
			// ================================== 对外系统编码
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NAME_EXCEED_150"));
					continue;
				}
			}
			entity.setExtCode(value);
			// ================================== 备注
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NAME_EXCEED_150"));
					continue;
				}
			}
			entity.setNote(value);
			// ================================== 是 否质检点
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断是否为空数据
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_IS_SIP_REQEIRED"));
				continue;
			}
			// 判断是否质检点数据是否合法
			if (!yesOrNo.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_IS_SIP_VALUE_RULE"));
				continue;
			}
			entity.setIsSip(yesOrNo.get(value));

			// ======================================

			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 判断启用是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_ENABLED_REQUIRED"));
				continue;
			}
			// 判断启用是否合法
			if (!enabledDict.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_ENABLED_VALUE_RULE"));
				continue;
			}

			entity.setEnabled(enabledDict.get(value));

			// ====================
			if (ulocMap.get(
					entity.getTmPlantId() + entity.getTmWorkshopId() + entity.getTmLineId() + entity.getNo()) == null) {
				addUlocList.add(entity);
			} else {
				updateUlocMap.put(i + 1, entity);
			}
		}
		List<TmUloc> updateList = needUpdateEntitys(updateUlocMap);
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImport(addUlocList, ConstantUtils.IMPORT_BATCH_COUNT, ConstantUtils.OPERATION_INSERT, addCount);
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

		Set<Integer> repeatLindexes = updateUlocMap.keySet();

		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "UlOC_MANAGEMENT"));
		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));

		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmUloc> list, int num, String insert, StringBuffer buffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmUloc>> parts = SpiltUtils.averageAssign(list, num);
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

	private List<TmUloc> needUpdateEntitys(Map<Integer, TmUloc> updatePlantMap) {
		List<TmUloc> updateList = new ArrayList<TmUloc>();
		for (Integer key : updatePlantMap.keySet()) {
			TmUloc insert = updatePlantMap.get(key);
			TmUloc uloc = new TmUloc();
			uloc.setTmPlantId(insert.getTmPlantId());
			uloc.setTmWorkshopId(insert.getTmWorkshopId());
			uloc.setTmLineId(insert.getTmLineId());
			uloc.setNo(insert.getNo());
			TmUloc newData = findUniqueByEg(uloc);
			uloc.setName(insert.getName());
			uloc.setIsSip(insert.getIsSip());
			uloc.setEnabled(insert.getEnabled());
			updateList.add(newData);
		}
		return updateList;
	}

	@Override
	public Integer checkNo(String no) {
		final TmUloc eg = new TmUloc();
		eg.setNo(no);
		final List<TmUloc> list = dao.findByEg(eg);
		if (list.size() > 0) {
			return list.get(0).getId();
		}
		return null;
	}

	@Override
	public Workbook getWorkbookTemp(String downName, String templatePath, List<TmUloc> list) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}

	/**
	 * 获取工位列表
	 * 
	 * @return List<DictEntry>
	 * @param uloc
	 * @author ltx
	 * @Time 2017/4/21
	 */

	@Override
	public List<DictEntry> getDictItem(TmUloc param) {
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmUloc bean : (param == null ? findAll() : findByEg(param))) {
			final DictEntry dict = new DictEntry();
			dict.setCode(bean.getId().toString());
			dict.setName("[" + bean.getNo() + "]" + (StringUtils.isNotBlank(bean.getName()) ? bean.getName() : ""));
			dictList.add(dict);
		}
		return dictList;
	}

	@Override
	public List<DictEntry> getUlocAll() {
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		List<TmUloc> list = ((TmUlocDao) dao).getUlocNgExitEnter("");
		for (final TmUloc bean : list) {
			final DictEntry dict = new DictEntry();
			dict.setCode(bean.getId().toString());
			dict.setName("[" + bean.getNo() + "]" + (StringUtils.isNotBlank(bean.getName()) ? bean.getName() : ""));
			dictList.add(dict);
		}
		return dictList;
	}

	/**
	 * 添加组装字典数据,返回map类型,方便扩展字段
	 */
	@Override
	public List<Map<String, Object>> getDictItemMap(TmUloc param) {
		List<Map<String, Object>> dictList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (final TmUloc bean : (param == null ? findAll() : findByEg(param))) {
			map = new HashMap<String, Object>();
			map.put("code", bean.getId().toString());
			map.put("name", "[" + bean.getNo() + "]" + (StringUtils.isNotBlank(bean.getName()) ? bean.getName() : ""));
			map.put("isSip", bean.getIsSip());// 是否为 质检点 YES为是 NO为否
			dictList.add(map);
		}
		return dictList;
	}

	@Override
	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmUloc> ulocList,
			String parentHeadStr, String childHeadStr, String filePath) {
		String[] parentHead = parentHeadStr.split(",");
		String[] childHead = childHeadStr.split(",");
		List<Map<String, Object>> parentExportList = new ArrayList<Map<String, Object>>();
		Map<Integer, List<Map<String, Object>>> childExportMap = new HashMap<Integer, List<Map<String, Object>>>();
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, DictEntry> enabled = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_ENABLED);
		// 获取参数信息
		Map<Integer, TsParameter> parameterMap = new HashMap<Integer, TsParameter>();
		for (final TsParameter parameter : parameterService.findAll()) {
			parameterMap.put(parameter.getId(), parameter);
		}
		for (int i = 0; i < ulocList.size(); i++) {
			TmUloc uloc = ulocList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(parentHead[0], uloc.getId());
			map.put(parentHead[1],
					getString(uloc.getTmPlant().getNo()) + "-" + getString(uloc.getTmPlant().getNameCn()));
			map.put(parentHead[2],
					getString(uloc.getTmWorkshop().getNo()) + "-" + getString(uloc.getTmWorkshop().getNameCn()));
			map.put(parentHead[3], getString(uloc.getTmLine().getNo()) + "-" + getString(uloc.getTmLine().getNameCn()));
			map.put(parentHead[4], getString(uloc.getNo()));
			map.put(parentHead[5], StringUtil.getString(getString(uloc.getName())));
			map.put(parentHead[6], StringUtil.getString(getString(uloc.getExtCode())));
			map.put(parentHead[7], StringUtil.getString(getString(uloc.getNote())));
			map.put(parentHead[8], yesOrNo.containsKey(uloc.getIsSip()) ? yesOrNo.get(uloc.getIsSip()).getName() : "");
			map.put(parentHead[9],
					enabled.containsKey(uloc.getEnabled()) ? enabled.get(uloc.getEnabled()).getName() : "");
			// 查询工位下面的质检项信息
			TmUlocSip eg = new TmUlocSip();
			eg.setTmUlocId(uloc.getId());
			List<TmUlocSip> ulocSipList = ulocSipService.findByEg(eg);
			List<Map<String, Object>> childExportList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < ulocSipList.size(); j++) {
				TmUlocSip ulocSip = ulocSipList.get(j);
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put(childHead[0], getString(parameterMap.get(ulocSip.getTsParameterId()).getCode()) + "-"
						+ getString(parameterMap.get(ulocSip.getTsParameterId()).getName()));
				childMap.put(childHead[1], getString(ulocSip.getDetail()));
				childMap.put(childHead[2], yesOrNo.containsKey(ulocSip.getIsExamine())
						? yesOrNo.get(ulocSip.getIsExamine()).getName() : "");
				childExportList.add(childMap);
			}
			childExportMap.put(uloc.getId(), childExportList);
			parentExportList.add(map);
		}
		result = BaseExcelUtil.exportDataAll(response, parentExportList, parentHead, childExportMap, childHead,
				filePath);
		return result;
	}

	@Override
	public Map<String, TmUloc> initUlocMapByConditions(Integer plantId, Integer workshopId, Integer lineId) {

		if (plantId == null)
			return null;
		QueryCriteria criteria = new QueryCriteria();
		/* criteria.setQueryRelationEntity(true); */
		criteria.addQueryCondition("tmPlantId", plantId.toString());
		if (workshopId != null) {
			criteria.addQueryCondition("tmWorkshopId", workshopId.toString());
		}
		if (lineId != null) {
			criteria.addQueryCondition("tmLineId", lineId.toString());
		}
		criteria.setQueryPage(false);
		PageResult<TmUloc> result = dao.findBy(criteria);
		List<TmUloc> ulocs = result.getContent();
		Map<String, TmUloc> map = new HashMap<String, TmUloc>();
		for (TmUloc tmUloc : ulocs) {
			map.put(tmUloc.getNo() + "-" + tmUloc.getName(), tmUloc);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelDataAll(Workbook workbook, ExcelImportPojo excelImportPojo, HttpServletRequest req) {
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);
		// 新增entity容器
		Map<TmUloc, List<TmUlocSip>> addMap = new HashMap<TmUloc, List<TmUlocSip>>();
		// 更新entity容器
		Map<TmUloc, List<TmUlocSip>> updateMap = new HashMap<TmUloc, List<TmUlocSip>>();
		// 错误信息
		List<String> errorInfos = new ArrayList<String>();
		// 获取Excel信息
		Map<String, Object> resultMap = BaseExcelUtil.readExcelDataAll(workbook, excelImportPojo);
		// 读取excel中Uloc的信息
		Map<Integer, Object> tmUlocMap = (Map<Integer, Object>) resultMap.get("parentMap");
		// 读取excel中UlocSip的信息
		Map<Integer, Object> tmUlocSipMap = (Map<Integer, Object>) resultMap.get("childrenMap");

		// 工位质检项Map
		final Map<String, TmUlocSip> ulocSipMap = new HashMap<String, TmUlocSip>();
		for (final TmUlocSip e : ulocSipService.findAll()) {
			ulocSipMap.put(e.getTmUlocId() + "-" + e.getTsParameterId(), e);
		}

		// ======验证数据======
		this.verifyDataAll(tmUlocMap, tmUlocSipMap, addMap, updateMap, errorInfos, req, ulocSipMap);
		// 保存或更新操作
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImportDataAll(ConstantUtils.OPERATION_INSERT, addMap, addCount, ulocSipMap);
			// 修改数据
			if ("true".equals(repeatOrUpdateFlag)) {
				// 更新对象获取数据库记录主键
				transToUpdateMap(updateMap);
				batchImportDataAll(null, updateMap, updateCount, ulocSipMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if ("true".equals(isRollBack)) {
				throw new BusinessException("IMPORT_DATA_VALID_ERROR_INFO",
						getMessage(req, "IMPORT_UNKNOWN_EXCEPTION"));
			} else {
				return getMessage(req, "IMPORT_UNKNOWN_EXCEPTION");
			}
		}

		if (resultMap.get("blankLineInfo") != null) {
			errorInfos.add((String) resultMap.get("blankLineInfo"));
		}
		// 获取重复数据的行标集
		Set<Integer> repeatLindexes = new TreeSet<Integer>();
		for (Integer linedex : tmUlocMap.keySet()) {
			if (updateMap.containsKey(tmUlocMap.get(linedex))) {
				repeatLindexes.add(linedex + 1);// 表观比是实际多1
			}
		}
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				repeatLindexes, errorInfos, req, getMessage(req, "ULOC_AND_ULOC_SIP_IMPORT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}

	private void transToUpdateMap(Map<TmUloc, List<TmUlocSip>> updateMap) {
		for (TmUloc updateEntity : updateMap.keySet()) {
			TmUloc eg = new TmUloc();
			eg.setTmPlantId(updateEntity.getTmPlantId());
			eg.setTmWorkshopId(updateEntity.getTmWorkshopId());
			eg.setTmLineId(updateEntity.getTmLineId());
			eg.setNo(updateEntity.getNo());
			TmUloc newData = findUniqueByEg(eg);
			updateEntity.setId(newData.getId());
		}
	}

	private void batchImportDataAll(String insertOrUpdate, Map<TmUloc, List<TmUlocSip>> map, StringBuffer countBuffer,
			Map<String, TmUlocSip> ulocSipMap) {
		int successCount = 0;
		if (!map.isEmpty()) {
			try {
				for (Entry<TmUloc, List<TmUlocSip>> entrySet : map.entrySet()) {
					TmUloc uloc = entrySet.getKey();
					List<TmUlocSip> children = entrySet.getValue();
					if ("insert".equals(insertOrUpdate)) {
						TmUloc savedUloc = doSave(uloc);
						if (children != null) {
							for (TmUlocSip tmUlocSip : children) {
								tmUlocSip.setTmUlocId(savedUloc.getId());
								ulocSipService.doSave(tmUlocSip);
							}
						}
						successCount += 1;
					} else {
						TmUloc updatedUloc = doUpdate(uloc);
						// 添加更新的子表数据
						if (children != null) {
							for (TmUlocSip tmUlocSip : children) {
								tmUlocSip.setTmUlocId(updatedUloc.getId());
								if (ulocSipMap
										.containsKey(tmUlocSip.getTmUlocId() + "-" + tmUlocSip.getTsParameterId())) {
									tmUlocSip.setId(ulocSipMap
											.get(tmUlocSip.getTmUlocId() + "-" + tmUlocSip.getTsParameterId()).getId());
									ulocSipService.doUpdate(tmUlocSip);
								} else {
									ulocSipService.doSave(tmUlocSip);
								}
							}
						}
						successCount += 1;
					}
				}
				countBuffer.append(successCount);
			} catch (Exception e) {
				e.printStackTrace();
				countBuffer.append(successCount);
				throw new RuntimeException();
			}
		} else {
			countBuffer.append(successCount);
		}

	}

	@SuppressWarnings("unchecked")
	private void verifyDataAll(Map<Integer, Object> tmUlocMap, Map<Integer, Object> tmUlocSipMap,
			Map<TmUloc, List<TmUlocSip>> addMap, Map<TmUloc, List<TmUlocSip>> updateMap, List<String> errorInfos,
			HttpServletRequest req, Map<String, TmUlocSip> ulocSipMap) {

		// 工厂Map
		final Map<String, TmPlant> plantMap = new HashMap<String, TmPlant>();
		for (final TmPlant e : plantService.findAll()) {
			plantMap.put(e.getNo() + "-" + e.getNameCn(), e);
		}

		// 车间Map
		final Map<String, TmWorkshop> workShopMap = new HashMap<String, TmWorkshop>();
		for (final TmWorkshop e : workshopService.findAll()) {
			workShopMap.put(e.getTmPlantId() + e.getNo() + "-" + e.getNameCn(), e);
		}

		// 产线Map
		final Map<String, TmLine> lineMap = new HashMap<String, TmLine>();
		for (final TmLine e : lineService.findAll()) {
			lineMap.put(e.getTmPlantId() + e.getTmWorkshopId() + e.getNo() + "-" + e.getNameCn(), e);
		}

		// 参数Map
		final Map<String, TsParameter> parameterMap = new HashMap<String, TsParameter>();
		for (final TsParameter e : parameterService.findAll()) {
			parameterMap.put(e.getCode() + "-" + e.getName(), e);
		}

		// yesOrNo
		Map<String, String> yesOrNo = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO)) {
			yesOrNo.put(e.getName(), e.getCode());
		}

		// 是否启用
		final Map<String, String> enabledDict = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabledDict.put(e.getName(), e.getCode());
		}

		// 工位Map
		final Map<String, TmUloc> ulocMap = new HashMap<String, TmUloc>();
		for (final TmUloc e : findAll()) {
			ulocMap.put(e.getTmPlantId() + e.getTmWorkshopId() + e.getTmLineId() + e.getNo(), e);
		}

		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;

		// 主循环
		loopMain: for (Integer linedex : tmUlocMap.keySet()) {
			TmUloc uloc = (TmUloc) tmUlocMap.get(linedex);
			List<TmUlocSip> ulocSipList = (List<TmUlocSip>) tmUlocSipMap.get(linedex);
			// ========主表=======
			// -------- 工厂---------
			value = uloc.getTmPlant().getNameCn();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_PLANT_REQUIRED"));
				continue;
			}
			if (!plantMap.containsKey(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_PLANT_NO_NOT_EXIST"));
				continue;
			}
			// 判断工厂是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_PLANT_NOT_ENABLED"));
				continue;
			}
			uloc.setTmPlantId(plantMap.get(value).getId());
			// ========== 车 间 校 验==========

			value = uloc.getTmWorkshop().getNameCn();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_WORKSHOP_REQUIRED"));
				continue;
			}
			if (!workShopMap.containsKey(uloc.getTmPlantId() + value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_WORKSHOP_NOT_EXIST"));
				continue;
			}
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(workShopMap.get(uloc.getTmPlantId() + value).getEnabled())) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_WORKSHOP_NOT_ENABLED"));
				continue;
			}
			uloc.setTmWorkshopId(workShopMap.get(uloc.getTmPlantId() + value).getId());
			// ======== 产 线 校 验=======

			value = uloc.getTmLine().getNameCn();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_LINE_REQUIRED"));
				continue;
			}
			if (!lineMap.containsKey(uloc.getTmPlantId() + uloc.getTmWorkshopId() + value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_LINE_NOT_EXIST"));
				continue;
			}
			// 判断产线是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF
					.equals((lineMap.get(uloc.getTmPlantId() + uloc.getTmWorkshopId() + value).getEnabled()))) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_LINE_NOT_ENABLED"));
				continue;
			}
			uloc.setTmLineId(lineMap.get(uloc.getTmPlantId() + uloc.getTmWorkshopId() + value).getId());

			// -------- 验证工位编号 ----------
			value = uloc.getNo();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NO_REQUIRED"));
				continue;
			}
			if (value.length() > 100) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NO_EXCEED_100"));
				continue;
			}
			if (StringUtils.isNotBlank(value) && (!Pattern.compile("[\\w-\\s]+$").matcher(value).matches()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NO_RULE"));
				continue;
			}
			uloc.setNo(value);

			// ----------- 验证名称 ------------
			value = uloc.getName();

			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NAME_EXCEED_VALUE"));
				continue;
			}
			if (value.length() > 150) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NAME_EXCEED_150"));
				continue;
			}
			uloc.setName(value);

			// --------- 验证对外系统编码 --------------
			value = uloc.getExtCode();

			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 100) {
					errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_ULOC_EXT_CODE_EXCEED_100"));
					continue;
				}
			}
			uloc.setExtCode(value);

			// --------- 验证备注 --------------
			value = uloc.getNote();
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 100) {
					errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_ULOC_NOTE_EXCEED_100"));
					continue;
				}
			}
			uloc.setNote(value);
			// --------- 验证是否质检点 --------------
			value = uloc.getIsSip();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_IS_SIP_REQEIRED"));
				continue;
			}
			if (!yesOrNo.containsKey(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_IS_SIP_VALUE_RULE"));
				continue;
			}
			uloc.setIsSip(yesOrNo.get(value));

			// -------- 验证是否启用 -----------
			value = uloc.getEnabled();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_ENABLED_REQUIRED"));
				continue;
			}
			if (!enabledDict.containsKey(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_ENABLED_VALUE_RULE"));
				continue;
			}
			uloc.setEnabled(enabledDict.get(value));

			// ================ 子 表 ================
			if (ulocSipList != null && !ulocSipList.isEmpty()) {
				int lineIndex = 0;// 行计数器
				Set<String> duplicateVerifier = new HashSet<String>();// 重复验证器
				for (TmUlocSip ulocSip : ulocSipList) {
					lineIndex++;
					// ------- 参数校验 ---------
					value = ulocSip.getTsParameter().getName();
					if (StringUtils.isBlank(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "ULOC_SIP_IMPORT_PARAMETER_REQUIRED"));
						continue loopMain;
					}
					if (!parameterMap.containsKey(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "ULOC_SIP_IMPORT_PARAMETER_NO_NOT_EXIST"));
						continue loopMain;
					}
					ulocSip.setTsParameterId(parameterMap.get(value).getId());

					// -------- 验证详细信息 ------
					value = ulocSip.getDetail();
					if (StringUtils.isNotBlank(value)) {
						if (value.length() > 150) {
							errorInfos.add(DI + ((linedex + 1) + lineIndex)
									+ getMessage(req, "ULOC_SIP_IMPORT_DETAILE_INFORMATION"));
							continue loopMain;
						}
						ulocSip.setDetail(value);
					}
					// ---------- 验证是否必检 ----------
					value = ulocSip.getIsExamine();
					if (StringUtils.isBlank(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "ULOC_SIP_IMPORT_IS_EXAMINE_NOT_NULL"));
						continue loopMain;
					}

					if (!yesOrNo.containsKey(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "ULOC_SIP_IMPORT_IS_EXAMINE_FORMAT_ERROR"));
						continue loopMain;
					}
					ulocSip.setIsExamine(yesOrNo.get(value));
					// 质检项参数重复数据验证，不允许有重复的数据
					if (duplicateVerifier.contains(ulocSip.getTmUlocId() + "-" + ulocSip.getTsParameterId())) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "ULOC_SIP_IMPORT_EXIST_DUPLICATE_RECORD"));
						continue loopMain;
					}
					duplicateVerifier.add(ulocSip.getTmUlocId() + "-" + ulocSip.getTsParameterId());
				}
			}
			// 版本号不为空，默认更新
			if (ulocMap.containsKey(uloc.getTmPlantId() + uloc.getTmWorkshopId() + uloc.getTmLineId() + uloc.getNo())) {
				updateMap.put(uloc, ulocSipList);
			} else {
				// 新增
				addMap.put(uloc, ulocSipList);
			}
		}

	}

	@Override
	public TmUloc getUlocById(Integer tmUlocId) {
		return ((TmUlocDao) dao).findById(tmUlocId);
	}

	@Override
	public List<Map<String, Object>> getUlocNgExitEnterMap() {
		List<Map<String, Object>> dictList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		List<TmUloc> list = ((TmUlocDao) dao).getUlocNgExitEnter(ConstantUtils.ULOC_NG_EXIT_ENTER);
		for (final TmUloc bean : list) {
			map = new HashMap<String, Object>();
			map.put("code", bean.getId().toString());
			map.put("name", bean.getNo());
			map.put("isEntrance", bean.getIsEntrance());// 工位特征
			dictList.add(map);
		}
		return dictList;
	}

	@Override
	public List<DictEntry> getDictItemExitEntrance(String parameter) {
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		List<TmUloc> list = ((TmUlocDao) dao).getUlocNgExitEnter(parameter);
		for (final TmUloc bean : list) {
			final DictEntry dict = new DictEntry();
			dict.setCode(bean.getNo());
			dict.setName("[" + bean.getNo() + "]" + (StringUtils.isNotBlank(bean.getName()) ? bean.getName() : ""));
			dictList.add(dict);
		}
		return dictList;
	}

	public Map<Integer, TmUloc> ulocMap(TmUloc tmUloc) {
		Map<Integer, TmUloc> map = new HashMap<Integer, TmUloc>();
		List<TmUloc> list = (null == tmUloc ? findAll() : findByEg(tmUloc));
		if (null != list && list.size() > 0) {
			for (final TmUloc bean : list) {
				map.put(bean.getId(), bean);
			}
		}
		return map;
	}

	@Override
	public TmUloc getRedisUloc(String lineNo, String ulocNo) {
		TmLine line = lineService.findByNo(lineNo);
		HashOperations<String, String, TmUloc> opsForHash = redisTemplate.opsForHash();
		TmUloc tmUloc = opsForHash.get("WIS_ULOC_KEY", lineNo + ":" + ulocNo);
		if (tmUloc == null) {
			tmUloc = getUloc(line.getId(), ulocNo);
			if (tmUloc != null) {
				opsForHash.put("WIS_ULOC_KEY", ulocNo, tmUloc);
			}
		}
		return tmUloc;
	}

	public TmUloc getUloc(Integer tmLineId, String ulocNo) {
		TmUloc eg = new TmUloc();
		eg.setTmLineId(tmLineId);
		eg.setNo(ulocNo);
		return this.findUniqueByEg(eg);
	}
	
	@Override
	public TmUloc getUloc(String ulocNo) {
		TmUloc uloc = null;
		if (StringUtils.isNotBlank(ulocNo)) {
			String lineNo = globalConfigurationService.getValueByKey(ConstantUtils.R5_LINE_NO);
			uloc = getRedisUloc(lineNo, ulocNo);
			if (null == uloc) {
				throw new BusinessException("获取" + ulocNo + "工位失败，检查工位基础数据。");
			}
			if (null != uloc) {
				if (StringUtils.isBlank(uloc.getRfidIP()) || null == uloc.getRfidPort()) {
					throw new BusinessException("工位编号：" + ulocNo + "：没有维护IP或没有维护端口号。");
				}
			}
		}
		return uloc;
	}
	
	@Override
	public List<DictEntry> queryDictItem(QueryCriteria criteria,boolean isName) {
		ActionUtils.addUserDataPermission(criteria, "tmLineId");
		criteria.setQueryPage(false);
		criteria.setOrderDirection(OrderEnum.ASC);
		criteria.setOrderField("no");
		List<TmUloc> beans = findBy(criteria).getContent();
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmUloc e : beans) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			if(isName) {
				if(StringUtils.isNotEmpty(e.getName())) {
					dict.setName("[" + e.getNo() + "]" + (StringUtils.isNotBlank(e.getName()) ? e.getName() : ""));
					dictList.add(dict);
				}
			}else {
				dict.setName("[" + e.getNo() + "]" + (StringUtils.isNotBlank(e.getName()) ? e.getName() : ""));
				dictList.add(dict);
			}
		}
		return dictList;
	}
	
	
}
