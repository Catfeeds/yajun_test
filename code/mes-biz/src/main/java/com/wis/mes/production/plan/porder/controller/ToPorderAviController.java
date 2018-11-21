package com.wis.mes.production.plan.porder.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.production.plan.porder.entity.ToPorder;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPath;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathService;
import com.wis.mes.production.plan.porder.service.ToPorderAviService;
import com.wis.mes.production.plan.porder.service.ToPorderService;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderAviController
 * 
 * @author liuzejun
 *
 * @date 2017年5月31日 上午10:06:19
 * 
 * @Description:生产序列 Controller
 */
@Controller
@RequestMapping(value = "/avi")
public class ToPorderAviController extends BaseController {

	@Autowired
	private ToPorderAviService service;
	@Autowired
	private ToPorderService porderService;
	@Autowired
	private ToPorderAviPathService aviPathService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, ModelMap modelMap, String toPorderId) {
		modelMap.addAttribute("toPorderId", toPorderId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/production-plan/porder/avi/avi_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/doHoldInput")
	public ModelAndView doHoldInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("id", id);
		return new ModelAndView("/production-plan/porder/avi/avi_hold");
	}

	@ResponseBody
	@RequestMapping(value = "/doHold")
	public JsonActionResult doHold(HttpServletResponse response, Integer id, String note) {
		ToPorderAvi avi = service.findById(id);
		checkPorderisHold(avi.getToPorderId());
		avi.setNote(note);
		avi.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
		service.doUpdate(avi);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 检查工单是否HOLD
	 * 
	 * @param toPorderId
	 */
	private void checkPorderisHold(Integer toPorderId) {
		ToPorder porder = porderService.findById(toPorderId);
		if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(porder.getIsHold())) {
			throw new BusinessException("PORDER_HOLD_CANNOT_OPERATE_ERROR");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/doCancelHold")
	public JsonActionResult doCancelHold(HttpServletResponse response, Integer id) {
		ToPorderAvi avi = service.findById(id);
		checkPorderisHold(avi.getToPorderId());
		avi.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		avi.setNote("");
		service.doUpdate(avi);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/doCancelTask")
	public JsonActionResult doCancelTask(HttpServletResponse response, String ids) {
		for (Integer id : getIds(ids)) {
			service.doCancelTask(id);
		}
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/printTransferCard")
	public String printTransferCard(Model model, Integer id) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<ToPorderAviPath> aviPaths = aviPathService.getAviPaths(id, true);
		for (ToPorderAviPath aviPath : aviPaths) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("SEQ", aviPath.getSeq());
			params.put("TM_ULOC_ID", aviPath.getUloc().getNo());
			params.put("PROCESS_NAME", aviPath.getUloc().getName());
			list.add(params);
		}
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		// 动态指定报表模板url
		model.addAttribute("url", "/WEB-INF/jasper/transferCard.jasper");

		ToPorderAvi avi = service.findById(id, true);
		ToPorder porder = porderService.findById(avi.getToPorderId(), true);
		model.addAttribute("SN", avi.getNo());
		model.addAttribute("PPATH_NO", avi.getPath().getNo());
		model.addAttribute("LINE_NO_NAME", avi.getLine().getNo());
		model.addAttribute("TM_PART_ID", porder.getPart().getNo());
		model.addAttribute("NO", porder.getNo());
		model.addAttribute("PORDER_NO", porder.getNo());
		// 指定报表格式
		model.addAttribute("format", "pdf");
		model.addAttribute("jrMainDataSource", jrDataSource);
		// 对应jasper-defs.xml中的bean id
		return "iReportView";
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String toPorderId, String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		if (StringUtils.isNotEmpty(toPorderId)) {
			criteria.getQueryCondition().put("toPorderId", toPorderId);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList("no"));
		final PageResult<ToPorderAvi> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "编号", "工艺路径", "BOM", "数量", "上线数量", "下线数量", "报废数量", "生产开始时间", "生产结束时间", "状态",
				"是否HOLD", "备注" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}

}
