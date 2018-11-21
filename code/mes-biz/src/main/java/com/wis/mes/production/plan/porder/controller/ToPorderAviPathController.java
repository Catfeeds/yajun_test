package com.wis.mes.production.plan.porder.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPath;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathService;
import com.wis.mes.production.plan.porder.service.ToPorderAviService;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderAviPathController
 * 
 * @author liuzejun
 *
 * @date 2017年5月31日 上午10:05:34
 * 
 * @Description: 生产序列下的工艺路径信息Controller
 */
@Controller
@RequestMapping(value = "/aviPath")
public class ToPorderAviPathController extends BaseController {

	@Autowired
	private ToPorderAviPathService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private ToPorderAviService aviService;
	@Autowired
	private TmUlocService ulocService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap, Integer aviId, HttpServletRequest request) {
		modelMap.addAttribute("aviId", aviId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/production-plan/porder/path/avi_path_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort() == null ? "id" : criteria.getSort());
		criteria.setUseCacheSql(false);
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, concordancyResult(service.findBy(criteria)));
	}

	/**
	 * 把查询结果中的parentsId改成ulocNo
	 * 
	 * @param pathUlocs
	 * @return
	 */
	private PageResult<ToPorderAviPath> concordancyResult(PageResult<ToPorderAviPath> pathUlocs) {
		List<ToPorderAviPath> content = pathUlocs.getContent();
		for (ToPorderAviPath uloc : content) {
			List<String> asList = Arrays.asList(uloc.getParentId().split(","));
			StringBuffer ulocNos = new StringBuffer("[");
			for (ToPorderAviPath ppathUloc : content) {
				if (asList.contains(ppathUloc.getId().toString())) {
					ulocNos.append(ppathUloc.getSeq() + "_" + ppathUloc.getUloc().getNo()).append(",");
				}
			}
			ulocNos.deleteCharAt(ulocNos.length() - 1);
			if (ulocNos.length() > 0) {
				ulocNos.append("]");
			}
			uloc.setParentId(ulocNos.toString());
			changeTimeMsToMin(uloc);
		}
		return pathUlocs;
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap, Integer aviId) {

		modelMap.addAttribute("aviId", aviId);
		// 工位
		modelMap.addAttribute("ulocOptions", getUlocOptions(aviId));
		// 不是下线点的数据
		List<Map<String, Object>> queryPathUloc = service.selectNotOfflineDataByAviId(aviId);
		modelMap.addAttribute("ulocQty", queryPathUloc.size());
		modelMap.addAttribute("parents", queryPathUloc);
		// 工序类型
		modelMap.addAttribute("ulocTypOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ULOC_TYPE));
		// 是否
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/production-plan/porder/path/avi_path_add");
	}

	/**
	 * 查询工位
	 * 
	 * @param tmPathId
	 * @return
	 */
	private List<DictEntry> getUlocOptions(Integer aviId) {
		Integer plantId = aviService.getPorderPlantIdByAviId(aviId);
		ToPorderAvi avi = aviService.findById(aviId);
		TmUloc uloc = new TmUloc();
		uloc.setTmPlantId(plantId);
		uloc.setTmLineId(avi.getTmLineId());
		uloc.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return ulocService.getDictItem(uloc);
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, ToPorderAviPath bean, String ulocType) {
		changeTimeMinToMs(bean);
		bean = service.doSavePathUloc(bean, ulocType);
		logService.doAddLog("ToPorderAviPath", bean);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 把时间分钟转成毫秒
	 * 
	 * @param bean
	 */
	private void changeTimeMinToMs(ToPorderAviPath bean) {
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
	private void changeTimeMsToMin(ToPorderAviPath bean) {
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

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap, Integer aviId) {
		ToPorderAviPath bean = service.findById(Integer.valueOf(id));
		changeTimeMsToMin(bean);
		modelMap.addAttribute("bean", bean);

		modelMap.addAttribute("aviId", aviId);
		// 工位
		modelMap.addAttribute("ulocOptions", getUlocOptions(aviId));
		// 不是下线点的数据
		List<Map<String, Object>> queryPathUloc = service.selectNotOfflineDataByAviId(aviId);
		modelMap.addAttribute("ulocQty", queryPathUloc.size());
		modelMap.addAttribute("parents", queryPathUloc);
		// 工序类型
		modelMap.addAttribute("ulocTypOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ULOC_TYPE));
		// 是否
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/production-plan/porder/path/avi_path_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, ToPorderAviPath bean, String ulocType) {
		ToPorderAviPath before = service.findById(bean.getId());
		changeTimeMinToMs(before);
		changeTimeMinToMs(bean);
		service.doUpdatePathUloc(before, bean, ulocType);
		logService.doUpdateLog("ToPorderAviPath", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<ToPorderAviPath> list = service.findAllInIds(deleteIds);
		service.doDeleteIdsAndUpdateSeq(deleteIds, list);
		logService.doDeleteLog("ToPorderAviPath", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/getEchartsData")
	public Map<String, String> getEchartsData(String aviId) {
		return service.getEchartsData(aviId);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String aviId, String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		if (StringUtils.isNotEmpty(aviId)) {
			criteria.getQueryCondition().put("toPorderAviId", aviId);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList("no"));
		final PageResult<ToPorderAviPath> findBy = concordancyResult(service.findBy(criteria));
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "工位顺序", "工位", "上一节点集合", "操作时间", "间隔通过时间", "是否上线点", "是否下线点", "是否质检点", "是否自动入库",
				"入库等待时间", "是否报工点", "对应ERP工序号", "备注" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/mainListInput")
	public ModelAndView mainListInput(ModelMap modelMap, Integer toPorderAviPathId, String currentPageId) {
		modelMap.addAttribute("toPorderAviPathId", toPorderAviPathId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		return new ModelAndView("/production-plan/porder/path/avi_path_main");
	}
}
