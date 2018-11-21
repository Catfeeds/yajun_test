package com.wis.mes.master.nc.service.impl;

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
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.SpiltUtils;
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.nc.dao.TmNcGroupDao;
import com.wis.mes.master.nc.entity.TmFaultGrade;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.entity.TmNcGroup;
import com.wis.mes.master.nc.service.TmFaultGradeService;
import com.wis.mes.master.nc.service.TmNcGroupService;
import com.wis.mes.master.nc.service.TmNcService;
import com.wis.mes.util.StringUtil;

@Service
public class TmNcGroupServiceImpl extends BaseServiceImpl<TmNcGroup> implements TmNcGroupService{

	@Autowired
	public void setDao(final TmNcGroupDao dao) {
		this.dao = dao;
	}

	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private ImportLogService importLogService;
	@Autowired
	private TmNcService ncService;
	@Autowired
	private TmFaultGradeService faultGradeService;
	@Autowired
	private TmLineService lineService;

	@Override
	public Map<String, Object> exportExcelData(final HttpServletResponse response, final List<TmNcGroup> list,
			final String filePath, final String[] header) {
		final List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		// 将每列数据值与标题对应
		for (final TmNcGroup bean : list) {
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("产线", null!=bean.getTmLine()?bean.getTmLine().getNo():"");
			map.put("故障组编号", bean.getNo());
			map.put("故障组描述", bean.getName() == null ? "" : bean.getName());
			map.put("备注", bean.getRemarks() == null ? "" : bean.getRemarks());
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
		// 获取不合格代码Map
		final Map<String, TmNcGroup> ncGroupMap = new HashMap<String, TmNcGroup>();
		for (TmNcGroup ncGroup : findAll()) {
			ncGroupMap.put(ncGroup.getNo(), ncGroup);
		}
		// 产线Map
		final Map<String, TmLine> lineMap = new HashMap<String, TmLine>();
		for (final TmLine e : lineService.findAll()) {
			lineMap.put(e.getNo(), e);
		}
		// 需要插入的对象List容器
		final List<TmNcGroup> addList = new ArrayList<TmNcGroup>();
		// 需要更新的对象Map容器 (Excel的行数:对象)
		final Map<Integer, TmNcGroup> updateMap = new HashMap<Integer, TmNcGroup>();
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
			TmNcGroup entity = new TmNcGroup();
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {// 第一格为空
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// 判断 产线是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_LINE_REQUIRED"));
				continue;
			}
//			// 判断 产线格式
//			if (value.lastIndexOf("-") < 0) {
//				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_LINE_PATTERN_ERROR"));
//				continue;
//			}
			// 判断产线是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF
					.equals(lineMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "ULOC_IMPORT_LINE_NOT_ENABLED"));
				continue;
			}

			entity.setTmLineId(lineMap.get(value).getId());
			
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断编码是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "NC_GROUP_IMPORT_ERROR_ONE"));
				continue;
			}
			// 判断编码长度
			if (value.length() > 100) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "NC_GROUP_IMPORT_ERROR_SIX"));
				continue;
			}
			// 判断不合格组编码是否符合规则 编号[A-Z][0-9](首字母除Y和J)
			if ((!Pattern.compile("^[A-I|K-X|Z]\\d{0,1}$").matcher(value).find())
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find()) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "NO_ONLY"));
				continue;
			}

			entity.setNo(value);
			// 判断描述
			index++;
			value = LoadUtils.getCell(row, index);

			// 判断描述是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "NC_GROUP_IMPORT_ERROR_THREE"));
				continue;
			}
			// 判断描述长度
			if (value.length() > 150) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "NC_GROUP_IMPORT_ERROR_SEVEN"));
				continue;
			}
			entity.setName(value);
			// 判断对外系统编码
			index++;
			value = LoadUtils.getCell(row, index);
			// 如果不是空，进行判断
			if (!StringUtils.isBlank(value)) {
				// 判断长度
				if (value.length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "NC_IMPORT_ERROR_ELEVEN","描述",150));
					continue;
				}
				entity.setRemarks(value);
			}
			// =========新增还是更新，放入各自集合中===========
			if (ncGroupMap.get(entity.getNo()) == null) {
				addList.add(entity);
			} else {
				updateMap.put(i + 1, entity);
			}
		}
		List<TmNcGroup> updateList = needUpdateEntitys(updateMap);
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
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "NC_GROUP_MANAGEMENT"));
		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmNcGroup> list, int num, String insert, StringBuffer buffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmNcGroup>> parts = SpiltUtils.averageAssign(list, num);
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

	private List<TmNcGroup> needUpdateEntitys(Map<Integer, TmNcGroup> updateMap) {
		List<TmNcGroup> updateList = new ArrayList<TmNcGroup>();
		for (Integer key : updateMap.keySet()) {
			TmNcGroup insert = updateMap.get(key);
			TmNcGroup ncGroup = new TmNcGroup();
			ncGroup.setNo(insert.getNo());
			TmNcGroup newData = findUniqueByEg(ncGroup);
			newData.setName(insert.getName());
			newData.setExtCode(insert.getExtCode());
			updateList.add(newData);
		}
		return updateList;
	}

	@Override
	public List<DictEntry> getDictItem(final TmNcGroup eg) {
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		final List<TmNcGroup> list = eg == null ? findAll() : findByEg(eg);
		for (final TmNcGroup bean : list) {
			final DictEntry dict = new DictEntry();
			dict.setCode(bean.getId().toString());
			dict.setName(bean.getNo() + ConstantUtils.STRING_ROD + bean.getName());
			dictList.add(dict);
		}
		return dictList;
	}

	@Override
	public void checkUnique(final TmNcGroup operationBean, final String operationType, final List<String> errorList) {
		final TmNcGroup eg = new TmNcGroup();
		TmNcGroup bean = null;
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
//		if (operationBean.getName() != null) {
//			// 校验描述是否已存在
//			eg.setNo(null);
//			eg.setName(operationBean.getName());
//			bean = findUniqueByEg(eg);
//			if (bean != null) {
//				// 新增操作校验
//				if (ConstantUtils.STRING_ADD.equals(operationType)) {
//					throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE,
//							errorList.get(ConstantUtils.MATH_ZERO));
//				} else if (ConstantUtils.STRING_UPDATE.equals(operationType)) {
//					// 修改操作校验
//					if (!bean.getId().equals(operationBean.getId())) {
//						throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE,
//								errorList.get(ConstantUtils.MATH_ZERO));
//					}
//				}
//			}
//		}
	}

	@Override
	public Workbook getWorkbookTemp(String downName, String templatePath, List<TmNcGroup> list) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}

	@Override
	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmNcGroup> list,
			String parentHeadStr, String childHeadStr, String filePath) {
		String[] parentHead = parentHeadStr.split(",");
		String[] childHead = childHeadStr.split(",");
		final Map<Integer, TmFaultGrade> faultGradeMap = new HashMap<Integer, TmFaultGrade>();
		for (final TmFaultGrade e : faultGradeService.findAll()) {
			faultGradeMap.put(e.getId(), e);
		}
		List<Map<String, Object>> parentExportList = new ArrayList<Map<String, Object>>();
		Map<Integer, List<Map<String, Object>>> childExportMap = new HashMap<Integer, List<Map<String, Object>>>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			TmNcGroup ncGroup = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(parentHead[0], ncGroup.getId());
			map.put(parentHead[1], null != ncGroup.getTmLine()?ncGroup.getTmLine().getNo():"");
			map.put(parentHead[2], ncGroup.getNo());
			map.put(parentHead[3], StringUtil.getString(ncGroup.getName()));
			map.put(parentHead[4], StringUtil.getString(ncGroup.getRemarks()));
			// 查询不合格组下面的不合格代码
			TmNc eg = new TmNc();
			eg.setTmNcGroupId(ncGroup.getId());
			List<TmNc> ncList = ncService.findByEg(eg);
			List<Map<String, Object>> childExportList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < ncList.size(); j++) {
				TmNc nc = ncList.get(j);
				String ncLevel = "";
				if(null != nc.getTmFaultGradeId() && faultGradeMap.containsKey(nc.getTmFaultGradeId())){
					ncLevel = faultGradeMap.get(nc.getTmFaultGradeId()).getNgLevel();
				}
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put(childHead[0], nc.getNo());
				childMap.put(childHead[1], StringUtil.getString(nc.getName()));
				childMap.put(childHead[2], StringUtil.getString(ncLevel));
				childMap.put(childHead[3], StringUtil.getString(nc.getRemarks()));
				childExportList.add(childMap);
			}
			childExportMap.put(ncGroup.getId(), childExportList);
			parentExportList.add(map);
		}
		result = BaseExcelUtil.exportDataAll(response, parentExportList, parentHead, childExportMap, childHead,
				filePath);
		return result;
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
		Map<TmNcGroup, List<TmNc>> addMap = new HashMap<TmNcGroup, List<TmNc>>();
		// 更新entity容器
		Map<TmNcGroup, List<TmNc>> updateMap = new HashMap<TmNcGroup, List<TmNc>>();
		// 错误信息
		List<String> errorInfos = new ArrayList<String>();
		// 获取Excel信息
		Map<String, Object> resultMap = BaseExcelUtil.readExcelDataAll(workbook, excelImportPojo);
		// 读取excel中不合格组的信息
		Map<Integer, Object> ncGroupMap = (Map<Integer, Object>) resultMap.get("parentMap");
		// 读取excel中不合格代码信息
		Map<Integer, Object> ncMap = (Map<Integer, Object>) resultMap.get("childrenMap");
		// ======验证数据======
		this.verifyDataAll(ncGroupMap, ncMap, addMap, updateMap, errorInfos, req);
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
		for (Integer linedex : ncGroupMap.keySet()) {
			if (updateMap.containsKey(ncGroupMap.get(linedex))) {
				repeatLindexes.add(linedex + 1);// 表观比是实际多1
			}
		}
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				repeatLindexes, errorInfos, req, getMessage(req, "NC_GROUP_AND_NC_IMPORTL"));
		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}

	private void transToUpdateMap(Map<TmNcGroup, List<TmNc>> updateMap) {
		for (TmNcGroup updateEntity : updateMap.keySet()) {
			TmNcGroup eg = new TmNcGroup();
			eg.setNo(updateEntity.getNo());
			TmNcGroup newData = findUniqueByEg(eg);
			updateEntity.setId(newData.getId());
		}
	}

	private void batchImportDataAll(String insertOrUpdate, Map<TmNcGroup, List<TmNc>> map, StringBuffer countBuffer) {
		int successCount = 0;
		if (!map.isEmpty()) {
			try {
				for (Entry<TmNcGroup, List<TmNc>> entrySet : map.entrySet()) {
					TmNcGroup ncGroup = entrySet.getKey();
					List<TmNc> children = entrySet.getValue();
					if ("insert".equals(insertOrUpdate)) {
						TmNcGroup savedNcGroup = doSave(ncGroup);
						if (children != null) {
							for (TmNc tmNc : children) {
								tmNc.setTmNcGroupId(savedNcGroup.getId());
								ncService.doSave(tmNc);
							}
						}
						successCount += 1;
					} else {
						TmNcGroup updatedNcGroup = doUpdate(ncGroup);
						TmNc eg = new TmNc();
						eg.setTmNcGroupId(updatedNcGroup.getId());
						List<TmNc> before = ncService.findByEg(eg);
						// 删除原有的子表数据
						for (TmNc tmNc : before) {
							ncService.doDeleteById(tmNc.getId());
						}
						// 添加更新的子表数据
						if (children != null) {
							for (TmNc tmUlocSip : children) {
								tmUlocSip.setTmNcGroupId(updatedNcGroup.getId());
								ncService.doSave(tmUlocSip);
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
	private void verifyDataAll(Map<Integer, Object> ncGroupMap, Map<Integer, Object> ncMap,
			Map<TmNcGroup, List<TmNc>> addMap, Map<TmNcGroup, List<TmNc>> updateMap, List<String> errorInfos,
			HttpServletRequest req) {
		// 不合格组map
		final Map<String, TmNcGroup> tmNcGroupMap = new HashMap<String, TmNcGroup>();
		for (final TmNcGroup e : findAll()) {
			tmNcGroupMap.put(e.getNo(), e);
		}
		final Map<String, TmFaultGrade> faultGradeMap = new HashMap<String, TmFaultGrade>();
		for (final TmFaultGrade e : faultGradeService.findAll()) {
			faultGradeMap.put(e.getNgLevel(), e);
		}
		
		// 产线Map
		final Map<String, TmLine> lineMap = new HashMap<String, TmLine>();
		for (final TmLine e : lineService.findAll()) {
			lineMap.put(e.getNo(), e);
		}
		//存放明细列表数据
		final List<TmNc> newNcList = new ArrayList<TmNc>();
		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;
		// 主循环
		loopMain: for (Integer linedex : ncGroupMap.keySet()) {
			TmNcGroup ncGroup = (TmNcGroup) ncGroupMap.get(linedex);
			List<TmNc> ncList = (List<TmNc>) ncMap.get(linedex);
			// ========== 主表 ===========
			value = ncGroup.getLineNo();
			// 判断 产线是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_LINE_REQUIRED"));
				continue;
			}
			// 判断产线是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF
					.equals(lineMap.get(value).getEnabled())) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "ULOC_IMPORT_LINE_NOT_ENABLED"));
				continue;
			}
			ncGroup.setTmLineId(lineMap.get(value).getId());
			
			
			// ------- 故障组编号 --------
			value = ncGroup.getNo();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "NC_GROUP_IMPORT_ERROR_ONE"));
				continue;
			}
			if (value.length() > 100) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "NC_GROUP_IMPORT_ERROR_SIX"));
				continue;
			}
			if ((!Pattern.compile("^[A-I|K-X|Z]\\d{0,1}$").matcher(value).find())
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find()) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "NO_ONLY"));
				continue;
			}
			ncGroup.setNo(value);
			// ------- 故障组名称（描述） --------
			value = ncGroup.getName();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "NC_GROUP_IMPORT_ERROR_THREE"));
				continue;
			}
			if (value.length() > 150) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "NC_GROUP_IMPORT_ERROR_SEVEN"));
				continue;
			}
			ncGroup.setName(StringUtil.getString(value));
			// ========== 子表 ===========
			if (ncList != null && !ncList.isEmpty()) {
				int lineIndex = 0;// 行计数器
				Set<String> duplicateVerifier = new HashSet<String>();// 重复验证器
				for (TmNc nc : ncList) {
					lineIndex++;
					nc.setNo(ncService.createTemplateNo(ncGroup.getLineNo()+ncGroup.getNo()));
					/*value = nc.getNo();
					if (StringUtils.isBlank(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "NC_IMPORT_ERROR_TWO"));
						continue loopMain;
					}
					if (value.length() > 100) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "NC_IMPORT_ERROR_NINE"));
						continue loopMain;
					}
					if ((!Pattern.compile("[\\w\\d-_\\\\\\/]+").matcher(value).find())
							|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find()) {
						errorInfos
								.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "IMPORT_DATA_VALID_ERROR_NO"));
						continue loopMain;
					}*/
					
					// --------- 故障名称（描述） ----------
					value = nc.getName();
					if (StringUtils.isBlank(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "NC_IMPORT_ERROR_FOUR"));
						continue loopMain;
					}
					if (value.length() > 150) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex) + getMessage(req, "NC_IMPORT_ERROR_TEN"));
						continue loopMain;
					}
					nc.setName(value);
					// ---------故障等级 ----------
					value = nc.getNgLevel();
					if (!StringUtils.isBlank(value)) {
						if (value.length() > 10) {
							errorInfos.add(DI + (linedex + 1) + getMessage(req, "NC_IMPORT_ERROR_ELEVEN","故障等级",10));
							continue;
						}
						if(faultGradeMap.containsKey(value)){
							nc.setTmFaultGradeId(faultGradeMap.get(value).getId());
						}
					}
					// --------- 故障代码备注 ----------
					value = nc.getRemarks();
					if (!StringUtils.isBlank(value)) {
						if (value.length() > 150) {
							errorInfos.add(DI + (linedex + 1) + getMessage(req, "NC_IMPORT_ERROR_ELEVEN","备注",150));
							continue;
						}
						nc.setRemarks(value);
					}
					// 不合格代码重复数据验证，不允许有重复的数据
					if (duplicateVerifier.contains(nc.getNo())) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "NC_GROUP_IMPORT_EXIST_DUPLICATE_RECORD"));
						continue loopMain;
					}
					duplicateVerifier.add(nc.getNo());
					newNcList.add(nc);
				}
			}
			// 版本号不为空，默认更新
			if (tmNcGroupMap.containsKey(ncGroup.getNo())) {
				updateMap.put(ncGroup, newNcList);
			} else {
				// 新增
				addMap.put(ncGroup, newNcList);
			}
		}
	}

	@Override
	public List<DictEntry> queryDictItem(QueryCriteria criteria) {
		ActionUtils.addUserDataPermission(criteria, "tmLineId");
		criteria.setQueryPage(false);
		List<TmNcGroup> beans = findBy(criteria).getContent();
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmNcGroup e : beans) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getNo() + "-" + e.getName());
			dictList.add(dict);
		}
		return dictList;
	}
	
}
