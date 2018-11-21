package com.wis.mes.master.line.service.impl;

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

import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.SpiltUtils;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.line.dao.TmLineDao;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Service
public class TmLineServiceImpl extends BaseServiceImpl<TmLine> implements TmLineService {

	@Autowired
	public void setDao(TmLineDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TmPlantService plantService;

	@Autowired
	private ImportLogService importLogService;

	@Autowired
	private TmWorkshopService workshopService;

	@Autowired
	private DictEntryService entryService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	/**
	 * 获取产线列表
	 * 
	 * @return List<DictEntry>
	 * @param line
	 * @author zhf
	 * @Time 2017/4/14
	 */
	public List<DictEntry> getDictItem(TmLine line) {
		List<TmLine> lines = (line == null ? findAll() : findByEg(line));
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmLine e : lines) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getNo() + "-" + e.getNameCn());
			dictList.add(dict);
		}
		return dictList;
	}
	
	@Override
	public List<DictEntry> queryDictItem(QueryCriteria criteria) {
		criteria.getQueryCondition().put("enabled", ConstantUtils.TYPE_CODE_ENABLED_ON);
		ActionUtils.addUserDataPermission(criteria, "id");
		criteria.setQueryPage(false);
		List<TmLine> lines = findBy(criteria).getContent();
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmLine e : lines) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getNo() + "-" + e.getNameCn());
			dictList.add(dict);
		}
		return dictList;
	}
	
	public TmLine findByNo(String lineNo){
		if(StringUtils.isNotBlank(lineNo)){
			TmLine eg = TmLine.createLineNo(lineNo);
			return findUniqueByEg(eg);
		}
		return null;
	}
	
	/**
	 * 获取产线列表
	 * 
	 * @return Map<String,Object>
	 * @param line
	 * @author zyj
	 * @Time 2018/4/17
	 */
	public Map<Integer,TmLine> lineMaps(TmLine line) {
		List<TmLine> lines = (line == null ? findAll() : findByEg(line));
		Map<Integer,TmLine> map = new HashMap<Integer,TmLine>();
		for (final TmLine e : lines) {
			map.put(e.getId(), e);
		}
		return map;
	}

	/**
	 * @author zhf
	 *
	 * @date 2017/4/14
	 *
	 * @desc 根据车间工厂id 查一条产线的 名字和id
	 */
	@Override
	public List<TmLine> findLineNameOrIdById(Integer plantId) {
		TmLine line = new TmLine();
		line.setTmPlantId(plantId);
		line.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return findByEg(line);
	}

	/**
	 * @author zhf
	 *
	 * @date 2017/4/14
	 *
	 * @desc 查全部产线名字和id
	 */
	@Override
	public List<TmLine> findLineNameOrId() {
		return findAll();
	}

	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmLine> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> enabled = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabled.put(e.getCode(), e.getName());
		}
		for (int i = 0; i < list.size(); i++) {
			TmLine tmLine = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			String enabledValue = enabled.get(tmLine.getEnabled());
			tmLine.setEnabled(enabledValue);
			map.put("工厂", tmLine.getPlant().getNo() + "-" + tmLine.getPlant().getNameCn());
			map.put("车间", tmLine.getWorkshop().getNo() + "-" + tmLine.getWorkshop().getNameCn());
			map.put("产线编号", tmLine.getNo());
			map.put("中文名称", tmLine.getNameCn());
			map.put("英文名称", tmLine.getNameEn());
			map.put("启用", tmLine.getEnabled());
			exportDataList.add(map);
		}
		result = BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		return result;
	}

	@SuppressWarnings("unchecked")
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
			workShopMap.put(e.getNo() + "-" + e.getNameCn(), e);
		}

		// 数据库存在产线Map
		final Map<String, TmLine> lineMap = new HashMap<String, TmLine>();
		for (final TmLine e : findAll()) {
			lineMap.put(e.getTmPlantId() + e.getTmWorkshopId() + e.getNo(), e);
		}

		// 需要插入的对象List容器
		final List<TmLine> addList = new ArrayList<TmLine>();
		// 需要修改的产线Map
		final Map<Integer, TmLine> updateLineMap = new HashMap<Integer, TmLine>();

		// 是否启用的字典Map
		final Map<String, String> enabledDict = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabledDict.put(e.getName(), e.getCode());
		}

		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();
		final String DI = getMessage(req, "DI");
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		String value = null;
		Row row;
		int judgeSize = 6;// 数据表格的列数（字段数）
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmLine entity = new TmLine();

			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {// 第一格为空
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// 判断工厂信息格式
			if (value.lastIndexOf("-") < 0) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_PLANT_NO_NAME_ERROR"));
				continue;
			}
			// 判断工厂信息是否存在
			if (null == plantMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_ERROR_TWELVE"));
				continue;
			}
			// 判断工厂是否启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_ERROR_THREE"));
				continue;
			}
			entity.setTmPlantId(plantMap.get(value).getId());

			// 判断车间
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断格式
			if (value.lastIndexOf("-") < 0) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_WORKSHOP_NO_NAME_ERROR"));
				continue;
			}
			// 车间是否存在
			if (null == workShopMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_ERROR_FIVE"));
				continue;
			}
			// 判断车间是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(workShopMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_ERROR_SIX"));
				continue;
			}
			entity.setTmWorkshopId(workShopMap.get(value).getId());

			// 产线编号
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断产线编号是否为空
			if (StringUtils.isEmpty(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_ERROR_EIGHT"));
				continue;
			}
			// 判断产线编号的长度是否超过36位
			if (value.trim().length() > 36) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_NO_UN_EXCEED_THIRTY_SIX"));
				continue;
			}
			// 判断产线编号是否符合规则 编号只允许输入英文、数字、中划线-、下划线_和反斜杠/
			if (!StringUtils.isBlank(value) && (!Pattern.compile("[\\w\\d-_\\\\\\/]+").matcher(value).find()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_ERROR_NINE"));
				continue;
			}
			entity.setNo(value);

			// 产线中文名称
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断产线中文名称是否为空
			if (StringUtils.isNotBlank(value)) {
				// 判断文本长度是否超过150位
				if (value.trim().length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_NAME_UN_EXCEED_HUNDRED_FIFTY"));
					continue;
				}
			}

			entity.setNameCn(value);

			// 产线英文名称
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断文本长度是否超过100位
			if (StringUtils.isNotBlank(value)) {
				if (value.trim().length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_ENAME_UN_EXCEED_HUNDRED"));
					continue;
				}
				// 校验产线英文名称是否为英文
				if (!Pattern.compile("[\\w\\d-_\\\\\\/\\s]+").matcher(value).find()
						|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find()) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_ERROR_SIXTEEN"));
					continue;
				}
			}

			entity.setNameEn(value);

			// 启用
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断启用是否为空
			if (null == value || StringUtils.isEmpty(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_ERROR_TEN"));
				continue;
			}
			// 判断启用是否合法
			if (enabledDict.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "LINE_IMPORT_ERROR_ELEVEN"));
				continue;
			}

			entity.setEnabled(enabledDict.get(value));

			// 新增还是更新，放入各自集合中
			if (lineMap.get(entity.getTmPlantId() + entity.getTmWorkshopId() + entity.getNo()) == null) {
				addList.add(entity);
			} else {
				updateLineMap.put(i + 1, entity);
			}
		}
		List<TmLine> updateList = needUpdateEntitys(updateLineMap);
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

		Set<Integer> repeatLindexes = updateLineMap.keySet();

		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "LINE_MANAGEMENT"));
		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));

		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmLine> list, int num, String insert, StringBuffer buffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmLine>> parts = SpiltUtils.averageAssign(list, num);
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

	private List<TmLine> needUpdateEntitys(Map<Integer, TmLine> updateLineMap) {
		List<TmLine> updateList = new ArrayList<TmLine>();
		for (final Integer key : updateLineMap.keySet()) {
			TmLine excel = updateLineMap.get(key);
			TmLine line = new TmLine();
			line.setTmPlantId(excel.getTmPlantId());
			line.setTmWorkshopId(excel.getTmWorkshopId());
			line.setNo(excel.getNo());
			TmLine newData = findUniqueByEg(line);
			newData.setNameCn(excel.getNameCn());
			newData.setNameEn(excel.getNameEn());
			newData.setEnabled(excel.getEnabled());
			updateList.add(newData);
		}
		return updateList;
	}

	/**
	 * @author zhf
	 *
	 * @date 2017/4/177
	 *
	 * @desc 产线excel模板导出
	 */
	@Override
	public Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmLine> list) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}

	@Override
	public List<TmLine> findLIneNameOrPlantId(Integer plantId) {
		TmLine line = new TmLine();
		line.setTmPlantId(plantId);
		line.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return findByEg(line);
	}
}
