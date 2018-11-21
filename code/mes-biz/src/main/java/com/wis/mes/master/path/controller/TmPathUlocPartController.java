package com.wis.mes.master.path.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.utils.SerialNumUtil;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.path.entity.TmPath;
import com.wis.mes.master.path.entity.TmPathUlocPart;
import com.wis.mes.master.path.service.TmPathService;
import com.wis.mes.master.path.service.TmPathUlocPartService;

/**
 * 工艺路径站点 关键件Controller
 * 
 * @author jiuxian
 *
 */
@Controller
@RequestMapping(value = "/path/uloc/part")
public class TmPathUlocPartController extends BaseController {

	@Autowired
	private TmPathUlocPartService service;
	@Autowired
	private TmPartService partService;
	@Autowired
	private TmPathService pathService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Integer tmPathUlocId) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/master/path/part/path_uloc_part_list");
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

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap, Integer tmPathUlocId) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		Integer tmPlantId = pathService.getByPathUlocId(tmPathUlocId).getTmPlantId();
		modelMap.addAttribute("partOptions", getPartDictItem(tmPlantId));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/master/path/part/path_uloc_part_add");
	}

	private List<DictEntry> getPartDictItem(Integer tmPlantId) {
		TmPart part = new TmPart();
		part.setTmPlantId(tmPlantId);
		Map<String, Object> partMap = new HashMap<String, Object>();
		partMap.put("ENABLED", ConstantUtils.TYPE_CODE_ENABLED_ON);
		partMap.put("TYPE", Arrays.asList(ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED,
				ConstantUtils.ENTRY_CODE_PART_TYPE_PARTS));
		partMap.put("TM_PLANT_ID", tmPlantId.toString());
		return partService.getDictItem(partMap);
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TmPathUlocPart bean) {
		checkPathIsComplete(bean.getTmPathUlocId());
		TmPathUlocPart eg = new TmPathUlocPart();
		eg.setTmPartId(bean.getTmPartId());
		eg.setTmPathUlocId(bean.getTmPathUlocId());
		if (service.findByEg(eg).size() > 0) {
			throw new BusinessException("PATH_ULOC_PART_UNIQUE_ERROR");
		}

		bean = service.doSave(bean);
		logService.doAddLog("TmPathUlocPart", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(String id, ModelMap modelMap, Integer tmPathUlocId) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		Integer tmPlantId = pathService.getByPathUlocId(tmPathUlocId).getTmPlantId();
		modelMap.addAttribute("partOptions", getPartDictItem(tmPlantId));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/master/path/part/path_uloc_part_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmPathUlocPart bean) {
		checkPathIsComplete(bean.getTmPathUlocId());
		TmPathUlocPart before = service.findById(bean.getId());

		TmPathUlocPart eg = new TmPathUlocPart();
		eg.setTmPartId(bean.getTmPartId());
		eg.setTmPathUlocId(bean.getTmPathUlocId());
		List<TmPathUlocPart> findByEg = service.findByEg(eg);
		for (TmPathUlocPart tmPathUlocPart : findByEg) {
			if (!tmPathUlocPart.getId().equals(bean.getId())) {
				throw new BusinessException("PATH_ULOC_PART_UNIQUE_ERROR");
			}
		}
		service.doUpdate(bean);
		logService.doUpdateLog("TmPathUlocPart", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmPathUlocPart> list = service.findAllInIds(deleteIds);
		for (TmPathUlocPart ulocPart : list) {
			checkPathIsComplete(ulocPart.getTmPathUlocId());
		}
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmPathUlocPart", list);
		return ActionUtils.handleResult(response);
	}

	private void checkPathIsComplete(Integer tmPathUlocId) {
		TmPath path = pathService.getByPathUlocId(tmPathUlocId);
		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(path.getStatus())) {
			throw new BusinessException("PATH_ULOC_DELETE_ERROR");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String tmPathUlocId) throws IOException {
		criteria.getQueryCondition().put("tmPathUlocId", tmPathUlocId);
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "物料", "数量", "顺序号", "是否批次追溯", "备注"};
		service.exportExcelData(response, service.findBy(criteria).getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/graph_listInput")
	public ModelAndView graph_listInput(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Integer tmPathUlocId, String rectSeq, Integer tmPlantId) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute("tmPlantId", tmPlantId);
		modelMap.addAttribute("rectSeq", rectSeq);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		return new ModelAndView("/master/path/path_graph/part/path_uloc_part_list_graph");
	}

	@ResponseBody
	@RequestMapping(value = "/graph_list")
	public JsonActionResult graph_list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			Integer tmPathUlocId) {
		if (tmPathUlocId == null) {
			return ActionUtils.handleResult(response);
		}
		criteria.getQueryCondition().put("tmPathUlocId", tmPathUlocId.toString());
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/graph_addInput")
	public ModelAndView graph_addInput(ModelMap modelMap, Integer tmPlantId, String rectSeq, String tmPathUlocId) {
		modelMap.addAttribute("rectSeq", rectSeq);
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute("partOptions", getPartDictItem(tmPlantId));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/master/path/path_graph/part/path_uloc_part_add_graph");
	}

	@ResponseBody
	@RequestMapping(value = "/graph_add")
	public JsonActionResult graph_add(HttpServletResponse response, TmPathUlocPart bean) {
		if (bean.getTmPathUlocId() != null) {
			checkPathIsComplete(bean.getTmPathUlocId());
			TmPathUlocPart eg = new TmPathUlocPart();
			eg.setTmPartId(bean.getTmPartId());
			eg.setTmPathUlocId(bean.getTmPathUlocId());
			eg.setRectSeq(bean.getRectSeq());
			if (service.findByEg(eg).size() > 0) {
				throw new BusinessException("PATH_ULOC_PART_UNIQUE_ERROR");
			}
			bean = service.doSave(bean);
			logService.doAddLog("TmPathUlocPart", bean);
		} else {
			TmPart part = partService.findById(bean.getTmPartId());
			bean.setPart(part);
			bean.setId(SerialNumUtil.getInstance().nextInt(ConstantUtils.SERIAL_PATH_ULOC_PART_ID));
		}
		return ActionUtils.handleResult(bean);
	}

	@RequestMapping(value = "/graph_updateInput")
	public ModelAndView graph_updateInput(TmPathUlocPart bean, String id, ModelMap modelMap, Integer tmPathUlocId,
			String rectSeq, Integer tmPlantId) {
		modelMap.addAttribute("rectSeq", rectSeq);
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		if (tmPathUlocId != null) {
			tmPlantId = pathService.getByPathUlocId(tmPathUlocId).getTmPlantId();
			modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		} else {
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute("partOptions", getPartDictItem(tmPlantId));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/master/path/path_graph/part/path_uloc_part_update_graph");
	}

	@ResponseBody
	@RequestMapping(value = "/graph_update")
	public JsonActionResult graph_update(HttpServletResponse response, TmPathUlocPart bean) {
		if (bean.getTmPathUlocId() != null) {
			checkPathIsComplete(bean.getTmPathUlocId());
			TmPathUlocPart before = service.findById(bean.getId());
			TmPathUlocPart eg = new TmPathUlocPart();
			eg.setTmPartId(bean.getTmPartId());
			eg.setTmPathUlocId(bean.getTmPathUlocId());
			eg.setRectSeq(bean.getRectSeq());
			List<TmPathUlocPart> findByEg = service.findByEg(eg);
			for (TmPathUlocPart tmPathUlocPart : findByEg) {
				if (!tmPathUlocPart.getId().equals(bean.getId())) {
					throw new BusinessException("PATH_ULOC_PART_UNIQUE_ERROR");
				}
			}
			service.doUpdate(bean);
			logService.doUpdateLog("TmPathUlocPart", before, bean);
		} else {
			TmPart part = partService.findById(bean.getTmPartId());
			bean.setPart(part);
		}
		return ActionUtils.handleResult(bean);
	}
}
