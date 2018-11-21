package com.wis.mes.master.bom.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
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
import com.wis.mes.master.base.service.impl.MasterBaseServiceImpl;
import com.wis.mes.master.bom.dao.TmBomDetailDao;
import com.wis.mes.master.bom.entity.TmBom;
import com.wis.mes.master.bom.entity.TmBomDetail;
import com.wis.mes.master.bom.service.TmBomDetailService;
import com.wis.mes.master.bom.service.TmBomService;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;

@Service
public class TmBomDetailServiceImpl extends MasterBaseServiceImpl<TmBomDetail> implements TmBomDetailService {

	@Autowired
	public void setDao(TmBomDetailDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@Autowired
	private TmBomService bomService;

	@Autowired
	private TmUlocService ulocService;

	@Autowired
	private TmPartService partService;

	@Autowired
	private ImportLogService importLogService;

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmBomDetail> list, String filePath,
			String[] headers) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);

		for (TmBomDetail bomDetail : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("工位", bomDetail.getUloc().getNo() + "-" + bomDetail.getUloc().getName());
			map.put("物料", bomDetail.getPart().getNo() + "-" + bomDetail.getPart().getNameCn());
			map.put("数量", bomDetail.getQty());
			map.put("顺序号", bomDetail.getSeq());
			map.put("是否物料追溯",
					yesOrNo.containsKey(bomDetail.getIsTrac()) ? yesOrNo.get(bomDetail.getIsTrac()).getName() : "");
			map.put("是否批次追溯", yesOrNo.containsKey(bomDetail.getIsBatchTrac())
					? yesOrNo.get(bomDetail.getIsBatchTrac()).getName() : "");
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, filePath, headers);
	}

	@Override
	public List<TmBomDetail> getTmBomDetails(Integer tmBomId) {
		TmBomDetail bomDetail = new TmBomDetail();
		bomDetail.setTmBomId(tmBomId);
		return findByEg(bomDetail);
	}

	@Override
	public List<TmBomDetail> findByForginKey(Integer tmBomId) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.addQueryCondition("tmBomId", tmBomId.toString());
		criteria.setQueryPage(false);
		PageResult<TmBomDetail> result = (PageResult<TmBomDetail>) dao.findBy(criteria);
		return result.getContent();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req, String tmBomId) {

		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

		TmBom bom = bomService.findById(Integer.parseInt(tmBomId));
		// 先判断bom状态，"维护完成"时，不能更新bomDetial
		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(bom.getStatus())) {
			throw new BusinessException("BOM_DETAIL_IMPORT_BOM_STATUS_COMPLETE");
		}

		// Bom相应工厂、车间、产线下的所有工位map
		Map<String, TmUloc> ulocMap = ulocService.initUlocMapByConditions(bom.getTmPlantId(), bom.getTmWorkshopId(),
				bom.getTmLineId());

		// 指定工厂下的part Map
		Map<String, TmPart> partMap = partService.initPartMapByAppointPlant(bom.getTmPlantId());

		// yesOrNo
		Map<String, String> yesOrNo = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO)) {
			yesOrNo.put(e.getName(), e.getCode());
		}
		// 判断是否重复数据的Map
		Map<String, TmBomDetail> bomDetailMap = new HashMap<String, TmBomDetail>();
		TmBomDetail tmBomDetial = new TmBomDetail();
		tmBomDetial.setTmBomId(bom.getId());
		for (TmBomDetail bomDetail : findByEg(tmBomDetial)) {
			bomDetailMap.put(bomDetail.getTmUlocId() + "-" + bomDetail.getTmPartId(), bomDetail);
		}

		// 需要插入的工位
		final List<TmBomDetail> addList = new ArrayList<TmBomDetail>();
		// 需要修改的工位Map
		final Map<Integer, TmBomDetail> updateMap = new HashMap<Integer, TmBomDetail>();
		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();

		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
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
			TmBomDetail entity = new TmBomDetail();

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

			// 工位是否启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(ulocMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_ULOC_NOT_ENABLED"));
				continue;
			}
			entity.setTmUlocId(ulocMap.get(value).getId());

			// ============== 物料校验 ==============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_PART_REQUIRED"));
				continue;
			}
			// 判断 物料是否在匹配的工厂下
			if (!partMap.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_PART_NOT_EXIST"));
				continue;
			}

			// 物料是否启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(partMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_PART_NOT_ENABLED"));
				continue;
			}
			entity.setTmPartId(partMap.get(value).getId());
			
			// ============== 数量校验 ==============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_QTY_REQUIRED"));
				continue;
			}
			if (!value.matches("^\\d+$")) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_QTY_PATTERN_ERROR"));
				continue;
			}
			entity.setQty(Integer.parseInt(value));
			
			// ============== 顺序号校验 ==============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_SEQ_REQUIRED"));
				continue;
			}
			if (!value.matches("^[0-9]*[1-9][0-9]*$")) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_SEQ_PATTERN_ERROR"));
				continue;
			}
			entity.setSeq(Integer.parseInt(value));
			
			// ============== 是否物料追溯 ==============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_IS_TRAC_REQUIRED"));
				continue;
			}
			// 不合法
			if (yesOrNo.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_IS_TRAC_PATTERN_ERROR"));
				continue;
			}
			entity.setIsTrac(yesOrNo.get(value));

			// ============== 是否批次追溯 ==============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_IS_BATCHT_RAC_REQUIRED"));
				continue;
			}
			// 不合法
			if (yesOrNo.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_IS_BATCH_TRAC_PATTERN_ERROR"));
				continue;
			}
			entity.setIsBatchTrac(yesOrNo.get(value));
			// ============== 备注  ==============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_DETAIL_IMPORT_IS_BATCHT_RAC_REQUIRED"));
					continue;
				}
			}
			entity.setNote(value);
			entity.setTmBomId(bom.getId());

			// 更新或者新增放到相应的容器中
			if (bomDetailMap.get(entity.getTmUlocId() + "-" + entity.getTmPartId()) == null) {
				addList.add(entity);
			} else {
				updateMap.put(i + 1, entity);
			}

		}
		List<TmBomDetail> updateList = needUpdateEntitys(updateMap);
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

	private void batchImport(List<TmBomDetail> list, int num, String insert, StringBuffer countBuffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmBomDetail>> parts = SpiltUtils.averageAssign(list, num);
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

	private List<TmBomDetail> needUpdateEntitys(Map<Integer, TmBomDetail> updatePathMap) {
		List<TmBomDetail> updateList = new ArrayList<TmBomDetail>();
		for (Integer key : updatePathMap.keySet()) {
			TmBomDetail insert = updatePathMap.get(key);
			TmBomDetail bomDetail = new TmBomDetail();
			bomDetail.setTmBomId(insert.getTmBomId());
			bomDetail.setTmUlocId(insert.getTmUlocId());
			bomDetail.setTmPartId(insert.getTmPartId());
			TmBomDetail newData = findUniqueByEg(bomDetail);
			insert.setId(newData.getId());
			updateList.add(insert);
		}
		return updateList;
	}

}
