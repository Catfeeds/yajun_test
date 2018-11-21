package com.wis.mes.master.classmanage.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.SpiltUtils;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.classmanage.dao.TmClassManagerDetailDao;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.classmanage.entity.TmClassManagerDetail;
import com.wis.mes.master.classmanage.service.TmClassManagerDetailService;
import com.wis.mes.master.classmanage.service.TmClassManagerService;
import com.wis.mes.master.employeeno.entity.TmEmployeeNo;
import com.wis.mes.master.employeeno.service.TmEmployeeNoService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.util.StringUtil;

@Service
public class TmClassManagerDetailServiceImpl extends BaseServiceImpl<TmClassManagerDetail>
		implements TmClassManagerDetailService {

	@Autowired
	public void setDao(TmClassManagerDetailDao dao) {
		this.dao = dao;
	}

	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TmClassManagerService classManagerService;
	@Autowired
	private TmEmployeeNoService employeeNoService;
	@Autowired
	private ImportLogService importLogService;

	@Override
	public void exportExcelData(HttpServletResponse response, List<TmClassManagerDetail> content, String filePath,
			String[] headers) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		for (TmClassManagerDetail bean : content) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("工位编号", bean.getTmUloc().getNo());
			map.put("加工工艺", StringUtils.isNotEmpty(bean.getTmUloc().getName())?bean.getTmUloc().getName():"");
			map.put("作业员工号", bean.getTmEmployeeNo().getNo());
			map.put("姓名", bean.getTmEmployeeNo().getName());
			map.put("备注", StringUtil.getString(bean.getRemark()));
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, filePath, headers);
	}

	private Map<String, TmEmployeeNo> employeeNoMap() {
		List<TmEmployeeNo> findAll = employeeNoService.findAll();
		Map<String, TmEmployeeNo> map = new HashMap<String, TmEmployeeNo>();
		for (TmEmployeeNo bean : findAll) {
			map.put(bean.getNo(), bean);
		}
		return map;
	}

	private Map<String, TmClassManagerDetail> getClassManagerDetailMap(Integer id) {
		TmClassManagerDetail eg = new TmClassManagerDetail();
		eg.setTmClassManagerId(id);
		Map<String, TmClassManagerDetail> map = new HashMap<String, TmClassManagerDetail>();
		List<TmClassManagerDetail> findByEg = this.findByEg(eg);
		for (TmClassManagerDetail bean : findByEg) {
			map.put(id + "_" + bean.getTmUlocId(), bean);
		}
		return map;
	}

	private Map<String, TmUloc> ulocNoMap(Integer tmPlantId, Integer tmLineId) {
		TmUloc eg = new TmUloc();
		eg.setTmPlantId(tmPlantId);
		eg.setTmLineId(tmLineId);
		List<TmUloc> findAll = ulocService.findByEg(eg);
		Map<String, TmUloc> map = new HashMap<String, TmUloc>();
		for (TmUloc bean : findAll) {
			map.put(bean.getNo(), bean);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(Workbook book, HttpServletRequest req, String tmClassManagerId) {

		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

		TmClassManager classManager = classManagerService.findById(Integer.valueOf(tmClassManagerId));

		// Bom相应工厂、车间、产线下的所有工位map
		Map<String, TmUloc> ulocMap = ulocNoMap(classManager.getTmPlantId(), classManager.getTmLineId());
		Map<String, TmEmployeeNo> employeeNoMap = employeeNoMap();
		Map<String, TmClassManagerDetail> classManagerDetailMap = getClassManagerDetailMap(classManager.getId());

		// 需要插入的工位
		final List<TmClassManagerDetail> addList = new ArrayList<TmClassManagerDetail>();
		final List<TmClassManagerDetail> updateList = new ArrayList<TmClassManagerDetail>();
		// 需要修改的工位Map
		final Map<Integer, TmClassManagerDetail> updateMap = new HashMap<Integer, TmClassManagerDetail>();
		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();
		Set<String> uniqueSet = new HashSet<String>();
		// 读取第一章表格内容
		final Sheet sheet = book.getSheetAt(0);
		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;
		Row row;
		// 循环输出表格中的内容
		int totalInt = 0;
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			totalInt++;
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmClassManagerDetail entity = new TmClassManagerDetail();

			// ============== 工 位 校 验 ===========
			// 是否为空
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_ULOC_REQUIRED"));
				continue;
			}
			// 判断 工位是否在匹配的位置下
			if (!ulocMap.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_ULOC_NOT_EXIST"));
				continue;
			}
			if (!uniqueSet.add(value)) {
				continue;
			}
			entity.setTmUlocId(ulocMap.get(value).getId());
			
			//工艺名称
			index++;
			value = LoadUtils.getCell(row, index).trim();

			// ============== 工作员编号 ==============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + "行，工作员编号不能为空!");
				continue;
			}

			if (!employeeNoMap.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + "行，工作员编号不存在!");
				continue;
			}
			entity.setTmEmployeeNoId(employeeNoMap.get(value).getId());

			//作业员姓名
			index++;
			value = LoadUtils.getCell(row, index).trim();

			// ============== 备注 ==============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_IS_BATCHT_RAC_REQUIRED"));
					continue;
				}
			}
			entity.setRemark(value);
			entity.setTmClassManagerId(Integer.valueOf(tmClassManagerId));

			// 更新或者新增放到相应的容器中
			if (classManagerDetailMap.get(entity.getTmUlocId() + "_" + entity.getTmUlocId()) == null) {
				addList.add(entity);
			} else {
				updateMap.put(i + 1, entity);
				TmClassManagerDetail tmClassManagerDetail = classManagerDetailMap
						.get(entity.getTmUlocId() + "_" + entity.getTmUlocId());
				tmClassManagerDetail.setTmEmployeeNoId(entity.getTmEmployeeNoId());
				tmClassManagerDetail.setRemark(entity.getRemark());
				updateList.add(tmClassManagerDetail);
			}
		}
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
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "BOM_MANAGEMENT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");

	}

	private void batchImport(List<TmClassManagerDetail> list, int num, String insert, StringBuffer countBuffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmClassManagerDetail>> parts = SpiltUtils.averageAssign(list, num);
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

}
