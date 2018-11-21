package com.wis.mes.master.supplier.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.SpiltUtils;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.supplier.dao.TmSupplierDao;
import com.wis.mes.master.supplier.entity.TmSupplier;
import com.wis.mes.master.supplier.entity.TmSupplierPart;
import com.wis.mes.master.supplier.service.TmSupplierPartService;
import com.wis.mes.master.supplier.service.TmSupplierService;

@Service
public class TmSupplierServiceImpl extends BaseServiceImpl<TmSupplier> implements TmSupplierService {

	@Autowired
	public void setDao(TmSupplierDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Autowired
	private TmSupplierPartService supplierPartService;

	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@Autowired
	private ImportLogService importLogService;

	@Override
	public void doRemoveByBatch(Integer[] ids) {
		for (Integer supplierId : ids) {
			TmSupplier tmSupplier = findById(supplierId);
			tmSupplier.setNo(UUID.randomUUID().toString());
			tmSupplier.setRemoved(1);
			doUpdate(tmSupplier);
		}
	}

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmSupplier> list, String fileName,
			String[] header) {

		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		// 是否启用
		Map<String, DictEntry> enabled = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_ENABLED);
		for (TmSupplier supplier : list) {
				Map<String, Object> map = new HashMap<String, Object>();
			map.put("供应商编号", supplier.getNo());
			map.put("供应商名称", supplier.getName());
			map.put("地址", getStr(supplier.getAddr()));
			map.put("联系人1", getStr(supplier.getContact1()));
			map.put("电话号码1", getStr(supplier.getTelNo1()));
			map.put("传真号码1", getStr(supplier.getFaxNo1()));
			map.put("手机号码1", getStr(supplier.getMobileNo1()));
			map.put("邮件地址1", getStr(supplier.getEmaile1()));
			map.put("联系人2", getStr(supplier.getContact2()));
			map.put("电话号码2", getStr(supplier.getTelNo2()));
			map.put("传真号码2", getStr(supplier.getFaxNo2()));
			map.put("手机号码2", getStr(supplier.getMobileNo2()));
			map.put("邮件地址2", getStr(supplier.getEmaile2()));
			map.put("备注", getStr(supplier.getNotes()));
			map.put("启用", getStr(enabled.get(supplier.getEnabled()).getName()));
				exportDataList.add(map);
			}
			return BaseExcelUtil.exportData(response, exportDataList, fileName, header);
		}

	private String getStr(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	@Override
	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmSupplier> list,
			String parentHeader, String childHeader, String filePath) {
		String[] parentHeads = parentHeader.split(",");
		String[] childHeads = childHeader.split(",");
		List<Map<String, Object>> parentExportList = new ArrayList<Map<String, Object>>();
		Map<Integer, List<Map<String, Object>>> childExportMap = new HashMap<Integer, List<Map<String, Object>>>();
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, String> enabled = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabled.put(e.getCode(), e.getName());
		}
		Map<String, String> type = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE)) {
			type.put(e.getCode(), e.getName());
		}
		for (int i = 0; i < list.size(); i++) {
			TmSupplier supplier = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(parentHeads[0], supplier.getId());
			map.put(parentHeads[1], supplier.getNo());
			map.put(parentHeads[2], supplier.getName());
			map.put(parentHeads[3], getStr(supplier.getAddr()));
			map.put(parentHeads[4], getStr(supplier.getContact1()));
			map.put(parentHeads[5], getStr(supplier.getTelNo1()));
			map.put(parentHeads[6], getStr(supplier.getFaxNo1()));
			map.put(parentHeads[7], getStr(supplier.getMobileNo1()));
			map.put(parentHeads[8], getStr(supplier.getEmaile1()));
			map.put(parentHeads[9], getStr(supplier.getContact2()));
			map.put(parentHeads[10], getStr(supplier.getTelNo2()));
			map.put(parentHeads[11], getStr(supplier.getFaxNo2()));
			map.put(parentHeads[12], getStr(supplier.getMobileNo2()));
			map.put(parentHeads[13], getStr(supplier.getEmaile2()));
			map.put(parentHeads[14], getStr(supplier.getNotes()));
			map.put(parentHeads[15], getStr(enabled.get(supplier.getEnabled())));

			List<TmSupplierPart> supplierParts = supplierPartService.findByForeignKey(supplier.getId());
			List<Map<String, Object>> childExportList = new ArrayList<Map<String, Object>>();

			for (TmSupplierPart supplierPart : supplierParts) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put(childHeads[0], supplierPart.getPart().getNo());
				childMap.put(childHeads[1], supplierPart.getPart().getNameCn());
				childMap.put(childHeads[2], type.get(supplierPart.getPart().getType()));
				childMap.put(childHeads[3], enabled.get(supplierPart.getEnabled()));
				childExportList.add(childMap);
			}
			childExportMap.put(supplier.getId(), childExportList);
			parentExportList.add(map);
		}
		result = BaseExcelUtil.exportDataAll(response, parentExportList, parentHeads, childExportMap, childHeads,
				filePath);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req) {

		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);


		// 重复数据情况下判断唯一性的map
		final Map<String, TmSupplier> uniqueMap = new HashMap<String, TmSupplier>();
		for (final TmSupplier e : findAll()) {
			uniqueMap.put(e.getNo(), e);
		}

		// 是否启用的数据字典Map
		final Map<String, String> enabledDict = new HashMap<String, String>();
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabledDict.put(e.getName(), e.getCode());
		}

		// 需要插入的物料清单Map
		final List<TmSupplier> addList = new ArrayList<TmSupplier>();
		// 需要更新的Bom容器
		final Map<Integer, TmSupplier> updateMap = new HashMap<Integer, TmSupplier>();
		// 格式错误的信息
		final List<String> errorInfos = new ArrayList<String>();
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;
		Row row;
		int judgeSize = 15;// 数据表格的列数（字段数）
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmSupplier entity = new TmSupplier();
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// ============== 供 应 商 编 号 ==============
			// 编号
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_NO_REQUIRED"));
				continue;
			}
			if (!Pattern.compile("[\\w-/]+").matcher(value).matches()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find()) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_NO_RULE"));
				continue;
			}
			entity.setNo(value);

			// =========== 供 应 商 名 称 ==========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 内容是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_NAME_REQUIRED"));
				continue;
			}
			entity.setName(value);

			// ========== 地 址 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				entity.setAddr(value);
			} else {
				entity.setAddr("");
			}

			// ========== 联 系 人 1 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				entity.setContact1(value);
			} else {
				entity.setContact1("");
			}

			// ========== 电 话 号 码 1 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (!value.matches("^[0][1-9]{2,3}-[0-9]{5,10}$")
						&& !value.matches("^[1-9]{1}[0-9]{5,8}$")
						&& !value.matches("^[0][1-9]{2,3}-[0-9]{5,10}-[0-9]{1,5}$")) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_TEL_NO1_ERROR"));
					continue;
				}
				entity.setTelNo1(value);
			} else {
				entity.setTelNo1("");
			}

			// ========== 传 真 号 码 1 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (!value.matches("^[0][1-9]{2,3}-[0-9]{5,10}$")
						&& !value.matches("^[1-9]{1}[0-9]{5,8}$")
						&& !value.matches("^[0][1-9]{2,3}-[0-9]{5,10}-[0-9]{1,5}$")) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_FAX_NO1_ERROR"));
					continue;
				}
				entity.setFaxNo1(value);
			} else {
				entity.setFaxNo1("");
			}

			// ========== 手 机 号 码 1 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (!value.matches("^[1][3,4,5,7,8][0-9]{9}$")) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_MOBILE_NO1_ERROR"));
					continue;
				}
				entity.setMobileNo1(value);
			} else {
				entity.setMobileNo1("");
			}

			// ========== 邮 件 地 址 1 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (!value
						.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_EMAIL_NO1_ERROR"));
					continue;
				}
				entity.setEmaile1(value);
			} else {
				entity.setEmaile1("");
			}

			// ========== 联 系 人 2 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				entity.setContact2(value);
			} else {
				entity.setContact2("");
			}

			// ========== 电 话 号 码 2 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (!value.matches("^[0][1-9]{2,3}-[0-9]{5,10}$")
						&& !value.matches("^[1-9]{1}[0-9]{5,8}$")
						&& !value.matches("^[0][1-9]{2,3}-[0-9]{5,10}-[0-9]{1,5}$")) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_TEL_NO2_ERROR"));
					continue;
				}
				entity.setTelNo2(value);
			} else {
				entity.setTelNo2("");
			}

			// ========== 传 真 号 码 2 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (!value.matches("^[0][1-9]{2,3}-[0-9]{5,10}$")
						&& !value.matches("^[1-9]{1}[0-9]{5,8}$")
						&& !value.matches("^[0][1-9]{2,3}-[0-9]{5,10}-[0-9]{1,5}$")) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_FAX_NO2_ERROR"));
					continue;
				}
				entity.setFaxNo2(value);
			} else {
				entity.setFaxNo2("");
			}

			// ========== 手 机 号 码 2 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (!value.matches("^[1][3,4,5,7,8][0-9]{9}$")) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_MOBILE_NO2_ERROR"));
					continue;
				}
				entity.setMobileNo2(value);
			} else {
				entity.setMobileNo2("");
			}

			// ========== 邮 件 地 址 2 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (!value
						.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_EMAIL_NO2_ERROR"));
					continue;
				}
				entity.setEmaile2(value);
			} else {
				entity.setEmaile2("");
			}

			// ========== 备 注 ===========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				entity.setNotes(value);
			} else {
				entity.setNotes("");
			}

			// ========== 启 用 ==========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 判断启用是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_ENABLED_REQUIRED"));
				continue;
			}
			// 判断启用是否合法
			if (!enabledDict.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "SUPPLIER_IMPORT_ENABLED_VALUE_RULE"));
				continue;
			}
			entity.setEnabled(enabledDict.get(value));
			entity.setRemoved(0);
			// ========== 添 加 到 容 器 中 ========
			if (!uniqueMap.containsKey(entity.getNo())) {
				addList.add(entity);
			} else {
				// 默认更新
				updateMap.put(i + 1, entity);
			}
		}

		List<TmSupplier> updateList = needUpdateEntitys(updateMap);
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
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "BOM_MANAGEMENT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");

	}

	private void batchImport(List<TmSupplier> list, int num, String operation, StringBuffer countBuffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmSupplier>> parts = SpiltUtils.averageAssign(list, num);
			try {
				for (int i = 0; i < parts.size(); i++) {
					if ("insert".equals(operation)) {
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

	private List<TmSupplier> needUpdateEntitys(Map<Integer, TmSupplier> updateMap) {
		List<TmSupplier> updateList = new ArrayList<TmSupplier>();
		for (Integer key : updateMap.keySet()) {
			TmSupplier updateEnity = updateMap.get(key);
			TmSupplier eg = new TmSupplier();
			eg.setNo(updateEnity.getNo());
			TmSupplier uniqueEntity = findUniqueByEg(eg);
			updateEnity.setId(uniqueEntity.getId());
			updateList.add(updateEnity);
		}
		return updateList;
	}
}
