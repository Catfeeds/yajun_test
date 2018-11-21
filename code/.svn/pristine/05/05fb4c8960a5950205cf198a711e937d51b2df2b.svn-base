package com.wis.mes.master.part.service.impl;

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
import com.wis.mes.master.part.dao.TmPartDao;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.util.StringUtil;

@Service
public class TmPartServiceImpl extends BaseServiceImpl<TmPart> implements TmPartService {

	@Autowired
	public void setDao(final TmPartDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Autowired
	private ImportLogService importLogService;

	@Autowired
	private TmPlantService plantService;

	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@Override
	public Map<String, Object> exportExcelData(final HttpServletResponse response, final List<TmPart> list,
			final String filePath, final String[] header) {
		final List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		if (list != null && list.size() > ConstantUtils.MATH_ZERO) {
			// 启用字典Map
			final Map<String, String> enabledMap = new HashMap<String, String>();
			for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
				enabledMap.put(e.getCode(), e.getName());
			}
			// 物料类型字典Map
			final Map<String, String> partTypeMap = new HashMap<String, String>();
			for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE)) {
				partTypeMap.put(e.getCode(), e.getName());
			}
			// 单位数据字典
			Map<String, DictEntry> baseUnitType = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_BASE_UNIT);
			
			// 质保单位数据字典
			Map<String, DictEntry> uKUnitMap = entryService.getMapTypeCode(ConstantUtils.EQUIPMENT_MAINTENANCE_TIME_TYPE);

			// 将每列数据值与标题对应
			for (final TmPart bean : list) {
				final Map<String, Object> map = new HashMap<String, Object>();
				map.put("公司", bean.getPlant().getId() == null ? ""
						: (bean.getPlant().getNo() + "-" + bean.getPlant().getNameCn()));
				map.put("物料编号", bean.getNo());
				map.put("物料名称", bean.getNameCn());
				map.put("物料类型", (StringUtil.isEmpty(bean.getType()) ? ConstantUtils.STRING_EMPTY
						: partTypeMap.get(bean.getType())));
				map.put("单位", baseUnitType.containsKey(bean.getBaseUnit())
						? baseUnitType.get(bean.getBaseUnit()).getName() : "");
				map.put("规格型号", StringUtil.getString(bean.getSpectyp()));
				map.put("质保单位", uKUnitMap.containsKey(bean.getUkunit())
						? uKUnitMap.get(bean.getUkunit()).getName() : "");
				map.put("质保时间", StringUtil.getString(DateUtils.formatDate(bean.getUktime())));
				map.put("启用", (StringUtil.isEmpty(bean.getEnabled()) ? ConstantUtils.STRING_EMPTY
						: enabledMap.get(bean.getEnabled())));
				map.put("备注", StringUtil.getString(bean.getRemarks()));
				exportDataList.add(map);
			}
			return BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		}
		return null;
	}

	@Override
	public List<DictEntry> getDictItem(final TmPart eg) {
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmPart bean : (eg == null ? findAll() : findByEg(eg))) {
			final DictEntry dict = new DictEntry();
			if (ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED.equals(bean.getType()) 
					|| ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED.equals(bean.getType())) {	
				dict.setCode(bean.getId().toString());
				dict.setName(bean.getNo() + ConstantUtils.STRING_ROD + bean.getNameCn());
				dictList.add(dict);
			}
		}
		return dictList;
	}

	@Override
	public List<DictEntry> getDictItem(final Map<String, Object> param) {
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmPart bean : ((TmPartDao) dao).getDictItem(param)) {
			final DictEntry dict = new DictEntry();
			dict.setCode(bean.getId().toString());
			dict.setName(bean.getNo() + ConstantUtils.STRING_ROD + bean.getNameCn());
			dictList.add(dict);
		}
		return dictList;
	}

	/**
	 * 导入excel数据
	 * 
	 * @param workbook
	 * @author ryy
	 * @Time 2017/5/11
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(final Workbook workbook, HttpServletRequest req) {

		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

		// 工厂Map,编码对应工厂实体
		final Map<String, TmPlant> plantMap = new HashMap<String, TmPlant>();
		for (final TmPlant e : plantService.findAll()) {
			plantMap.put(e.getNo() + "-" + e.getNameCn(), e);
		}
		// 数据库存在的物料Map,（工厂Id和物料编码为唯一索引）对应物料实体
		final Map<String, TmPart> partMap = new HashMap<String, TmPart>();
		for (final TmPart e : this.findAll()) {
			partMap.put(e.getTmPlantId() + e.getNo(), e);
		}

		// 需要插入的物料List
		final List<TmPart> addPartList = new ArrayList<TmPart>();
		// 需要更新的物料Map (Excel的行数:Part对象)
		final Map<Integer, TmPart> updatePartMap = new HashMap<Integer, TmPart>();
		// 格式错误的信息
		final List<String> errorInfos = new ArrayList<String>();
		// 数据字典Map(name<==>code)
		final Map<String, String> entryDict = new HashMap<String, String>();
		String enabledKeyName = "";
		String partTypeKeyName  = "";
		String baseUnitKeyName  = "";
		String ukUnitKeyName  = "";
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			entryDict.put(e.getName(), e.getCode());
			enabledKeyName+=e.getName()+",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE)) {
			entryDict.put(e.getName(), e.getCode());
			partTypeKeyName+=e.getName()+",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_BASE_UNIT)) {
			entryDict.put(e.getName(), e.getCode());
			baseUnitKeyName+=e.getName()+",";
		}
		
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_MAINTENANCE_TIME_TYPE)) {//质保单位
			entryDict.put(e.getName(), e.getCode());
			ukUnitKeyName+=e.getName()+",";
		}
		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;
		int indexTem;
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		Row row = null;
		int judgeSize = 10;// 数据表格的列数（字段数）
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;// 第一列
			TmPart entity = new TmPart();
			// 工厂编号名称
			value = LoadUtils.getCell(row, index);

			if (StringUtils.isBlank(value)) {
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			if (StringUtils.isBlank(value)) {
				// 公司必填
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_PLANT_NO_REQUIRED"));
				continue;
			}
			indexTem = value.lastIndexOf("-");
			if (indexTem < 0) {
				// 不符合"编号-名称"格式
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_PLANT_NO_NAME_ERROR"));
				continue;
			}
			// 判断公司编号是否存在
			if (null == plantMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_PLANT_NO_UN_EXISTENT"));
				continue;
			}
			// 判断公司是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_PLANT_UNENABLED"));
				continue;
			}
			entity.setTmPlantId(plantMap.get(value).getId());

			// 物料编号
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断物料编号是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_NO_REQUIRED"));
				continue;
			}
			// 判断物料编号的长度是否超过100位
			if (value.trim().length() > 100) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_NO_UN_EXCEED_100"));
				continue;
			}
			// 判断物料编号是否符合规则 编号只允许输入英文、数字、中划线-、下划线_和反斜杠/
			if (StringUtils.isNotBlank(value) && (!Pattern.compile("[\\w-\\s]+$").matcher(value).find()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_NO_RULE"));
				continue;
			}
			entity.setNo(value);

			// 物料中文名称
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断物料中文名称是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_NAME_REQUIRED"));
				continue;
			}
			// 判断文本长度是否超过150位
			if (value.trim().length() > 150) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_MAXLENGTH_VERIFY","物料名称","150"));
				continue;
			}
			entity.setNameCn(value);
			// 物料类型
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_TYPE_REQUIRED"));
				continue;
			}
			// 判断物料类型是否合法
			if (entryDict.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_TYPE_VERIFY","物料类型",partTypeKeyName));
				continue;
			}
			entity.setType(entryDict.get(value));
			
			// 计量单位
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断单位是否合法
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_TYPE_VERIFY","单位",baseUnitKeyName));
					continue;
				}
				entity.setBaseUnit(entryDict.get(value.trim()));
			}
			// 规格型号
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断文本长度是否超过100位
				if (value.trim().length() > 50) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_MAXLENGTH_VERIFY","规格型号","50"));
					continue;
				}
				entity.setSpectyp(value.trim());
			}
			//质保单位
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断质保单位是否合法
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_TYPE_VERIFY","质保单位",ukUnitKeyName));
					continue;
				}
				entity.setUkunit(entryDict.get(value));
			}
			// 质保时间
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断质保时间是否是合法
				if (!DateUtils.checkDate(value.trim())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_TIME_VERIFY","质保时间"));
					continue;
				}
				entity.setUktime(DateUtils.parse(value.trim(), "yyyy-MM-dd"));
			}
			// 启用
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断启用是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_ENABLED_REQUIRED"));
				continue;
			}
			// 判断启用是否合法
			if (entryDict.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_TYPE_VERIFY","启用",enabledKeyName));
				continue;
			}
			entity.setEnabled(entryDict.get(value));
			// 备注
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				// 判断备注长度
				if (value.trim().length() > 256) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PART_IMPORT_MAXLENGTH_VERIFY","备注","256"));
					continue;
				}
				entity.setRemarks(value.trim());
			}
			// 新增还是更新，放入各自集合中
			if (partMap.get(entity.getTmPlantId() + entity.getNo()) == null) {
				addPartList.add(entity);
			} else {
				updatePartMap.put(i + 1, entity);
			}
		}

		List<TmPart> needUpdatParts = needUpdatePart(updatePartMap);
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImport(addPartList, ConstantUtils.IMPORT_BATCH_COUNT, ConstantUtils.OPERATION_INSERT, addCount);
			// 修改数据
			if ("true".equals(repeatOrUpdateFlag)) {
				batchImport(needUpdatParts, ConstantUtils.IMPORT_BATCH_COUNT, null, updateCount);
			}
		} catch (Exception e) {
			if ("true".equals(isRollBack)) {
				throw new BusinessException("IMPORT_DATA_VALID_ERROR_INFO",
						getMessage(req, "IMPORT_UNKNOWN_EXCEPTION"));
			} else {
				return getMessage(req, "IMPORT_UNKNOWN_EXCEPTION");
			}
		}

		Set<Integer> repeatLindexes = updatePartMap.keySet();

		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				totalInt, repeatLindexes, errorInfos,
				req, getMessage(req, "PART_MANAGEMENT"));
		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));

		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmPart> list, int num, String insert, StringBuffer countBuffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmPart>> parts = SpiltUtils.averageAssign(list, num);
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
				countBuffer.append(successCount);
			} catch (Exception e) {
				countBuffer.append(successCount);
				throw new RuntimeException();
			}
		} else {
			countBuffer.append(successCount);
		}
	}

	private List<TmPart> needUpdatePart(Map<Integer, TmPart> updatePartMap) {
		List<TmPart> updateList = new ArrayList<TmPart>();
		for (Integer key : updatePartMap.keySet()) {
			TmPart insert = updatePartMap.get(key);
			TmPart eg = new TmPart();
			eg.setTmPlantId(insert.getTmPlantId());
			eg.setNo(insert.getNo());
			TmPart newData = findUniqueByEg(eg);
			newData.setNameCn(insert.getNameCn());
			newData.setNameCnS(insert.getNameCnS());
			newData.setNameEn(insert.getNameEn());
			newData.setNameEnS(insert.getNameEnS());
			newData.setType(insert.getType());
			newData.setBatchQty(insert.getBatchQty());
			newData.setBaseUnit(insert.getBaseUnit());
			newData.setEnabled(insert.getEnabled());
			updateList.add(newData);
		}
		return updateList;
	}

	@Override
	public Workbook getWorkbook(final String templatePath) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}

	@Override
	public void checkUnique(final TmPart operationBean, final String operationType, final List<String> errorList) {
		final TmPart eg = new TmPart();
		eg.setTmPlantId(operationBean.getTmPlantId());
		TmPart bean = null;
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

	}

	@Override
	public Map<String, TmPart> initPartMapByAppointPlant(Integer plantId) {
		if (plantId == null)
			return null;
		QueryCriteria criteria = new QueryCriteria();
		criteria.addQueryCondition("tmPlantId", plantId.toString());
		criteria.setQueryPage(false);
		PageResult<TmPart> result = (PageResult<TmPart>) dao.findBy(criteria);
		List<TmPart> parts = result.getContent();
		Map<String, TmPart> map = new HashMap<String, TmPart>();
		for (TmPart part : parts) {
			map.put(part.getNo() + "-" + part.getNameCn(), part);
		}
		return map;
	}
}
