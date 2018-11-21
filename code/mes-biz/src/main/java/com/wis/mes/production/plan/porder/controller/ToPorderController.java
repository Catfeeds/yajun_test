package com.wis.mes.production.plan.porder.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
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
import com.wis.basis.systemadmin.service.UserService;
import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.bom.entity.TmBom;
import com.wis.mes.master.bom.service.TmBomService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.path.entity.TmPath;
import com.wis.mes.master.path.entity.TmPathUloc;
import com.wis.mes.master.path.service.TmPathService;
import com.wis.mes.master.path.service.TmPathUlocService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.production.plan.porder.entity.ToPorder;
import com.wis.mes.production.plan.porder.service.ToPorderAviService;
import com.wis.mes.production.plan.porder.service.ToPorderService;
import com.wis.mes.util.BarcodeUtil;
import com.wis.mes.util.StringUtil;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderController
 * 
 * @author liuzejun
 *
 * @date 2017年6月1日 上午10:44:03
 * 
 * @Description: 工单Controller
 */
@Controller
@RequestMapping(value = "/porder")
public class ToPorderController extends BaseController {

	@Autowired
	private ToPorderService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private TmPartService partService;
	@Autowired
	private TmBomService bomService;
	@Autowired
	private TmPathService pathService;
	@Autowired
	private TmPathUlocService pathUlocService;
	@Autowired
	private ToPorderAviService aviService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, Integer id) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, id);
		return new ModelAndView("/production-plan/porder/porder_main");
	}

	@RequestMapping(value = "/porderListInput")
	public ModelAndView porderListInput(ModelMap modelMap, Integer currentPageId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("plantOptions", plantService.getDictItem(null));
		modelMap.addAttribute("partOptions", partService.getDictItem(new TmPart(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("productStatusOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PORDER_PRODUCT_STATUS));
		modelMap.addAttribute("taskStatusOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PORDER_TASK_STATUS));
		modelMap.addAttribute("instorageOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PORDER_INSTORAGE_STATUS));
		modelMap.addAttribute("prioirtyOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PORDER_PRIORITY));
		return new ModelAndView("/production-plan/porder/porder_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria) {
		criteria.setFuzzyQueryFileds(Arrays.asList("no"));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.getQueryCondition().put("tmPlantIdIN",
				userService.queryPlantPermissionByUserId(WebContextHolder.getCurrentUser().getId()));
		//查询工单列表时,如果条件中有根据生产状态为CLOSE查询的则显示生产状态为CLOSE,否则不显示。
		if(criteria.getQueryCondition().containsValue(ConstantUtils.ENTRY_CODE_STATUS_CLOSE)){
			criteria.getQueryCondition().put("showClose", ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
		}else{
			criteria.getQueryCondition().put("showClose", ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		}
		if (StringUtil.isNotBlank(criteria.getSort())) {
			criteria.setOrderField(criteria.getSort());
			criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		} else {
			criteria.setOrderField("priority");
			criteria.setOrderDirection(OrderEnum.ASC);
		}

		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap) {
		modelMap.addAttribute("plantOptions",
				plantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("priorityOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PORDER_PRIORITY));
		return new ModelAndView("/production-plan/porder/porder_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, ToPorder bean) {
		if (StringUtil.isBlank(bean.getPriority())) {
			bean.setPriority("Z");
		}
		bean = service.doSavePorder(bean);
		logService.doAddLog("ToPorder", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("plantOptions",
				plantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("priorityOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PORDER_PRIORITY));
		modelMap.addAttribute("partOptions", partService.getDictItem(new TmPart(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		return new ModelAndView("/production-plan/porder/porder_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, ToPorder bean) {
		ToPorder before = service.findById(bean.getId());
		if (before.getTaskQty() == null) {
			before.setTaskQty(0);
		}
		//总数量<派工数量
		if (bean.getQty() < before.getTaskQty()) {
			throw new BusinessException("PORDER_QTY_LESS_THAN_TASK_QTY_ERROR", bean.getQty(), before.getTaskQty());
		} else if (bean.getQty().toString().equals(before.getTaskQty().toString())) {//总数量==派工数量
			bean.setTaskStatus(ConstantUtils.ENTRY_CODE_PORDER_TASK_STATUS_ALL);
		} else {//总数量>派工数量
			bean.setTaskStatus(ConstantUtils.ENTRY_CODE_PORDER_TASK_STATUS_PART);
		}
		if (StringUtil.isBlank(bean.getPriority())) {
			bean.setPriority("Z");
		}
		service.doUpdate(bean);
		logService.doUpdateLog("ToPorder", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<ToPorder> list = service.findAllInIds(deleteIds);
		try {
			service.doDeleteById(deleteIds);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			String[] foreignKeys = getMessage(request, "PORDER_FOREIGN_KEY").split(",");
			if (StringUtils.isNotBlank(errMsg)) {
				for (String foreignKey : foreignKeys) {
					if (errMsg.indexOf(foreignKey) != -1) {
						throw new BusinessException(foreignKey);
					}
				}
				throw e;
			}
		}
		logService.doDeleteLog("ToPorder", list);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 根据工厂查询物料信息
	 * 
	 * @param tmPlantId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getParts")
	public JsonActionResult getParts(HttpServletResponse response, Integer tmPlantId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("TYPE", Arrays.asList(ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED,
				ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED));
		param.put("ENABLED", ConstantUtils.TYPE_CODE_ENABLED_ON);
		param.put("TM_PLANT_ID", tmPlantId);
		PageResult<DictEntry> result = new PageResult<DictEntry>();
		result.setContent(partService.getDictItem(param));
		return ActionUtils.handleListResult(response, result);
	}

	@ResponseBody
	@RequestMapping(value = "/getPartsByPlantId")
	public List<DictEntry> getParts(Integer tmPlantId) {
		TmPart part = null;
		if (null != tmPlantId) {
			part = new TmPart();
			part.setTmPlantId(tmPlantId);
		}
		return partService.getDictItem(part);
	}

	/**
	 * 根据工厂和物料查询工艺路径
	 * 
	 * @param tmPartId
	 * @param tmPlantId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPaths")
	public List<TmPath> getPaths(Integer tmPartId, Integer tmPlantId) {
		return pathService.getPathByPlantAndParts(tmPlantId, tmPartId);
	}

	/**
	 * 根据工厂和物料查询BOM信息
	 * 
	 * @param tmPlantId
	 * @param tmPartId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getBoms")
	public List<TmBom> getBoms(Integer tmPlantId, Integer tmPartId) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryPage(false);
		criteria.setQueryRelationEntity(false);
		Map<String, Object> queryCondition = new HashMap<String, Object>();
		queryCondition.put("tmPlantId", tmPlantId.toString());
		queryCondition.put("tmPartId", tmPartId.toString());
		queryCondition.put("enabled", ConstantUtils.TYPE_CODE_ENABLED_ON);
		queryCondition.put("status", ConstantUtils.MAINTAIN_STATUS_COMPLETE);
		criteria.setQueryCondition(queryCondition);
		criteria.setOrderField("maxFlag");
		criteria.setOrderDirection(OrderEnum.DESC);
		return bomService.findBy(criteria).getContent();
	}

	@RequestMapping(value = "/taskInput")
	public ModelAndView taskInput(ModelMap modelMap, Integer toPorderId) {
		modelMap.addAttribute("toPorderId", toPorderId);
		ToPorder porder = service.findById(toPorderId);
		modelMap.addAttribute("porder", porder);
		return new ModelAndView("/production-plan/porder/porder_task");
	}

	/**
	 * 派工
	 * 
	 * @param toPorderId
	 *            工单ID
	 * @param taskQty
	 *            序列数量
	 * @param everyQty
	 *            每个序列的数量
	 * @param tmPathId
	 *            工艺路径ID
	 * @param tmBomId
	 *            BOM ID
	 */
	@ResponseBody
	@RequestMapping(value = "/task")
	public JsonActionResult task(HttpServletResponse response, Integer toPorderId, Integer taskQty, Integer everyQty,
			Integer tmPathId, Integer tmBomId, String note) {
		service.doTask(toPorderId, taskQty, everyQty, tmPathId, tmBomId, note);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/doCancelTask")
	public JsonActionResult doCancelTask(HttpServletResponse response, Integer toPorderId) {
		service.doCancelTask(toPorderId);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/doHoldInput")
	public ModelAndView doHoldInput(ModelMap modelMap, Integer id) {
		modelMap.addAttribute("id", id);
		return new ModelAndView("/production-plan/porder/porder_hold");
	}

	@ResponseBody
	@RequestMapping(value = "/doHold")
	public JsonActionResult doHold(HttpServletResponse response, Integer id, String note) {
		ToPorder porder = service.findById(id);
		porder.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
		porder.setNote(note);
		service.doUpdate(porder);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/doCancelHold")
	public JsonActionResult doCancelHold(HttpServletResponse response, Integer id) {
		ToPorder porder = service.findById(id);
		porder.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		porder.setNote("");
		service.doUpdate(porder);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/doCloseInput")
	public ModelAndView doCloseInput(ModelMap modelMap, Integer id) {
		modelMap.addAttribute("id", id);
		return new ModelAndView("/production-plan/porder/porder_close");
	}

	@ResponseBody
	@RequestMapping(value = "/doClose")
	public JsonActionResult doClose(HttpServletResponse response, Integer id, String closeReason) {
		service.doClose(id, closeReason);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/printTransferCardHtml")
	public ModelAndView printTransferCardHtml(Model model, Integer id) {
		ToPorder porder = service.findById(id, true);
		model.addAttribute("bean", porder);
		if (porder.getTmPathId() == null) {
			model.addAttribute("pathUlocs", null);
		} else {
			List<TmPathUloc> pathUlocs = pathUlocService
					.getPathUlocAndRelationEntity(String.valueOf(porder.getTmPathId()));
			model.addAttribute("pathUlocs", pathUlocs);
		}
		return new ModelAndView("/production-plan/porder/porder_card");
	}

	@RequestMapping(value = "/barStream")
	public void barStream(HttpServletResponse response, String context) throws IOException {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			BarcodeUtil.generateBarToStream(context, outputStream);
		} catch (IOException e) {
			logger.info(ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "/checkPorderIsHavePath")
	public boolean checkPorderIsHavePath(Integer id) {
		ToPorder porder = service.findById(id);
		if (porder.getTmPathId() == null) {
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/printTransferCard")
	public String printTransferCard(Model model, Integer id) {
		ToPorder porder = service.findById(id, true);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<TmPathUloc> pathUlocs = pathUlocService.getPathUlocAndRelationEntity(porder.getTmPathId().toString());
		for (TmPathUloc pathUloc : pathUlocs) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("SEQ", pathUloc.getSeq());
			params.put("TM_ULOC_ID", pathUloc.getUloc().getNo());
			params.put("PROCESS_NAME", pathUloc.getUloc().getName());
			list.add(params);
		}
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		// 动态指定报表模板url
		model.addAttribute("url", "/WEB-INF/jasper/transferCard.jasper");
		model.addAttribute("SN", porder.getNo());
		model.addAttribute("PPATH_NO", porder.getPath().getNo());
		TmLine line = lineService.findById(aviService.getLineId(porder.getTmPathId()));
		model.addAttribute("LINE_NO_NAME", porder.getTmPathId() == null ? "" : (line.getNo() + "-" + line.getNameCn()));
		model.addAttribute("TM_PART_ID", porder.getPart().getNo() + "-" + porder.getPart().getNameCn());
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
			String downName, String ids) throws IOException {

		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList("no"));
		final PageResult<ToPorder> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "工单编号", "工厂", "物料", "数量", "派工数量", "工艺路径", "BOM", "计划开始时间", "计划结束时间", "实际开始时间",
				"实际结束时间", "生产状态", "派工状态", "入库状态", "上线数量", "下线数量", "报废数量", "入库数量", "优先级", "是否ERP下达", "是否HOLD", "备注",
				"关闭理由" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}
}
