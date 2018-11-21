package com.wis.mes.master.uloc.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
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
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.basis.configuration.service.TsParameterService;
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.base.service.impl.MasterBaseServiceImpl;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.entity.TmNcGroup;
import com.wis.mes.master.nc.service.TmNcGroupService;
import com.wis.mes.master.nc.service.TmNcService;
import com.wis.mes.master.uloc.dao.TmUlocSipDao;
import com.wis.mes.master.uloc.entity.TmUlocSip;
import com.wis.mes.master.uloc.entity.TmUlocSipNc;
import com.wis.mes.master.uloc.service.TmUlocSipNcService;
import com.wis.mes.master.uloc.service.TmUlocSipService;

@Service
public class TmUlocSipServiceImpl extends MasterBaseServiceImpl<TmUlocSip> implements TmUlocSipService {
	@Autowired
	public void setDao(TmUlocSipDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TsParameterService parameterService;
	@Autowired
	private ImportLogService importLogService;
	@Autowired
	private TmUlocSipNcService ulocSipNcService;
	@Autowired
	private TmNcGroupService ncGroupService;
	@Autowired
	private TmNcService ncService;


	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmUlocSip> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		// 将每列数据值与标题对应
		for (TmUlocSip bean : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("参数", bean.getTsParameter().getCode() + "-" + bean.getTsParameter().getName());
			map.put("详细信息", bean.getDetail());
			map.put("是否必检", yesOrNo.containsKey(bean.getIsExamine()) ? yesOrNo.get(bean.getIsExamine()).getName() : "");
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}

	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req, String tmUlocId) {
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

		// 工位参数Map
		final Map<Integer, TmUlocSip> ulocSipMap = new HashMap<Integer, TmUlocSip>();
		TmUlocSip eg = new TmUlocSip();
		eg.setTmUlocId(Integer.valueOf(tmUlocId));
		for (final TmUlocSip e : findByEg(eg)) {
			ulocSipMap.put(e.getTsParameterId(), e);
		}

		// 参数Map,（code+name）对应参数实体
		final Map<String, TsParameter> parameterMap = new HashMap<String, TsParameter>();
		for (final TsParameter e : parameterService.findAll()) {
			parameterMap.put(e.getCode() + "-" + e.getName(), e);
		}

		// 需要插入的对象List容器
		final List<TmUlocSip> addList = new ArrayList<TmUlocSip>();
		// 需要更新的工位质检参数Map,(Excel的行数:TmUlocSip对象)
		final Map<Integer, TmUlocSip> updateUlocSipMap = new HashMap<Integer, TmUlocSip>();
		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();

		// 数据字典Map
		Map<String, String> yesOrNoMap = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO)) {
			yesOrNoMap.put(e.getName(), e.getCode());
		}

		final String DI = getMessage(req, "DI");

		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		String value = null;
		Row row;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmUlocSip entity = new TmUlocSip();
			entity.setTmUlocId(Integer.valueOf(tmUlocId));
			// 参数
			value = LoadUtils.getCell(row, index);
			// 判断参数是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_IMPORT_PARAMETER_REQUIRED"));
				continue;
			}
			// 判断参数格式
			if (value.lastIndexOf("-") < 0) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_IMPORT_PARAMETER_PATTERN_ERROR"));
				continue;
			}
			// 判断参数是否存在
			if (null == parameterMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_IMPORT_PARAMETER_NO_NOT_EXIST"));
				continue;
			}

			entity.setTsParameterId(parameterMap.get(value).getId());

			// 验证详细信息
			index++;
			value = LoadUtils.getCell(row, index);
			if (!StringUtils.isBlank(value)) {
				if (value.length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_IMPORT_DETAILE_INFORMATION"));
					continue;
				}
			}
			entity.setDetail(value);
			// 是否必检
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断“是否必检”是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_IMPORT_IS_EXAMINE_NOT_NULL"));
				continue;
			}
			// 判断“是否必填”值是否合法
			if (null == yesOrNoMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_SIP_IMPORT_IS_EXAMINE_FORMAT_ERROR"));
				continue;
			}
			entity.setIsExamine(yesOrNoMap.get(value));

			// 新增还是更新，放入各自集合中
			if (ulocSipMap.get(entity.getTsParameterId()) == null) {
				addList.add(entity);
			} else {
				updateUlocSipMap.put(i + 1, entity);
			}

		}

		List<TmUlocSip> needUpdatParts = needUpdateUlocSip(updateUlocSipMap);
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImport(addList, ConstantUtils.IMPORT_BATCH_COUNT, ConstantUtils.OPERATION_INSERT, addCount);
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
		// 返回信息
		StringBuffer msgTip = new StringBuffer();
		List<ImportLog> logs = new ArrayList<ImportLog>();
		msgTip.append("成功新增:");
		msgTip.append(addCount);
		msgTip.append("条记录;<br/>");
		if ("true".equals(repeatOrUpdateFlag)) {
			msgTip.append("成功更新:");
			msgTip.append(updateCount);
			msgTip.append("条记录;<br/>");
		} else {
			if (!updateUlocSipMap.keySet().isEmpty()) {
				msgTip.append("因系统存在重复数据，而导入失败的记录:<br/>");
				int count = 0;
				for (Integer rowIndex : updateUlocSipMap.keySet()) {
					StringBuffer msgLog = new StringBuffer("重复数据：");
					ImportLog log = new ImportLog();
					msgLog.append(DI + rowIndex + "行");
					if (count < ConstantUtils.TIPS_COUNT_FOR_DUPLICATE_ERROR) {
						msgTip.append(DI + rowIndex + "行,");
					}
					if (count == ConstantUtils.TIPS_COUNT_FOR_DUPLICATE_ERROR) {
						msgTip.append(ConstantUtils.SUSPENSION_POINTS);
					}
					log.setErrorDesc(msgLog.toString());
					logs.add(log);
					count++;
				}
				msgTip.append("<br/>");
			}
		}

		if (!errorInfos.isEmpty()) {
			msgTip.append("因格式错误，而导入失败的数据:<br/>");
			int count = 0;
			for (String errorInfo : errorInfos) {
				StringBuffer msgLog = new StringBuffer("错误数据：");
				ImportLog log = new ImportLog();
				msgLog.append(errorInfo);
				if (count < ConstantUtils.TIPS_COUNT_FOR_PATTERN_ERROR) {
					msgTip.append(errorInfo);
					msgTip.append("<br/>");
				}
				if (count == ConstantUtils.TIPS_COUNT_FOR_PATTERN_ERROR) {
					msgTip.append(ConstantUtils.SUSPENSION_POINTS);
				}
				log.setErrorDesc(msgLog.toString());
				log.setOperateEntityName(getMessage(req, "UlOC_SIP_MANAGEMENT"));
				logs.add(log);
				count++;
			}
		}
		importLogService.doSaveBatch(logs);
		return msgTip.toString();
	}

	private List<TmUlocSip> needUpdateUlocSip(Map<Integer, TmUlocSip> updateUlocSipMap) {
		List<TmUlocSip> updateList = new ArrayList<TmUlocSip>();
		for (Integer key : updateUlocSipMap.keySet()) {
			TmUlocSip insert = updateUlocSipMap.get(key);
			TmUlocSip eg = new TmUlocSip();
			eg.setTmUlocId(insert.getTmUlocId());
			eg.setTsParameterId(insert.getTsParameterId());
			TmUlocSip newData = findUniqueByEg(eg);
			newData.setDetail(insert.getDetail());
			newData.setIsExamine(insert.getIsExamine());
			updateList.add(newData);
		}
		return updateList;
	}

	private void batchImport(List<TmUlocSip> list, int num, String insert, StringBuffer buffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmUlocSip>> parts = SpiltUtils.averageAssign(list, num);
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
	public String doImportExcelDataAll(Workbook workbook, ExcelImportPojo excelImportPojo, HttpServletRequest req,
			String tmUlocId) {
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);
		// 新增entity容器
		Map<TmUlocSip, List<TmUlocSipNc>> addMap = new HashMap<TmUlocSip, List<TmUlocSipNc>>();
		// 更新entity容器
		Map<TmUlocSip, List<TmUlocSipNc>> updateMap = new HashMap<TmUlocSip, List<TmUlocSipNc>>();
		// 错误信息
		List<String> errorInfos = new ArrayList<String>();
		// 获取Excel信息
		Map<String, Object> resultMap = BaseExcelUtil.readExcelDataAll(workbook, excelImportPojo);
		// 读取excel中UlocSip的信息
		Map<Integer, Object> tmUlocSipMap = (Map<Integer, Object>) resultMap.get("parentMap");
		// 读取excel中UlocSipNc的信息
		Map<Integer, Object> tmUlocSipNcMap = (Map<Integer, Object>) resultMap.get("childrenMap");
		// ======验证数据======
		this.verifyDataAll(tmUlocSipMap, tmUlocSipNcMap, addMap, updateMap, errorInfos, req, tmUlocId);
		// 保存或更新操作
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImportDataAll(ConstantUtils.OPERATION_INSERT, addMap, addCount);
			// 修改数据
			if ("true".equals(repeatOrUpdateFlag)) {
				// 更新对象获取数据库记录主键
				transToUpdateMap(updateMap);
				batchImportDataAll(null, updateMap, updateCount);
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
		for (Integer linedex : tmUlocSipMap.keySet()) {
			if (updateMap.containsKey(tmUlocSipMap.get(linedex))) {
				repeatLindexes.add(linedex + 1);// 表观比是实际多1
			}
		}
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				repeatLindexes, errorInfos, req, getMessage(req, "ULOC_SIP_IMPORT_ALL_FAIL"));
		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}

	@SuppressWarnings("unchecked")
	private void verifyDataAll(Map<Integer, Object> tmUlocSipMap, Map<Integer, Object> tmUlocSipNcMap,
			Map<TmUlocSip, List<TmUlocSipNc>> addMap, Map<TmUlocSip, List<TmUlocSipNc>> updateMap,
			List<String> errorInfos, HttpServletRequest req,String tmUlocId) {
		// yesOrNo
		Map<String, String> yesOrNo = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO)) {
			yesOrNo.put(e.getName(), e.getCode());
		}
		// 参数Map
		final Map<String, TsParameter> parameterMap = new HashMap<String, TsParameter>();
		for (final TsParameter e : parameterService.findAll()) {
			parameterMap.put(e.getCode() + "-" + e.getName(), e);
		}
		// 工位质检项Map
		final Map<String, TmUlocSip> ulocSipMap = new HashMap<String, TmUlocSip>();
		for (final TmUlocSip e : findAll()) {
			ulocSipMap.put(e.getTmUlocId() + "-" + e.getTsParameterId(), e);
		}
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
		//已存在数据
		Map<String, TmUlocSipNc> ulocSipNcMap = new HashMap<String, TmUlocSipNc>();
		for (TmUlocSipNc entity : ulocSipNcService.findAll()) {
			ulocSipNcMap.put(entity.getTmNcGroupId() + "-" + entity.getTmNcId(), entity);
		}

		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;
		// 主循环
		loopMain: for (Integer linedex : tmUlocSipMap.keySet()) {
			TmUlocSip ulocSip = (TmUlocSip) tmUlocSipMap.get(linedex);
			List<TmUlocSipNc> ulocSipNcList = (List<TmUlocSipNc>) tmUlocSipNcMap.get(linedex);
			ulocSip.setTmUlocId(Integer.valueOf(tmUlocId));
			// ========主表=======
			//------参数-----
			value = ulocSip.getTsParameter().getName();
			// 判断参数是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_SIP_IMPORT_PARAMETER_REQUIRED"));
				continue;
			}
			// 判断参数格式
			if (value.lastIndexOf("-") < 0) {
				errorInfos.add(DI + (linedex + 1)  + getMessage(req, "ULOC_SIP_IMPORT_PARAMETER_PATTERN_ERROR"));
				continue;
			}
			// 判断参数是否存在
			if (null == parameterMap.get(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_SIP_IMPORT_PARAMETER_NO_NOT_EXIST"));
				continue;
			}
			ulocSip.setTsParameterId(parameterMap.get(value).getId());
			// ------详细信息-------
			value = ulocSip.getDetail();
			if (!StringUtils.isBlank(value)) {
				if (value.length() > 150) {
					errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_SIP_IMPORT_DETAILE_INFORMATION"));
					continue;
				}
			}
			ulocSip.setDetail(value);
			//------是否必检点
			value = ulocSip.getIsExamine();
			// 判断“是否必检”是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_SIP_IMPORT_IS_EXAMINE_NOT_NULL"));
				continue;
			}
			// 判断“是否必填”值是否合法
			if (null == yesOrNo.get(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_SIP_IMPORT_IS_EXAMINE_FORMAT_ERROR"));
				continue;
			}
			ulocSip.setIsExamine(yesOrNo.get(value));
			// ================ 子 表 ================
			if (ulocSipNcList != null && !ulocSipNcList.isEmpty()) {
				int lineIndex = 0;// 行计数器
				Set<String> duplicateVerifier = new HashSet<String>();// 重复验证器
				for (TmUlocSipNc ulocSipNc : ulocSipNcList) {
					lineIndex++;
					// ------- 不合格组 ---------
					value = ulocSipNc.getNcGroup().getName();
					if (StringUtils.isBlank(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_GROUP_NULL"));
						continue loopMain;
					}
					if (value.lastIndexOf("-") < 0) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_GROUP_FORMAT"));
						continue loopMain;
					}
					if (!ncGroupMap.containsKey(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_GROUP_NOT_EXIST"));
						continue loopMain;
					}
					ulocSipNc.setTmNcGroupId(ncGroupMap.get(value).getId());
					// ------- 不合格代码 ---------
					value = ulocSipNc.getNc().getName();
					if (StringUtils.isBlank(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_NULL"));
						continue loopMain;
					}
					if (value.lastIndexOf("-") < 0) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_FORMAT"));
						continue loopMain;
					}
					if (!ncMap.containsKey(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "ULOC_SIP_NC_IMPORT_NC_NOT_EXIST"));
						continue loopMain;
					}
					ulocSipNc.setTmNcId(ncMap.get(value).getId());
					// -------- 备注 ---------
					value = ulocSipNc.getNote();
					if (StringUtils.isNotBlank(value)) {
						if (value.length() > 100) {
							errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "ULOC_SIP_NC_IMPORT_NOTE_LENGTH"));
							continue loopMain;
						}
					}
					ulocSipNc.setNote(value);
					if (duplicateVerifier.contains(ulocSipNc.getTmNcGroupId() + "-" + ulocSipNc.getTmNcId())) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "ULOC_SIP_ALL_IMPORT_EXIST_DUPLICATE_RECORD"));
						continue loopMain;
					}
					duplicateVerifier.add(ulocSipNc.getTmNcGroupId() + "-" + ulocSipNc.getTmNcId());
				}
			}
			// 版本号不为空，默认更新
			if (ulocSipMap.containsKey(ulocSip.getTmUlocId() + "-" + ulocSip.getTsParameterId())){
				updateMap.put(ulocSip, ulocSipNcList);
			} else {
				// 新增
				addMap.put(ulocSip, ulocSipNcList);
			}
		}
	}

	private void transToUpdateMap(Map<TmUlocSip, List<TmUlocSipNc>> updateMap) {
		for (TmUlocSip updateEntity : updateMap.keySet()) {
			TmUlocSip eg = new TmUlocSip();
			eg.setTmUlocId(updateEntity.getTmUlocId());
			eg.setTsParameterId(updateEntity.getTsParameterId());
			TmUlocSip newData = findUniqueByEg(eg);
			updateEntity.setId(newData.getId());
		}
	}

	private void batchImportDataAll(String insertOrUpdate, Map<TmUlocSip, List<TmUlocSipNc>> map,
			StringBuffer countBuffer) {
		int successCount = 0;
		if (!map.isEmpty()) {
			try {
				for (Entry<TmUlocSip, List<TmUlocSipNc>> entrySet : map.entrySet()) {
					TmUlocSip ulocSip = entrySet.getKey();
					List<TmUlocSipNc> children = entrySet.getValue();
					if ("insert".equals(insertOrUpdate)) {
						TmUlocSip savedUloc = doSave(ulocSip);
						if (children != null) {
							for (TmUlocSipNc tmUlocSipNc : children) {
								tmUlocSipNc.setTmUlocSipId(savedUloc.getId());
								ulocSipNcService.doSave(tmUlocSipNc);
							}
						}
						successCount += 1;
					} else {
						TmUlocSip updatedUlocSip = doUpdate(ulocSip);
						TmUlocSipNc eg = new TmUlocSipNc();
						eg.setTmUlocSipId(updatedUlocSip.getId());
						List<TmUlocSipNc> before = ulocSipNcService.findByEg(eg);
						// 删除原有的子表数据
						for (TmUlocSipNc tmUlocSipNc : before) {
							ulocSipNcService.doDeleteById(tmUlocSipNc.getId());
						}
						// 添加更新的子表数据
						if (children != null) {
							for (TmUlocSipNc tmUlocSipNc : children) {
								tmUlocSipNc.setTmUlocSipId(updatedUlocSip.getId());
								ulocSipNcService.doSave(tmUlocSipNc);
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

	@Override
	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmUlocSip> ulocSipList,
			String parentHeadStr, String childHeadStr, String filePath) {
		String[] parentHead = parentHeadStr.split(",");
		String[] childHead = childHeadStr.split(",");
		List<Map<String, Object>> parentExportList = new ArrayList<Map<String, Object>>();
		Map<Integer, List<Map<String, Object>>> childExportMap = new HashMap<Integer, List<Map<String, Object>>>();
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		Map<String, Object> result = new HashMap<String, Object>();
		// 获取参数信息
		Map<Integer, TsParameter> parameterMap = new HashMap<Integer, TsParameter>();
		for (final TsParameter parameter : parameterService.findAll()) {
			parameterMap.put(parameter.getId(), parameter);
		}
		// 不合格組
		Map<Integer, String> ncGroupMap = new HashMap<Integer, String>();
		for (TmNcGroup entity : ncGroupService.findAll()) {
			ncGroupMap.put(entity.getId(), entity.getNo()+"-"+entity.getName());
		}
		// 不合格代碼
		Map<Integer, String> ncMap = new HashMap<Integer, String>();
		for (TmNc entity : ncService.findAll()) {
			ncMap.put(entity.getId(), entity.getNo()+"-"+entity.getName());
		}

		for (int i = 0; i < ulocSipList.size(); i++) {
			TmUlocSip ulocSip = ulocSipList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(parentHead[0], ulocSip.getId());
			map.put(parentHead[1], parameterMap.get(ulocSip.getTsParameterId()).getCode()+
					"-"+parameterMap.get(ulocSip.getTsParameterId()).getName());
			map.put(parentHead[2], ulocSip.getDetail());
			map.put(parentHead[3], yesOrNo.get(ulocSip.getIsExamine()).getName());
			// 查询工位下面的质检项信息
			TmUlocSipNc eg = new TmUlocSipNc();
			eg.setTmUlocSipId(ulocSip.getId());
			List<TmUlocSipNc> ulocSipNcList = ulocSipNcService.findByEg(eg);
			List<Map<String, Object>> childExportList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < ulocSipNcList.size(); j++) {
				TmUlocSipNc ulocSipNc = ulocSipNcList.get(j);
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put(childHead[0], ncGroupMap.get(ulocSipNc.getTmNcGroupId()));
				childMap.put(childHead[1], ncMap.get(ulocSipNc.getTmNcId()));
				childMap.put(childHead[2], ulocSipNc.getNote());
				childExportList.add(childMap);
			}
			childExportMap.put(ulocSip.getId(), childExportList);
			parentExportList.add(map);
		}
		result = BaseExcelUtil.exportDataAll(response, parentExportList, parentHead, childExportMap, childHead,
				filePath);
		return result;
	}

}
