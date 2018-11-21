package com.wis.mes.master.path.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.master.path.entity.TmPath;
import com.wis.mes.master.path.entity.TmPathUloc;
import com.wis.mes.master.path.service.TmPathService;
import com.wis.mes.master.path.service.TmPathUlocService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;

@Controller
@RequestMapping(value = "/pathUloc")
public class TmPathUlocController extends BaseController {

	@Autowired
	private TmPathUlocService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmPathService pathService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmUlocService ulocService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, ModelMap modelMap, Integer tmPathId) {
		modelMap.addAttribute("tmPathId", tmPathId);
		modelMap.addAttribute("workOrder", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_WORK_ORDER));
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/master/path/uloc/path_uloc_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort() == null ? "seq" : criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, concordancyResult(service.findBy(criteria)));
	}

	/**
	 * 把查询结果中的parentsId改成ulocNo
	 * 
	 * @param pathUlocs
	 * @return
	 */
	private PageResult<TmPathUloc> concordancyResult(PageResult<TmPathUloc> pathUlocs) {
		List<TmPathUloc> content = pathUlocs.getContent();
		for (TmPathUloc tmPathUloc : content) {
			List<String> asList = Arrays
					.asList((tmPathUloc.getParentId() == null ? "" : tmPathUloc.getParentId()).split(","));
			StringBuffer ulocNos = new StringBuffer("[");
			for (TmPathUloc ppathUloc : content) {
				if (asList.contains(ppathUloc.getId().toString())) {
					ulocNos.append(ppathUloc.getSeq() + "_" + ppathUloc.getUloc().getNo()).append(",");
				}
			}
			ulocNos.deleteCharAt(ulocNos.length() - 1);
			if (ulocNos.length() > 0) {
				ulocNos.append("]");
			}
			tmPathUloc.setParentId(ulocNos.toString());
			changeTimeMsToMin(tmPathUloc);
		}
		return pathUlocs;
	}

	/**
	 * 把时间分钟转成毫秒
	 * 
	 * @param bean
	 */
	private void changeTimeMinToMs(TmPathUloc bean) {
		if (bean.getInstorageTime() != null) {
			bean.setInstorageTime(bean.getInstorageTime().longValue() * 1000 * 60);
		}
		if (bean.getIntervalTime() != null) {
			bean.setIntervalTime(bean.getIntervalTime().longValue() * 1000 * 60);
		}
		if (bean.getOperateTime() != null) {
			bean.setOperateTime(bean.getOperateTime().longValue() * 1000 * 60);
		}
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

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap, Integer tmPathId) {
		modelMap.addAttribute("tmPathId", tmPathId);
		// 工位
		modelMap.addAttribute("ulocOptions", getUlocOptions(tmPathId));
//		// 不是下线点的数据
//		List<Map<String, Object>> queryPathUloc = service.queryByPathIdAndUlocType(tmPathId,
//				" = " + "'" + ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO + "'");
//		modelMap.addAttribute("ulocQty", queryPathUloc.size());
//		modelMap.addAttribute("parents", queryPathUloc);
		//工位顺序
		modelMap.addAttribute("workOrder", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_WORK_ORDER));
		// 工序类型
		//modelMap.addAttribute("ulocTypOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ULOC_TYPE));
		// 是否
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/master/path/uloc/path_uloc_add");
	}

	/**
	 * 查询工位
	 * 
	 * @param tmPathId
	 * @return
	 */
	private List<DictEntry> getUlocOptions(Integer tmPathId) {
		TmPath path = pathService.findById(tmPathId);
		TmUloc uloc = new TmUloc();
		uloc.setTmPlantId(path.getTmPlantId());
		uloc.setTmWorkshopId(path.getTmWorkshopId());
		uloc.setTmLineId(path.getTmLineId());
		uloc.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return ulocService.getDictItem(uloc);
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TmPathUloc bean, String ulocType) {
		changeTimeMinToMs(bean);
		bean = service.doSavePathUloc(bean, ulocType);
		logService.doAddLog("TmPathUloc", bean);
		return ActionUtils.handleResult(bean.getId());
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(ModelMap modelMap, String id) {
		TmPathUloc pathUlocBean = service.findById(Integer.valueOf(id));
		changeTimeMsToMin(pathUlocBean);
		modelMap.addAttribute("bean", pathUlocBean);
		//工位顺序
		modelMap.addAttribute("workOrder", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_WORK_ORDER));
		// 工位
		modelMap.addAttribute("ulocOptions", getUlocOptions(pathUlocBean.getTmPathId()));
//		List<Map<String, Object>> queryPathUloc = service.queryByPathIdAndUlocType(pathUlocBean.getTmPathId(),
//				" = " + "'" + ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO + "'");
//		modelMap.addAttribute("ulocQty", queryPathUloc.size());
//		modelMap.addAttribute("parents", queryPathUloc);
//		// 工序类型
//		modelMap.addAttribute("ulocTypOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ULOC_TYPE));
//		// 是否
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/master/path/uloc/path_uloc_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmPathUloc bean, String ulocType) {
		TmPathUloc before = service.findById(bean.getId());
		changeTimeMinToMs(bean);
		changeTimeMinToMs(before);
		service.doUpdatePathUloc(before, bean, ulocType);
		logService.doUpdateLog("TmPathUloc", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmPathUloc> list = service.findAllInIds(deleteIds);
		//TmPath path = pathService.findById(list.get(0).getTmPathId());
		//zhuzw 2018/3/22 此逻辑暂时不需要
//		// 艺路径维护完成，不能新增、修改和删除!
//		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(path.getStatus())) {
//			throw new BusinessException("PATH_ULOC_DELETE_ERROR");
//		}
//		service.doDeleteIdsAndUpdateSeq(deleteIds, path.getId(), list);
		// service.doDeleteById(deleteIds);
		service.doDeletePathUlocOrParam(deleteIds);
		logService.doDeleteLog("TmPathUloc", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/getEchartsData")
	public Map<String, String> getEchartsData(String tmPathId) {
		return service.getEchartsData(tmPathId);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String ids, String tmPathId) throws IOException {
		criteria.getQueryCondition().put("tmPathId", tmPathId);
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setOrderDirection(OrderEnum.ASC);
		criteria.setOrderField("id");
		PageResult<TmPathUloc> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "工序顺序", "工序", "上一节点集合", "操作时间", "间隔通过时间", "是否上线点", "是否下线点", "是否质检点", "是否自动入库",
				"入库等待时间", "是否报工点", "对应ERP工序号", "备注" };
		service.exportExcelData(response, concordancyResult(findBy).getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}

}
