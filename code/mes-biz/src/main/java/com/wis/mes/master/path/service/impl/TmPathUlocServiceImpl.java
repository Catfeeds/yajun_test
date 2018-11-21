package com.wis.mes.master.path.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.path.dao.TmPathUlocDao;
import com.wis.mes.master.path.entity.TmPath;
import com.wis.mes.master.path.entity.TmPathLigature;
import com.wis.mes.master.path.entity.TmPathUloc;
import com.wis.mes.master.path.service.TmPathLigatureService;
import com.wis.mes.master.path.service.TmPathService;
import com.wis.mes.master.path.service.TmPathUlocService;
import com.wis.mes.util.StringUtil;

@Service
public class TmPathUlocServiceImpl extends BaseServiceImpl<TmPathUloc> implements TmPathUlocService {

	@Autowired
	public void setDao(final TmPathUlocDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmPathService pathService;
	@Autowired
	private TmPathLigatureService pathPathsService;

	@Override
	public Map<String, Object> exportExcelData(final HttpServletResponse response, final List<TmPathUloc> list,
			final String filePath, final String[] header) {
		// YES/NO
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);

		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		for (TmPathUloc pathUloc : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("工序顺序", pathUloc.getSeq());
			map.put("工序", pathUloc.getUloc().getNo() + "-" + pathUloc.getUloc().getName());
			map.put("上一节点集合", pathUloc.getParentId());
			map.put("操作时间", pathUloc.getOperateTime() == null ? "" : pathUloc.getOperateTime());
			map.put("间隔通过时间", pathUloc.getIntervalTime() == null ? "" : pathUloc.getIntervalTime());
			map.put("是否质检点", yesOrNo.get(pathUloc.getIsSip()).getName());
			map.put("是否自动入库", yesOrNo.get(pathUloc.getIsAutoInstorage()).getName());
			map.put("入库等待时间", pathUloc.getInstorageTime() == null ? "" : pathUloc.getInstorageTime());
			map.put("是否报工点", yesOrNo.get(pathUloc.getIsReported()).getName());
			map.put("对应ERP工序号", pathUloc.getErpUlocCode() == null ? "" : pathUloc.getErpUlocCode());
			map.put("备注", pathUloc.getNote() == null ? "" : pathUloc.getNote());
			map.put("是否上线点",
					yesOrNo.containsKey(pathUloc.getIsOnline()) ? yesOrNo.get(pathUloc.getIsOnline()).getName() : "");
			map.put("是否下线点",
					yesOrNo.containsKey(pathUloc.getIsOffline()) ? yesOrNo.get(pathUloc.getIsOffline()).getName() : "");
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}

	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req, String TmPathId) {

		return null;
	}

	@Override
	public List<TmPathUloc> getPathUlocs(Integer tmPathId) {
		TmPathUloc pathUloc = new TmPathUloc();
		pathUloc.setTmPathId(tmPathId);
		return this.findByEg(pathUloc);
	}

	@Override
	public List<Map<String, Object>> queryPathUloc(Integer tmPathId) {
		return ((TmPathUlocDao) dao).queryPathUloc(tmPathId);
	}

	@Override
	public List<Map<String, Object>> findByIds(String ids) {
		return ((TmPathUlocDao) dao).findByIds(ids);
	}

	@Override
	public String getUlocsByIds(String ids) {
		List<Map<String, Object>> findByIds = findByIds(ids);
		StringBuffer ulocNos = new StringBuffer("[");
		for (Map<String, Object> map : findByIds) {
			ulocNos.append(map.get("ulocNo")).append(",");
		}
		ulocNos.deleteCharAt(ulocNos.length() - 1);
		if (ulocNos.length() > 0) {
			ulocNos.append("]");
		}
		return ulocNos.toString();
	}

	/**
	 * X从100开始，每极加100
	 * 
	 * Y从100开始，每极加100，如果介于两极之间，求平均数
	 * 
	 * 
	 * Map<String,Map<String,List<Map<String,Map<String,Map<String,<Map<String>>>>>>>>
	 */
	@Override
	public Map<String, String> getEchartsData(String tmPathId) {
		// 其它节点存放
		List<String> roots = new ArrayList<String>();
		JSONArray datas = new JSONArray();
		List<TmPathUloc> pathUlocs = getPathUlocAndRelationEntity(tmPathId);
		int x = 100;
		int y = 100;
		Random random = new Random();

		for (TmPathUloc pathUloc : pathUlocs) {
			JSONObject data = new JSONObject();
			data.put("name", pathUloc.getSeq() + "_" + pathUloc.getUloc().getNo());
			data.put("x", x);
			data.put("y", y);
			datas.add(data);
			x += 100;
			y += random.nextInt(100);
		}

		Map<String, TmPathUloc> pathUlocIdMap = getPathUlocIdMap(pathUlocs);
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

	private Map<String, TmPathUloc> getPathUlocIdMap(List<TmPathUloc> pathUlocs) {
		Map<String, TmPathUloc> map = new HashMap<String, TmPathUloc>();
		for (TmPathUloc pathUloc : pathUlocs) {
			map.put(pathUloc.getId().toString(), pathUloc);
		}
		return map;
	}

	/**
	 * 为开始节点添加子节点
	 * 
	 * @param pathUlocs
	 * @param datasMap
	 */
	private Map<String, List<String>> getSons(List<TmPathUloc> pathUlocs) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (TmPathUloc pathUloc : pathUlocs) {
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

	/**
	 * 首先把所有开始节点找到
	 *
	 * 遍历找出这些开始节点的子节点
	 *
	 * Map<key,Object>,key:开始节点的ID，Object:Map<String,Object>key:是子节点，Object是TmUlocPath
	 * 
	 */

	private void getStartUlocAndRemove(List<TmPathUloc> pathUlocs, List<String> list) {
		Iterator<TmPathUloc> iterator = pathUlocs.iterator();
		while (iterator.hasNext()) {
			TmPathUloc pathUloc = iterator.next();
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(pathUloc.getIsOnline())) {
				list.add(pathUloc.getId().toString());
				iterator.remove();
			}
		}
	}

	/**
	 * 根据ID排序，并且查询关联实体
	 * 
	 * @param tmPathId
	 * @return
	 */
	public List<TmPathUloc> getPathUlocAndRelationEntity(String tmPathId) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setOrderField("id");
		criteria.setOrderDirection(OrderEnum.ASC);
		criteria.getQueryCondition().put("tmPathId", tmPathId);
		return findBy(criteria).getContent();
	}
	/*
	 * private Root createTree(List<Node> nodes, String rootNode) { if
	 * (nodes.size() < 0) { return null; } Root root = new Root(rootNode); for
	 * (Node node : nodes) { if (node.parentNode.equals(rootNode)) {
	 * root.node.childNodes.add(new TreeNode(node)); } else {
	 * addChild(root.node, node); } } return root; }
	 * 
	 * private void addChild(TreeNode treeNode, Node node) { for (TreeNode item
	 * : treeNode.childNodes) { if
	 * (item.node.currentNode.equals(node.parentNode)) { // 找到对应的父亲
	 * item.childNodes.add(new TreeNode(node)); } else { if
	 * (item.childNodes.size() > 0) { addChild(item, node); } } } }
	 * 
	 * private class Root { private TreeNode node;
	 * 
	 * public Root(String roots) { node = new TreeNode(new Node(roots)); } }
	 * 
	 * private class TreeNode { private List<Node> nodes; private
	 * Map<Node,List<TreeNode>> childNodes;
	 * 
	 * public TreeNode(List<Node> nodes) { this.nodes= nodes; this.childNodes =
	 * new ArrayList<TreeNode>(); } }
	 * 
	 * private class Node { private String currentNode; private String
	 * parentNode; private int x; private int y;
	 * 
	 * public Node(String currentNode, String parentNode, int x, int y) {
	 * super(); this.currentNode = currentNode; this.parentNode = parentNode;
	 * this.x = x; this.y = y; }
	 * 
	 * public Node(String currentNode) { super(); this.currentNode =
	 * currentNode; }
	 * 
	 * }
	 */

	@Override
	public TmPathUloc doSavePathUloc(TmPathUloc bean, String ulocType) {
		//之前的业务不符合现在的需求，暂时注释   zhuzw
//		TmPath path = pathService.findById(bean.getTmPathId());
//		List<TmPathUloc> pathUlocs = getPathUlocs(bean.getTmPathId());
//		for (TmPathUloc pathUloc : pathUlocs) {
//			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(pathUloc.getIsOnline())
//					&& ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(pathUloc.getIsOffline())) {
//				throw new BusinessException("PATH_ULOC_ADD_ULOCTYPE_ERROR", pathUloc.getSeq());
//			}
//		}
//
//		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(path.getStatus())) {
//			throw new BusinessException("PATH_ULOC_DELETE_ERROR");
//		}
//
		setUlocType(bean, ulocType);
		// 非上线点，必须选择上一节点
//		if (!ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOnline())
//				&& StringUtil.isBlank(bean.getParentId())) {
//			throw new BusinessException("PATH_ULOC_OFFLINE_ERROR");
//		}
//		bean.setSeq(getMaxSeq(bean.getTmPathId()));
		return doSave(bean);
	}

	/**
	 * 获取最大seq
	 * 
	 * @param tmPathId
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getMaxSeq(Integer tmPathId) {
		List<Map<String, Object>> queryPathUloc = queryPathUloc(tmPathId);
		if (queryPathUloc != null && queryPathUloc.size() > 0) {
			return String
					.valueOf(Integer.parseInt(queryPathUloc.get(queryPathUloc.size() - 1).get("seq").toString()) + 1);
		} else {
			return "1";
		}
	}

	@Override
	public List<Map<String, Object>> queryByPathIdAndUlocType(Integer tmPathId, String ulocType) {
		return ((TmPathUlocDao) dao).queryByPathIdAndUlocType(tmPathId, ulocType);
	}

	@Override
	public void doDeleteIdsAndUpdateSeq(Integer[] deleteIds, Integer id, List<TmPathUloc> list) {
		doDeletePathUlocChilds(Arrays.toString(deleteIds).replace("[", "").replace("]", ""));
		List<TmPathUloc> pathUlocData = getPathUlocAndRelationEntity(String.valueOf(id));
		if (deleteIds.length != pathUlocData.size()) {
			for (TmPathUloc pathUloc : list) {
				for (TmPathUloc tmPathUloc : pathUlocData) {
					if (StringUtil.isNotBlank(tmPathUloc.getParentId())) {
						List<String> parents = Arrays.asList(tmPathUloc.getParentId().split(","));
						if (parents.contains(pathUloc.getId().toString())) {
							throw new BusinessException("PATH_ULOC_DELETE_CHECK_ERROR", pathUloc.getSeq(),
									tmPathUloc.getSeq());
						}
					}
				}
			}
		}
		doDeleteById(deleteIds);
		pathUlocData = getPathUlocAndRelationEntity(String.valueOf(id));
		if (pathUlocData.size() > 0) {
			int seq = 1;
			for (TmPathUloc tmPathUloc : pathUlocData) {
				tmPathUloc.setSeq(String.valueOf(seq++));
			}
			doUpdateBatch(pathUlocData);
		}
	}

	/**
	 * 给是否上线点，是否下线点赋值
	 * 
	 * @param bean
	 * @param ulocType
	 */
	private void setUlocType(TmPathUloc bean, String ulocType) {
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
	public void doUpdatePathUloc(TmPathUloc before, TmPathUloc bean, String ulocType) {

//		// 艺路径维护完成，不能新增、修改和删除!
//		TmPath path = pathService.findById(before.getTmPathId());
//		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(path.getStatus())) {
//			throw new BusinessException("PATH_ULOC_DELETE_ERROR");
//		}
//		List<TmPathUloc> pathUlocs = getPathUlocs(before.getTmPathId());
//		for (TmPathUloc pathUloc : pathUlocs) {
//			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(pathUloc.getIsOnline())
//					&& ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(pathUloc.getIsOffline())
//					&& !before.getId().toString().equals(bean.getId().toString())) {
//				throw new BusinessException("PATH_ULOC_ADD_ULOCTYPE_ERROR", pathUloc.getSeq());
//			}
//		}
//		setUlocType(bean, ulocType);
//		// 非上线点，必须选择上一节点
//		if (!ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOnline())
//				&& StringUtil.isBlank(bean.getParentId())) {
//			throw new BusinessException("PATH_ULOC_OFFLINE_ERROR");
//		}
//		bean.setTmPathId(before.getTmPathId());
//		bean.setSeq(before.getSeq());
		doUpdate(bean);
	}

	@Override
	public void doDeletePathUlocChilds(String tmPathUlocIds) {
		((TmPathUlocDao) dao).deletePathUlocChilds(tmPathUlocIds);
	}

	@Override
	public List<TmPathUloc> findByForginKey(Integer forginId) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.addQueryCondition("tmPathId", forginId.toString());
		criteria.setQueryPage(false);
		criteria.setOrderDirection(OrderEnum.ASC);
		PageResult<TmPathUloc> result = (PageResult<TmPathUloc>) dao.findBy(criteria);
		return result.getContent();
	}

	@Override
	public String getGraphJSONData(Integer tmPathId) {
		TmPath pathBean = pathService.findById(tmPathId);
		List<TmPathUloc> pathUlocs = getPathUlocAndRelationEntity(tmPathId.toString());
		List<TmPathLigature> pathPathsData = getPathPathsData(tmPathId);
		JSONObject data = new JSONObject();
		data.put("states", getStatesJSON(pathUlocs));
		data.put("paths", getPathsJSON(pathPathsData));
		data.put("props", getPropsJSON(pathBean));
		return data.toString();
	}

	private String getStatesJSON(List<TmPathUloc> pathUlocs) {
		JSONObject data = new JSONObject();
		for (TmPathUloc bean : pathUlocs) {
			changeTimeMsToMin(bean);
			JSONObject rectJSON = new JSONObject();
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsSip())) {
				rectJSON.put("type", "sip");
			} else if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOnline())) {
				rectJSON.put("type", "online");
			} else if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOffline())) {
				rectJSON.put("type", "offline");
			} else if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOffline())
					&& ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOnline())) {
				rectJSON.put("type", "task");
			} else {
				rectJSON.put("type", "scan");
			}

			JSONObject textJSON = new JSONObject();
			textJSON.put("text", bean.getUloc().getNo() + "-" + bean.getUloc().getName());
			rectJSON.put("text", textJSON.toString());

			JSONObject attrJSON = new JSONObject();
			attrJSON.put("x", Double.valueOf(bean.getX()));
			attrJSON.put("y", Double.valueOf(bean.getY()));
			attrJSON.put("width", Double.valueOf(bean.getWidth()));
			attrJSON.put("height", Double.valueOf(bean.getHeight()));
			rectJSON.put("attr", attrJSON.toString());

			JSONObject props = new JSONObject();

			JSONObject value = new JSONObject();
			value.put("value", bean.getUloc().getNo() + "-" + bean.getUloc().getName());
			props.put("ulocNo", value.toString());

			value.put("value", StringUtil.getString(bean.getIsOnline()));
			props.put("isOnline", value.toString());

			value.put("value", StringUtil.getString(bean.getIsOffline()));
			props.put("isOffline", value.toString());

			value.put("value", bean.getIsSip());
			props.put("isSip", value.toString());

			value.put("value", bean.getIsAutoInstorage());
			props.put("isAutoInstorage", value.toString());

			value.put("value", bean.getIsReported());
			props.put("isReported", value.toString());

			value.put("value", bean.getErpUlocCode());
			props.put("erpUlocCode", value.toString());

			value.put("value", StringUtil.getString(bean.getOperateTime()));
			props.put("operateTime", value.toString());

			value.put("value", StringUtil.getString(bean.getIntervalTime()));
			props.put("intervalTime", value.toString());

			value.put("value", StringUtil.getString(bean.getInstorageTime()));
			props.put("inStroageTime", value.toString());

			value.put("value", bean.getNote());
			props.put("note", value.toString());

			value.put("value", bean.getTmUlocId());
			props.put("ulocId", value.toString());

			value.put("value", bean.getId());
			props.put("tmPathUlocId", value.toString());

			rectJSON.put("props", props.toString());
			data.put(bean.getRectSeq(), rectJSON.toString());
		}
		return data.toString();
	}

	private String getPathsJSON(List<TmPathLigature> pathPathsData) {
		JSONObject data = new JSONObject();
		for (TmPathLigature bean : pathPathsData) {
			JSONObject paths = new JSONObject();
			paths.put("from", bean.getFromDiv());
			paths.put("to", bean.getToDiv());
			paths.put("dots", JSONArray.fromObject(bean.getDots()).toString());
			paths.put("text", JSONObject.fromObject(bean.getText()).toString());
			paths.put("textPos", JSONObject.fromObject(bean.getTextPos()).toString());
			data.put(bean.getNo(), paths.toString());
		}
		return data.toString();
	}

	private String getPropsJSON(TmPath pathBean) {
		JSONObject data = new JSONObject();
		JSONObject props = new JSONObject();

		JSONObject value = new JSONObject();
		value.put("value", pathBean.getNo());
		props.put("pathNo", value.toString());

		value.put("value", pathBean.getName());
		props.put("pathName", value.toString());

		value.put("value", StringUtil.getString(pathBean.getTmPartId()));
		props.put("part", value.toString());

		value.put("value", pathBean.getEnabled());
		props.put("enabled", value.toString());

		value.put("value", pathBean.getId());
		props.put("tmPathId", value.toString());

		data.put("props", props.toString());

		return data.toString();
	}

	/**
	 * 工艺路径的连线信息
	 * 
	 * @param tmPathId
	 * @return
	 */
	private List<TmPathLigature> getPathPathsData(Integer tmPathId) {
		TmPathLigature eg = new TmPathLigature();
		eg.setTmPathId(tmPathId);
		return pathPathsService.findByEg(eg);
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
	public void doDeletePathUlocOrParam(Integer[] tmPathUlocIds) {
		//先删除站点对于的参数
		((TmPathUlocDao) dao).doDeletePathUlocOrParam(Arrays.toString(tmPathUlocIds).replace("[", "").replace("]", ""));
		//在删除站点
		dao.deleteByBatch(tmPathUlocIds);
	}

	@Override
	public List<Map<String, Object>> getTmPathIdsByTmPathUlocIds(Integer[] tmPathIds) {
		
		return ((TmPathUlocDao) dao).getTmPathIdsByTmPathUlocIds(Arrays.toString(tmPathIds).replace("[", "").replace("]", ""));
	}

}
