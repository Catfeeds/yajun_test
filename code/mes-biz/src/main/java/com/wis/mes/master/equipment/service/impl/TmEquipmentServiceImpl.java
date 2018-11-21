package com.wis.mes.master.equipment.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
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
import com.wis.mes.master.equipment.dao.TmEquipmentDao;
import com.wis.mes.master.equipment.dao.impl.TmEquipmentDaoImpl;
import com.wis.mes.master.equipment.entity.TmEquipment;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Service
public class TmEquipmentServiceImpl extends BaseServiceImpl<TmEquipment> implements TmEquipmentService {

	@Autowired
	public void setDao(TmEquipmentDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private TmUlocService ulocService;

	@Autowired
	private TmWorkshopService workshopService;

	@Autowired
	private ImportLogService importLogService;
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_DEFAULT);
	SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATETIME_DEFAULT);
	/**
	 * @desc 导出
	 * @author ryy
	 */
	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmEquipment> list, String fileName,
			String[] header) {

		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		// 设备状态
		Map<String, DictEntry> equipmentStatus = entryService.getMapTypeCode(ConstantUtils.EQUIPMENT_NAME_STATUS);

		for (TmEquipment equipment : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("事部", equipment.getPlant().getNo());
			map.put("编号", equipment.getNo());
			map.put("名称", getStr(equipment.getName()));
			map.put("状态", equipmentStatus.containsKey(equipment.getStatus())?equipmentStatus.get(equipment.getStatus()).getName():"");
			map.put("IP地址",getStr(equipment.getIpAddress()));
			map.put("PLC型号",getStr(equipment.getPlcTyp()));
			map.put("PLC品牌", getStr(equipment.getPlcBrand()));
			map.put("网络模块型号",getStr(equipment. getNetworkModelTyp()));
			map.put("物理位置", equipment.getTmUlocId() == null ? "": (equipment.getUloc().getNo() + "-" + getStr(equipment.getUloc().getName())));
			map.put("设备供应商", getStr(equipment.getEquipmentSupplier()));
			map.put("备注", getStr(equipment.getNote()));
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, fileName, header);
	}

	private String getStr(Object obj) {

		return obj == null ? "" : obj.toString();
	}

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
			plantMap.put(e.getNo(), e);
		}
		
		// 工位map
		final Map<String, TmUloc> ulocMap = new HashMap<String, TmUloc>();
		for (final TmUloc e : ulocService.findAll()) {
			String ulocKey = e.getTmPlantId() + "-";
			ulocKey += e.getNo();
			if(StringUtils.isNotBlank(e.getName())){
				ulocKey+="-"+e.getName();
			}
			ulocMap.put(ulocKey , e);
		}

		// 重复数据情况下判断唯一性的map
		final Map<String, TmEquipment> uniqueEquipmentMap = new HashMap<String, TmEquipment>();
		for (final TmEquipment e : findAll()) {
			uniqueEquipmentMap.put(e.getTmPlantId() + "-" + e.getNo(), e);
		}

		// 是否启用的数据字典Map
		final Map<String, String> enabledDict = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabledDict.put(e.getName(), e.getCode());
		}

		// 设备状态map
		final Map<String, String> equipmentStatus = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_NAME_STATUS)) {
			equipmentStatus.put(e.getName(), e.getCode());
		}

		// 需要插入的物料清单Map
		final List<TmEquipment> addList = new ArrayList<TmEquipment>();
		// 需要更新的Bom容器
		final Map<Integer, TmEquipment> updateMap = new HashMap<Integer, TmEquipment>();
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
			TmEquipment entity = new TmEquipment();

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
				errorInfos.add(DI + (i + 1) + getMessage(req, "EQUIPMENT_IMPORT_PLANT_REQUIRED"));
				continue;
			}
			if (!plantMap.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "EQUIPMENT_IMPORT_PLANT_NO_OR_NAMECN_ERROR"));
				continue;
			}
			// 判断工厂是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "EQUIPMENT_IMPORT_PLANT_NOT_ENABLED"));
				continue;
			}
			entity.setTmPlantId(plantMap.get(value).getId());

			// ============ 编 号 校 验 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {

				// 编号校验规则
				// 判断工厂编号的长度是否超过150位
				if (value.trim().length() > 150) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "EQUIPMENT_IMPORT_NO_UN_EXCEED_150"));
					continue;
				}
				// 判断工厂编号是否符合规则 编号只允许输入英文、数字、中划线-、下划线_和反斜杠/
				if (StringUtils.isNotBlank(value) && (!Pattern.compile("[\\w-\\s]+$").matcher(value).matches()
						|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "EQUIPMENT_IMPORT_NO_RULE"));
					continue;
				}
				entity.setNo(value);
			} else {
				errorInfos.add(DI + (i + 1) + getMessage(req, "EQUIPMENT_IMPORT_NO_REQUIRED"));
				continue;
			}

			// =========== 中 文 名 称 ==========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 不能为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "EQUIPMENT_IMPORT_NAME_REQUIRED"));
				continue;
			}
			entity.setName(value);
			
			// =========== 设 备 状 态 ============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 不为空时
			if (StringUtils.isNotBlank(value)) {
				// 验证是否合法
				if (!equipmentStatus.containsKey(value)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "EQUIPMENT_IMPORT_STATUS_VALUE_RULE"));
					continue;
				}

				entity.setStatus(equipmentStatus.get(value));
			} else {
				entity.setStatus("");
			}
			// =========== IP地址 ============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 不为空时
			if (StringUtils.isNotBlank(value)) {
				entity.setIpAddress(value);
			} else {
				entity.setIpAddress("");
			}
			
			// =========== PLC型号 ============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 不为空时
			if (StringUtils.isNotBlank(value)) {
				entity.setPlcTyp(value);
			} else {
				entity.setPlcTyp("");
			}

			// =========== PLC品牌 ============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 不为空时
			if (StringUtils.isNotBlank(value)) {
				entity.setPlcBrand(value);
			} else {
				entity.setPlcBrand("");
			}
			// =========== 网络模块型号 ============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 不为空时
			if (StringUtils.isNotBlank(value)) {
				entity.setNetworkModelTyp(value);
			} else {
				entity.setNetworkModelTyp("");
			}
			
			// =========== 物理工位============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 不为空时
			if (StringUtils.isNotBlank(value)) {
				// 验证工位是否在相应的位置下
				if (!ulocMap.containsKey(entity.getTmPlantId() + "-" + value)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "EQUIPMENT_IMPORT_ULOC_NOT_EXIST"));
					continue;
				}
				entity.setTmUlocId(ulocMap.get(entity.getTmPlantId() + "-" + value).getId());
			}

			// ===========设备供应商名称============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 不为空时
			if (StringUtils.isNotBlank(value)) {
				// 验证是否合法
				entity.setEquipmentSupplier(value);
			} else {
				entity.setEquipmentSupplier("");
			}
			// =========== 备 注 ============
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 不为空时
			if (StringUtils.isNotBlank(value)) {
				// 验证是否合法
				entity.setNote(value);
			} else {
				entity.setNote("");
			}
			// ========== 添 加 entity ========
			if (!uniqueEquipmentMap
					.containsKey(entity.getTmPlantId() + "-"+ entity.getNo())) {
				addList.add(entity);
			} else {
				// 默认更新
				updateMap.put(i + 1, entity);
			}
		}

		List<TmEquipment> updateList = needUpdateEntitys(updateMap);
		// 新增数据
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

		// =========页面提醒，保存导入的错误信息日志========
		Set<Integer> repeatLindexes = updateMap.keySet();
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "EQUIPMENT_MANAGEMENT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmEquipment> list, int num, String insert, StringBuffer countBuffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmEquipment>> parts = SpiltUtils.averageAssign(list, num);
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

	private List<TmEquipment> needUpdateEntitys(Map<Integer, TmEquipment> updateMap) {
		List<TmEquipment> updateList = new ArrayList<TmEquipment>();
		for (Integer key : updateMap.keySet()) {
			TmEquipment insert = updateMap.get(key);
			TmEquipment eg = new TmEquipment();
			eg.setTmPlantId(insert.getTmPlantId());
			eg.setTmWorkshopId(insert.getTmWorkshopId());
			eg.setNo(insert.getNo());
			TmEquipment newData = findUniqueByEg(eg);
			insert.setId(newData.getId());
			updateList.add(insert);
		}
		return updateList;
	}
	
	/**
	 * 获取设备列表
	 */
	@Override
	public List<DictEntry> getDictItem(TmEquipment param) {
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmEquipment bean : (param == null ? findAll() : findByEg(param))) {
			final DictEntry dict = new DictEntry();
			dict.setCode(bean.getId().toString());
			dict.setName(bean.getNo() + ConstantUtils.STRING_ROD + bean.getName());
			dictList.add(dict);
		}
		return dictList;
	}
	
	@Override
	public List<DictEntry> queryDictItem(QueryCriteria criteria) {
		criteria.setQueryPage(false);
		List<TmEquipment> beans = findBy(criteria).getContent();
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmEquipment e : beans) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getName());
			dictList.add(dict);
			
		}
		return dictList;
	}
	
	@Override
	public List<DictEntry> queryDictItemNo(QueryCriteria criteria) {
		criteria.setQueryPage(false);
		List<TmEquipment> beans = findBy(criteria).getContent();
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmEquipment e : beans) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getNo());
			dict.setName(e.getNo() + ConstantUtils.STRING_ROD + e.getName());
			dictList.add(dict);
			
		}
		return dictList;
	}
	@Override
	public TmEquipment findByPropertyNumber(String value) {
		return ((TmEquipmentDao)dao).findByPropertyNumber(value);
	}

	@Override
	public TmEquipment findNameByNo(String no) {
		return ((TmEquipmentDao)dao).findNameByNo(no);
	}

	@Override
	public TmEquipment findMouldNameById(String mouldArray) {
		return ((TmEquipmentDao)dao).findMouldNameById(mouldArray);
	}

}
