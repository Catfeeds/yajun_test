package com.wis.mes.production.plan.porder.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.path.entity.TmPathUloc;
import com.wis.mes.master.path.service.TmPathUlocService;
import com.wis.mes.production.plan.porder.dao.ToPorderAviPathDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPath;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathParameterService;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathPartService;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathService;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathSipService;
import com.wis.mes.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ToPorderAviPathServiceImpl extends BaseServiceImpl<ToPorderAviPath> implements ToPorderAviPathService {

	@Autowired
	public void setDao(ToPorderAviPathDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TmPathUlocService pathUlocService;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private ToPorderAviPathSipService aviPathSipService;
	@Autowired
	private ToPorderAviPathParameterService aviPathParameterService;
	@Autowired
	private ToPorderAviPathPartService aviPartService;

	@Override
	public void doSavePath(Integer aviId, Integer tmPathId) {
		List<TmPathUloc> pathUlocs = pathUlocService.getPathUlocs(tmPathId);
		//KEY为ID,value为SEQ
		Map<String, String> pathUlocIdSeqMap = getPathUlocIdSeqMap(pathUlocs);
		List<ToPorderAviPath> aviPaths = new ArrayList<ToPorderAviPath>();
		for (TmPathUloc pathUloc : pathUlocs) {
			ToPorderAviPath aviPath = new ToPorderAviPath();
			aviPath.setToPorderAviId(aviId);
			aviPath.setSeq(pathUloc.getSeq());//顺序号	
			aviPath.setTmUlocId(pathUloc.getTmUlocId());//工位
			aviPath.setIsOnline(pathUloc.getIsOnline());//是否上线点
			aviPath.setIsOffline(pathUloc.getIsOffline());//是否下线点
			aviPath.setParentId(pathUloc.getParentId());//上一节点集合
			aviPath.setOperateTime(pathUloc.getOperateTime());//操作时间
			aviPath.setIntervalTime(pathUloc.getIntervalTime());//间隔下线时间
			aviPath.setIsSip(pathUloc.getIsSip());//是否质检点
			aviPath.setIsAutoInstorage(pathUloc.getIsAutoInstorage());//是否自动入库
			aviPath.setInstorageTime(pathUloc.getInstorageTime());//入库等待时间
			aviPath.setIsReported(pathUloc.getIsReported());//是否报工点
			aviPath.setErpUlocCode(pathUloc.getErpUlocCode());//对应ERPcode
			aviPath.setNote(pathUloc.getNote());//备注
			aviPaths.add(aviPath);
			ToPorderAviPath doSave = doSave(aviPath);
			//保存质检项信息
			aviPathSipService.doSaveAviPathSip(doSave.getId(), pathUloc.getId());
			//保存参数信息
			aviPathParameterService.doSaveAviPathParameter(doSave.getId(), pathUloc.getId());
			//保存关键件信息
			aviPartService.doSaveAviPart(doSave.getId(), pathUloc.getId());
		}
		//把对应的parentId修改回来
		Map<String, String> aviPathSeqIdMap = getAviPathsSeqId(aviPaths);
		for (ToPorderAviPath aviPath : aviPaths) {
			if (StringUtil.isNotBlank(aviPath.getParentId())) {
				StringBuffer parentsIdBuffer = new StringBuffer();
				for (String parentId : Arrays.asList(aviPath.getParentId().split(","))) {
					parentsIdBuffer.append(aviPathSeqIdMap.get(pathUlocIdSeqMap.get(parentId))).append(",");
				}
				if (parentsIdBuffer.length() > 0) {
					parentsIdBuffer.deleteCharAt(parentsIdBuffer.length() - 1);
				}
				aviPath.setParentId(parentsIdBuffer.toString());
			}
		}
		doUpdateBatch(aviPaths);
		logService.doAddLog("ToPorderAviPath", aviPaths);
	}

	/**
	 * KEY为SEQ,value为ID
	 * 
	 * @param aviPaths
	 * @return
	 */
	private Map<String, String> getAviPathsSeqId(List<ToPorderAviPath> aviPaths) {
		Map<String, String> map = new HashMap<String, String>();
		for (ToPorderAviPath aviPath : aviPaths) {
			map.put(aviPath.getSeq(), aviPath.getId().toString());
		}
		return map;
	}

	/**
	 * KEY为ID,value为SEQ
	 * 
	 * @param pathUlocs
	 * @return
	 */
	private Map<String, String> getPathUlocIdSeqMap(List<TmPathUloc> pathUlocs) {
		Map<String, String> map = new HashMap<String, String>();
		for (TmPathUloc pathUloc : pathUlocs) {
			map.put(pathUloc.getId().toString(), pathUloc.getSeq());
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> selectNotOfflineDataByAviId(Integer aviId) {
		return ((ToPorderAviPathDao) dao).selectNotOfflineDataByAviId(aviId);
	}

	@Override
	public ToPorderAviPath doSavePathUloc(ToPorderAviPath bean, String ulocType) {
		List<ToPorderAviPath> aviPaths = getAviPaths(bean.getToPorderAviId(), false);
		for (ToPorderAviPath aviPath : aviPaths) {
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(aviPath.getIsOnline())
					&& ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(aviPath.getIsOffline())) {
				throw new BusinessException("PATH_ULOC_ADD_ULOCTYPE_ERROR", aviPath.getSeq());
			}
		}
		setUlocType(bean, ulocType);
		// 非上线点，必须选择上一节点
		if (!ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOnline())
				&& StringUtil.isBlank(bean.getParentId())) {
			throw new BusinessException("PATH_ULOC_OFFLINE_ERROR");
		}
		bean.setSeq(getMaxSeq(bean.getToPorderAviId()));
		return doSave(bean);
	}

	/**
	 * 获取最大seq
	 * 
	 * @param tmPathId
	 * @return
	 */
	private String getMaxSeq(Integer aviId) {
		List<ToPorderAviPath> aviPath = getAviPaths(aviId, false);
		if (aviPath.size() > 0) {
			return String.valueOf(Integer.parseInt(aviPath.get(aviPath.size() - 1).getSeq()) + 1);
		} else {
			return "1";
		}
	}

	@Override
	public List<ToPorderAviPath> getAviPaths(Integer aviId, boolean queryRelationEntity) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(queryRelationEntity);
		criteria.setQueryPage(false);
		criteria.setOrderDirection(OrderEnum.ASC);
		criteria.setOrderField("seq");
		criteria.getQueryCondition().put("toPorderAviId", aviId.toString());
		criteria.setUseCacheSql(false);
		return findBy(criteria).getContent();
	}

	/**
	 * 给是否上线点，是否下线点赋值
	 * 
	 * @param bean
	 * @param ulocType
	 */
	private void setUlocType(ToPorderAviPath bean, String ulocType) {
		if (ulocType != null) {
			String[] ulocTypes = ulocType.split(",");
			if (ulocTypes.length == 0) {
				bean.setIsOffline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
				bean.setIsOnline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
			} else if (ulocTypes.length == 1) {
				if (ConstantUtils.ENTRY_CODE_ULOC_TYPE_ONLINE.equals(ulocTypes[0])) {
					bean.setIsOnline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
					bean.setIsOffline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
				} else {
					bean.setIsOnline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
					bean.setIsOffline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
				}
			} else {
				bean.setIsOffline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
				bean.setIsOnline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
			}
		} else {
			bean.setIsOffline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
			bean.setIsOnline(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		}
	}

	@Override
	public void doUpdatePathUloc(ToPorderAviPath before, ToPorderAviPath bean, String ulocType) {
		setUlocType(bean, ulocType);
		// 非上线点，必须选择上一节点
		if (!ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOnline())
				&& StringUtil.isBlank(bean.getParentId())) {
			throw new BusinessException("PATH_ULOC_OFFLINE_ERROR");
		}
		bean.setToPorderAviId(before.getToPorderAviId());
		bean.setSeq(before.getSeq());
		doUpdate(bean);
	}

	@Override
	public Map<String, String> getEchartsData(String aviId) {
		// 其它节点存放
		List<String> roots = new ArrayList<String>();
		JSONArray datas = new JSONArray();
		List<ToPorderAviPath> pathUlocs = getAviPaths(Integer.valueOf(aviId), true);
		int x = 100;
		int y = 100;
		Random random = new Random();

		for (ToPorderAviPath pathUloc : pathUlocs) {
			JSONObject data = new JSONObject();
			data.put("name", pathUloc.getSeq() + "_" + pathUloc.getUloc().getNo());
			data.put("x", x);
			data.put("y", y);
			datas.add(data);
			x += 100;
			y += random.nextInt(100);
		}

		Map<String, ToPorderAviPath> pathUlocIdMap = getPathUlocIdMap(pathUlocs);
		// 获取开始节点，删除开始节点
		getStartUlocAndRemove(pathUlocs, roots);

		// List<Node> nodes = new ArrayList<Node>();

		JSONArray links = new JSONArray();
		Map<String, List<String>> sons = getSons(pathUlocs);
		for (String parent : sons.keySet()) {
			List<String> childs = sons.get(parent);
			for (String child : childs) {
				JSONObject link = new JSONObject();
				link.put("source",
						pathUlocIdMap.get(parent).getSeq() + "_" + pathUlocIdMap.get(parent).getUloc().getNo());
				link.put("target",
						pathUlocIdMap.get(child).getSeq() + "_" + pathUlocIdMap.get(child).getUloc().getNo());
				JSONObject lineStyle = new JSONObject();
				JSONObject curveness = new JSONObject();
				curveness.put("curveness", 0.2);
				lineStyle.put("normal", curveness);
				link.put("lineStyle", lineStyle);
				links.add(link);
			}
		}
		// 返回到前端
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("datas", datas.toString());
		returnMap.put("links", links.toString());
		return returnMap;
	}

	private Map<String, ToPorderAviPath> getPathUlocIdMap(List<ToPorderAviPath> pathUlocs) {
		Map<String, ToPorderAviPath> map = new HashMap<String, ToPorderAviPath>();
		for (ToPorderAviPath pathUloc : pathUlocs) {
			map.put(pathUloc.getId().toString(), pathUloc);
		}
		return map;
	}

	private void getStartUlocAndRemove(List<ToPorderAviPath> pathUlocs, List<String> list) {
		Iterator<ToPorderAviPath> iterator = pathUlocs.iterator();
		while (iterator.hasNext()) {
			ToPorderAviPath pathUloc = iterator.next();
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(pathUloc.getIsOnline())) {
				list.add(pathUloc.getId().toString());
				iterator.remove();
			}
		}
	}

	/**
	 * 为开始节点添加子节点
	 * 
	 * @param pathUlocs
	 * @param datasMap
	 */
	private Map<String, List<String>> getSons(List<ToPorderAviPath> pathUlocs) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (ToPorderAviPath pathUloc : pathUlocs) {
			if (StringUtil.isBlank(pathUloc.getParentId())) {
				if (map.containsKey("ONLINE")) {
					List<String> childs = map.get("ONLINE");
					childs.add(pathUloc.getId().toString());
					map.put("ONLINE", childs);
				} else {
					List<String> childs = new ArrayList<String>();
					childs.add(pathUloc.getId().toString());
					map.put("ONLINE", childs);
				}
			} else {
				String[] parents = pathUloc.getParentId().split(",");
				for (String parent : parents) {
					if (map.containsKey(parent)) {
						List<String> childs = map.get(parent);
						childs.add(pathUloc.getId().toString());
						map.put(parent, childs);
					} else {
						List<String> childs = new ArrayList<String>();
						childs.add(pathUloc.getId().toString());
						map.put(parent, childs);
					}
				}
			}
		}
		return map;
	}

	@Override
	public void doDeleteIdsAndUpdateSeq(Integer[] deleteIds, List<ToPorderAviPath> list) {
		List<ToPorderAviPath> aviPaths = getAviPaths(list.get(0).getToPorderAviId(), false);
		if (deleteIds.length == aviPaths.size()) {
			doDeleteById(deleteIds);
		} else {
			for (ToPorderAviPath aviPath1 : list) {
				for (ToPorderAviPath aviPath2 : aviPaths) {
					if (StringUtil.isNotBlank(aviPath2.getParentId())) {
						List<String> parents = Arrays.asList(aviPath2.getParentId().split(","));
						if (parents.contains(aviPath1.getId().toString())) {
							throw new BusinessException("PATH_ULOC_DELETE_CHECK_ERROR", aviPath1.getSeq(),
									aviPath2.getSeq());
						}
					}
				}
			}
			doDeleteById(deleteIds);
			aviPaths = getAviPaths(list.get(0).getToPorderAviId(), false);
			if (aviPaths.size() > 0) {
				int seq = 1;
				for (ToPorderAviPath aviPath : aviPaths) {
					aviPath.setSeq(String.valueOf(seq++));
				}
				doUpdateBatch(aviPaths);
			}
		}
	}

	@Override
	public void exportExcelData(HttpServletResponse response, List<ToPorderAviPath> content, String filePath,
			String[] header) {

		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		// YES/NO
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		for (ToPorderAviPath bean : content) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("工位顺序", bean.getSeq());
			map.put("工位", bean.getUloc().getNo() + "-" + bean.getUloc().getName());
			map.put("上一节点集合", bean.getParentId());
			map.put("操作时间", bean.getOperateTime() == null ? "" : bean.getOperateTime());
			map.put("间隔通过时间", bean.getIntervalTime() == null ? "" : bean.getIntervalTime());
			map.put("是否上线点", yesOrNo.get(bean.getIsOnline()).getName());
			map.put("是否下线点", yesOrNo.get(bean.getIsOffline()).getName());
			map.put("是否质检点", yesOrNo.get(bean.getIsSip()).getName());
			map.put("是否自动入库", yesOrNo.get(bean.getIsAutoInstorage()).getName());
			map.put("入库等待时间", bean.getInstorageTime() == null ? "" : bean.getInstorageTime());
			map.put("是否报工点", yesOrNo.get(bean.getIsReported()).getName());
			map.put("对应ERP工序号", bean.getErpUlocCode() == null ? "" : bean.getErpUlocCode());
			map.put("备注", bean.getNote() == null ? "" : bean.getNote());
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, filePath, header);

	}

	@Override
	public Integer getPorderPlantIdByAviPathId(Integer toPorderAviPathId) {
		return ((ToPorderAviPathDao) dao).getPorderPlantIdByAviPathId(toPorderAviPathId);
	}
}
