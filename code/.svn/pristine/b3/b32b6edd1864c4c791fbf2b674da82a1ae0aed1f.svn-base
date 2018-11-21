package com.wis.mes.master.bom.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.basis.numRule.entity.NumRuleValue;
import com.wis.basis.numRule.service.TsNumRuleService;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.base.service.impl.MasterBaseServiceImpl;
import com.wis.mes.master.bom.dao.TmBomDao;
import com.wis.mes.master.bom.entity.TmBom;
import com.wis.mes.master.bom.entity.TmBomDetail;
import com.wis.mes.master.bom.service.TmBomDetailService;
import com.wis.mes.master.bom.service.TmBomService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Service
public class TmBomServiceImpl extends MasterBaseServiceImpl<TmBom> implements TmBomService {

	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TmBomDetailService bomDetailService;
	@Autowired
	private TsNumRuleService numRuleService;
	@Autowired
	private DictEntryService entryService;

	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@Autowired
	private TmPlantService plantService;

	@Autowired
	private TmWorkshopService workshopService;

	@Autowired
	private TmLineService lineService;

	@Autowired
	private TmPartService partService;

	@Autowired
	private ImportLogService importLogService;

	@Autowired
	public void setDao(TmBomDao dao) {
		this.dao = dao;
	}

	@Override
	public TmBom doDuplicate(TmBom bom, List<String> errorList) throws Exception {
		//TmBom bom = findById(id, true);
		NumRuleValue seqRuleNo = getSeqRuleNo(bom);
		try {
			bom.setBomversion(new Random().nextInt(1000) + "");
			bom.setStatus(ConstantUtils.MAINTAIN_STATUS_REPAIR);
			bom.setMaxFlag(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
			bom = doSave(bom);
			List<TmBomDetail> findBomDetail = findBomDetail(bom.getId());
			for (TmBomDetail bomDetail : findBomDetail) {
				bomDetail.setTmBomId(bom.getId());
			}
			bomDetailService.doSaveBatch(findBomDetail);
			bom.setBomversion(numRuleService.getSerialNumber(ConstantUtils.SERIALIZABLE_TYPE_BOM_VERSION, seqRuleNo));
			doUpdate(bom);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			if (StringUtils.isNotBlank(errMsg)) {
				if (errMsg.indexOf(errorList.get(0)) != -1) {
					throw new BusinessException("BOM_VERSION_ERROR");
				} else {
					throw e;
				}
			} else {
				throw e;
			}
		}
		return bom;
	}

	private List<TmBomDetail> findBomDetail(Integer bomId) {
		TmBomDetail bomDetail = new TmBomDetail();
		bomDetail.setTmBomId(bomId);
		return bomDetailService.findByEg(bomDetail);
	}

	@Override
	public NumRuleValue getSeqRuleNo(TmBom bom) {
		NumRuleValue ruleNo = new NumRuleValue();
		ruleNo.setPlantNo(bom.getPlant().getNo());
		ruleNo.setPartNo(bom.getPart().getNo());

		if (bom.getWorkShop() != null) {
			ruleNo.setWorkShopNo(bom.getWorkShop().getNo());
		}

		if (bom.getLine() != null) {
			ruleNo.setLineNo(bom.getLine().getNo());
		}
		return ruleNo;
	}

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmBom> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();

		// 启用ON/OFF
		Map<String, DictEntry> enabledType = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_ENABLED);
		// YES/NO
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		// 物料状态
		Map<String, DictEntry> bomStatus = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS);

		for (TmBom bom : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("工厂", bom.getPlant().getNo() + "-" + bom.getPlant().getNameCn());
			map.put("成品", bom.getPart().getNo() + "-" + bom.getPart().getNameCn());
			map.put("车间", (bom.getWorkShop().getNo() == null ? ""
					: bom.getWorkShop().getNo()
							+ (bom.getWorkShop().getNameCn() == null ? "" : "-" + bom.getWorkShop().getNameCn())));
			map.put("产线",
					(bom.getLine().getNo() == null ? ""
							: bom.getLine().getNo()
									+ (bom.getLine().getNameCn() == null ? "" : "-" + bom.getLine().getNameCn())));
			map.put("状态", bomStatus.containsKey(bom.getStatus()) ? bomStatus.get(bom.getStatus()).getName() : "");
			map.put("版本号", bom.getBomversion());
			map.put("是否最大版本", yesOrNo.containsKey(bom.getMaxFlag()) ? yesOrNo.get(bom.getMaxFlag()).getName() : "");
			map.put("启用", enabledType.containsKey(bom.getEnabled()) ? enabledType.get(bom.getEnabled()).getName() : "");
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}

	@Override
	public TmBom doSaveBom(TmBom bean, String errorMsg) throws Exception {
		if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getMaxFlag())
				&& ConstantUtils.TYPE_CODE_ENABLED_ON.equals(bean.getEnabled())) {
			TmBom bom = new TmBom();
			bom.setMaxFlag(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
			bom.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
			bom.setTmPartId(bean.getTmPartId());
			List<TmBom> bomEg = this.findByEg(bom);
			if (bomEg.size() > 0) {
				throw new BusinessException("BOM_UNIQUNE_ERROR");
			}
		}
		try {
			bean.setBomversion(String.valueOf(new Random().nextInt(1000)));
			bean = this.doSave(bean);
			// 获取SEQ
			bean = this.findById(bean.getId(), true);
			bean.setBomversion(numRuleService.getSerialNumber(ConstantUtils.SERIALIZABLE_TYPE_BOM_VERSION,
					this.getSeqRuleNo(bean)));
			this.doUpdate(bean);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			if (StringUtils.isNotBlank(errMsg)) {
				if (errMsg.indexOf(errorMsg) != -1) {
					throw new BusinessException("BOM_VERSION_ERROR");
				}
			}
			throw e;
		}
		return bean;
	}

	/**
	 * 导入
	 * 
	 * @author RYY
	 * @date 17/05/31
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req) {
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
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
		// 产线Map(包含车间缺省时情况Map2)
		final Map<String, TmLine> lineMap1 = new HashMap<String, TmLine>();
		final Map<String, TmLine> lineMap2 = new HashMap<String, TmLine>();
		for (final TmLine e : lineService.findAll()) {
			lineMap1.put(e.getTmPlantId() + e.getTmWorkshopId() + e.getNo() + "-" + e.getNameCn(), e);
			lineMap2.put(e.getTmPlantId() + e.getNo() + "-" + e.getNameCn(), e);
		}

		// 物料(成品)Map
		final Map<String, TmPart> partMap = new HashMap<String, TmPart>();
		for (final TmPart e : partService.findAll()) {
			partMap.put(e.getTmPlantId() + e.getNo() + "-" + e.getNameCn(), e);
		}

		// 正在启用并且为最大版本号的BOM的map
		final Map<String, TmBom> bomMap = new HashMap<String, TmBom>();
		TmBom egBom = new TmBom();
		egBom.setMaxFlag(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
		egBom.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		for (final TmBom e : findByEg(egBom)) {
			bomMap.put(e.getTmPlantId() + "-" + e.getTmPartId(), e);
		}

		// 重复数据情况下判断唯一性的map
		final Map<String, TmBom> uniqueBomMap = new HashMap<String, TmBom>();
		for (final TmBom e : findAll()) {
			uniqueBomMap.put(e.getTmPlantId() + "-" + e.getTmPartId() + "-" + e.getTmWorkshopId() + "-"
					+ e.getTmLineId() + "-" + e.getBomversion(), e);
		}

		// 是否启用的数据字典Map
		final Map<String, String> enabledDict = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabledDict.put(e.getName(), e.getCode());
		}

		// 维护状态字典Map
		final Map<String, String> maintainStatusDict = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS)) {
			maintainStatusDict.put(e.getName(), e.getCode());
		}

		// 是否最大版本Map
		final Map<String, String> isMaxVersion = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO)) {
			isMaxVersion.put(e.getName(), e.getCode());
		}

		// 需要插入的物料清单Map
		final List<TmBom> addBomList = new ArrayList<TmBom>();
		// 需要更新的Bom容器
		final Map<Integer, TmBom> updateMap = new HashMap<Integer, TmBom>();
		// 格式错误的信息
		final List<String> errorInfos = new ArrayList<String>();
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;
		Row row;
		int judgeSize = 8;// 数据表格的列数（字段数）
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {

			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmBom entity = new TmBom();
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// ============== 工 厂 校 验 ==============
			// 工厂编号
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_PLANT_REQUIRED"));
				continue;
			}
			if (!plantMap.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_PLANT_NO_OR_NAMECN_ERROR"));
				continue;
			}
			// 判断工厂是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_PLANT_NOT_ENABLED"));
				continue;
			}
			entity.setTmPlantId(plantMap.get(value).getId());

			// =========== 物 料 校 验 ==========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 内容是否为空，格式是否错误
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_PART_REQUIRED"));
				continue;
			}
			if (!partMap.containsKey(entity.getTmPlantId() + value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_PART_NOT_EXIST"));
				continue;
			}

			// 判断物料是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(partMap.get(entity.getTmPlantId() + value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_PART_NOT_ENABLED"));
				continue;
			}

			// 判断物料是否是成品或半成品
			if (!(ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED
					.equals(partMap.get(entity.getTmPlantId() + value).getType())
					|| ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED
							.equals(partMap.get(entity.getTmPlantId() + value).getType()))) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_PART_CANNOT_PATHED"));
				continue;
			}
			entity.setTmPartId(partMap.get(entity.getTmPlantId() + value).getId());

			// ========== 车 间 校 验 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (!workShopMap.containsKey(entity.getTmPlantId() + value)) {
					// 车间不存在
					errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_WORKSHOP_NOT_EXIST"));
					continue;
				}

				// 判断车间是否已启用
				if (ConstantUtils.TYPE_CODE_ENABLED_OFF
						.equals(workShopMap.get(entity.getTmPlantId() + value).getEnabled())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_WORKSHOP_NOT_ENABLED"));
					continue;
				}
				entity.setTmWorkshopId(workShopMap.get(entity.getTmPlantId() + value).getId());
			} else {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_WORKSHOP_REQUIRED"));
				continue;
			}

			// ============ 产 线 校 验 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {

				// 工厂车间下的产线是否存在
				Boolean containsFlag = StringUtils.isNotBlank(LoadUtils.getCell(row, index - 1))
						? lineMap1.containsKey(entity.getTmPlantId() + entity.getTmWorkshopId() + value)
						: lineMap2.containsKey(entity.getTmPlantId() + value);

				TmLine mapValue = StringUtils.isNotBlank(LoadUtils.getCell(row, index - 1))
						? lineMap1.get(entity.getTmPlantId() + entity.getTmWorkshopId() + value)
						: lineMap2.get(entity.getTmPlantId() + value);

				if (!containsFlag) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_LINE_NOT_EXIST"));
					continue;
				}

				// 判断产线是否已启用
				if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(mapValue.getEnabled())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_LINE_NOT_ENABLED"));
					continue;
				}
				entity.setTmLineId(mapValue.getId());
			} else {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_LINE_REQUIRED"));
				continue;
			}

			// =========== 版 本 号 校 验 ==========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 如果内容不为空,默认为更新数据
			if (StringUtils.isNotBlank(value)) {

				if (!uniqueBomMap.containsKey(entity.getTmPlantId() + "-" + entity.getTmPartId() + "-"
						+ entity.getTmWorkshopId() + "-" + entity.getTmLineId() + "-" + value)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_VERSION_NOT_EXIST"));
					continue;
				}
				entity.setBomversion(value);
			}
			// =========== 维 护 状 态 校 验 ============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 判断启用是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_MAINTAIN_STATUS_REQUIRED"));
				continue;
			}
			// 判断维护状态是否合法
			if (!maintainStatusDict.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_MAINTAIN_STATUS_VALUE_RULE"));
				continue;
			}
			entity.setStatus(maintainStatusDict.get(value));

			// ======== 是 否 最 大 版 本 校 验 ========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 判断是否最大版本是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_MAX_FLAG_REQUIRED"));
				continue;
			}
			// 判断是否最大版本号是否合法
			if (!isMaxVersion.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_MAX_FLAG_VALUE_RULE"));
				continue;
			}
			entity.setMaxFlag(isMaxVersion.get(value));

			// ========== 是 否 启 用 校 验 ==========

			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 判断启用是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_ENABLED_REQUIRED"));
				continue;
			}
			// 判断启用是否合法
			if (!enabledDict.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_ENABLED_VALUE_RULE"));
				continue;
			}
			entity.setEnabled(enabledDict.get(value));

			// ========== 添 加 BOM ========
			if (StringUtils.isBlank(LoadUtils.getCell(row, 4).trim())) {
				// 如果entity为最大版本号
				if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(entity.getMaxFlag())) {
					// 判断数据库中是否有相同的BOM，正在启用，并且为最大版本号
					if (bomMap.containsKey(entity.getTmPlantId() + "-" + entity.getTmPartId())) {
						errorInfos.add(DI + (i + 1) + getMessage(req, "BOM_IMPORT_BOM_UNIQUNE_ERROR"));
						continue;
					}
				}
				addBomList.add(entity);
			} else {
				// 默认更新
				updateMap.put(i + 1, entity);
			}

		}

		List<TmBom> updateList = needUpdateEntitys(updateMap);
		// 新增数据
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImport(addBomList, ConstantUtils.IMPORT_BATCH_COUNT, ConstantUtils.OPERATION_INSERT, addCount, req);
			// 修改数据
			if ("true".equals(repeatOrUpdateFlag)) {
				batchImport(updateList, ConstantUtils.IMPORT_BATCH_COUNT, null, updateCount, req);
			}
		} catch (Exception e) {
			if ("true".equals(isRollBack)) {
				throw new BusinessException("IMPORT_DATA_VALID_ERROR_INFO",
						getMessage(req, "IMPORT_UNKNOWN_EXCEPTION"));
			} else {
				return getMessage(req, "IMPORT_UNKNOWN_EXCEPTION");
			}
		}

		// =========页面提醒，保存导入的错误信息日志========
		Set<Integer> repeatLindexes = updateMap.keySet();
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "BOM_MANAGEMENT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");

	}

	private void batchImport(List<TmBom> list, int num, String operation, StringBuffer countBuffer,
			HttpServletRequest req) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmBom>> parts = SpiltUtils.averageAssign(list, num);
			try {
				for (int i = 0; i < parts.size(); i++) {
					if ("insert".equals(operation)) {
						doSaveBatch(parts.get(i), req);
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


	public void doSaveBatch(List<TmBom> entities, HttpServletRequest req) throws Exception {

		for (int i = 0; i < entities.size(); i++) {
			doSaveBom(entities.get(i), getMessage(req, "BOM_VERSION_UNIQUE"));
		}
	}

	public TmBom doSave(TmBom bom, HttpServletRequest req) throws Exception {
		TmBom saveBom = doSaveBom(bom, getMessage(req, "BOM_VERSION_UNIQUE"));
		return saveBom;
	}

	private List<TmBom> needUpdateEntitys(Map<Integer, TmBom> updateMap) {
		List<TmBom> updateList = new ArrayList<TmBom>();
		for (Integer key : updateMap.keySet()) {
			TmBom updateEnity = updateMap.get(key);
			TmBom eg = new TmBom();
			eg.setTmPlantId(updateEnity.getTmPlantId());
			eg.setTmPartId(updateEnity.getTmPartId());
			eg.setBomversion(updateEnity.getBomversion());
			TmBom uniqueEntity = findUniqueByEg(eg);
			updateEnity.setId(uniqueEntity.getId());
			updateList.add(updateEnity);
		}
		return updateList;
	}

	@Override
	public Workbook getWorkbookTemp(String templatePath) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}

	@Override
	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmBom> list, String parentHeader,
			String childHeader, String filePath) {

		String[] parentHeads = parentHeader.split(",");
		String[] childHeads = childHeader.split(",");
		List<Map<String, Object>> parentExportList = new ArrayList<Map<String, Object>>();
		Map<Integer, List<Map<String, Object>>> childExportMap = new HashMap<Integer, List<Map<String, Object>>>();
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, String> enabled = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabled.put(e.getCode(), e.getName());
		}

		Map<String, String> maintainStatus = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS)) {
			maintainStatus.put(e.getCode(), e.getName());
		}

		Map<String, String> yesOrNo = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO)) {
			yesOrNo.put(e.getCode(), e.getName());
		}

		for (int i = 0; i < list.size(); i++) {
			TmBom tmBom = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();

			map.put(parentHeads[0], tmBom.getId());
			map.put(parentHeads[1], tmBom.getPlant().getNo() + "-" + tmBom.getPlant().getNameCn());
			map.put(parentHeads[2], tmBom.getPart().getNo() + "-" + tmBom.getPart().getNameCn());
			map.put(parentHeads[3], tmBom.getWorkShop().getNo() == null ? ""
					: (tmBom.getWorkShop().getNo() + "-" + tmBom.getWorkShop().getNameCn()));
			map.put(parentHeads[4], tmBom.getLine().getNo() == null ? ""
					: (tmBom.getLine().getNo() + "-" + tmBom.getLine().getNameCn()));
			map.put(parentHeads[5], tmBom.getBomversion());
			map.put(parentHeads[6], maintainStatus.get(tmBom.getStatus()));
			map.put(parentHeads[7], yesOrNo.get(tmBom.getMaxFlag()));
			map.put(parentHeads[8], enabled.get(tmBom.getEnabled()));

			List<TmBomDetail> tmBomDetails = bomDetailService.findByForginKey(tmBom.getId());
			List<Map<String, Object>> childExportList = new ArrayList<Map<String, Object>>();

			for (TmBomDetail bomDetail : tmBomDetails) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put(childHeads[0], bomDetail.getUloc().getNo() + "-" + bomDetail.getUloc().getName());
				childMap.put(childHeads[1], bomDetail.getPart().getNo() + "-" + bomDetail.getPart().getNameCn());
				childMap.put(childHeads[2], bomDetail.getQty());
				childMap.put(childHeads[3], bomDetail.getSeq());
				childMap.put(childHeads[4], yesOrNo.get(bomDetail.getIsTrac()));
				childMap.put(childHeads[5], yesOrNo.get(bomDetail.getIsBatchTrac()));
				childExportList.add(childMap);
			}
			childExportMap.put(tmBom.getId(), childExportList);
			parentExportList.add(map);
		}
		result = BaseExcelUtil.exportDataAll(response, parentExportList, parentHeads, childExportMap, childHeads,
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

		// ======== 三 大 容 器 =========
		// 新增entity容器
		Map<TmBom, List<TmBomDetail>> addMap = new HashMap<TmBom, List<TmBomDetail>>();
		// 更新entity容器
		Map<TmBom, List<TmBomDetail>> updateMap = new HashMap<TmBom, List<TmBomDetail>>();
		// 错误信息
		List<String> errorInfos = new ArrayList<String>();
		// =========================

		// 获取Excel信息
		Map<String, Object> resultMap = null;
		try {
			resultMap = BaseExcelUtil.readExcelDataAll(workbook, excelImportPojo);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		// 读取excel中BOM信息
		Map<Integer, Object> bomMap = (Map<Integer, Object>) resultMap.get("parentMap");
		// 读取excel中BOM明细信息
		Map<Integer, Object> bomDetailMap = (Map<Integer, Object>) resultMap.get("childrenMap");
		// 覆盖或更新标识
		// 验证联表数据
		this.verifyDataAll(bomMap, bomDetailMap, addMap, updateMap, errorInfos, req);

		// 保存或更新操作
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImportDataAll(ConstantUtils.OPERATION_INSERT, addMap, addCount, req);
			// 修改数据
			if ("true".equals(repeatOrUpdateFlag)) {
				// 更新对象获取数据库记录主键
				transToUpdateMap(updateMap);
				batchImportDataAll(null, updateMap, updateCount, req);
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
		for (Integer linedex : bomMap.keySet()) {
			if (updateMap.containsKey(bomMap.get(linedex))) {
				repeatLindexes.add(linedex + 1);// 表观比是实际多1
			}
		}
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				bomMap.size(), repeatLindexes, errorInfos, req, getMessage(req, "BOM_MANAGEMENT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");

	}

	/**
	 * 联表统一循环导入
	 * 
	 * @param insertOrUpdate
	 * @param map
	 * @param countBuffer
	 */
	private void batchImportDataAll(String insertOrUpdate, Map<TmBom, List<TmBomDetail>> map,
			StringBuffer countBuffer, HttpServletRequest req) {
		int successCount = 0;
		if (!map.isEmpty()) {
			try {
				for (Entry<TmBom, List<TmBomDetail>> entrySet : map.entrySet()) {
					TmBom parent = entrySet.getKey();
					List<TmBomDetail> children = entrySet.getValue();
					if ("insert".equals(insertOrUpdate)) {
						TmBom savedBom = doSave(parent, req);
						if (children != null) {
							for (TmBomDetail tmBomDetail : children) {
								tmBomDetail.setTmBomId(savedBom.getId());
							}
							bomDetailService.doSaveBatch(children);
						}
						successCount += 1;
					} else {
						TmBom updatedBom = doUpdate(parent);
						TmBomDetail eg = new TmBomDetail();
						eg.setTmBomId(updatedBom.getId());
						List<TmBomDetail> before = bomDetailService.findByEg(eg);
						// 删除原有的子表数据
						Integer[] ids = new Integer[before.size()];
						for (int i = 0; i < before.size(); i++) {
							TmBomDetail tmBomDetail = before.get(i);
							ids[i] = tmBomDetail.getId();
						}
						bomDetailService.doDeleteById(ids);
						// 添加更新的子表数据
						if (children != null) {
							for (TmBomDetail tmBomDetail : children) {
								tmBomDetail.setTmBomId(updatedBom.getId());
							}
							bomDetailService.doSaveBatch(children);
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

	private void transToUpdateMap(Map<TmBom, List<TmBomDetail>> updateMap) {
		for (TmBom updateEntity : updateMap.keySet()) {
			TmBom egBom = new TmBom();
			egBom.setBomversion(updateEntity.getBomversion());
			egBom.setTmPlantId(updateEntity.getTmPlantId());
			egBom.setTmPartId(updateEntity.getTmPartId());
			TmBom newData = findUniqueByEg(egBom);
			updateEntity.setId(newData.getId());
		}
	}

	/**
	 * @desc 校验对象化的Excel数据
	 * @author ryy
	 * @date 17/06/12
	 */
	@SuppressWarnings("unchecked")
	private void verifyDataAll(Map<Integer, Object> tmBomMap, Map<Integer, Object> tmBomDetailMap,
			Map<TmBom, List<TmBomDetail>> addMap, Map<TmBom, List<TmBomDetail>> updateMap, List<String> errorInfos,
			HttpServletRequest req) {

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
		// 产线Map(包含车间缺省时情况Map2)
		final Map<String, TmLine> lineMap1 = new HashMap<String, TmLine>();
		final Map<String, TmLine> lineMap2 = new HashMap<String, TmLine>();
		for (final TmLine e : lineService.findAll()) {
			lineMap1.put(e.getTmPlantId() + e.getTmWorkshopId() + e.getNo() + "-" + e.getNameCn(), e);
			lineMap2.put(e.getTmPlantId() + e.getNo() + "-" + e.getNameCn(), e);
		}

		// 物料(成品)Map
		final Map<String, TmPart> partMap = new HashMap<String, TmPart>();
		for (final TmPart e : partService.findAll()) {
			partMap.put(e.getTmPlantId() + e.getNo() + "-" + e.getNameCn(), e);
		}

		// 正在启用并且为最大版本号的BOM的map
		final Map<String, TmBom> bomMap = new HashMap<String, TmBom>();
		TmBom egBom = new TmBom();
		egBom.setMaxFlag(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
		egBom.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		for (final TmBom e : findByEg(egBom)) {
			bomMap.put(e.getTmPlantId() + "-" + e.getTmPartId(), e);
		}

		// 重复数据情况下判断唯一性的map
		final Map<String, TmBom> uniqueBomMap = new HashMap<String, TmBom>();
		for (final TmBom e : findAll()) {
			uniqueBomMap.put(e.getTmPlantId() + "-" + e.getTmPartId() + "-" + e.getBomversion(), e);
		}

		// 判断工位uloc是否存在的四种情形，四个map
		final Map<String, TmUloc> ulocMap = new HashMap<String, TmUloc>();
		final Map<String, TmUloc> ulocMapLineAbsent = new HashMap<String, TmUloc>();
		final Map<String, TmUloc> ulocMapWorkShopAbsent = new HashMap<String, TmUloc>();
		final Map<String, TmUloc> ulocMapWorkShopAndLineAbsent = new HashMap<String, TmUloc>();
		for (TmUloc e : ulocService.findAll()) {
			ulocMap.put(e.getTmPlantId() + "-" + e.getTmWorkshopId() + "-" + e.getTmLineId() + "-" + e.getNo() + "-"
					+ e.getName(), e);
			ulocMapLineAbsent.put(e.getTmPlantId() + "-" + e.getTmWorkshopId() + "-" + e.getNo() + "-" + e.getName(),
					e);
			ulocMapWorkShopAbsent.put(e.getTmPlantId() + "-" + e.getTmLineId() + "-" + e.getNo() + "-" + e.getName(),
					e);
			ulocMapWorkShopAndLineAbsent.put(e.getTmPlantId() + "-" + e.getNo() + "-" + e.getName(), e);
		}

		// 是否启用的数据字典Map
		final Map<String, String> enabledDict = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabledDict.put(e.getName(), e.getCode());
		}

		// 维护状态字典Map
		final Map<String, String> maintainStatusDict = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS)) {
			maintainStatusDict.put(e.getName(), e.getCode());
		}

		// yesOrNo
		Map<String, String> yesOrNo = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO)) {
			yesOrNo.put(e.getName(), e.getCode());
		}
		// 是否最大版本Map
		final Map<String, String> isMaxVersion = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO)) {
			isMaxVersion.put(e.getName(), e.getCode());
		}
		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;
		// 主循环
		loopMain: for (Integer linedex : tmBomMap.keySet()) {
			TmBom tmBom = (TmBom) tmBomMap.get(linedex);
			List<TmBomDetail> tmBomDetails = (List<TmBomDetail>) tmBomDetailMap.get(linedex);

			// ========主表=======
			// -------- 工厂---------
			value = tmBom.getPlant().getNameCn();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_PLANT_REQUIRED"));
				continue;
			}
			if (!plantMap.containsKey(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_PLANT_NO_OR_NAMECN_ERROR"));
				continue;
			}
			// 判断工厂是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_PLANT_NOT_ENABLED"));
				continue;
			}
			tmBom.setTmPlantId(plantMap.get(value).getId());

			// -------- 物 料 ---------

			value = tmBom.getPart().getNameCn();
			// 内容是否为空，格式是否错误
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_PART_REQUIRED"));
				continue;
			}
			if (!partMap.containsKey(tmBom.getTmPlantId() + value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_PART_NOT_EXIST"));
				continue;
			}

			// 判断物料是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(partMap.get(tmBom.getTmPlantId() + value).getEnabled())) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_PART_NOT_ENABLED"));
				continue;
			}

			// 判断物料是否是成品或半成品
			if (!(ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED
					.equals(partMap.get(tmBom.getTmPlantId() + value).getType())
					|| ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED
							.equals(partMap.get(tmBom.getTmPlantId() + value).getType()))) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_PART_CANNOT_PATHED"));
				continue;
			}
			tmBom.setTmPartId(partMap.get(tmBom.getTmPlantId() + value).getId());

			// ========== 车 间 校 验==========

			value = tmBom.getWorkShop().getNameCn();
			if (StringUtils.isNotBlank(value)) {
				if (!workShopMap.containsKey(tmBom.getTmPlantId() + value)) {
					// 车间不存在
					errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_WORKSHOP_NOT_EXIST"));
					continue;
				}

				// 判断车间是否已启用
				if (ConstantUtils.TYPE_CODE_ENABLED_OFF
						.equals(workShopMap.get(tmBom.getTmPlantId() + value).getEnabled())) {
					errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_WORKSHOP_NOT_ENABLED"));
					continue;
				}
				tmBom.setTmWorkshopId(workShopMap.get(tmBom.getTmPlantId() + value).getId());
			}else{
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_WORKSHOP_REQUIRED"));
				continue;
			}

			// ======== 产 线 校 验=======

			value = tmBom.getLine().getNameCn();
			if (StringUtils.isNotBlank(value)) {

				// 工厂车间下的产线是否存在
				Boolean containsFlag = StringUtils.isNotBlank(tmBom.getWorkShop().getNameCn())
						? lineMap1.containsKey(tmBom.getTmPlantId() + tmBom.getTmWorkshopId() + value)
						: lineMap2.containsKey(tmBom.getTmPlantId() + value);

				TmLine mapValue = StringUtils.isNotBlank(tmBom.getWorkShop().getNameCn())
						? lineMap1.get(tmBom.getTmPlantId() + tmBom.getTmWorkshopId() + value)
						: lineMap2.get(tmBom.getTmPlantId() + value);

				if (!containsFlag) {
					errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_LINE_NOT_EXIST"));
					continue;
				}

				// 判断产线是否已启用
				if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(mapValue.getEnabled())) {
					errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_LINE_NOT_ENABLED"));
					continue;
				}
				tmBom.setTmLineId(mapValue.getId());
			}else{
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_LINE_REQUIRED"));
				continue;
			}

			// ========== 版 本 号 校 验=========
			value = tmBom.getBomversion();
			// 如果内容不为空,默认为更新数据
			if (StringUtils.isNotBlank(value)) {

				if (!uniqueBomMap
						.containsKey(tmBom.getTmPlantId() + "-" + tmBom.getTmPartId() + "-" + tmBom.getBomversion())) {
					errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_VERSION_NOT_EXIST"));
					continue;
				}
			}

			// ======= 维 护 状 态 校 验=========

			value = tmBom.getStatus();
			// 判断启用是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_MAINTAIN_STATUS_REQUIRED"));
				continue;
			}
			// 判断维护状态是否合法
			if (!maintainStatusDict.containsKey(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_MAINTAIN_STATUS_VALUE_RULE"));
				continue;
			}
			tmBom.setStatus(maintainStatusDict.get(value));

			// =========== 是 否 最 大 版 本 校 验 =========
			value = tmBom.getMaxFlag();
			// 判断是否最大版本是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_MAX_FLAG_REQUIRED"));
				continue;
			}
			// 判断是否最大版本号是否合法
			if (!isMaxVersion.containsKey(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_MAX_FLAG_VALUE_RULE"));
				continue;
			}
			tmBom.setMaxFlag(isMaxVersion.get(value));

			// ======= 是 否 启 用 校 验======

			value = tmBom.getEnabled();
			// 判断启用是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "PATH_IMPORT_ENABLED_REQUIRED"));
				continue;
			}
			// 判断启用是否合法
			if (!enabledDict.containsKey(value)) {
				errorInfos.add(DI + (linedex + 1) + getMessage(req, "PATH_IMPORT_ENABLED_VALUE_RULE"));
				continue;
			}
			tmBom.setEnabled(enabledDict.get(value));

			// ===== 添 加 BOM ======

			// 如果entity为最大版本号
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(tmBom.getMaxFlag())) {
				// 判断数据库中是否有相同的BOM，正在启用，并且为最大版本号
				if (StringUtils.isBlank(tmBom.getBomversion())
						&& bomMap.containsKey(tmBom.getTmPlantId() + "-" + tmBom.getTmPartId())) {
					errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_BOM_UNIQUNE_ERROR"));
					continue;
				}
			}
			// ================ 子 表 ================

			if (tmBomDetails != null && !tmBomDetails.isEmpty()) {
				int lineIndex = 0;// 行计数器
				Set<String> duplicateVerifier = new HashSet<String>();// 重复验证器
				for (TmBomDetail entity : tmBomDetails) {
					lineIndex++;
					// ============== 工 位 校 验 ===========
					// 是否为空
					value = entity.getUloc().getName();
					if (StringUtils.isBlank(value)) {
						errorInfos.add(
								DI + ((linedex + 1) + lineIndex) + getMessage(req, "BOM_DETAIL_IMPORT_ULOC_REQUIRED"));
						continue loopMain;
					}
					// 判断 工位是否在匹配的位置下
					boolean existFlag = false;
					TmUloc mapValue = null;
					if (StringUtils.isBlank(tmBom.getLine().getNameCn())
							&& StringUtils.isBlank(tmBom.getWorkShop().getNameCn())) {
						if ((mapValue = ulocMapWorkShopAndLineAbsent.get(tmBom.getTmPlantId() + "-" + value)) != null) {
							existFlag = true;
						}
					} else if (StringUtils.isBlank(tmBom.getLine().getNameCn())
							&& StringUtils.isNotBlank(tmBom.getWorkShop().getNameCn())) {
						if ((mapValue = ulocMapLineAbsent
								.get(tmBom.getTmPlantId() + "-" + tmBom.getTmWorkshopId() + "-" + value)) != null) {
							existFlag = true;
						}
					} else if (StringUtils.isNotBlank(tmBom.getLine().getNameCn())
							&& StringUtils.isBlank(tmBom.getWorkShop().getNameCn())) {
						if ((mapValue = ulocMapWorkShopAbsent
								.get(tmBom.getTmPlantId() + "-" + tmBom.getTmLineId() + "-" + value)) != null) {
							existFlag = true;
						}
					} else {
						if ((mapValue = ulocMap.get(tmBom.getTmPlantId() + "-" + tmBom.getTmWorkshopId() + "-"
								+ tmBom.getTmLineId() + "-" + value)) != null) {
							existFlag = true;
						}
					}

					if (!existFlag) {
						errorInfos.add(
								DI + ((linedex + 1) + lineIndex) + getMessage(req, "BOM_DETAIL_IMPORT_ULOC_NOT_EXIST"));
						continue loopMain;
					}

					// 工位是否启用
					if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(mapValue.getEnabled())) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "BOM_DETAIL_IMPORT_ULOC_NOT_ENABLED"));
						continue loopMain;
					}
					entity.setTmUlocId(mapValue.getId());

					// ============== 物料校验 ==============
					value = entity.getPart().getNameCn();
					if (StringUtils.isBlank(value)) {
						errorInfos.add(
								DI + ((linedex + 1) + lineIndex) + getMessage(req, "BOM_DETAIL_IMPORT_PART_REQUIRED"));
						continue loopMain;
					}
					// 判断 物料是否在匹配的工厂下
					if (!partMap.containsKey(tmBom.getTmPlantId() + value)) {
						errorInfos.add(
								DI + ((linedex + 1) + lineIndex) + getMessage(req, "BOM_DETAIL_IMPORT_PART_NOT_EXIST"));
						continue loopMain;
					}

					// 物料是否启用
					if (ConstantUtils.TYPE_CODE_ENABLED_OFF
							.equals(partMap.get(tmBom.getTmPlantId() + value).getEnabled())) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "BOM_DETAIL_IMPORT_PART_NOT_ENABLED"));
						continue loopMain;
					}
					entity.setTmPartId(partMap.get(tmBom.getTmPlantId() + value).getId());

					// ============== 数量校验 ==============
					value = String.valueOf(entity.getQty() == null ? "" : entity.getQty());
					if (StringUtils.isBlank(value)) {
						errorInfos.add(
								DI + ((linedex + 1) + lineIndex) + getMessage(req, "BOM_DETAIL_IMPORT_QTY_REQUIRED"));
						continue loopMain;
					}
					entity.setQty(Integer.parseInt(value));

					// ============== 顺序号校验 ==============
					value = String.valueOf(entity.getSeq() == null ? "" : entity.getSeq());
					if (StringUtils.isBlank(value)) {
						errorInfos.add(
								DI + ((linedex + 1) + lineIndex) + getMessage(req, "BOM_DETAIL_IMPORT_SEQ_REQUIRED"));
						continue loopMain;
					}
					if (!value.matches("^[0-9]*[1-9][0-9]*$")) {
						// 正整数
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "BOM_DETAIL_IMPORT_SEQ_PATTERN_ERROR"));
						continue loopMain;
					}
					entity.setSeq(Integer.parseInt(value));

					// ============== 是否物料追溯 ==============
					value = entity.getIsTrac();
					if (StringUtils.isBlank(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "BOM_DETAIL_IMPORT_IS_TRAC_REQUIRED"));
						continue loopMain;
					}
					// 不合法
					if (yesOrNo.get(value) == null) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "BOM_DETAIL_IMPORT_IS_TRAC_PATTERN_ERROR"));
						continue loopMain;
					}
					entity.setIsTrac(yesOrNo.get(value));

					// ============== 是否批次追溯 ==============
					value = entity.getIsBatchTrac();
					if (StringUtils.isBlank(value)) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "BOM_DETAIL_IMPORT_IS_BATCHT_RAC_REQUIRED"));
						continue loopMain;
					}
					// 不合法
					if (yesOrNo.get(value) == null) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "BOM_DETAIL_IMPORT_IS_BATCH_TRAC_PATTERN_ERROR"));
						continue loopMain;
					}
					entity.setIsBatchTrac(yesOrNo.get(value));
					
					// =============== 备注 ==================
					value = entity.getNote();
					if (StringUtils.isNotBlank(value)) {
						if (value.length() > 150) {
							errorInfos.add(DI + ((linedex + 1) + lineIndex)
									+ getMessage(req, "BOM_DETAIL_IMPORT_NOTEC_REQUIRED"));
							continue loopMain;
						}
					}
					// Bom明细重复数据验证，不允许有重复的数据
					if (duplicateVerifier.contains(entity.getUloc().getName() + "-" + entity.getPart().getNameCn())) {
						errorInfos.add(DI + ((linedex + 1) + lineIndex)
								+ getMessage(req, "BOM_DETAIL_IMPORT_EXIST_DUPLICATE_RECORD"));
						continue loopMain;
					}
					duplicateVerifier.add(entity.getUloc().getName() + "-" + entity.getPart().getNameCn());
				}
			}

			// 版本号不为空，默认更新
			if (StringUtils.isNotBlank(tmBom.getBomversion())) {
				// 更新TmBom时，如果"维护完成"，不允许编辑子表信息
				if (StringUtils.isNotBlank(tmBom.getBomversion())
						&& ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(tmBom.getStatus())) {
					errorInfos.add(DI + (linedex + 1) + getMessage(req, "BOM_IMPORT_MAINTAIN_STATUS_COMPLETE"));
					continue;
				}
				updateMap.put(tmBom, tmBomDetails);
			} else {
				// 新增
				addMap.put(tmBom, tmBomDetails);
			}

		}

	}

}
