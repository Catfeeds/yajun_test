package com.wis.mes.master.path.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
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
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.path.dao.TmPathDao;
import com.wis.mes.master.path.entity.TmPath;
import com.wis.mes.master.path.entity.TmPathLigature;
import com.wis.mes.master.path.entity.TmPathUloc;
import com.wis.mes.master.path.entity.TmPathUlocParameter;
import com.wis.mes.master.path.entity.TmPathUlocPart;
import com.wis.mes.master.path.entity.TmPathUlocSip;
import com.wis.mes.master.path.service.TmPathLigatureService;
import com.wis.mes.master.path.service.TmPathService;
import com.wis.mes.master.path.service.TmPathUlocParameterService;
import com.wis.mes.master.path.service.TmPathUlocPartService;
import com.wis.mes.master.path.service.TmPathUlocService;
import com.wis.mes.master.path.service.TmPathUlocSipService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;
import com.wis.mes.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class TmPathServiceImpl extends BaseServiceImpl<TmPath> implements TmPathService {

	@Autowired
	public void setDao(final TmPathDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TmPathUlocService pathUlocService;
	@Autowired
	private DictEntryService entryService;
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
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TmPathLigatureService pathLigatureService;
	@Autowired
	private TmPathUlocPartService pathUlocPartService;
	@Autowired
	private TmPathUlocParameterService pathUlocParameterService;
	@Autowired
	private TmPathUlocSipService pathUlocSipService;

	@Override
	public Map<String, Object> exportExcelData(final HttpServletResponse response, final List<TmPath> list,
			final String filePath, final String[] header) {
		// 启用ON/OFF
		Map<String, DictEntry> enabledType = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_ENABLED);
		// 维护状态
		Map<String, DictEntry> maintainStatus = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS);

		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		for (TmPath path : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("工厂", path.getPlant().getNo() + "-" + path.getPlant().getNameCn());
			map.put("车间", path.getWorkshop().getId() == null ? ""
					: (path.getWorkshop().getNo() + "-" + path.getWorkshop().getNameCn()));
			map.put("产线",
					path.getLine().getId() == null ? "" : (path.getLine().getNo() + "-" + path.getLine().getNameCn()));
			map.put("物料",
					path.getPart().getId() == null ? "" : (path.getPart().getNo() + "-" + path.getPart().getNameCn()));
			map.put("编号", path.getNo());
			map.put("名称", path.getName());
			map.put("来源工艺路径", path.getCopyPath().getId() == null ? ""
					: (path.getCopyPath().getNo() + "-" + path.getCopyPath().getName()));
			map.put("维护状态", maintainStatus.get(path.getStatus()).getName());
			map.put("启用", enabledType.get(path.getEnabled()).getName());
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}

	@Override
	public List<DictEntry> getDictItem(final TmPath eg) {
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmPath bean : (eg == null ? findAll() : findByEg(eg))) {
			final DictEntry dict = new DictEntry();
			dict.setCode(bean.getId().toString());
			dict.setName(bean.getNo() + ConstantUtils.STRING_ROD + bean.getName());
			dictList.add(dict);
		}
		return dictList;
	}

	/**
	 * 单表导入excel数据
	 * 
	 * @param workbook
	 * @author ryy
	 * @Time 2017/5/31
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(final Workbook workbook, HttpServletRequest req) {

		//覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		//回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

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
		// 产线Map
		final Map<String, TmLine> lineMap = new HashMap<String, TmLine>();
		//车间缺省时，判断产线是否属于指定工厂用Map
		final Map<String, TmLine> lineMapExWorkShop = new HashMap<String, TmLine>();
		for (final TmLine e : lineService.findAll()) {
			lineMap.put(e.getTmPlantId() + e.getTmWorkshopId() + e.getNo() + "-" + e.getNameCn(), e);
			lineMapExWorkShop.put(e.getTmPlantId() + e.getNo() + "-" + e.getNameCn(), e);
		}

		//物料Map
		final Map<String, TmPart> partMap = new HashMap<String, TmPart>();
		for (final TmPart e : partService.findAll()) {
			partMap.put(e.getTmPlantId() + e.getNo() + "-" + e.getNameCn(), e);
		}

		//系统中已存在工艺路径的map
		final Map<String, TmPath> pathMap = new HashMap<String, TmPath>();
		for (final TmPath e : findAll()) {
			pathMap.put(e.getTmPlantId() + e.getNo(), e);
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

		// 需要插入的工艺路径Map
		final List<TmPath> addList = new ArrayList<TmPath>();
		// 需要修改的工艺路径Map
		final Map<Integer, TmPath> updateMap = new HashMap<Integer, TmPath>();
		//格式错误的信息 
		final List<String> errorInfos = new ArrayList<String>();
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		//"第"
		final String DI = getMessage(req, "DI");
		String value = null;
		boolean existFlag;
		TmLine mapVal;
		Row row;
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			totalInt++;
			row = sheet.getRow(i); // 获得行
			int index = 0;
			existFlag = false;
			mapVal = null;
			TmPath entity = new TmPath();

			//================================== 工 厂 校 验 ======================================
			// 工厂编号
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PLANT_REQUIRED"));
				continue;
			}

			// 判断工厂是否存在
			if (!plantMap.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PLANT_NOT_EXIST"));
				continue;
			}
			// 判断工厂是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PLANT_NOT_ENABLED"));
				continue;
			}
			entity.setTmPlantId(plantMap.get(value).getId());

			//================================== 车 间 校 验 ======================================

			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				if (!workShopMap.containsKey(entity.getTmPlantId() + value)) {
					//车间不存在
					errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_WORKSHOP_NOT_EXIST"));
					continue;
				}

				// 判断车间是否已启用
				if (ConstantUtils.TYPE_CODE_ENABLED_OFF
						.equals(workShopMap.get(entity.getTmPlantId() + value).getEnabled())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_WORKSHOP_NOT_ENABLED"));
					continue;
				}
				entity.setTmWorkshopId(workShopMap.get(entity.getTmPlantId() + value).getId());
			}

			//================================== 产 线 校 验 ======================================

			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {

				if (StringUtils.isNotBlank(LoadUtils.getCell(row, index - 1))) {
					existFlag = lineMap.containsKey(entity.getTmPlantId() + entity.getTmWorkshopId() + value);
					mapVal = lineMap.get(entity.getTmPlantId() + entity.getTmWorkshopId() + value);
				} else {
					existFlag = lineMapExWorkShop.containsKey(entity.getTmPlantId() + value);
					mapVal = lineMapExWorkShop.get(entity.getTmPlantId() + value);
				}
				//判断工厂车间下的产线是否存在
				if (!existFlag) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_LINE_NOT_EXIST"));
					continue;
				}

				// 判断产线是否已启用
				if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(mapVal.getEnabled())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_LINE_NOT_ENABLED"));
					continue;
				}
				entity.setTmLineId(mapVal.getId());
			}

			//================================== 物 料 校 验 ======================================

			index++;
			value = LoadUtils.getCell(row, index).trim();
			//内容不为空时
			if (StringUtils.isNotBlank(value)) {
				//工厂下的物料是否存在
				if (!partMap.containsKey(entity.getTmPlantId() + value)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PART_NOT_EXIST"));
					continue;
				}

				// 判断物料是否已启用
				if (ConstantUtils.TYPE_CODE_ENABLED_OFF
						.equals(partMap.get(entity.getTmPlantId() + value).getEnabled())) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PART_NOT_ENABLED"));
					continue;
				}
				//判断物料是否零件
				if (!(ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED
						.equals(partMap.get(entity.getTmPlantId() + value).getType())
						|| ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED
								.equals(partMap.get(entity.getTmPlantId() + value).getType()))) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PART_CANNOT_PATHED"));
					continue;
				}
				entity.setTmPartId(partMap.get(entity.getTmPlantId() + value).getId());
			}

			//================================== 工 艺 路 径 校 验 ======================================
			//工艺路径编号
			index++;
			value = LoadUtils.getCell(row, index).trim();
			//内容是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PATH_NO_REQUIRED"));
				continue;
			}

			// 判断工艺路径编号的长度是否超过100位
			if (value.length() > 100) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PATH_NO_EXCEED_100"));
				continue;
			}
			// 判断工艺路径编号是否符合规则 编号只允许输入英文、数字、中划线-、下划线_和反斜杠/    [\\w\\d-_\\\\\\/]+
			if (StringUtils.isNotBlank(value) && (!Pattern.compile("[\\w-\\s]+$").matcher(value).matches()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PATH_NO_RULE"));
				continue;
			}
			entity.setNo(value);

			//工艺路径名称
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 判断工艺路径中文名称是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PATH_NAME_REQUIRED"));
				continue;
			}
			// 判断文本长度是否超过150位
			if (value.length() > 150) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_PATH_NAME_EXCEED_150"));
				continue;
			}
			entity.setName(value);

			//================================== 来 源 工 艺 路 径 校 验 ======================================

			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_COPY_PATH_MUST_BLANK"));
				continue;
			}

			//================================== 维 护 状 态 校 验 ======================================
			index++;
			value = LoadUtils.getCell(row, index).trim();
			// 判断启用是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_MAINTAIN_STATUS_REQUIRED"));
				continue;
			}
			// 判断启用是否合法
			if (!maintainStatusDict.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PATH_IMPORT_MAINTAIN_STATUS_VALUE_RULE"));
				continue;
			}
			entity.setStatus(maintainStatusDict.get(value));

			//================================== 是 否 启 用 校 验 ======================================

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

			//================================== 添 加 & 更 新  ======================================

			//新增还是更新，放入各自集合中
			if (pathMap.get(entity.getTmPlantId() + entity.getNo()) == null) {
				addList.add(entity);
			} else {
				updateMap.put(i + 1, entity);
			}

		}

		List<TmPath> updateList = needUpdateEntitys(updateMap);
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
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "PATH_MANAGEMENT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmPath> list, int num, String insert, StringBuffer countBuffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmPath>> parts = SpiltUtils.averageAssign(list, num);
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

	private List<TmPath> needUpdateEntitys(Map<Integer, TmPath> updatePathMap) {
		List<TmPath> updateList = new ArrayList<TmPath>();
		for (Integer key : updatePathMap.keySet()) {
			TmPath insert = updatePathMap.get(key);
			TmPath path = new TmPath();
			path.setTmPlantId(insert.getTmPlantId());
			path.setNo(insert.getNo());
			TmPath newData = findUniqueByEg(path);
			newData.setTmWorkshopId(insert.getTmWorkshopId());
			newData.setTmLineId(insert.getTmLineId());
			newData.setTmPartId(insert.getTmPartId());
			newData.setName(insert.getName());
			newData.setStatus((insert.getStatus()));
			newData.setEnabled(insert.getEnabled());
			updateList.add(newData);
		}
		return updateList;
	}

	@Override
	public TmPath doDuplicate(TmPath bean, String messags) throws Exception {
		try {
			Integer tmPathid=bean.getId(); //得到被复制的基础id
			List<TmPathUloc> tmPathUlocList=pathUlocService.getPathUlocs(tmPathid); //根据id得到基础数据工艺路径下所有的站点
			bean.setId(null);
			bean.setCreateTime(DateUtils.getCurrentDate());
			bean=dao.save(bean);
			StringBuffer sb=new StringBuffer();
			for(TmPathUloc tmPathUloc:tmPathUlocList){
				sb.append(tmPathUloc.getId()+",");
				Integer tmPathUlocId=tmPathUloc.getId();
				tmPathUloc.setId(null);  //将id设置为null，插入数据时id自增长
				tmPathUloc.setTmPathId(bean.getId());
				tmPathUloc.setCreateTime(DateUtils.getCurrentDate()); //设置新增时间
				tmPathUloc=pathUlocService.doSave(tmPathUloc);
				
				QueryCriteria queryCriteria=new QueryCriteria();
				queryCriteria.getQueryCondition().put("tmPathUlocId", tmPathUlocId.toString());
				queryCriteria.setQueryRelationEntity(false);
				queryCriteria.setQueryPage(false);
				PageResult<TmPathUlocParameter> resultList=pathUlocParameterService.findBy(queryCriteria);
				List<TmPathUlocParameter> paramList=resultList.getContent();
				for (TmPathUlocParameter parameter:paramList) {
					parameter.setId(null);
					parameter.setTmPathUlocId(tmPathUloc.getId());
					parameter.setCreateTime(DateUtils.getCurrentDate()); //设置新增时间
				}
				pathUlocParameterService.doSaveBatch(paramList);
			} 
			return bean;
		
			/*
			zhuzw 2018/3/22 工艺路径复制功能的老逻辑，暂时注释
			TmPath path = findById(bean.getId());//工艺路径
			path.setNo(bean.getNo());
			path.setName(bean.getName());
			path.setStatus(ConstantUtils.MAINTAIN_STATUS_REPAIR);
			path.setCopyPathId(path.getId());
			path = doSave(path);
			List<TmPathUloc> pathUlocs = pathUlocService.getPathUlocs(bean.getId());
			// key为id，value为seq
			Map<String, String> idSeqMapOld = new HashMap<String, String>();
			//key seq，value id
			Map<String, Integer> seqIdMapOld = new HashMap<String, Integer>();
			for (TmPathUloc pathUloc : pathUlocs) {
				pathUloc.setTmPathId(path.getId());
				idSeqMapOld.put(pathUloc.getId().toString(), pathUloc.getSeq());
				seqIdMapOld.put(pathUloc.getSeq(), pathUloc.getId());
			}
			pathUlocService.doSaveBatch(pathUlocs);
			//工艺路径线的信息
			doCopyPathLigature(bean.getId(), path.getId());
			// key 为Seq ，value id
			Map<String, String> seqIdMapNew = new HashMap<String, String>();
			for (TmPathUloc tmPathUloc : pathUlocs) {
				seqIdMapNew.put(tmPathUloc.getSeq(), tmPathUloc.getId().toString());
			}
			for (TmPathUloc tmPathUloc : pathUlocs) {
				doCopyUlocParts(seqIdMapOld.get(tmPathUloc.getSeq()), tmPathUloc.getId());//保存关键件信息
				doCopyUlocParameter(seqIdMapOld.get(tmPathUloc.getSeq()), tmPathUloc.getId());//保存参数信息
				doCopyUlocSip(seqIdMapOld.get(tmPathUloc.getSeq()), tmPathUloc.getId());//保存质检项
				if (StringUtil.isNotBlank(tmPathUloc.getParentId())) {

					String[] parents = tmPathUloc.getParentId().split(",");
					StringBuffer parentsIdBuffer = new StringBuffer();
					for (String parent : parents) {
						// 通过原parentId找到当时的seq，再根据seq找到parentId
						parentsIdBuffer.append(seqIdMapNew.get(idSeqMapOld.get(parent))).append(",");
					}
					if (parentsIdBuffer.length() > 0) {
						parentsIdBuffer.deleteCharAt(parentsIdBuffer.length() - 1);
					}
					tmPathUloc.setParentId(parentsIdBuffer.toString());
				}
			}
			pathUlocService.doUpdateBatch(pathUlocs);
			return path;*/
		} catch (Exception e) {
			String errMsg = e.getMessage();
			if (errMsg.indexOf(messags) != -1) {
				throw new BusinessException("PATH_NO_UNIQUE_ERROR");
			} else {
				throw e;
			}
		}

	}

	/**
	 * 拷贝参数信息
	 * 
	 * @param oldPathUlocId
	 * @param newPathUlocId
	 */
	@SuppressWarnings("unused")
	private void doCopyUlocParameter(Integer oldPathUlocId, Integer newPathUlocId) {
		List<TmPathUlocParameter> list = pathUlocParameterService.getTmPathUlocParameters(oldPathUlocId);
		for (TmPathUlocParameter bean : list) {
			bean.setTmPathUlocId(newPathUlocId);
		}
		pathUlocParameterService.doSaveBatch(list);
	}

	/**
	 * 拷贝质检项信息
	 * 
	 * @param oldPathUlocId
	 * @param newPathUlocId
	 */
	@SuppressWarnings("unused")
	private void doCopyUlocSip(Integer oldPathUlocId, Integer newPathUlocId) {
		List<TmPathUlocSip> list = pathUlocSipService.getPathUloSips(oldPathUlocId);
		for (TmPathUlocSip bean : list) {
			bean.setTmPathUlocId(newPathUlocId);
		}
		pathUlocSipService.doSaveBatch(list);
	}

	/**
	 * 拷贝关键件信息
	 * 
	 * @param oldPathUlocId
	 * @param newPathUlocId
	 */
	@SuppressWarnings("unused")
	private void doCopyUlocParts(Integer oldPathUlocId, Integer newPathUlocId) {
		List<TmPathUlocPart> pathUlocParts = pathUlocPartService.getPathUlocParts(oldPathUlocId);
		for (TmPathUlocPart tmPathUlocPart : pathUlocParts) {
			tmPathUlocPart.setTmPathUlocId(newPathUlocId);
		}
		pathUlocPartService.doSaveBatch(pathUlocParts);
	}

	/**
	 * 保存线的信息
	 * 
	 * @param oldPathId
	 * @param newPathId
	 */
	@SuppressWarnings("unused")
	private void doCopyPathLigature(Integer oldPathId, Integer newPathId) {
		TmPathLigature eg = new TmPathLigature();
		eg.setTmPathId(oldPathId);
		List<TmPathLigature> pathLigature = pathLigatureService.findByEg(eg);
		for (TmPathLigature tmPathLigature : pathLigature) {
			tmPathLigature.setTmPathId(newPathId);
		}
		pathLigatureService.doSaveBatch(pathLigature);
	}

	@Override
	public List<TmPath> getPathByPlantAndParts(Integer tmPlantId, Integer tmPartId) {
		return ((TmPathDao) dao).getPathByPlantAndParts(tmPlantId, tmPartId);
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
	public TmPath getByPathUlocId(Integer tmPathUlocId) {
		return ((TmPathDao) dao).getByPathUlocId(tmPathUlocId);
	}

	@Override
	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmPath> tmPathList,
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

		Map<String, String> maintainStatus = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS)) {
			maintainStatus.put(e.getCode(), e.getName());
		}

		Map<String, String> yesOrNo = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO)) {
			yesOrNo.put(e.getCode(), e.getName());
		}

		for (TmPath tmPath : tmPathList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(parentHeads[0], tmPath.getId());
			map.put(parentHeads[1], tmPath.getPlant().getNo() + "-" + tmPath.getPlant().getNameCn());
			map.put(parentHeads[2], tmPath.getWorkshop().getNo() == null ? ""
					: (tmPath.getWorkshop().getNo() + "-" + tmPath.getWorkshop().getNameCn()));
			map.put(parentHeads[3], tmPath.getLine().getNo() == null ? ""
					: (tmPath.getLine().getNo() + "-" + tmPath.getLine().getNameCn()));
			map.put(parentHeads[4], tmPath.getPart().getNo() == null ? ""
					: (tmPath.getPart().getNo() + "-" + tmPath.getPart().getNameCn()));
			map.put(parentHeads[5], tmPath.getNo());
			map.put(parentHeads[6], tmPath.getName());
			map.put(parentHeads[7], tmPath.getCopyPath().getNo() == null ? ""
					: (tmPath.getCopyPath().getNo() + "-" + tmPath.getCopyPath().getName()));
			map.put(parentHeads[8], maintainStatus.get(tmPath.getStatus()));
			map.put(parentHeads[9], enabled.get(tmPath.getEnabled()));

			List<TmPathUloc> tmPathUlocs = this.concordancyResult(pathUlocService.findByForginKey(tmPath.getId()));
			List<Map<String, Object>> childExportList = new ArrayList<Map<String, Object>>();

			for (TmPathUloc pathUloc : tmPathUlocs) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put(childHeads[0], pathUloc.getSeq());
				childMap.put(childHeads[1], pathUloc.getUloc().getNo() + "-" + pathUloc.getUloc().getName());
				childMap.put(childHeads[2], getValue(pathUloc.getParentId()));
				childMap.put(childHeads[3], getValue(pathUloc.getParentId()));
				childMap.put(childHeads[4], getValue(pathUloc.getIntervalTime()));
				childMap.put(childHeads[5], getValue(yesOrNo.get(pathUloc.getIsOnline())));
				childMap.put(childHeads[6], getValue(yesOrNo.get(pathUloc.getIsOffline())));
				childMap.put(childHeads[7], getValue(yesOrNo.get(pathUloc.getIsSip())));
				childMap.put(childHeads[8], getValue(yesOrNo.get(pathUloc.getIsAutoInstorage())));
				childMap.put(childHeads[9], getValue(pathUloc.getInstorageTime()));
				childMap.put(childHeads[10], getValue(yesOrNo.get(pathUloc.getIsReported())));
				childMap.put(childHeads[11], getValue(pathUloc.getErpUlocCode()));
				childMap.put(childHeads[12], getValue(pathUloc.getNote()));
				childExportList.add(childMap);
			}
			childExportMap.put(tmPath.getId(), childExportList);
			parentExportList.add(map);
		}
		result = BaseExcelUtil.exportDataAll(response, parentExportList, parentHeads, childExportMap, childHeads,
				filePath);
		return result;
	}

	//整合空值的展现形式
	private String getValue(Object obj) {
		return (obj == null) ? "" : (obj.toString());
	}

	/**
	 * 整合站点各属性的展现形式
	 * 
	 * @param pathUlocs
	 * @return
	 */
	private List<TmPathUloc> concordancyResult(List<TmPathUloc> pathUlocs) {
		for (TmPathUloc tmPathUloc : pathUlocs) {
			if (tmPathUloc.getParentId() != null) {
				List<String> asList = Arrays.asList(tmPathUloc.getParentId().split(","));
				StringBuffer ulocNos = new StringBuffer("[");
				for (TmPathUloc ppathUloc : pathUlocs) {
					if (asList.contains(ppathUloc.getId().toString())) {
						ulocNos.append(ppathUloc.getSeq() + "_" + ppathUloc.getUloc().getNo()).append(",");
					}
				}
				ulocNos.deleteCharAt(ulocNos.length() - 1);
				if (ulocNos.length() > 0) {
					ulocNos.append("]");
				}
				tmPathUloc.setParentId(ulocNos.toString());
			}
			changeTimeMsToMin(tmPathUloc);
		}
		return pathUlocs;
	}

	/**
	 * 把时间毫秒转成分钟
	 * 
	 * @param bean
	 */
	private void changeTimeMsToMin(TmPathUloc bean) {
		if (bean.getInstorageTime() != null) {
			bean.setInstorageTime(bean.getInstorageTime().longValue() / 1000 / 60);
		}
		if (bean.getIntervalTime() != null) {
			bean.setIntervalTime(bean.getIntervalTime().longValue() / 1000 / 60);
		}
		if (bean.getOperateTime() != null) {
			bean.setOperateTime(bean.getOperateTime().longValue() / 1000 / 60);
		}
	}

	@Override
	public TmPath doSavePathUlocRaphData(String data, Integer tmPathId, Integer tmPlantId, Integer tmWorkshopId,
			Integer tmLineId, String partArray, String parameterArray, String sipArray, Integer[] deleteTmPathUlocIds) {
		JSONObject dataObject = JSONObject.fromObject(data);
		//工艺路径属性
		TmPath pathBean = getTmPathBean(tmPathId, JSONObject.fromObject(dataObject.get("props")).get("props"),
				tmPlantId, tmWorkshopId, tmLineId);

		//工艺路径站点信息
		Map<String, List<TmPathUloc>> pathUlocData = getPathUlocData(pathBean.getId(), dataObject.get("states"));
		List<TmPathUloc> pathUlocAddList = pathUlocData.get("AddList");//新增的站点
		List<TmPathUloc> pathUlocUpdateList = pathUlocData.get("UpdateList");//需要修改的站点信息
		//新增和修改的站点的集合
		List<TmPathUloc> pathUlocList = new ArrayList<TmPathUloc>();
		pathUlocList.addAll(pathUlocAddList);
		pathUlocList.addAll(pathUlocUpdateList);

		//线的信息 
		List<TmPathLigature> pathPathsList = getPathPathsData(pathBean.getId(), dataObject.get("paths"));

		Map<String, Map<String, List<String>>> fromAndToMap = getFromAndToMap(pathPathsList);
		checkPathUloc(pathUlocList, fromAndToMap);

		if (tmPathId == null) {//新增
			pathUlocService.doSaveBatch(pathUlocList);
			doUpdateParentIdsAndSaveDetailData(pathUlocList, fromAndToMap, partArray, parameterArray, sipArray, "add");
			pathLigatureService.doSaveBatch(pathPathsList);
		} else {//修改
			pathLigatureService.doDeleteAllPathsByTmPathId(tmPathId);//删除所有连线
			pathUlocService.doDeleteById(deleteTmPathUlocIds);
			pathUlocService
					.doDeletePathUlocChilds(Arrays.toString(deleteTmPathUlocIds).replace("[", "").replace("]", ""));//根据删除的tmPathUlocId 删除所有子信息
			pathUlocService.doSaveBatch(pathUlocAddList);
			pathUlocService.doUpdateBatch(pathUlocUpdateList);

			doUpdateParentIdsAndSaveDetailData(pathUlocList, fromAndToMap, partArray, parameterArray, sipArray,
					"update");

			pathLigatureService.doSaveBatch(pathPathsList);
		}
		return pathBean;
	}

	/**
	 * 修改工艺路径站点父节点信息，并且保存站点子信息
	 * 
	 * @param pathUlocList
	 * @param fromAndToMap
	 * @param partArray
	 *            关键件
	 * @param parameterArray
	 *            参数
	 * @param sipArray
	 *            质检项
	 */
	private void doUpdateParentIdsAndSaveDetailData(List<TmPathUloc> pathUlocList,
			Map<String, Map<String, List<String>>> fromAndToMap, String partArray, String parameterArray,
			String sipArray, String type) {
		//关键件
		Map<String, List<TmPathUlocPart>> pathUlocPartMap = jsonArrayDataToBeanMap(partArray, TmPathUlocPart.class);
		List<TmPathUlocPart> pathUlocPartList = new ArrayList<TmPathUlocPart>();

		//参数
		Map<String, List<TmPathUlocParameter>> pathUlocParameterMap = jsonArrayDataToBeanMap(parameterArray,
				TmPathUlocParameter.class);
		List<TmPathUlocParameter> pathUlocParameterList = new ArrayList<TmPathUlocParameter>();

		//质检项
		Map<String, List<TmPathUlocSip>> pathUlocSipMap = jsonArrayDataToBeanMap(sipArray, TmPathUlocSip.class);
		List<TmPathUlocSip> pathUlocSipList = new ArrayList<TmPathUlocSip>();

		//key rectSeq：节点ID
		Map<String, TmPathUloc> map = new HashMap<String, TmPathUloc>();
		for (TmPathUloc bean : pathUlocList) {
			map.put(bean.getRectSeq(), bean);

			//关键件
			if (pathUlocPartMap.containsKey(bean.getRectSeq())) {
				List<TmPathUlocPart> partList = pathUlocPartMap.get(bean.getRectSeq());
				for (TmPathUlocPart part : partList) {
					part.setTmPathUlocId(bean.getId());
				}
				pathUlocPartList.addAll(partList);
			}

			//参数
			if (pathUlocParameterMap.containsKey(bean.getRectSeq())) {
				List<TmPathUlocParameter> parameterList = pathUlocParameterMap.get(bean.getRectSeq());
				for (TmPathUlocParameter tmPathUlocParameter : parameterList) {
					tmPathUlocParameter.setTmPathUlocId(bean.getId());
				}
				pathUlocParameterList.addAll(parameterList);
			}

			//质检项
			if (pathUlocSipMap.containsKey(bean.getRectSeq())) {
				bean.setIsSip(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
				List<TmPathUlocSip> sipList = pathUlocSipMap.get(bean.getRectSeq());
				for (TmPathUlocSip tmPathUlocSip : sipList) {
					tmPathUlocSip.setTmPathUlocId(bean.getId());
				}
				pathUlocSipList.addAll(sipList);
			} else {
				if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.contains(bean.getIsSip())) {//并且是新增的时
					if ("add".equals(type)) {
						throw new BusinessException("PATH_ULOC_IS_SIP_NOT_PARAMETER_ERROR",
								ulocService.getUlocById(bean.getTmUlocId()).getNo());
					} else {
						List<TmPathUlocSip> pathUloSips = pathUlocSipService.getPathUloSips(bean.getId());
						if (pathUloSips.size() == 0) {
							throw new BusinessException("PATH_ULOC_IS_SIP_NOT_PARAMETER_ERROR",
									ulocService.getUlocById(bean.getTmUlocId()).getNo());
						}
					}

				}
			}
		}
		//检查工位的子节点是否有相同项
		checkUlocSonAndParentIsSame(fromAndToMap, map);

		//key:子节点;value:父节点集合
		Map<String, List<String>> toFromMap = fromAndToMap.get("toFromMap");
		for (Map.Entry<String, List<String>> entry : toFromMap.entrySet()) {
			List<String> rectSeqs = entry.getValue();//父节点
			TmPathUloc bean = map.get(entry.getKey());
			for (String rectSeq : rectSeqs) {
				Integer id = map.get(rectSeq).getId();
				if (StringUtil.isNotBlank(bean.getParentId())) {
					bean.setParentId(bean.getParentId() + "," + id);
				} else {
					bean.setParentId(id.toString());
				}
			}
		}
		pathUlocService.doUpdateBatch(pathUlocList);
		pathUlocPartService.doSaveBatch(pathUlocPartList);
		pathUlocParameterService.doSaveBatch(pathUlocParameterList);
		pathUlocSipService.doSaveBatch(pathUlocSipList);
	}

	/**
	 * 检查工位的子节点是否有相同项
	 * 
	 * @param fromToMap
	 *            key:父节点DivId,Value:子节点DivId集合
	 * @param ulocMap
	 *            key DivId,Value 节点对象
	 */
	private void checkUlocSonAndParentIsSame(Map<String, Map<String, List<String>>> fromAndToMap,
			Map<String, TmPathUloc> ulocMap) {

		Map<String, List<String>> fromToMap = fromAndToMap.get("fromToMap");
		for (Map.Entry<String, List<String>> entry : fromToMap.entrySet()) {//校验子信息有没有重复项
			List<String> sons = entry.getValue();
			Set<Integer> sonsIds = new HashSet<Integer>();
			for (String string : sons) {
				sonsIds.add(ulocMap.get(string).getTmUlocId());
			}
			if (sons.size() > sonsIds.size()) {
				TmUloc uloc = ulocService.getUlocById(ulocMap.get(entry.getKey()).getTmUlocId());
				throw new BusinessException("PATH_ULOC_SONS_SAME_ERROR", uloc.getNo() + "-" + uloc.getName());
			}
		}

		Map<String, List<String>> toFromMap = fromAndToMap.get("toFromMap");
		for (Map.Entry<String, List<String>> entry : toFromMap.entrySet()) {//检验父信息有没有重复项
			List<String> parents = entry.getValue();
			Set<Integer> parentIds = new HashSet<Integer>();
			for (String parent : parents) {
				parentIds.add(ulocMap.get(parent).getTmUlocId());
			}
			if (parents.size() > parentIds.size()) {
				TmUloc uloc = ulocService.getUlocById(ulocMap.get(entry.getKey()).getTmUlocId());
				throw new BusinessException("PATH_ULOC_SONS_SAME_ERROR", uloc.getNo() + "-" + uloc.getName());
			}
		}

	}

	/**
	 * 把jsonArray 转化成 Map
	 * 
	 * @param <E>
	 * 
	 * @param jsonArrayStr
	 * @param clazz
	 * @return key:rectSeq,value List<对象>
	 */
	private <E> Map<String, List<E>> jsonArrayDataToBeanMap(String jsonArrayStr, Class<?> clazz) {
		Map<String, List<E>> map = new HashMap<String, List<E>>();
		JSONArray jsonArray = JSONArray.fromObject(jsonArrayStr);
		for (Object object : jsonArray) {
			JSONObject jsonObject = JSONObject.fromObject(object);
			@SuppressWarnings("unchecked")
			List<E> list = (List<E>) JSONArray.toCollection(JSONArray.fromObject(jsonObject.get("value")), clazz);
			map.put(jsonObject.getString("key"), list);
		}
		return map;
	}

	/**
	 * 校验工艺路径节点信息
	 * 
	 * @param pathUlocList
	 * @param pathPathsList
	 */
	private void checkPathUloc(List<TmPathUloc> pathUlocList, Map<String, Map<String, List<String>>> fromAndToMap) {
		Map<String, List<String>> fromToMap = fromAndToMap.get("fromToMap");
		Map<String, List<String>> toFromMap = fromAndToMap.get("toFromMap");

		checkIsHaveNoLigatureUloc(pathUlocList, fromToMap, toFromMap);//检查未连线的站点
		checkUlocSonsIsSame(fromToMap);//检查当前站点的多个子节点是否相同

		Map<String, Boolean> isSelectOnlineAndOffline = isSelectOnlineAndOffline(pathUlocList);

		for (TmPathUloc bean : pathUlocList) {
			if (!toFromMap.containsKey(bean.getRectSeq())) {
				if (!isSelectOnlineAndOffline.get("isOnline")) {//没有上线点
					bean.setIsOnline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
				}
			}
			if (!fromToMap.containsKey(bean.getRectSeq())) {
				if (!isSelectOnlineAndOffline.get("isOffline")) {//没有下线点
					bean.setIsOffline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
				}
			}
		}

		isSelectOnlineAndOffline = isSelectOnlineAndOffline(pathUlocList);
		if (!isSelectOnlineAndOffline.get("isOnline")) {//没有上线点
			throw new BusinessException("PATH_ULOC_NO_ONLINE_ERROR");
		}
		if (!isSelectOnlineAndOffline.get("isOffline")) {//没有下线点
			throw new BusinessException("PATH_ULOC_NO_OFFLINE_ERROR");
		}

		Collections.sort(pathUlocList, new Comparator<TmPathUloc>() {

			@Override
			public int compare(TmPathUloc o1, TmPathUloc o2) {
				if (o1.getIsOnline().equals(o1.getIsOffline())) {
					return o1.getIsOffline().compareTo(o2.getIsOffline());
				} else {
					return o2.getIsOnline().compareTo(o1.getIsOnline());
				}
			}
		});
		int seq = 1;
		for (TmPathUloc bean : pathUlocList) {
			bean.setSeq(String.valueOf(seq++));
		}
	}

	private void checkUlocSonsIsSame(Map<String, List<String>> fromToMap) {
		for (Map.Entry<String, List<String>> entry : fromToMap.entrySet()) {
			List<String> list = entry.getValue();
			if (list.size() <= 1) {
				continue;
			} else {

			}
		}

	}

	/**
	 * 校验是否有未连线的站点
	 * 
	 * @param pathUlocList
	 * @param fromToMap
	 * @param toFromMap
	 */
	private void checkIsHaveNoLigatureUloc(List<TmPathUloc> pathUlocList, Map<String, List<String>> fromToMap,
			Map<String, List<String>> toFromMap) {
		if (pathUlocList.size() == 1) {//只有一个站点，是上线点又是下线点
			for (TmPathUloc bean : pathUlocList) {
				bean.setIsOnline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
				bean.setIsOffline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
			}
			return;
		}

		for (TmPathUloc bean : pathUlocList) {
			if (!fromToMap.containsKey(bean.getRectSeq()) && !toFromMap.containsKey(bean.getRectSeq())) {
				TmUloc uloc = ulocService.findById(bean.getTmUlocId());
				throw new BusinessException("PATH_ULOC_HAVE_NO_PATH", uloc.getNo() + "-" + uloc.getName());//有没连线的点
			}
		}

	}

	/**
	 * 是否选择上线点和下线点
	 * 
	 * @param pathUlocList
	 * @return
	 */
	private Map<String, Boolean> isSelectOnlineAndOffline(List<TmPathUloc> pathUlocList) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("isOnline", false);
		map.put("isOffline", false);
		for (TmPathUloc bean : pathUlocList) {
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOnline())) {
				map.put("isOnline", true);
			}
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOffline())) {
				map.put("isOffline", true);
			}
		}
		return map;
	}

	/**
	 * 工艺路径节点信息
	 * 
	 * @param tmPathId
	 * @param object
	 * @return
	 */
	private Map<String, List<TmPathUloc>> getPathUlocData(Integer tmPathId, Object object) {
		Map<String, List<TmPathUloc>> returnMap = new HashMap<String, List<TmPathUloc>>();
		List<TmPathUloc> pathUlocAddList = new ArrayList<TmPathUloc>();
		List<TmPathUloc> pathUlocUpdateList = new ArrayList<TmPathUloc>();
		JSONObject states = JSONObject.fromObject(object);
		for (Object key : states.keySet()) {
			JSONObject rect = JSONObject.fromObject(states.get(key));
			JSONObject props = JSONObject.fromObject(rect.get("props"));

			TmPathUloc pathUloc = new TmPathUloc();
			if (StringUtil.isNotBlank(props.getJSONObject("tmPathUlocId").getString("value"))) {
				pathUloc.setId(props.getJSONObject("tmPathUlocId").getInt("value"));
			}
			pathUloc.setTmPathId(tmPathId);
			pathUloc.setTmUlocId(props.getJSONObject("ulocId").getInt("value"));
			pathUloc.setParentId("");
			checkIsNumber(props.getJSONObject("ulocNo").getString("value"),
					props.getJSONObject("operateTime").getString("value"),
					props.getJSONObject("intervalTime").getString("value"),
					props.getJSONObject("inStroageTime").getString("value"), pathUloc);

			pathUloc.setIsSip(props.getJSONObject("isSip").getString("value"));
			pathUloc.setIsAutoInstorage(props.getJSONObject("isAutoInstorage").getString("value"));
			pathUloc.setIsReported(props.getJSONObject("isReported").getString("value"));
			pathUloc.setErpUlocCode(props.getJSONObject("erpUlocCode").getString("value"));
			pathUloc.setNote(props.getJSONObject("note").getString("value"));
			if (StringUtil.isBlank(props.getJSONObject("isOnline").getString("value"))) {
				pathUloc.setIsOnline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
			} else {
				pathUloc.setIsOnline(props.getJSONObject("isOnline").getString("value"));
			}

			if (StringUtil.isBlank(props.getJSONObject("isOffline").getString("value"))) {
				pathUloc.setIsOffline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
			} else {
				pathUloc.setIsOffline(props.getJSONObject("isOffline").getString("value"));
			}

			JSONObject attrJSON = JSONObject.fromObject(rect.get("attr"));
			pathUloc.setX(attrJSON.getString("x"));
			pathUloc.setY(attrJSON.getString("y"));
			pathUloc.setWidth(attrJSON.getString("width"));
			pathUloc.setHeight(attrJSON.getString("height"));
			pathUloc.setRectSeq(key.toString());
			if (pathUloc.getId() == null) {
				pathUlocAddList.add(pathUloc);
			} else {
				pathUlocUpdateList.add(pathUloc);
			}
		}
		returnMap.put("AddList", pathUlocAddList);
		returnMap.put("UpdateList", pathUlocUpdateList);
		return returnMap;
	}

	/**
	 * 校验分钟数是不是合法的数字
	 * 
	 * @param uloc
	 * @param operateTime
	 * @param intervalTime
	 * @param inStroageTime
	 * @param pathUloc
	 */
	private void checkIsNumber(String uloc, String operateTime, String intervalTime, String inStroageTime,
			TmPathUloc pathUloc) {
		Pattern pattern = Pattern.compile("[0-9]*");

		if (StringUtil.isNotBlank(operateTime)) {
			Matcher isNum = pattern.matcher(operateTime);
			if (!isNum.matches()) {
				throw new BusinessException("PATH_ULOC_OPERATETIME_ERROR", uloc);
			} else {
				pathUloc.setOperateTime(Long.valueOf(operateTime) * 1000 * 60);
			}
		}

		if (StringUtil.isNotBlank(intervalTime)) {
			Matcher isNum = pattern.matcher(intervalTime);
			if (!isNum.matches()) {
				throw new BusinessException("PATH_ULOC_INTERVALTIME_ERROR", uloc);
			} else {
				pathUloc.setIntervalTime(Long.valueOf(intervalTime) * 1000 * 60);
			}

		}
		if (StringUtil.isNotBlank(inStroageTime)) {
			Matcher isNum = pattern.matcher(inStroageTime);
			if (!isNum.matches()) {
				throw new BusinessException("PATH_ULOC_INSTROAGETIME_ERROR", uloc);
			} else {
				pathUloc.setInstorageTime(Long.valueOf(inStroageTime) * 1000 * 60);
			}
		}
	}

	/**
	 * 站点之间的连线信息
	 * 
	 * @param tmPathId
	 * @param object
	 * @return
	 */
	private List<TmPathLigature> getPathPathsData(Integer tmPathId, Object object) {
		List<TmPathLigature> pathPathsList = new ArrayList<TmPathLigature>();
		JSONObject paths = JSONObject.fromObject(object);
		for (Object key : paths.keySet()) {
			JSONObject props = JSONObject.fromObject(paths.get(key));
			TmPathLigature bean = new TmPathLigature();
			bean.setTmPathId(tmPathId);
			bean.setNo(key.toString());
			bean.setFromDiv(props.getString("from"));
			bean.setToDiv(props.getString("to"));
			bean.setDots(props.getJSONArray("dots").toString());
			bean.setText(props.getJSONObject("text").toString());
			bean.setTextPos(props.getJSONObject("textPos").toString());
			pathPathsList.add(bean);
		}
		return pathPathsList;
	}

	private TmPath getTmPathBean(Integer tmPathId, Object props, Integer tmPlantId, Integer tmWorkshopId,
			Integer tmLineId) {
		JSONObject object = JSONObject.fromObject(props);
		TmPath bean = new TmPath();
		bean.setId(tmPathId);
		bean.setTmPlantId(tmPlantId);
		bean.setTmWorkshopId(tmWorkshopId);
		bean.setTmLineId(tmLineId);
		String tmPartId = JSONObject.fromObject(object.get("part")).getString("value");
		bean.setTmPartId(StringUtil.isNotBlank(tmPartId) ? Integer.valueOf(tmPartId) : null);
		String no = JSONObject.fromObject(object.get("pathNo")).getString("value");
		String name = JSONObject.fromObject(object.get("pathName")).getString("value");
		if (StringUtil.isBlank(no) || StringUtil.isBlank(name)) {
			throw new BusinessException("PATN_NO_OR_NAME_NULL_ERROR");
		}
		if (StringUtils.isNotBlank(no) && (!Pattern.compile("[\\w-\\s]+$").matcher(no).matches()
				|| Pattern.compile("[\u4e00-\u9fa5]").matcher(no).find())) {
			throw new BusinessException("PATH_NO_MATCH_ERROR");
		}
		bean.setNo(no);
		bean.setName(name);
		bean.setStatus(ConstantUtils.MAINTAIN_STATUS_REPAIR);
		bean.setEnabled(JSONObject.fromObject(object.get("enabled")).getString("value"));
		return this.doSaveOrUpdate(bean);
	}

	private Map<String, Map<String, List<String>>> getFromAndToMap(List<TmPathLigature> pathPathsList) {
		Map<String, Map<String, List<String>>> map = new HashMap<String, Map<String, List<String>>>();
		Map<String, List<String>> fromToMap = new HashMap<String, List<String>>();
		Map<String, List<String>> toFromMap = new HashMap<String, List<String>>();
		for (TmPathLigature path : pathPathsList) {
			if (fromToMap.containsKey(path.getFromDiv())) {
				List<String> list = fromToMap.get(path.getFromDiv());
				list.add(path.getToDiv());
			} else {
				List<String> list = new ArrayList<String>();
				list.add(path.getToDiv());
				fromToMap.put(path.getFromDiv(), list);
			}

			if (toFromMap.containsKey(path.getToDiv())) {
				List<String> list = toFromMap.get(path.getToDiv());
				list.add(path.getFromDiv());
			} else {
				List<String> list = new ArrayList<String>();
				list.add(path.getFromDiv());
				toFromMap.put(path.getToDiv(), list);
			}
		}
		map.put("fromToMap", fromToMap);
		map.put("toFromMap", toFromMap);
		return map;
	}

	@Override
	public void doDeletePathUlocParam(Integer[] tmPathIds) {
		List<Map<String,Object>> pathUlocList=pathUlocService.getTmPathIdsByTmPathUlocIds(tmPathIds);
		if(StringUtil.isNotEmpty(pathUlocList)){
			Integer[] pathUlocIds=new Integer[pathUlocList.size()];
			for(int i=0,c=pathUlocList.size();i<c;i++){
				pathUlocIds[i]=Integer.valueOf(pathUlocList.get(i).get("ID").toString());
			}
			pathUlocService.doDeletePathUlocOrParam(pathUlocIds);
		}
		dao.deleteByBatch(tmPathIds);
	}

}
