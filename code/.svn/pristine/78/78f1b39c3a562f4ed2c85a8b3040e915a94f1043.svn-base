package com.wis.mes.master.metalPlate.service.impl;

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
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.SpiltUtils;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.metalPlate.dao.TmSheetMetalMaterialDao;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;
import com.wis.mes.master.metalPlate.service.TmSheetMetalMaterialService;
import com.wis.mes.util.StringUtil;

@Service
public class TmSheetMetalMaterialServiceImpl extends BaseServiceImpl<TmSheetMetalMaterial>
		implements TmSheetMetalMaterialService {

	@Autowired
	private DictEntryService entryService;

	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@Autowired
	private ImportLogService importLogService;

	@Autowired
	public void setDao(TmSheetMetalMaterialDao dao) {
		this.dao = dao;
	}

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmSheetMetalMaterial> list,
			String filePath, String[] header) {
		final List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		if (list != null && list.size() > ConstantUtils.MATH_ZERO) {
			Map<String, DictEntry> smModelType = entryService.getMapTypeCode(ConstantUtils.SM_MODEL_TYPE);
			Map<String, DictEntry> smPlaceType = entryService.getMapTypeCode(ConstantUtils.SM_PLACE_TYPE);
			Map<String, DictEntry> smMaterialDrawingNo = entryService
					.getMapTypeCode(ConstantUtils.SM_MATERIAL_DRAWING_NO);
			Map<String, DictEntry> smDrawingNoLevel = entryService.getMapTypeCode(ConstantUtils.SM_DRAWING_NO_LEVEL);
			Map<String, DictEntry> smAssemblyLine = entryService.getMapTypeCode(ConstantUtils.SM_ASSEMBLY_LINE);
			Map<String, DictEntry> smCoatingWith = entryService.getMapTypeCode(ConstantUtils.SM_COATING_WITH);
			Map<String, DictEntry> smMachiningSurface = entryService.getMapTypeCode(ConstantUtils.SM_MACHINING_SURFACE);
			Map<String, DictEntry> smProgression = entryService.getMapTypeCode(ConstantUtils.SM_PROGRESSION);
			Map<String, DictEntry> smComponent = entryService.getMapTypeCode(ConstantUtils.SM_COMPONENT);
			Map<String, DictEntry> smTexture = entryService.getMapTypeCode(ConstantUtils.SM_TEXTURE);
			Map<String, DictEntry> smPlateThickness = entryService.getMapTypeCode(ConstantUtils.SM_PLATE_THICKNESS);
			Map<String, DictEntry> smMaterialSize = entryService.getMapTypeCode(ConstantUtils.SM_MATERIAL_SIZE);
			Map<String, DictEntry> smRegionMark = entryService.getMapTypeCode(ConstantUtils.SM_REGION_MARK);

			// 将每列数据值与标题对应
			for (final TmSheetMetalMaterial bean : list) {
				final Map<String, Object> map = new HashMap<String, Object>();
				map.put("机种大类",smModelType.containsKey(bean.getModelType()) ? smModelType.get(bean.getModelType()).getName(): "");
//				map.put("机种大类", bean.getModelType());
				map.put("室内机/室外机",
						smPlaceType.containsKey(bean.getPlaceType()) ? smPlaceType.get(bean.getPlaceType()).getName()
								: "");
				map.put("材料图号",
						smMaterialDrawingNo.containsKey(bean.getMaterialDrawingNo())
								? smMaterialDrawingNo.get(bean.getMaterialDrawingNo()).getName()
								: "");
				map.put("流水线用图号",
						smAssemblyLine.containsKey(bean.getAssemblyLine())
								? smAssemblyLine.get(bean.getAssemblyLine()).getName()
								: "");
				map.put("流水线用图号等级",
						smDrawingNoLevel.containsKey(bean.getMlLevel())
								? smDrawingNoLevel.get(bean.getMlLevel()).getName()
								: "");
				map.put("涂装用图号",
						smCoatingWith.containsKey(bean.getCoatingWith())
								? smCoatingWith.get(bean.getCoatingWith()).getName()
								: "");
				map.put("涂装用图号等级",
						smDrawingNoLevel.containsKey(bean.getCwLevel())
								? smDrawingNoLevel.get(bean.getCwLevel()).getName()
								: "");
				map.put("加工図面用涂号",
						smMachiningSurface.containsKey(bean.getMachiningSurface())
								? smMachiningSurface.get(bean.getMachiningSurface()).getName()
								: "");
				map.put("铁板加工用图号等级",
						smDrawingNoLevel.containsKey(bean.getMsLevel())
								? smDrawingNoLevel.get(bean.getMsLevel()).getName()
								: "");
				map.put("程序号", (StringUtil.isEmpty(bean.getProgramNumber()) ? ConstantUtils.STRING_EMPTY
						: getEntrysByKeys(bean.getProgramNumber(), ConstantUtils.SM_MACHINING_SURFACE)));
				map.put("累进",
						smProgression.containsKey(bean.getProgression())
								? smProgression.get(bean.getProgression()).getName()
								: "");
				map.put("部品名",
						smComponent.containsKey(bean.getComponent()) ? smComponent.get(bean.getComponent()).getName()
								: "");
				map.put("后工程", StringUtil.getString(bean.getAfterProcess()));
				map.put("材质",
						smTexture.containsKey(bean.getTexture()) ? smTexture.get(bean.getTexture()).getName() : "");
				map.put("板厚",
						smPlateThickness.containsKey(bean.getPlateThickness())
								? smPlateThickness.get(bean.getPlateThickness()).getName()
								: "");
				map.put("开料尺寸",
						smMaterialSize.containsKey(bean.getMaterialSize())
								? smMaterialSize.get(bean.getMaterialSize()).getName()
								: "");
				map.put("批次数", bean.getBatchNumber());
				map.put("批次上限", bean.getBatchCap());
				map.put("工程数", bean.getStepNumber());
				map.put("区域标识",
						smRegionMark.containsKey(bean.getRegionMark())
								? smRegionMark.get(bean.getRegionMark()).getName()
								: "");
				map.put("机台", bean.getMachineTagNames());
				/*map.put("机台", (StringUtil.isEmpty(bean.getMachineTags()) ? ConstantUtils.STRING_EMPTY
						: getEntrysByKeys(bean.getMachineTags(), ConstantUtils.SM_MACHINE_TAGS)));*/
				map.put("模具厂家", StringUtil.getString(bean.getManufacturers()));
				map.put("完成识别码", StringUtil.getString(bean.getIdentificationCode()));
				map.put("模具编号", (StringUtil.isEmpty(bean.getToolNumber()) ? ConstantUtils.STRING_EMPTY
						: getEntrysByKeys(bean.getToolNumber(), ConstantUtils.SM_TOOL_NUMBER)));
				map.put("剩余库存数", bean.getInventoryNumber());
				map.put("标准CT", bean.getStandardCt());
				map.put("PLC编码", StringUtil.getString(bean.getPlcNo()));
				map.put("备注", StringUtil.getString(bean.getRemark()));
				exportDataList.add(map);
			}
			return BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		}
		return null;
	}

	private String getEntrysByKeys(String keys, String entryCode) {
		String[] arr = keys.split(",");
		String entryVal = "";
		Map<String, DictEntry> entryMap = entryService.getMapTypeCode(entryCode);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].indexOf("|") != -1) {
				String[] arr2 = arr[i].split("[|]");
				String ev = "";
				for (int j = 0; j < arr2.length; j++) {
					if (entryMap.containsKey(arr2[j])) {
						ev += entryService.getMapTypeCode(entryCode).get(arr2[j]).getName() + "|";
					} else {
						ev += "|";
					}
				}
				entryVal += ev.substring(0, ev.length() - 1) + ",";
			} else {
				if (entryMap.containsKey(arr[i])) {
					entryVal += entryService.getMapTypeCode(entryCode).get(arr[i]).getName() + ",";
				} else {
					entryVal += arr[i] + ",";
				}
			}
		}
		return entryVal.substring(0, entryVal.length() - 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req) {

		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

		final List<String> errorInfos = new ArrayList<String>();

		final List<TmSheetMetalMaterial> addList = new ArrayList<TmSheetMetalMaterial>();
		final Map<Integer, TmSheetMetalMaterial> updateMap = new HashMap<Integer, TmSheetMetalMaterial>();

		final Map<String, String> entryDict = new HashMap<String, String>();
		String modelTypeKeyName = "";
		String placeTypeKeyName = "";
		String materialDrawingNoKeyName = "";
		String drawingNoLevelKeyName = "";
		String assemblyLineKeyName = "";
		String coatingWithKeyName = "";
		String machiningSurfaceKeyName = "";
		String progressionKeyName = "";
		String componentKeyName = "";
		String textureKeyName = "";
		String plateThicknessKeyName = "";
		String materialSizeKeyName = "";
		String machineTagsKeyName = "";
		String toolNumberKeyName = "";
		String regionMarkKeyName = "";

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_MODEL_TYPE)) {
			entryDict.put(e.getName(), e.getCode());
			modelTypeKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_PLACE_TYPE)) {
			entryDict.put(e.getName(), e.getCode());
			placeTypeKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_MATERIAL_DRAWING_NO)) {
			entryDict.put(e.getName(), e.getCode());
			materialDrawingNoKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_DRAWING_NO_LEVEL)) {
			entryDict.put(e.getName(), e.getCode());
			drawingNoLevelKeyName += e.getName() + ",";
		}
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_ASSEMBLY_LINE)) {
			entryDict.put(e.getName(), e.getCode());
			assemblyLineKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_COATING_WITH)) {
			entryDict.put(e.getName(), e.getCode());
			coatingWithKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_MACHINING_SURFACE)) {
			entryDict.put(e.getName(), e.getCode());
			machiningSurfaceKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_PROGRESSION)) {
			entryDict.put(e.getName(), e.getCode());
			progressionKeyName += e.getName() + ",";
		}
		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_COMPONENT)) {
			entryDict.put(e.getName(), e.getCode());
			componentKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_TEXTURE)) {
			entryDict.put(e.getName(), e.getCode());
			textureKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_PLATE_THICKNESS)) {
			entryDict.put(e.getName(), e.getCode());
			plateThicknessKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_MATERIAL_SIZE)) {
			entryDict.put(e.getName(), e.getCode());
			materialSizeKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_MACHINE_TAGS)) {
			entryDict.put(e.getName(), e.getCode());
			machineTagsKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_TOOL_NUMBER)) {
			entryDict.put(e.getName(), e.getCode());
			toolNumberKeyName += e.getName() + ",";
		}

		for (final DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SM_REGION_MARK)) {
			entryDict.put(e.getName(), e.getCode());
			regionMarkKeyName += e.getName() + ",";
		}
		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;
		TmSheetMetalMaterial entity = null;
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		Row row = null;
		int judgeSize = 25;// 数据表格的列数（字段数）
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			int index = 0;
			entity = new TmSheetMetalMaterial();

			if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
				break;
			}
			totalInt++;

			// 机种大类
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_MODELTYPE_REQUIRED"));
				continue;
			}
			if (entryDict.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
						getMessage(req, "METALPLATE_MODELTYPE"), modelTypeKeyName));
				continue;
			}
			entity.setModelType(entryDict.get(value.trim()));
//			entity.setModelType(value.trim());

			// 室内机/室外机
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
							getMessage(req, "METALPLATE_PLACETYPE"), placeTypeKeyName));
					continue;
				}
				entity.setPlaceType(entryDict.get(value.trim()));
			}

			// 材料图号
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_MATERIALDRAWINGNO_REQUIRED"));
				continue;
			}
			/*if (entryDict.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
						getMessage(req, "METALPLATE_MATERIALDRAWINGNO"), materialDrawingNoKeyName));
				continue;
			}
			entity.setMaterialDrawingNo(entryDict.get(value.trim()));*/
			entity.setMaterialDrawingNo(value.trim());

			// 流水线用
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
							getMessage(req, "METALPLATE_ASSEMBLYLINE"), assemblyLineKeyName));
					continue;
				}
				entity.setAssemblyLine(entryDict.get(value.trim()));
			}

			// 图号等级
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1)
							+ getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
									getMessage(req, "METALPLATE_ASSEMBLYLINE") + getMessage(req, "METALPLATE_MLLEVEL"),
									drawingNoLevelKeyName));
					continue;
				}
				entity.setMlLevel(entryDict.get(value.trim()));
			}

			// 涂装用
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
							getMessage(req, "METALPLATE_COATINGWITH"), coatingWithKeyName));
					continue;
				}
				entity.setCoatingWith(entryDict.get(value.trim()));
			}

			// 图号等级
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1)
							+ getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
									getMessage(req, "METALPLATE_COATINGWITH") + getMessage(req, "METALPLATE_CWLEVEL"),
									drawingNoLevelKeyName));
					continue;
				}
				entity.setCwLevel(entryDict.get(value.trim()));
			}

			// 加工図面
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_MACHININGSURFACE_REQUIRED"));
				continue;
			}
			if (entryDict.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
						getMessage(req, "METALPLATE_MACHININGSURFACE"), machiningSurfaceKeyName));
				continue;
			}
			entity.setMachiningSurface(entryDict.get(value.trim()));

			TmSheetMetalMaterial bean = new TmSheetMetalMaterial();
			bean.setMachiningSurface(entryDict.get(value.trim()));
			Integer num = findByEg(bean).size();
			if (num > 0) {
				errorInfos
						.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_MACHININGSURFACE_EXIST", value.trim()));
				continue;
			}
			// 图号等级
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
							getMessage(req, "METALPLATE_MACHININGSURFACE") + getMessage(req, "METALPLATE_CWLEVEL"),
							drawingNoLevelKeyName));
					continue;
				}
				entity.setMsLevel(entryDict.get(value.trim()));
			}

			// 程序号
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_PROGRAMNUMBER_REQUIRED"));
				continue;
			}
			String pnVal = entryDictGetValue(entryDict, value);
			if (StringUtils.isBlank(pnVal)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
						getMessage(req, "METALPLATE_PROGRAMNUMBER"), machiningSurfaceKeyName));
				continue;
			}
			entity.setProgramNumber(pnVal);

			// 累进
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
							getMessage(req, "METALPLATE_PROGRESSION"), progressionKeyName));
					continue;
				}
				entity.setProgression(entryDict.get(value.trim()));
			}

			// 部品名
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_COMPONENT_REQUIRED"));
				continue;
			}
			if (entryDict.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
						getMessage(req, "METALPLATE_COMPONENT"), componentKeyName));
				continue;
			}
			entity.setComponent(entryDict.get(value.trim()));

			// 后工程
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				entity.setAfterProcess(value);
			}

			// 材质
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
							getMessage(req, "METALPLATE_TEXTURE"), textureKeyName));
					continue;
				}
				entity.setTexture(entryDict.get(value.trim()));
			}

			// 板厚
			index++;
			value = LoadUtils.getCell(row, index);
			/*if (StringUtils.isNotBlank(value)) {
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
							getMessage(req, "METALPLATE_PLATETHICKNESS"), plateThicknessKeyName));
					continue;
				}
				entity.setPlateThickness(entryDict.get(value.trim()));
			}*/
			if (StringUtils.isBlank(value)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
							getMessage(req, "METALPLATE_PLATETHICKNESS"), plateThicknessKeyName));
					continue;
			}
			entity.setPlateThickness(value.trim());
			// 开料尺寸
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (entryDict.get(value) == null) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
							getMessage(req, "METALPLATE_MATERIALSIZE"), materialSizeKeyName));
					continue;
				}
				entity.setMaterialSize(entryDict.get(value.trim()));
			}

			// 批次数：批次数必填,必须是整数
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value) || !isInteger(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_BATCHNUMBER_REQUIRED"));
				continue;
			}
			entity.setBatchNumber(Integer.parseInt(value));

			// 批次上限：批次上限必填,必须是整数,必须是批次数的倍数
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value) || !isInteger(value)
					|| Integer.parseInt(value) % entity.getBatchNumber() != 0) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_BATCHCAP_REQUIRED"));
				continue;
			}
			entity.setBatchCap(Integer.parseInt(value));

			// 工程数：工程数必填,必须是整数
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value) || !isInteger(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_STEPNUMBER_REQUIRED"));
				continue;
			}
			entity.setStepNumber(Integer.parseInt(value));

			// 区域标识
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_REGIONMARK_REQUIRED"));
				continue;
			}
			if (entryDict.get(value) == null) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
						getMessage(req, "METALPLATE_REGIONMARK"), regionMarkKeyName));
				continue;
			}
			entity.setRegionMark(entryDict.get(value.trim()));

			// 机台
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_MACHINETAGS_REQUIRED"));
				continue;
			}
			/*String mtVal = entryDictGetValue(entryDict, value);
			if (StringUtils.isBlank(mtVal)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TYPE_VERIFY",
						getMessage(req, "METALPLATE_MACHINETAGS"), machineTagsKeyName));
				continue;
			}*/
			entity.setMachineTagNames(value);

			// 模具厂家
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				entity.setManufacturers(value);
			}

			// 完成识别码
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_IDENTIFICATIONCODE_REQUIRED"));
				continue;
			}
			entity.setIdentificationCode(value);

			// 模具编号
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TOOLNUMBER_REQUIRED"));
				continue;
			}
			/*String tnVal = entryDictGetValue(entryDict, value);
			if (StringUtils.isBlank(tnVal)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_TOOLNUMBER_ERROR"));
				continue;
			}*/
			entity.setToolNumber(value);

			// 判断机台，模具的数量要求一致
			/*if (!checkQuantity(entity.getMachineTags(), entity.getToolNumber())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_CHECKQUANTITY_REQUIRED"));
				continue;
			}*/
			
			if (!checkQuantity(entity.getMachineTagNames(), entity.getToolNumber())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_CHECKQUANTITY_REQUIRED"));
				continue;
			}

			// 剩余库存数
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				value = "0";
			}
			entity.setInventoryNumber(Integer.parseInt(value));

			// 标准CT
			index++;
			double standardCt = 0D;
			value = LoadUtils.getCell(row, index);
			if (!StringUtils.isBlank(value)) {
				try {
					standardCt = Double.parseDouble(value);
				} catch (NumberFormatException e1) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_CHECKSTANDARDCT_REQUIRED"));
					continue;
				}
			}
			entity.setStandardCt(standardCt);

			// PLC编码
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				entity.setPlcNo(value);
			} else {
				errorInfos.add(DI + (i + 1) + getMessage(req, "METALPLATE_IMPORT_CHECKPLCNO_REQUIRED"));
				continue;
			}

			// 备注
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				entity.setRemark(value);
			}

			addList.add(entity);
		}
		List<TmSheetMetalMaterial> updateList = needUpdateEntitys(updateMap);
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			if (errorInfos.size() == 0) {
				batchImport(addList, ConstantUtils.IMPORT_BATCH_COUNT, ConstantUtils.OPERATION_INSERT, addCount);
				// 修改数据
				if ("true".equals(repeatOrUpdateFlag)) {
					batchImport(updateList, ConstantUtils.IMPORT_BATCH_COUNT, null, updateCount);
				}
			} else {
				addCount.append(0);
				updateCount.append(0);
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
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "METALPLATE_MANAGEMENT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmSheetMetalMaterial> list, int num, String insert, StringBuffer countBuffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmSheetMetalMaterial>> parts = SpiltUtils.averageAssign(list, num);
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

	private List<TmSheetMetalMaterial> needUpdateEntitys(Map<Integer, TmSheetMetalMaterial> updatePathMap) {
		List<TmSheetMetalMaterial> updateList = new ArrayList<TmSheetMetalMaterial>();
//		for (Integer key : updatePathMap.keySet()) {
//			TmSheetMetalMaterial insert = updatePathMap.get(key);
//			TmSheetMetalMaterial material = new TmSheetMetalMaterial();
//			material.setTmBomId(insert.getTmBomId());
//			TmSheetMetalMaterial newData = findUniqueByEg(material);
//			insert.setId(newData.getId());
//			updateList.add(insert);
//		}
		return updateList;
	}

	private boolean checkQuantity(String machineTags, String toolNumber) {
		if (machineTags.split(",").length == toolNumber.split(",").length) {
			return true;
		}
		/*if (machineTags.split(",").length == toolNumber.split(",").length) {
			return true;
		}*/
		return false;
	}

	private String entryDictGetValue(Map<String, String> entryDict, String value) {
		String[] arrs = value.split(",");
		String entryVal = "";
		if (value.indexOf(",") != -1) {
			arrs = value.split(",");
		} else if (value.indexOf("，") != -1) {
			arrs = value.split("，");
		} else if (value.indexOf(";") != -1) {
			arrs = value.split(";");
		} else if (value.indexOf("；") != -1) {
			arrs = value.split("；");
		}
		for (int i = 0; i < arrs.length; i++) {
			if (arrs[i].indexOf("|") > 0) {
				String[] arr2 = arrs[i].split("[|]");
				String ev = "";
				for (int j = 0; j < arr2.length; j++) {
					if (entryDict.containsKey(arr2[j])) {
						ev += entryDict.get(arr2[j]) + "|";
					} else {
						return null;
					}
				}
				entryVal += ev.substring(0, ev.length() - 1) + ",";
			}else {
				if (entryDict.containsKey(arrs[i])) {
					entryVal += entryDict.get(arrs[i]) + ",";
				} else {
					return null;
				}
			}
		}
		if (StringUtils.isBlank(entryVal)) {
			return null;
		}
		return entryVal.substring(0, entryVal.length() - 1);
	}

	@Override
	public Workbook getWorkbookTemp(String downName, String templatePath, List<TmSheetMetalMaterial> list) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}

	private static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	@Override
	public List<TmSheetMetalMaterial> findByMachiningSurface(String model) {
		return ((TmSheetMetalMaterialDao) dao).findByMachiningSurface(model);
	}
	
	@Override
	public Integer getInventoryNumber(String model) {
		// TODO Auto-generated method stub
		return ((TmSheetMetalMaterialDao) dao).getInventoryNumber(model);
	}
}
