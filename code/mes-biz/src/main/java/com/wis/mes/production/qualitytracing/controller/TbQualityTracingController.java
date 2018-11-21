package com.wis.mes.production.qualitytracing.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.classmanage.service.TmClassManagerService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.service.TmFaultGradeService;
import com.wis.mes.master.nc.service.TmNcGroupService;
import com.wis.mes.master.nc.service.TmNcService;
import com.wis.mes.master.nc.vo.NGVo;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;
import com.wis.mes.production.producttracing.entity.TbProductTracing;
import com.wis.mes.production.producttracing.service.TbProductTracingService;
import com.wis.mes.production.qualitytracing.entity.TbQualityTracing;
import com.wis.mes.production.qualitytracing.service.TbQualityTracingService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/tbQualityTracing")
public class TbQualityTracingController extends BaseController {

	@Autowired
	private TbQualityTracingService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmNcGroupService ncGroupService;
	@Autowired
	private TmNcService ncService;
	@Autowired
	private TmFaultGradeService faultGradeService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TbProductTracingService productTracingService;
	@Autowired
	private TmClassManagerService classmanagerService;
	@Autowired
	private TmWorktimeService worktimeService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("ncGroup", ncGroupService.queryDictItem(new QueryCriteria()));
		modelMap.addAttribute("optionShiftno", entryService.getEntryByTypeCode(ConstantUtils.SHIFT_TYPE));
		modelMap.addAttribute("optionNcStatus", entryService.getEntryByTypeCode(ConstantUtils.QUALITY_NC_STATUS));
		modelMap.addAttribute("faultGrades", JSONArray.fromObject(faultGradeService.getDictItem(null)).toString());
		modelMap.addAttribute("optionNgEntrance", JSONArray.fromObject(ulocService.getUlocNgExitEnterMap()).toString());
		modelMap.addAttribute("isRemoveds", entryService.getEntryByTypeCode(ConstantUtils.WHETHER_DELETE));
		return new ModelAndView("/qualitytracing/qualitytracing_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "backNumber", "machineOfName", "machineName" }));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		if(queryCondition.get("createTimeStart") == null || queryCondition.get("createTimeEnd")== null){
			Date date = new Date();
			queryCondition.put("createTimeStart", DateUtils.getPlusDay(date, -7));
			queryCondition.put("createTimeEnd", date);
		}
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("classmanagerOption", classmanagerService.getDictItemEntry(null));
		setModelMapDictionary(modelMap);
		return new ModelAndView("/qualitytracing/qualitytracing_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TbQualityTracing bean) {
		TbProductTracing eg = new TbProductTracing();
		eg.setBackNumber(bean.getBackNumber());
		eg.setMachineName(bean.getMachineName());
		List<TbProductTracing> productTracings = productTracingService.findByEg(eg);
		if (null != productTracings && productTracings.size() > 0) {
			bean.setTmClassManagerId(productTracings.get(0).getTmClassManagerId());
			bean.setSn(productTracings.get(0).getSn());
			bean.setMachineOfName(productTracingService.getEgModelName(bean.getBackNumber()));
		} else {
			throw new BusinessException("ERROR_KEY", "故障添加失败，产品未上线。");
		}
		if(StringUtils.isNoneBlank(bean.getShiftno())) {
			String lineNo = globalConfigurationService.getValueByKey(ConstantUtils.R5_LINE_NO);
			TmLine line = lineService.findUniqueByEg(TmLine.createLineNo(lineNo));
			List<TmWorktime> todayWorkTimes = worktimeService.todayWorkTime(line.getId(), bean.getShiftno());
			if(null != todayWorkTimes && todayWorkTimes.size()>0) {
				bean.setTmWorkTimeId(todayWorkTimes.get(0).getId());
			}
		}
		Date date = new Date();
		bean.setInfoSources(ConstantUtils.EMPLOYEE_JUDGE);
		bean.setEnteringTime(DateUtils.formatHMS(date));
		bean.setCreateTime(date);
		bean.setStatus(ConstantUtils.CREATED);
		bean = service.doSave(bean);
		/*productTracingService.updateUnhealthyCount(bean.getSn());*/
		logService.doAddLog("TbQualityTracing", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		TbQualityTracing bean = service.findById(Integer.valueOf(id));
		modelMap.addAttribute("bean", bean);
		List<NGVo> ncList = new ArrayList<NGVo>();
		if (null != bean.getTmNcGroupId()) {
			TmNc eg = new TmNc();
			eg.setTmNcGroupId(bean.getTmNcGroupId());
			ncList = ncService.getDictItem(eg);
		}
		modelMap.addAttribute("ncDict", ncList);
		modelMap.addAttribute("ncDictString", JSONArray.fromObject(ncList).toString());
		TmClassManager classManagerEg = new TmClassManager();
		classManagerEg.setId(null != bean.getTmClassManagerId()?bean.getTmClassManagerId():-1);
		modelMap.addAttribute("classmanagerOption", classmanagerService.getDictItemEntry(classManagerEg));
		setModelMapDictionary(modelMap);
		return new ModelAndView("/qualitytracing/qualitytracing_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TbQualityTracing bean) {
		TbQualityTracing before = service.findById(bean.getId());
		TbQualityTracing setBean = before;
		setBean.setTmNcGroupId(bean.getTmNcGroupId());
		setBean.setTmNcId(bean.getTmNcId());
		setBean.setNcProcessMode(bean.getNcProcessMode());
		setBean.setDiscoveryStation(bean.getDiscoveryStation());
		setBean.setNgEntrance(bean.getNgEntrance());
		service.doUpdate(setBean);
		logService.doUpdateLog("TbQualityTracing", before, setBean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TbQualityTracing> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TbQualityTracing", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/getNcItem")
	public JsonActionResult getNcItem(HttpServletResponse response, Integer tmNcGroupId) {
		TmNc eg = new TmNc();
		eg.setTmNcGroupId(tmNcGroupId);
		return ActionUtils.handleResult(ncService.getDictItem(eg));
	}

	@ResponseBody
	@RequestMapping(value = "/UnqualifiedClosure")
	public JsonActionResult UnqualifiedClosure(HttpServletResponse response, Integer id) {
		TbQualityTracing bean = service.findById(id);
		if (null == bean.getTmNcGroupId() || StringUtils.isBlank(bean.getNgEntrance())) {
			throw new BusinessException("ERROR_KEY", "请先提交，在进行故障关闭。");
		}
		if (bean.getStatus().equals(ConstantUtils.CLOSED)) {
			throw new BusinessException("ALREADY_CLOSED");
		}
		bean.setSubmitTime(new Date());
		bean.setStatus(ConstantUtils.CLOSED);
		service.doUpdate(bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, final String ids) throws IOException, InvalidFormatException {
		String[] title = new String[] { "创建时间", "班次", "班组", "SN", "背番号", "机种名","机号", "故障组编码", "故障编码", "故障内容",
				"信息来源", "发现工位", "故障等级", "NG出口", "适应NG入口", "NG入口", "状态", "录入人", "提交时间"};
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		if(queryCondition.get("createTimeStart") == null || queryCondition.get("createTimeEnd")== null){
			Date date = new Date();
			queryCondition.put("createTimeStart", DateUtils.getPlusDay(date, -7));
			queryCondition.put("createTimeEnd", date);
		}
		service.doExport(request, response, service, "TBQUALITY_TRACING", "故障机管理数据导出", title, criteria);
		return ActionUtils.handleResult(response);
	}
	
	private void setModelMapDictionary(ModelMap modelMap) {
		modelMap.addAttribute("optionShiftno", entryService.getEntryByTypeCode(ConstantUtils.SHIFT_TYPE));
		modelMap.addAttribute("optionNcStatus", entryService.getEntryByTypeCode(ConstantUtils.QUALITY_NC_STATUS));
		modelMap.addAttribute("faultGrades", JSONArray.fromObject(faultGradeService.getDictItem(null)).toString());
		modelMap.addAttribute("optionNgEntrance", JSONArray.fromObject(ulocService.getUlocNgExitEnterMap()).toString());
		modelMap.addAttribute("ncGroup", ncGroupService.queryDictItem(new QueryCriteria()));
		modelMap.addAttribute("optionUloc", ulocService.getUlocAll());
	}
}
